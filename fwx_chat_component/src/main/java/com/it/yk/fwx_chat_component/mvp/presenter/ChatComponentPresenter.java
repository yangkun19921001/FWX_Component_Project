package com.it.yk.fwx_chat_component.mvp.presenter;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.easeui.helper.HXHelper;
import com.iit.yk.chat_base_component.imuisample.manager.ChatComponentManager;
import com.iit.yk.chat_base_component.imuisample.models.DefaultUser;
import com.iit.yk.chat_base_component.imuisample.models.MyMessage;
import com.it.yk.fwx_chat_component.mvp.contract.ChatComponentContract;
import com.it.yk.fwx_chat_component.mvp.model.entity.ChatMessageEntity;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.yixia.camera.util.StringUtils;
import com.yk.component.res.qmui.QMUITipDialogHelper;
import com.yk.component.sdk.core.Constants;
import com.yk.component.sdk.db.DBManager;
import com.yk.component.sdk.manager.DispatchManager;
import com.yk.component.sdk.utils.LogHelper;

import org.litepal.crud.callback.FindMultiCallback;
import org.litepal.crud.callback.SaveCallback;
import org.litepal.crud.callback.UpdateOrDeleteCallback;
import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import cn.jiguang.imui.commons.models.IMessage;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import static com.hyphenate.easeui.EaseConstant.CHATTYPE_SINGLE;


@ActivityScope
public class ChatComponentPresenter extends BasePresenter<ChatComponentContract.Model, ChatComponentContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private ChatComponentManager mChatComponentManager;

    /**
     * 当前聊天室类型
     */
    private int mCurrentChatType = 0;
    private String mOtherId;

    @Inject
    List<MyMessage> myMessageList;

    @Inject
    public ChatComponentPresenter(ChatComponentContract.Model model, ChatComponentContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
        if (mChatComponentManager != null)
            mChatComponentManager.onDestory();
    }

    public void initChatView(TextView publicToolbarTitle) {
        try {
            mChatComponentManager = new ChatComponentManager();
            mChatComponentManager.init(mAppManager.getTopActivity());
            //跟谁聊天)
            mOtherId = mAppManager.getTopActivity().getIntent().getExtras().getString(Constants.IChat.otherId);
            if (!StringUtils.isEmpty(mOtherId)) {
                publicToolbarTitle.setText(mOtherId);
            } else {
                publicToolbarTitle.setText("未找到对方聊天号码,请联系管理员.");
            }
            switch (mAppManager.getTopActivity().getIntent().getExtras().getInt(Constants.IChat.ChatType)) {
                case Constants.IChat.OneChat:
                    mCurrentChatType = Constants.IChat.OneChat;
                    break;
                case Constants.IChat.GroupChat:
                    mCurrentChatType = Constants.IChat.GroupChat;
                    break;
            }
        } catch (Exception e) {
            LogHelper.e(TAG, this.getClass().getSimpleName() + "--initChatView_-" + e.getMessage());
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mChatComponentManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 设置发送消息的监听
     */
    public void addSendMessageListener() {
        mChatComponentManager.addIChatMessageListener(new ChatComponentManager.IChatMessageListener() {
            @Override
            public void sendMessage(int sendType, MyMessage message) {
                LogHelper.i(TAG, "----软件 chat_input ---" + message.toString());
                message.setUserInfo(new DefaultUser(mOtherId, mOtherId, mOtherId));
                sendHXMessage(sendType, message);
            }
        });
    }

    /**
     * 发送消息
     *
     * @param sendType
     * @param message
     */
    private void sendHXMessage(int sendType, MyMessage message) {
        saveChatMessage(message);
        switch (message.getType()) {
            case Constants.IChat.SEND_TEXT:
                sendTxtMessage(message);
                break;
            case Constants.IChat.SEND_IMAGE:
                sendImg(message);
                break;
            case Constants.IChat.SEND_VIDEO:
                sendVideo(message);
                break;
            case Constants.IChat.SEND_VOICE:
                sendVoice(message);
                break;
            case Constants.IChat.SEND_FILE:
                sendFile(message);
                break;
            case Constants.IChat.SEND_LOCATION:
                sendLocation(message);
                break;
            case Constants.IChat.SEND_CUSTOM:
                sendCustom(message);
                break;
        }
    }

    /**
     * 发送语音文件
     *
     * @param message
     */
    private void sendVoice(MyMessage message) {
        HXHelper.getInstance().getMessageEngine().sendVoiceMessage(CHATTYPE_SINGLE, message.getMediaFilePath(), Integer.parseInt(FileUtils.getDirLength(message.getMediaFilePath()) + ""), message.getFromUser().getId(), new EMCallBack() {
            @Override
            public void onSuccess() {
                LogHelper.d(TAG, "---sendVoice--onSuccess----" + message.getMediaFilePath());
                mChatComponentManager.upAdapter(message.getMsgId(), IMessage.MessageStatus.SEND_SUCCEED);
                upChatMessage(message.getMsgId(), IMessage.MessageStatus.SEND_SUCCEED);

            }

            @Override
            public void onError(int i, String s) {
                LogHelper.e(TAG, "---sendVoice--onError----" + s);
                mChatComponentManager.upAdapter(message.getMsgId(), IMessage.MessageStatus.SEND_FAILED);
                upChatMessage(message.getMsgId(), IMessage.MessageStatus.SEND_FAILED);
            }

            @Override
            public void onProgress(int i, String s) {
                LogHelper.d(TAG, "---sendVoice--onProgress----" + i + "---" + s);
            }
        });

    }

    /**
     * 发送文件
     *
     * @param message
     */
    private void sendFile(MyMessage message) {
        HXHelper.getInstance().getMessageEngine().sendFileMessage(CHATTYPE_SINGLE, message.getMediaFilePath(), message.getFromUser().getId(), new EMCallBack() {
            @Override
            public void onSuccess() {
                LogHelper.d(TAG, "---sendFile--onSuccess----" + message.getMediaFilePath());
                mChatComponentManager.upAdapter(message.getMsgId(), IMessage.MessageStatus.SEND_SUCCEED);
                upChatMessage(message.getMsgId(), IMessage.MessageStatus.SEND_SUCCEED);
            }

            @Override
            public void onError(int i, String s) {
                LogHelper.e(TAG, "---sendFile--onError----" + s);
                mChatComponentManager.upAdapter(message.getMsgId(), IMessage.MessageStatus.SEND_FAILED);
                upChatMessage(message.getMsgId(), IMessage.MessageStatus.SEND_FAILED);
            }

            @Override
            public void onProgress(int i, String s) {
                LogHelper.d(TAG, "---sendFile--onProgress----" + i + "---" + s);
            }
        });
    }

    /**
     * 发送位置
     *
     * @param message
     */
    private void sendLocation(MyMessage message) {
        HXHelper.getInstance().getMessageEngine().sendLocationMessage(CHATTYPE_SINGLE, 0, 0, "中关村", message.getFromUser().getId(), new EMCallBack() {
            @Override
            public void onSuccess() {
                LogHelper.d(TAG, "---sendLocation--onSuccess----" + message.getMediaFilePath());
                mChatComponentManager.upAdapter(message.getMsgId(), IMessage.MessageStatus.SEND_SUCCEED);
                upChatMessage(message.getMsgId(), IMessage.MessageStatus.SEND_SUCCEED);
            }

            @Override
            public void onError(int i, String s) {
                LogHelper.e(TAG, "---sendLocation--onError----" + s);
                mChatComponentManager.upAdapter(message.getMsgId(), IMessage.MessageStatus.SEND_FAILED);
                upChatMessage(message.getMsgId(), IMessage.MessageStatus.SEND_FAILED);
            }

            @Override
            public void onProgress(int i, String s) {
                LogHelper.d(TAG, "---sendLocation--onProgress----" + i + "---" + s);
            }
        });

    }

    private void sendCustom(MyMessage message) {
    }

    private void sendVideo(MyMessage message) {
        HXHelper.getInstance().getMessageEngine().sendVideoMessage(CHATTYPE_SINGLE, message.getMediaFilePath(), message.getMediaFilePath(), Integer.parseInt(FileUtils.getDirLength(message.getMediaFilePath()) + ""), message.getFromUser().getId(), new EMCallBack() {
            @Override
            public void onSuccess() {
                LogHelper.d(TAG, "---sendVideo--onSuccess----" + message.getMediaFilePath());
                mChatComponentManager.upAdapter(message.getMsgId(), IMessage.MessageStatus.SEND_SUCCEED);
                upChatMessage(message.getMsgId(), IMessage.MessageStatus.SEND_SUCCEED);
            }

            @Override
            public void onError(int i, String s) {
                LogHelper.e(TAG, "---sendVideo--onError----" + s);
                mChatComponentManager.upAdapter(message.getMsgId(), IMessage.MessageStatus.SEND_FAILED);
                upChatMessage(message.getMsgId(), IMessage.MessageStatus.SEND_FAILED);
            }

            @Override
            public void onProgress(int i, String s) {
                LogHelper.d(TAG, "---sendVideo--onProgress----" + i + "---" + s);
            }
        });
    }

    private void sendImg(MyMessage message) {
        HXHelper.getInstance().getMessageEngine().sendImgMessage(CHATTYPE_SINGLE, message.getMediaFilePath(), message.getFromUser().getId(), new EMCallBack() {
            @Override
            public void onSuccess() {
                LogHelper.d(TAG, "---sendImg--onSuccess----" + message.getMediaFilePath());
                mChatComponentManager.upAdapter(message.getMsgId(), IMessage.MessageStatus.SEND_SUCCEED);
                upChatMessage(message.getMsgId(), IMessage.MessageStatus.SEND_SUCCEED);
            }

            @Override
            public void onError(int i, String s) {
                LogHelper.e(TAG, "---sendImg--onError----" + s);
                mChatComponentManager.upAdapter(message.getMsgId(), IMessage.MessageStatus.SEND_FAILED);
                upChatMessage(message.getMsgId(), IMessage.MessageStatus.SEND_FAILED);
            }

            @Override
            public void onProgress(int i, String s) {
                LogHelper.d(TAG, "---sendImg--onProgress----" + i + "---" + s);
            }
        });
    }

    private void sendTxtMessage(MyMessage message) {
        HXHelper.getInstance().getMessageEngine().sendTxtMessage(CHATTYPE_SINGLE, message.getText(), message.getFromUser().getId(), new EMCallBack() {
            @Override
            public void onSuccess() {
                LogHelper.d(TAG, "---sendTxtMessage--onSuccess----" + message.getMediaFilePath());
                mChatComponentManager.upAdapter(message.getMsgId(), IMessage.MessageStatus.SEND_SUCCEED);
                upChatMessage(message.getMsgId(), IMessage.MessageStatus.SEND_SUCCEED);
            }

            @Override
            public void onError(int i, String s) {
                LogHelper.e(TAG, "---sendTxtMessage--onError----" + s);
                mChatComponentManager.upAdapter(message.getMsgId(), IMessage.MessageStatus.SEND_FAILED);
                upChatMessage(message.getMsgId(), IMessage.MessageStatus.SEND_FAILED);
            }

            @Override
            public void onProgress(int i, String s) {
                LogHelper.d(TAG, "---sendTxtMessage--onProgress----" + i + "---" + s);
            }
        });
    }

    /**
     * 保存会话消息记录和聊天消息记录
     *
     * @param myMessage
     */
    public void saveChatMessage(MyMessage myMessage) {
        ChatMessageEntity chatMessageEntity = new ChatMessageEntity();
        chatMessageEntity.setChatType(mCurrentChatType);
        chatMessageEntity.setFilePath(myMessage.getMediaFilePath());
        chatMessageEntity.setMessageContent(myMessage.getText());
        chatMessageEntity.setMessageId(myMessage.getMsgId());
        chatMessageEntity.setMessageState(myMessage.getMessageStatus().ordinal());
        chatMessageEntity.setMessageTime(TimeUtils.getNowString());
        chatMessageEntity.setOtherId(myMessage.getFromUser().getId());
        chatMessageEntity.setUserId(SPUtils.getInstance(Constants.ISP_Config.SP_NAME).getString(Constants.ISP_Config.USER_PHONE_NUMBER));
        chatMessageEntity.setMessageType(myMessage.getType());
        DBManager.getInstance().saveAsync(chatMessageEntity, new SaveCallback() {
            @Override
            public void onFinish(boolean success) {
                LogHelper.i(TAG, success == true ? "saveChatMessage-保存成功" : "saveChatMessage-保存失败");
            }
        });
    }

    public void upChatMessage(String megId, IMessage.MessageStatus messageStatus) {
        ChatMessageEntity chatMessageEntity = new ChatMessageEntity();
        chatMessageEntity.setMessageState(messageStatus.ordinal());
        DBManager.getInstance().upAllAsync(chatMessageEntity, "userId = ? and messageId = ?", SPUtils.getInstance(Constants.ISP_Config.SP_NAME).getString(Constants.ISP_Config.USER_PHONE_NUMBER), megId, new UpdateOrDeleteCallback() {
            @Override
            public void onFinish(int rowsAffected) {
                LogHelper.i(TAG, "upChatMessage---" + rowsAffected);
            }
        });

    }

    /**
     * 加载数据库中本地缓存消息
     */
    public void addDBChatHistoryMessage() {
        Observable.create(new ObservableOnSubscribe<List<ChatMessageEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ChatMessageEntity>> emitter) throws Exception {
                DBManager.getInstance().queryaAllLists("userId = ? and otherId = ?", SPUtils.getInstance(Constants.ISP_Config.SP_NAME).getString(Constants.ISP_Config.USER_PHONE_NUMBER)
                        , mOtherId, "messageTime", ChatMessageEntity.class, new FindMultiCallback() {
                            @Override
                            public void onFinish(List list) {
                                emitter.onNext(list);
                                emitter.onComplete();
                            }
                        });
            }
        }).subscribeOn(Schedulers.io())
                .map(new Function<List<ChatMessageEntity>, List<MyMessage>>() {
                    @Override
                    public List<MyMessage> apply(List<ChatMessageEntity> list) throws Exception {
                        return getHistoryList(list);
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        QMUITipDialogHelper.getInstance().showLoading(mAppManager.getTopActivity(), "加载中...", Constants.ILoad.Loading, 0, null);
                    }
                }).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                QMUITipDialogHelper.getInstance().showLoading(mAppManager.getTopActivity(), "加载中...", Constants.ILoad.LoadSuccess, 0, null);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<MyMessage>>(mErrorHandler) {
                    @Override
                    public void onNext(List<MyMessage> list) {
                        mChatComponentManager.loadListsData(list);
                    }
                });
    }

    private List<MyMessage> getHistoryList(List<ChatMessageEntity> list) {
        myMessageList.clear();
        if (list.size() > 0) {
            for (ChatMessageEntity chatMeg : list
                    ) {
                try {
                    MyMessage chatMessageEntity = new MyMessage(chatMeg.getMessageContent(), chatMeg.getMessageType());
                    switch (chatMeg.getMessageState()) {
                        case 0:
                            chatMessageEntity.setMessageStatus(IMessage.MessageStatus.CREATED);
                            break;
                        case 1:
                            chatMessageEntity.setMessageStatus(IMessage.MessageStatus.SEND_GOING);
                            break;
                        case 2:
                            chatMessageEntity.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                            break;
                        case 3:
                            chatMessageEntity.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                            break;
                        case 4:
                            chatMessageEntity.setMessageStatus(IMessage.MessageStatus.SEND_DRAFT);
                            break;
                        case 5:
                            chatMessageEntity.setMessageStatus(IMessage.MessageStatus.RECEIVE_GOING);
                            break;
                        case 6:
                            chatMessageEntity.setMessageStatus(IMessage.MessageStatus.RECEIVE_SUCCEED);
                            break;
                        case 7:
                            chatMessageEntity.setMessageStatus(IMessage.MessageStatus.RECEIVE_FAILED);
                            break;
                    }
                    chatMessageEntity.setUserInfo(new DefaultUser(chatMeg.getUserId(), chatMeg.getUserId(), chatMeg.getUserId()));
                    chatMessageEntity.setDuration(0);
                    chatMessageEntity.setMediaFilePath(chatMeg.getFilePath());
                    chatMessageEntity.setTimeString(chatMeg.getMessageTime());
                    myMessageList.add(chatMessageEntity);
                } catch (Exception e) {
                    LogHelper.e(TAG, "getHistoryList--" + e.getMessage());
                }
            }
        }
        return myMessageList;
    }

    /**
     * 将当前聊天人员发送出去
     */
    public void sendCurrentChatPerson() {
        if (mChatComponentManager.getAdapterData().size() >= 0) {
            int sendType = mChatComponentManager.getAdapterData().get(0).getType();

            switch (sendType) {
                case 13:
                    sendMessageToDispactchManager("自定义数据");
                    break;
                case 7:
                    sendMessageToDispactchManager("发送了一个视频");
                    break;
                case 3:
                    sendMessageToDispactchManager("发送了一个图片");
                    break;
                case 11:
                    sendMessageToDispactchManager("发送了一个文件");
                    break;
                case 9:
                    sendMessageToDispactchManager("发送了一个位置");
                    break;
                case 1:
                    sendMessageToDispactchManager(mChatComponentManager.getAdapterData().get(0).getText());
                    break;
                case 5:
                    sendMessageToDispactchManager("发送了一个音频");
                    break;
                default:
                    break;
            }
        }
    }

    private void sendMessageToDispactchManager(String content) {
        Bundle bundle = new Bundle();
        //最后一条聊天的消息 ID
        bundle.putString(Constants.IChat.MESSAGE_ID, mChatComponentManager.getAdapterData().get(0).getMsgId());
        //最后一条聊天的聊天消息类型
        bundle.putInt(Constants.IChat.MESSAGE_TYPE, mChatComponentManager.getAdapterData().get(0).getMessageStatus().ordinal());
        //最后一条聊天的类型（单聊/群聊）
        bundle.putInt(Constants.IChat.MESSAGE_SIG_GROUP, mCurrentChatType);
        //与谁聊天
        bundle.putString(Constants.IChat.otherId, mOtherId);
        bundle.putString(Constants.IChat.MESSAGE_CONTENT, content);
        bundle.putString(Constants.IChat.MESSAGE_TIME, mChatComponentManager.getAdapterData().get(0).getTimeString());
        DispatchManager.getInstance().sendMessage(Constants.IPostMessage.SEND_CURRENT_CHAT_PERSON, bundle);
    }

    @Subscriber(tag = Constants.IChat.SessionMessageToAdapter)
    public void addMessage(Message message) {
        MyMessage myMessage = (MyMessage) message.obj;
        if (null != message){
            mChatComponentManager.loadMessage(myMessage);
        }
    }
}

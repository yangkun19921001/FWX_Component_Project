package com.it.yk.weichatappcation.mvp.presenter;

import android.app.Application;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.it.yk.weichatappcation.R;
import com.it.yk.weichatappcation.mvp.contract.WeiChatContract;
import com.it.yk.weichatappcation.mvp.model.entity.WeiChatMessageHistoryEntity;
import com.it.yk.weichatappcation.mvp.ui.fragment.adapter.WeiChatHistoryAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yk.component.res.status_view.StatusViewHelper;
import com.yk.component.sdk.core.Constants;
import com.yk.component.sdk.db.DBManager;
import com.yk.component.sdk.manager.DispatchManager;
import com.yk.component.sdk.utils.LogHelper;

import org.litepal.crud.callback.FindMultiCallback;
import org.litepal.crud.callback.SaveCallback;
import org.litepal.crud.callback.UpdateOrDeleteCallback;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import cn.jiguang.imui.commons.models.IMessage;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class WeiChatPresenter extends BasePresenter<WeiChatContract.Model, WeiChatContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private StatusViewHelper mStatusViewHelper;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;


    @Inject
    List<WeiChatMessageHistoryEntity> mMessageLists;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;

    @Inject
    WeiChatHistoryAdapter mWeiChatHistoryAdapter;

    /**
     * 保存在后台的时候聊天消息记录
     *
     * @param mCurrentChatType 当前聊天的组聊或者单聊
     * @param mediaFilePath 文件路径
     * @param txt           文本
     * @param msgID        消息ID
     * @param messageStatus 消息状态
     * @param otherID        与谁聊天
     * @param messageType    消息类型
     */
    private int mCurrentChatType = -1;
    private String mMediaFilePath = "";
    private String mTxt = "";
    private String mMsgID = "";
    private int mMessageStatus = -1;
    private String mOtherID = "";
    private int mMessageType = -1;

    @Inject
    public WeiChatPresenter(WeiChatContract.Model model, WeiChatContract.View rootView
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
    }

    public void initView(View inflate) {
        mStatusViewHelper = new StatusViewHelper(mAppManager.getTopActivity(), inflate);
        mStatusViewHelper.initView(inflate);
        mRefreshLayout = mStatusViewHelper.getRefreshLayout();
        mRefreshLayout.setEnableRefresh(false);
        mRecyclerView = mStatusViewHelper.getRecyclerView();
        LayoutInflater from = LayoutInflater.from(mApplication);

        mWeiChatHistoryAdapter.addHeaderView(from.inflate(R.layout.public_layout_search_view, null));
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mWeiChatHistoryAdapter);
    }

    /**
     * 获取所有的会话历史记录
     */
    @Subscriber(tag = Constants.IWeiChat.UP_Adapter_HistorySessionMessage)
    public void getMessageHistoryData(String userId) {
        Observable.create(new ObservableOnSubscribe<List<WeiChatMessageHistoryEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<List<WeiChatMessageHistoryEntity>> emitter) {
                DBManager.getInstance().queryaAllLists("userId = ?", userId, "messageTime", WeiChatMessageHistoryEntity.class, new FindMultiCallback() {
                    @Override
                    public void onFinish(List list) {
                        emitter.onNext(list);
                        emitter.onComplete();
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<WeiChatMessageHistoryEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(List<WeiChatMessageHistoryEntity> list) {
                        if (list.size() > 0) {
                            mStatusViewHelper.showContent();
                            mMessageLists.clear();
                            mMessageLists.addAll(list);
                            mWeiChatHistoryAdapter.notifyDataSetChanged();
                        } else {
                            mStatusViewHelper.showEmpty();
                        }
                    }
                });
    }

    /**
     * 用于接收服务端的消息
     *
     * @param message
     */
    @Subscriber(tag = Constants.IWeiChat.RevicesMessage)
    public void revicesMessage(Message message) {
        List<EMMessage> emMessageList = (List<EMMessage>) message.obj;
        LogHelper.i(TAG, "收到服务端发送过来的 即时消息--" + emMessageList.toString());
        for (EMMessage emMessage :
                emMessageList) {
            handlerRevicesMessage(emMessage);

            //通知栏显示的消息

        }

    }

    /**
     * 处理接收到的消息
     *
     * @param message
     */
    private void handlerRevicesMessage(EMMessage message) {
        Bundle bundle = new Bundle();
        try {
            int messageType = EaseCommonUtils.getMessageType(message);
            switch (messageType) {
                case Constants.IHXMessageType.MESSAGE_TYPE_RECV_TXT:
                    EMTextMessageBody txt = (EMTextMessageBody) message.getBody();
                    LogHelper.i(TAG, "收到 TXT 消息--" + txt.getMessage());
                    //最后一条聊天的聊天消息类型
                    bundle.putInt(Constants.IChat.MESSAGE_TYPE, Constants.IChat.RECEIVE_TEXT);
                    bundle.putString(Constants.IChat.MESSAGE_CONTENT, message.getFrom() + ":" + txt.getMessage());
                    mTxt = txt.getMessage();
                    mMessageType = IMessage.MessageType.RECEIVE_TEXT.ordinal();
                    break;
                case Constants.IHXMessageType.MESSAGE_TYPE_RECV_IMAGE:
                    EMImageMessageBody image = (EMImageMessageBody) message.getBody();
                    LogHelper.i(TAG, "收到 IMAGE 消息--" + "文件名称-" + image.getFileName() + "---文件地址--" + image.getThumbnailUrl());
                    bundle.putInt(Constants.IChat.MESSAGE_TYPE, Constants.IChat.RECEIVE_IMAGE);
                    bundle.putString(Constants.IChat.MESSAGE_CONTENT, message.getFrom() + ":" + "发送了一个 IMAGE 消息");
                    mMediaFilePath = image.getThumbnailUrl();
                    mMessageType = IMessage.MessageType.RECEIVE_IMAGE.ordinal();
                    break;
                case Constants.IHXMessageType.MESSAGE_TYPE_RECV_VIDEO:
                    EMVideoMessageBody videoMessageBody = (EMVideoMessageBody) message.getBody();
                    LogHelper.i(TAG, "收到 Video 消息--" + "文件名称-" + videoMessageBody.getFileName() + "---文件地址--" + videoMessageBody.getThumbnailUrl());
                    bundle.putInt(Constants.IChat.MESSAGE_TYPE, Constants.IChat.RECEIVE_VIDEO);
                    bundle.putString(Constants.IChat.MESSAGE_CONTENT, message.getFrom() + ":" + "发送了一个 VIDEO 消息");
                    mMediaFilePath = videoMessageBody.getThumbnailUrl();
                    mMessageType = IMessage.MessageType.RECEIVE_VIDEO.ordinal();
                    break;
                case Constants.IHXMessageType.MESSAGE_TYPE_RECV_LOCATION:
                    EMLocationMessageBody locationMessageBody = (EMLocationMessageBody) message.getBody();
                    LogHelper.i(TAG, "收到 LOCATION 消息--" + "位置信息-" + locationMessageBody.getAddress());
                    bundle.putInt(Constants.IChat.MESSAGE_TYPE, Constants.IChat.RECEIVE_LOCATION);
                    bundle.putString(Constants.IChat.MESSAGE_CONTENT, message.getFrom() + ":" + "发送了一个 LOCATION 消息");
                    mMessageType = IMessage.MessageType.RECEIVE_LOCATION.ordinal();
                    break;
                case Constants.IHXMessageType.MESSAGE_TYPE_RECV_VOICE:
                    EMVoiceMessageBody voiceMessageBody = (EMVoiceMessageBody) message.getBody();
                    LogHelper.i(TAG, "收到 VOICE 消息--" + "文件名称-" + voiceMessageBody.getFileName() + "---文件地址--" + voiceMessageBody.getLocalUrl());
                    bundle.putInt(Constants.IChat.MESSAGE_TYPE, Constants.IChat.RECEIVE_VOICE);
                    bundle.putString(Constants.IChat.MESSAGE_CONTENT, message.getFrom() + ":" + "发送了一个 VOICE 消息");
                    mMediaFilePath = voiceMessageBody.getRemoteUrl();
                    mMessageType = IMessage.MessageType.RECEIVE_VOICE.ordinal();
                    break;
                case Constants.IHXMessageType.MESSAGE_TYPE_RECV_FILE:
                    EMFileMessageBody fileMessageBody = (EMFileMessageBody) message.getBody();
                    LogHelper.i(TAG, "收到 File 消息--" + "文件名称-" + fileMessageBody.getFileName() + "---文件地址--" + fileMessageBody.getLocalUrl());
                    bundle.putInt(Constants.IChat.MESSAGE_TYPE, Constants.IChat.RECEIVE_FILE);
                    bundle.putString(Constants.IChat.MESSAGE_CONTENT, message.getFrom() + ":" + "发送了一个 File 消息");
                    mMediaFilePath = fileMessageBody.getRemoteUrl();
                    mMessageType = IMessage.MessageType.RECEIVE_FILE.ordinal();
                    break;
                case Constants.IHXMessageType.MESSAGE_TYPE_RECV_EXPRESSION:
                    EMCmdMessageBody cmdMessageBody = (EMCmdMessageBody) message.getBody();
                    LogHelper.i(TAG, "收到 EMCmdMessageBody 消息--" + "EMCmdMessageBody-" + cmdMessageBody.getParams());
                    break;
            }
//            String ticker = EaseCommonUtils.getMessageDigest(message, mApplication);
            switch (message.getChatType()) {
                case Chat:
                    //最后一条聊天的类型（单聊/群聊）
                    bundle.putInt(Constants.IChat.MESSAGE_SIG_GROUP, Constants.IChat.OneChat);
                    mCurrentChatType = Constants.IChat.OneChat;
                    mOtherID = message.getFrom();
                    break;
                case GroupChat:
                    bundle.putInt(Constants.IChat.MESSAGE_SIG_GROUP, Constants.IChat.GroupChat);
                    mCurrentChatType = Constants.IChat.GroupChat;
                    break;
            }

            //最后一条聊天的消息 ID
            bundle.putString(Constants.IChat.MESSAGE_ID, message.getMsgId());

            //与谁聊天
            bundle.putString(Constants.IChat.otherId, message.getFrom());
            bundle.putString(Constants.IChat.MESSAGE_TIME, TimeUtils.getNowString());
            //将收到的消息发送出去用于保存聊天记录和会话记录
            DispatchManager.getInstance().sendMessage(Constants.IPostMessage.SEND_CURRENT_CHAT_PERSON, bundle);
            mMsgID = message.getMsgId();
            mMessageStatus = IMessage.MessageStatus.RECEIVE_SUCCEED.ordinal();

            mModel.saveChatMessage(mCurrentChatType, mMediaFilePath, mTxt, mMsgID, mMessageStatus, mOtherID, mMessageType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于接收历史会话记录
     */
    @Subscriber(tag = Constants.IWeiChat.UP_DB_HistorySessionMessage, mode = ThreadMode.MAIN)
    public void getSessionMessage(Message message) {
        Bundle bundle = message.getData();
        LogHelper.i(TAG, "WeiChat 模块 ----收到更新当前会话记录" +
                "---最后一条聊天的消息 ID---" + bundle.getString(Constants.IChat.MESSAGE_ID, "") +
                "---最后一条聊天的聊天消息类型---" + bundle.getInt(Constants.IChat.MESSAGE_TYPE, -1) +
                "---最后一条聊天的类型（单聊/群聊）---" + bundle.getInt(Constants.IChat.MESSAGE_SIG_GROUP, -1) +
                "---与谁聊天---" + bundle.getString(Constants.IChat.otherId, ""));


        WeiChatMessageHistoryEntity weiChatMessageHistoryEntity = new WeiChatMessageHistoryEntity();
        if (bundle.getInt(Constants.IChat.MESSAGE_SIG_GROUP, -1) == Constants.IChat.OneChat) {
            //适配器中消息类型
            weiChatMessageHistoryEntity.setItemType(Constants.IMessageType.OneChat);
        } else if (bundle.getInt(Constants.IChat.MESSAGE_SIG_GROUP, -1) == Constants.IChat.GroupChat) {
            weiChatMessageHistoryEntity.setItemType(Constants.IMessageType.GroupChat);
        }
        weiChatMessageHistoryEntity.setLastMessageContent(bundle.getString(Constants.IChat.MESSAGE_CONTENT));
        weiChatMessageHistoryEntity.setMessageTime(bundle.getString(Constants.IChat.MESSAGE_TIME));
        weiChatMessageHistoryEntity.setMessageId(bundle.getString(Constants.IChat.MESSAGE_ID, ""));
        weiChatMessageHistoryEntity.setOtherId(bundle.getString(Constants.IChat.otherId, ""));
        weiChatMessageHistoryEntity.setUserId(SPUtils.getInstance(Constants.ISP_Config.SP_NAME).getString(Constants.ISP_Config.USER_PHONE_NUMBER));

        if (!StringUtils.isEmpty(bundle.getString(Constants.IChat.MESSAGE_ID, "")))
            DBManager.getInstance().upAllAsync(weiChatMessageHistoryEntity, "userId = ? and otherId = ?", weiChatMessageHistoryEntity.getUserId(), weiChatMessageHistoryEntity.getOtherId(), new UpdateOrDeleteCallback() {
                @Override
                public void onFinish(int rowsAffected) {
                    LogHelper.i(TAG, "更新数据---" + (rowsAffected > 0 ? "成功" : "失败") + "--" + rowsAffected + "--条");
                    if (rowsAffected <= 0) {
                        DBManager.getInstance().saveAsync(weiChatMessageHistoryEntity, new SaveCallback() {
                            @Override
                            public void onFinish(boolean success) {
                                LogHelper.i(TAG, "保存数据---" + (success == true ? "成功" : "失败") + "--" + weiChatMessageHistoryEntity.toString());
                                if (success)
                                    DispatchManager.getInstance().dispatchMessage(Constants.IWeiChat.UP_Adapter_HistorySessionMessage, SPUtils.getInstance(Constants.ISP_Config.SP_NAME).getString(Constants.ISP_Config.USER_PHONE_NUMBER));
                            }
                        });
                    } else {
                        DispatchManager.getInstance().dispatchMessage(Constants.IWeiChat.UP_Adapter_HistorySessionMessage, SPUtils.getInstance(Constants.ISP_Config.SP_NAME).getString(Constants.ISP_Config.USER_PHONE_NUMBER));
                    }
                }
            });
        else
            LogHelper.i(TAG, "获取消息 ID 失败--" + bundle.getString(Constants.IChat.MESSAGE_ID, ""));
    }


}

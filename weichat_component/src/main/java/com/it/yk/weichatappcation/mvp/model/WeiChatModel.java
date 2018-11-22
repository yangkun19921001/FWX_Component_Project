package com.it.yk.weichatappcation.mvp.model;

import android.app.Application;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.Gson;
import com.iit.yk.chat_base_component.imuisample.models.DefaultUser;
import com.iit.yk.chat_base_component.imuisample.models.MyMessage;
import com.it.yk.fwx_chat_component.mvp.model.entity.ChatMessageEntity;
import com.it.yk.weichatappcation.mvp.contract.WeiChatContract;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.yk.component.sdk.core.Constants;
import com.yk.component.sdk.db.DBManager;
import com.yk.component.sdk.manager.DispatchManager;
import com.yk.component.sdk.utils.LogHelper;

import org.litepal.crud.callback.SaveCallback;

import java.util.List;

import javax.inject.Inject;

import cn.jiguang.imui.commons.models.IMessage;


@ActivityScope
public class WeiChatModel extends BaseModel implements WeiChatContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    List<MyMessage> mMessageList;
    private String TAG = getClass().getSimpleName();

    @Inject
    public WeiChatModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }


    public List<MyMessage> getHistoryList(List<ChatMessageEntity> list) {
        mMessageList.clear();
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

                    mMessageList.add(chatMessageEntity);
                } catch (Exception e) {
                    LogHelper.e("WeiChatModel", "getHistoryList--" + e.getMessage());
                }
            }
        }
        return mMessageList;
    }

    public MyMessage getMyMessage(ChatMessageEntity chatMeg) {
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
        chatMessageEntity.setUserInfo(new DefaultUser(chatMeg.getOtherId(), chatMeg.getUserId(), chatMeg.getUserId()));
        chatMessageEntity.setDuration(0);
        chatMessageEntity.setMediaFilePath(chatMeg.getFilePath());
        chatMessageEntity.setTimeString(chatMeg.getMessageTime());

        return chatMessageEntity;
    }


    /**
     * 保存在后台的时候聊天消息记录
     *
     * @param mCurrentChatType 当前聊天的组聊或者单聊
     * @param mediaFilePath    文件路径
     * @param txt              文本
     * @param msgID            消息ID
     * @param messageStatus    消息状态
     * @param otherID          与谁聊天
     * @param messageType
     */
    public void saveChatMessage(int mCurrentChatType, String mediaFilePath, String txt, String msgID, int messageStatus, String otherID, int messageType) {
        ChatMessageEntity chatMessageEntity = new ChatMessageEntity();
        chatMessageEntity.setChatType(mCurrentChatType);
        chatMessageEntity.setFilePath(mediaFilePath);
        chatMessageEntity.setMessageContent(txt);
        chatMessageEntity.setMessageId(msgID);
        chatMessageEntity.setMessageState(messageStatus);
        chatMessageEntity.setMessageTime(TimeUtils.getNowString());
        chatMessageEntity.setOtherId(otherID);
        chatMessageEntity.setUserId(SPUtils.getInstance(Constants.ISP_Config.SP_NAME).getString(Constants.ISP_Config.USER_PHONE_NUMBER));
        chatMessageEntity.setMessageType(messageType);
        DBManager.getInstance().saveAsync(chatMessageEntity, new SaveCallback() {
            @Override
            public void onFinish(boolean success) {
                LogHelper.i(TAG, success == true ? "saveChatMessage-保存成功" : "saveChatMessage-保存失败");
            }
        });
        chatMessageToMyMessage(chatMessageEntity);
    }


    /**
     * 将消息发送到聊天室
     *
     * @param chatMessageEntity
     */
    public void chatMessageToMyMessage(ChatMessageEntity chatMessageEntity) {
        MyMessage myMessage = getMyMessage(chatMessageEntity);
        DispatchManager.getInstance().sendMessage(Constants.IPostMessage.SESSION_TO_CHAT_ADAPTER, myMessage);
    }

}
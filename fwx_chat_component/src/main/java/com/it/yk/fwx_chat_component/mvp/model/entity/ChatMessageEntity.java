package com.it.yk.fwx_chat_component.mvp.model.entity;

import org.litepal.crud.LitePalSupport;

/**
 * Created by yangk on 2018/11/8.
 */

public class ChatMessageEntity extends LitePalSupport{
    /**
     * 我的ID
     */
    private String userId;
    /**
     * 聊天人的ID
     */
    private String otherId;
    /**
     * 单聊还是群聊
     */
    private int chatType;
    /**
     * 聊天消息类型
     */
    private int messageType;
    /**
     * 发送的消息还是接收到的消息
     */
    private int sendOrReceiver;
    /**
     * 消息内容
     */
    private String messageContent;

    /**
     * 附件地址
     */
    private String filePath;

    /**
     * 消息时间
     */
    private String messageTime;
    /**
     * 消息唯一ID
     */
    private String messageId;

    public int getMessageState() {
        return messageState;
    }

    public void setMessageState(int messageState) {
        this.messageState = messageState;
    }

    /**
     * 发送消息状态
     */
    private int messageState;

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getSendOrReceiver() {
        return sendOrReceiver;
    }

    public void setSendOrReceiver(int sendOrReceiver) {
        this.sendOrReceiver = sendOrReceiver;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


}

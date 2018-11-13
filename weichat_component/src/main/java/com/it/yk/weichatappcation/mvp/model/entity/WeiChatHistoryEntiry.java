package com.it.yk.weichatappcation.mvp.model.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by yangk on 2018/10/31.
 */

public class WeiChatHistoryEntiry implements MultiItemEntity {

    /**
     * 消息类型
     */
    private int messageType;

    /**
     * 消息唯一标识
     */
    private int messageOnlyId;

    /**
     * 消息名称
     */
    private String messageName;

    /**
     * 时间
     */
    private String messageTime;

    /**
     * 消息未读数量
     */
    private boolean isRead;

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    /**
     * 消息内容
     */
    private String messageContent;

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getMessageOnlyId() {
        return messageOnlyId;
    }

    public void setMessageOnlyId(int messageOnlyId) {
        this.messageOnlyId = messageOnlyId;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }




    @Override
    public int getItemType() {
        return messageType;
    }
}

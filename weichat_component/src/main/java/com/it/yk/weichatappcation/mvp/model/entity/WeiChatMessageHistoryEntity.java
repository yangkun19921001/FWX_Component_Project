package com.it.yk.weichatappcation.mvp.model.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by yangk on 2018/11/8.
 */
public class WeiChatMessageHistoryEntity extends LitePalSupport implements MultiItemEntity {

    /**
     * 我的ID
     */
    private String userId;

    /**
     * 对方ID
     */
    @Column(unique = true)
    private String otherId;

    /**
     * 消息唯一ID
     */
    private String messageId;

    /**
     * 发送消息的内容
     */
    private String lastMessageContent;

    /**
     * 发送消息的时间
     */
    private String messageTime;

    /**
     * 消息类型
     * 单聊，群聊，
     */
    private int itemType;

    /**
     * 未读消息个数
     */
    private int unReadMessageCount;

    public int getUnReadMessageCount() {
        return unReadMessageCount;
    }

    public void setUnReadMessageCount(int unReadMessageCount) {
        this.unReadMessageCount = unReadMessageCount;
    }



    public void setItemType(int itemType) {
        this.itemType = itemType;
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

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getLastMessageContent() {
        return lastMessageContent;
    }

    public void setLastMessageContent(String lastMessageContent) {
        this.lastMessageContent = lastMessageContent;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }



    @Override
    public int getItemType() {
        return itemType;
    }
}

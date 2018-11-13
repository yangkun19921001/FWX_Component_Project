package com.it.yk.contacts_component.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by yangk on 2018/11/6.
 * <p>
 * 新的朋友记录
 *
 * @Column(unique = true)              //是否唯一
 * @Column(defaultValue = "unknown")   //指定字段默认值
 * @Column(nullable = false)           //是否可以为空
 * @Column(ignore = true)              //是否可以忽略
 */

public class NewFriendsEntity extends LitePalSupport implements Parcelable{


    /**
     * 我的ID
     */
    private String userId;

    /**
     * 用户 ID
     */
    @Column(unique = true)
    private String otherId;

    /***
     * 当前请求或者好友的状态
     */

    private int state;

    /**
     * 请求或者回复内容
     */
    private String content;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    /**
     * 邀请的时间
     */
    private String time;


    public String getId() {
        return userId;
    }

    public void setId(String id) {
        this.userId = id;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public static enum UserState {
        //发送邀请
        REQUEST_START_INVITEED,
        //邀请成功
        REQUEST_SUCCEED_INVITEED,
        //邀请失败
        REQUEST_ERROR_INVITEED,

        //收到好友请求
        RECEIVE_FRIEND_INVITEED,
        //好友请求拒绝
        RECEIVE_FRIEND_ERROR_INVITEED,
        //好友请求成功
        RECEIVE_FRIEND_SUCCEED_INVITEED
    }

    @Override
    public String toString() {
        return "NewFriendsEntity{" +
                "id=" + userId +
                ", otherId=" + otherId +
                ", state=" + state +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.otherId);
        dest.writeInt(this.state);
        dest.writeString(this.content);
        dest.writeString(this.time);
    }

    public NewFriendsEntity() {
    }

    protected NewFriendsEntity(Parcel in) {
        this.userId = in.readString();
        this.otherId = in.readString();
        this.state = in.readInt();
        this.content = in.readString();
        this.time = in.readString();
    }

    public static final Creator<NewFriendsEntity> CREATOR = new Creator<NewFriendsEntity>() {
        @Override
        public NewFriendsEntity createFromParcel(Parcel source) {
            return new NewFriendsEntity(source);
        }

        @Override
        public NewFriendsEntity[] newArray(int size) {
            return new NewFriendsEntity[size];
        }
    };
}

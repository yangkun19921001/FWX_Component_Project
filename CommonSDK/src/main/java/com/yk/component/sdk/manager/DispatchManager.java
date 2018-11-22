package com.yk.component.sdk.manager;

import android.os.Bundle;
import android.os.Message;

import com.blankj.utilcode.util.StringUtils;
import com.jess.arms.integration.AppManager;
import com.yk.component.sdk.core.Constants;
import com.yk.component.sdk.utils.LogHelper;

import org.simple.eventbus.EventBus;

/**
 * Created by yangk on 2018/11/11.
 * 所有发出去消息的总管理
 */

public class DispatchManager<T> {

    private static DispatchManager instance;
    private String TAG = this.getClass().getSimpleName();

    public synchronized static DispatchManager getInstance() {
        if (instance == null)
            instance = new DispatchManager();
        return instance;
    }

    /**
     * 初始化接收到的消息
     *
     * @param appManager
     */
    public void initRevicesMessage(AppManager appManager) {
        appManager.setHandleListener(new AppManager.HandleListener() {
            @Override
            public void handleMessage(AppManager appManager, Message message) {
                switch (message.what) {
                    /**
                     * 收到消息
                     */
                    case Constants.IPostMessage.ON_MESSAGE_RECEIVED:
                        dispatchMessage(Constants.IWeiChat.RevicesMessage, message);
                        break;
                    /**
                     * 收到透传消息
                     */
                    case Constants.IPostMessage.ON_CMDMESSAGE_RECEIVED:
                        break;
                    /**
                     * 收到已读回执
                     */
                    case Constants.IPostMessage.ON_MESSAGE_READ:
                        break;
                    /**
                     * 收到已送达回执
                     */
                    case Constants.IPostMessage.ON_MESSAGE_DELIVERED:
                        break;
                    /**
                     * 消息被撤回
                     */
                    case Constants.IPostMessage.ON_MESSAGE_RECALLED:
                        break;
                    /**
                     * 消息状态变动
                     */
                    case Constants.IPostMessage.ON_MESSAGE_CHANGED:
                        break;
                    /**
                     * 保存当前与谁聊天的最后一条记录
                     */
                    case Constants.IPostMessage.SEND_CURRENT_CHAT_PERSON:
                        Bundle bundle = message.getData();
                        LogHelper.i(TAG, "收到更新当前会话记录" +
                                "---最后一条聊天的消息 ID---" + bundle.getString(Constants.IChat.MESSAGE_ID, "") +
                                "---最后一条聊天的聊天消息类型---" + bundle.getInt(Constants.IChat.MESSAGE_TYPE, -1) +
                                "---最后一条聊天的类型（单聊/群聊）---" + bundle.getInt(Constants.IChat.MESSAGE_SIG_GROUP, -1) +
                                "---与谁聊天---" + bundle.getString(Constants.IChat.otherId, ""));
                        dispatchMessage(Constants.IWeiChat.UP_DB_HistorySessionMessage, message);
                        break;

                    case Constants.IPostMessage.SESSION_TO_CHAT_ADAPTER:
                        LogHelper.i(TAG, "收到后台发来的消息 转发到适配器中");
                        dispatchMessage(Constants.IChat.SessionMessageToAdapter, message);
                        break;

                    case Constants.IPostMessage.SELECT_USER:
                        dispatchMessage(Constants.IChat.SELECT_USER, message);
                        break;
                    case Constants.IPostMessage.SELECT_USER_LIST:
                        dispatchMessage(Constants.IChat.SELECT_USER_LIST, message);
                        break;

                }
            }
        });
    }

    /**
     * 发送消息
     */
    public void sendMessage(int what, T t) {
        Message message = new Message();
        message.what = what;
        message.obj = t;
        AppManager.post(message);
    }

    public void sendMessage(int what, Bundle bundle) {
        Message message = new Message();
        message.what = what;
        message.setData(bundle);
        AppManager.post(message);
    }

    /**
     * 将收到的消息统一分发下去
     *
     * @param tag     接收消息注册的 TAG
     * @param message 发送消息的 body
     */
    public void dispatchMessage(String tag, Message message) {
        if (StringUtils.isEmpty(tag) || message == null)
            throw new NullPointerException("tag is null ? or message is null ?");
        EventBus.getDefault().post(message, tag);
    }

    /**
     * 将收到的消息统一分发下去
     *
     * @param tag     接收消息注册的 TAG
     * @param message 发送消息的 body
     */
    public void dispatchMessage(String tag, String message) {
        if (StringUtils.isEmpty(tag) || message == null)
            throw new NullPointerException("tag is null ? or message is null ?");
        EventBus.getDefault().post(message, tag);
    }
}

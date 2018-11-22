package com.hyphenate.easeui.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.EMServiceNotReadyException;

/**
 * Created by yangk on 2018/11/19.
 */

public class CallHelper {

    private ICallReceiver mICallReceiver;

    /**
     * 监听呼入电话监听
     */
    public void registerCallReceiver(Context context, ICallReceiver iCallReceiver) {
        mICallReceiver = iCallReceiver;
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        context.registerReceiver(new CallReceiver(), callFilter);
    }


    /**
     * 监听通话状态
     */
    public void addCallStateListener(final ICallStateListener iCallStateListener) {
        EMClient.getInstance().callManager().addCallStateChangeListener(new EMCallStateChangeListener() {
            @Override
            public void onCallStateChanged(CallState callState, CallError error) {
                switch (callState) {
                    case CONNECTING: // 正在连接对方
                        if (iCallStateListener != null)
                            iCallStateListener.onConnecting();
                        break;
                    case CONNECTED: // 双方已经建立连接
                        if (iCallStateListener != null)
                            iCallStateListener.onConnected();
                        break;

                    case ACCEPTED: // 电话接通成功
                        if (iCallStateListener != null)
                            iCallStateListener.onAccepted();
                        break;
                    case DISCONNECTED: // 电话断了
                        if (iCallStateListener != null)
                            iCallStateListener.onDisconnected();
                        break;
                    case NETWORK_UNSTABLE: //网络不稳定
                        if (error == CallError.ERROR_NO_DATA) {
                            //无通话数据

                        } else {
                        }
                        if (iCallStateListener != null)
                            iCallStateListener.onNetwork_Unstable();
                        break;
                    case NETWORK_NORMAL: //网络恢复正常
                        if (iCallStateListener != null)
                            iCallStateListener.onNetwork_Normal();
                        break;
                    default:
                        break;
                }

            }
        });
    }


    /**
     * 拨打语音电话
     */
    public void makeCall(CallType callType, String username, String ext) throws EMServiceNotReadyException {
        /**
         * 拨打语音通话
         * @param to
         * @throws EMServiceNotReadyException
         */
        switch (callType) {
            case AUDIO:
                EMClient.getInstance().callManager().makeVoiceCall(username, ext);
                break;
            case VIDEO:
                EMClient.getInstance().callManager().makeVideoCall(username, ext);
                break;
        }
    }

    /**
     * @return   获取拨打电话的扩展内容
     */
    public  String getCallExt(){
        return  EMClient.getInstance().callManager().getCurrentCallSession().getExt();
    }

    private class CallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 拨打方username
            String from = intent.getStringExtra("from");
            // call type
            String type = intent.getStringExtra("type");
            //跳转到通话页面
            if (mICallReceiver != null)
                mICallReceiver.onReceive(from, type);
        }
    }


    /**
     * 监听呼入电话监听
     */
    private interface ICallReceiver {
        void onReceive(String from, String type);
    }

    /**
     * 监听通话状态
     */
    public interface ICallStateListener {

        void onConnecting();

        void onConnected();

        void onAccepted();

        void onDisconnected();

        void onNetwork_Unstable();

        void onNetwork_Normal();
    }

    public enum CallType {
        AUDIO,
        VIDEO,
    }
}

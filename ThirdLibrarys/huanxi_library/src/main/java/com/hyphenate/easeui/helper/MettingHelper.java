package com.hyphenate.easeui.helper;

import android.app.Activity;
import android.view.ViewGroup;

import com.hyphenate.EMConferenceListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConference;
import com.hyphenate.chat.EMConferenceManager;
import com.hyphenate.chat.EMConferenceMember;
import com.hyphenate.chat.EMConferenceStream;
import com.hyphenate.chat.EMStreamParam;
import com.hyphenate.chat.EMStreamStatistics;
import com.hyphenate.easeui.view.ConferenceMemberView;

import java.util.List;

/**
 * Created by yangk on 2018/11/21.
 * 会议管理处理
 */
public class MettingHelper {
    /**
     * // 有成员加入
     * // 有成员离开
     * // 有流加入
     * // 有流移除
     * // 有流更新
     * // 被动离开
     * // 聊天室状态回调
     * // 流操作成功回调
     * // 当前说话者回调
     * // 收到会议邀请
     */
    private EMConferenceListener mEMConferenceListener;

    /**
     * 会议监听
     */
    public void addConferenceListener(final IConferenceListener iConferenceListener) {
        mEMConferenceListener = new EMConferenceListener() {

            @Override
            public void onMemberJoined(EMConferenceMember emConferenceMember) {
                // 有成员加入
                if (iConferenceListener != null)
                    iConferenceListener.onMemberJoined(emConferenceMember);
            }

            @Override
            public void onMemberExited(EMConferenceMember emConferenceMember) {
                // 有成员离开
                if (iConferenceListener != null)
                    iConferenceListener.onMemberExited(emConferenceMember);
            }

            @Override
            public void onStreamAdded(EMConferenceStream stream) {
                // 有流加入
                if (iConferenceListener != null)
                    iConferenceListener.onStreamAdded(stream);
            }

            @Override
            public void onStreamRemoved(EMConferenceStream stream) {
                // 有流移除
                if (iConferenceListener != null)
                    iConferenceListener.onStreamRemoved(stream);
            }

            @Override
            public void onStreamUpdate(EMConferenceStream stream) {
                // 有流更新
                if (iConferenceListener != null)
                    iConferenceListener.onStreamUpdate(stream);
            }

            @Override
            public void onPassiveLeave(int error, String message) {
                // 被动离开
                if (iConferenceListener != null)
                    iConferenceListener.onPassiveLeave(error, message);
            }

            @Override
            public void onConferenceState(ConferenceState state) {
                // 聊天室状态回调
                if (iConferenceListener != null)
                    iConferenceListener.onConferenceState(state);
            }

            @Override
            public void onStreamStatistics(EMStreamStatistics emStreamStatistics) {
                if (iConferenceListener != null)
                    iConferenceListener.onStreamStatistics(emStreamStatistics);
            }

            @Override
            public void onStreamSetup(String streamId) {
                // 流操作成功回调
                if (iConferenceListener != null)
                    iConferenceListener.onStreamSetup(streamId);
            }

            @Override
            public void onSpeakers(final List<String> speakers) {
                // 当前说话者回调
                if (iConferenceListener != null)
                    iConferenceListener.onSpeakers(speakers);
            }

            @Override
            public void onReceiveInvite(String confId, String password, String extension) {
                // 收到会议邀请
               /* if(easeUI.getTopActivity().getClass().getSimpleName().equals("ConferenceActivity")) {
                    return;
                }
                Intent conferenceIntent = new Intent(appContext, ConferenceActivity.class);
                conferenceIntent.putExtra(Constant.EXTRA_CONFERENCE_ID, confId);
                conferenceIntent.putExtra(Constant.EXTRA_CONFERENCE_PASS, password);
                conferenceIntent.putExtra(Constant.EXTRA_CONFERENCE_IS_CREATOR, false);
                conferenceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                appContext.startActivity(conferenceIntent);*/

                if (iConferenceListener != null)
                    iConferenceListener.onReceiveInvite(confId, password, extension);
            }

            @Override
            public void onRoleChanged(EMConferenceManager.EMConferenceRole emConferenceRole) {
                if (iConferenceListener != null)
                    iConferenceListener.onRoleChanged(emConferenceRole);
            }
        };
        // 在Activity#onCreate()中添加监听
        EMClient.getInstance().conferenceManager().addConferenceListener(mEMConferenceListener);
    }

    /**
     * 删除会议监听
     */
    public void removeConferenceListener() {
        if (mEMConferenceListener != null)
            // 在Activity#onDestroy()中移除监听
            EMClient.getInstance().conferenceManager().removeConferenceListener(mEMConferenceListener);
    }


    /**
     * 创建会议
     */
    public void createMetting(EMConferenceManager.EMConferenceType emConferenceType, String conference_password, final ICreateAndJoinConferenceListener iCreateJoinConferenceListener) {
        EMClient.getInstance().conferenceManager().createAndJoinConference(emConferenceType,
                conference_password, new EMValueCallBack<EMConference>() {
                    @Override
                    public void onSuccess(EMConference value) {
                        // 返回当前会议对象实例 value
                        // 可进行推流等相关操作
                        // 运行在子线程中，勿直接操作UI
                        if (iCreateJoinConferenceListener != null)
                            iCreateJoinConferenceListener.onSuccess(value);
                    }

                    @Override
                    public void onError(int error, String errorMsg) {
                        // 运行在子线程中，勿直接操作UI
                        if (iCreateJoinConferenceListener != null)
                            iCreateJoinConferenceListener.onError(error, errorMsg);
                    }
                });
    }

    public void joinConference(String conferenceId, String password, final ICreateAndJoinConferenceListener iCreateAndJoinConferenceListener) {
        EMClient.getInstance().conferenceManager().joinConference(conferenceId, password, new
                EMValueCallBack<EMConference>() {
                    @Override
                    public void onSuccess(EMConference value) {
                        // 返回当前会议对象实例 value
                        // 可进行推流等相关操作
                        // 运行在子线程中，勿直接操作UI
                        if (iCreateAndJoinConferenceListener != null)
                            iCreateAndJoinConferenceListener.onSuccess(value);
                    }

                    @Override
                    public void onError(int error, String errorMsg) {
                        // 运行在子线程中，勿直接操作UI
                        if (iCreateAndJoinConferenceListener != null)
                            iCreateAndJoinConferenceListener.onError(error, errorMsg);
                    }
                });
    }

    /**
     * 发布本地视频流
     */
    public  void pubLocalStream(EMValueCallBack<String> emValueCallBack) {
        EMStreamParam param = new EMStreamParam();
        param.setStreamType(EMConferenceStream.StreamType.NORMAL);
        param.setVideoOff(false);
        param.setAudioOff(false);

        EMClient.getInstance().conferenceManager().publish(param, emValueCallBack);
    }

    public void subscribeStream(EMConferenceStream stream, Activity activity, ViewGroup viewGroup,EMValueCallBack<String> emValueCallBack){
        ConferenceMemberView memberView = new ConferenceMemberView(activity);
        // 添加当前view到界面
        viewGroup.addView(memberView);
        // 设置当前view的一些状态
        memberView.setUsername(stream.getUsername());
        memberView.setStreamId(stream.getStreamId());
        memberView.setAudioOff(stream.isAudioOff());
        memberView.setVideoOff(stream.isVideoOff());
        memberView.setDesktop(stream.getStreamType() == EMConferenceStream.StreamType.DESKTOP);

        EMClient.getInstance().conferenceManager().subscribe(stream, memberView.getSurfaceView(), emValueCallBack);
    }

    /**
     * 邀请加入直播
     */
    public interface ICreateAndJoinConferenceListener {
        void onSuccess(EMConference value);
        // 返回当前会议对象实例 value
        // 可进行推流等相关操作
        // 运行在子线程中，勿直接操作UI
        void onError(int error, String errorMsg);
        // 运行在子线程中，勿直接操作UI
    }

    /**
     * 会议监听
     */
    public interface IConferenceListener {
        // 有成员加入
        void onMemberJoined(EMConferenceMember var1);

        // 有成员离开
        void onMemberExited(EMConferenceMember var1);

        // 有流加入
        void onStreamAdded(EMConferenceStream var1);

        // 有流移除
        void onStreamRemoved(EMConferenceStream var1);

        // 有流更新
        void onStreamUpdate(EMConferenceStream var1);

        // 被动离开
        void onPassiveLeave(int var1, String var2);

        // 聊天室状态回调
        void onConferenceState(EMConferenceListener.ConferenceState var1);

        void onStreamStatistics(EMStreamStatistics var1);

        // 流操作成功回调
        void onStreamSetup(String var1);

        // 当前说话者回调
        void onSpeakers(List<String> var1);

        // 收到会议邀请
        void onReceiveInvite(String var1, String var2, String var3);

        void onRoleChanged(EMConferenceManager.EMConferenceRole var1);
    }
}

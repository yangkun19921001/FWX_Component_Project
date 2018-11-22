package com.it.yk.fwx_chat_component.mvp.presenter;

import android.app.Application;

import com.blankj.utilcode.util.ToastUtils;
import com.hyphenate.EMConferenceListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConference;
import com.hyphenate.chat.EMConferenceManager;
import com.hyphenate.chat.EMConferenceMember;
import com.hyphenate.chat.EMConferenceStream;
import com.hyphenate.chat.EMStreamParam;
import com.hyphenate.chat.EMStreamStatistics;
import com.hyphenate.easeui.helper.HXHelper;
import com.hyphenate.easeui.helper.MettingHelper;
import com.hyphenate.easeui.view.ConferenceMemberView;
import com.hyphenate.util.EMLog;
import com.it.yk.fwx_chat_component.R;
import com.it.yk.fwx_chat_component.mvp.contract.GroupMettingContract;
import com.it.yk.fwx_chat_component.widget.MemberViewGroup;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.yk.component.sdk.core.Constants;
import com.yk.component.sdk.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class GroupMettingPresenter extends BasePresenter<GroupMettingContract.Model, GroupMettingContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private ConferenceMemberView localView;
    private MemberViewGroup callConferenceViewGroup;


    private List<EMConferenceStream> streamList = new ArrayList<>();
    private EMStreamParam normalParam;
    private EMConference conference;

    @Inject
    public GroupMettingPresenter(GroupMettingContract.Model model, GroupMettingContract.View rootView
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

    public void initView(){
        callConferenceViewGroup = (MemberViewGroup) mAppManager.getTopActivity().findViewById(R.id.surface_view_group);
        normalParam = new EMStreamParam();
        normalParam.setStreamType(EMConferenceStream.StreamType.NORMAL);
        normalParam.setVideoOff(true);
        normalParam.setAudioOff(false);
    }

    /**
     * 获取会议组员
     */
    public void getMettingMember() {
        if (mAppManager.getTopActivity().getIntent().getExtras() != null && mAppManager.getTopActivity().getIntent().getExtras().getStringArray(Constants.IChat.SELECT_USER_LIST) != null){
            String[] mettingMember = mAppManager.getTopActivity().getIntent().getExtras().getStringArray(Constants.IChat.SELECT_USER_LIST);
            LogHelper.i(TAG,"-获取当前会议组员-");
                initLocalConferenceView();

                HXHelper.getInstance().getMettingHelper().createMetting(EMConferenceManager.EMConferenceType.LargeCommunication, "", new MettingHelper.ICreateAndJoinConferenceListener() {

                    @Override
                    public void onSuccess(EMConference value) {
                        conference = value;
                        startAudioTalkingMonitor();
                        publish();
                    }

                    @Override
                    public void onError(int error, String errorMsg) {

                    }
                });

        }
    }

    /**
     * 开始推自己的数据
     */
    private void publish() {
        addSelfToList();

        EMClient.getInstance().conferenceManager().publish(normalParam, new EMValueCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                conference.setPubStreamId(value, EMConferenceStream.StreamType.NORMAL);
                localView.setStreamId(value);

                streamList.get(0).setStreamId(value);

                // Start to watch the phone call state.
            }

            @Override
            public void onError(int error, String errorMsg) {
                EMLog.e(TAG, "publish failed: error=" + error + ", msg=" + errorMsg);
            }
        });
    }


    private void addSelfToList() {
        EMConferenceStream localStream = new EMConferenceStream();
        localStream.setUsername(EMClient.getInstance().getCurrentUser());
        localStream.setStreamId("local-stream");
        streamList.add(localStream);
    }

    private void startAudioTalkingMonitor() {
        EMClient.getInstance().conferenceManager().startMonitorSpeaker(300);
    }

    private void stopAudioTalkingMonitor() {
        EMClient.getInstance().conferenceManager().stopMonitorSpeaker();
    }

    /**
     * 初始化多人音视频画面管理控件
     */
    private void initLocalConferenceView() {
        localView = new ConferenceMemberView(mAppManager.getTopActivity());
        localView.setVideoOff(true);
        localView.setAudioOff(true);
        localView.setUsername(EMClient.getInstance().getCurrentUser());
        EMClient.getInstance().conferenceManager().setLocalSurfaceView(localView.getSurfaceView());
        callConferenceViewGroup.addView(localView);
    }

    public void addJoinAndCreateMettingListener(){
     HXHelper.getInstance().getMettingHelper().addConferenceListener(new MettingHelper.IConferenceListener() {
         @Override
         public void onMemberJoined(EMConferenceMember var1) {
            mAppManager.getTopActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showShort(" joined conference!"+var1.memberName);
                }
            });
         }

         @Override
         public void onMemberExited(EMConferenceMember var1) {
             mAppManager.getTopActivity().runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     ToastUtils.showShort(" joined conference!--"+var1.memberName);
                 }
             });
         }

         @Override
         public void onStreamAdded(EMConferenceStream var1) {

         }

         @Override
         public void onStreamRemoved(EMConferenceStream var1) {

         }

         @Override
         public void onStreamUpdate(EMConferenceStream var1) {

         }

         @Override
         public void onPassiveLeave(int var1, String var2) {

         }

         @Override
         public void onConferenceState(EMConferenceListener.ConferenceState var1) {

         }

         @Override
         public void onStreamStatistics(EMStreamStatistics var1) {

         }

         @Override
         public void onStreamSetup(String var1) {

         }

         @Override
         public void onSpeakers(List<String> var1) {

         }

         @Override
         public void onReceiveInvite(String var1, String var2, String var3) {

         }

         @Override
         public void onRoleChanged(EMConferenceManager.EMConferenceRole var1) {

         }
     });
    }
}

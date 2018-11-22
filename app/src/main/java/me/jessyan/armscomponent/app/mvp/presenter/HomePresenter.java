package me.jessyan.armscomponent.app.mvp.presenter;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.helper.HXHelper;
import com.hyphenate.easeui.helper.MessageHelper;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.it.yk.contacts_component.mvp.model.entity.NewFriendsEntity;
import com.it.yk.contacts_component.mvp.ui.fragment.ContactsFragment;
import com.it.yk.find_component.mvp.ui.fragment.FindFragment;
import com.it.yk.fwx_chat_component.mvp.ui.activity.ChatComponentActivity;
import com.it.yk.me_component.mvp.ui.fragment.MeFragment;
import com.it.yk.weichatappcation.mvp.ui.fragment.WeiChatFragment;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;
import com.yk.component.res.tab.AlphaTabsIndicator;
import com.yk.component.res.tab.OnTabChangedListner;
import com.yk.component.res.view.TriangleDrawable;
import com.yk.component.sdk.core.Constants;
import com.yk.component.sdk.core.RouterHub;
import com.yk.component.sdk.db.DBManager;
import com.yk.component.sdk.manager.DispatchManager;
import com.yk.component.sdk.utils.EventBusTags;
import com.yk.component.sdk.utils.LogHelper;
import com.yk.component.sdk.utils.Utils;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import org.litepal.crud.callback.SaveCallback;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import me.jessyan.armscomponent.app.R;
import me.jessyan.armscomponent.app.mvp.contract.HomeContract;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import static com.hyphenate.easeui.utils.EaseUserUtils.getUserInfo;


@ActivityScope
public class HomePresenter extends BasePresenter<HomeContract.Model, HomeContract.View> implements View.OnClickListener {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;


    /**
     * 主页面核心页面
     */
    private List<Fragment> mFragments;
    /**
     * 当前的页面
     */
    private int mCurIndex = 0;

    /**
     * 当前页面标签
     */
    private String mCurrentTitlt = "";

    private WeiChatFragment mWeiChatFragment;
    private ContactsFragment mContactsFragment;
    private FindFragment mFindFragment;
    private MeFragment mMeFragment;
    private AlphaTabsIndicator mAlphaIndicator;
    private TextView mmPublicToolbarTitle;
    private ImageView mmPublicIvMore;
    private RelativeLayout mPublicToolbarMore;
    private EasyPopup mCirclePop;
    private AppManager mAppManager;

    @Inject
    public HomePresenter(HomeContract.Model model, HomeContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager mAppManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = mAppManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    /**
     * 初始化 TAB
     *
     * @param savedInstanceState
     * @param supportFragmentManager
     * @param alphaIndicator
     * @param mPublicToolbarTitle
     * @param mPublicIvMore
     * @param publicToolbarMore
     */
    public void initMainTab(Bundle savedInstanceState, FragmentManager supportFragmentManager, AlphaTabsIndicator alphaIndicator, TextView mPublicToolbarTitle, ImageView mPublicIvMore, RelativeLayout publicToolbarMore) {
        mAlphaIndicator = alphaIndicator;
        mmPublicToolbarTitle = mPublicToolbarTitle;
        mmPublicIvMore = mPublicIvMore;
        mPublicToolbarMore = publicToolbarMore;
        if (savedInstanceState == null) {
            mWeiChatFragment = WeiChatFragment.newInstance();
            mContactsFragment = ContactsFragment.newInstance();
            mFindFragment = FindFragment.newInstance();
            mMeFragment = MeFragment.newInstance();
        } else {
            mCurIndex = savedInstanceState.getInt(EventBusTags.ACTIVITY_FRAGMENT_REPLACE);
            FragmentManager fm = supportFragmentManager;
            mWeiChatFragment = (WeiChatFragment) FragmentUtils.findFragment(fm, WeiChatFragment.class);
            mContactsFragment = (ContactsFragment) FragmentUtils.findFragment(fm, ContactsFragment.class);
            mFindFragment = (FindFragment) FragmentUtils.findFragment(fm, FindFragment.class);
            mMeFragment = (MeFragment) FragmentUtils.findFragment(fm, MeFragment.class);
        }
        if (mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.add(mWeiChatFragment);
            mFragments.add(mContactsFragment);
            mFragments.add(mFindFragment);
            mFragments.add(mMeFragment);
        }
        FragmentUtils.add(supportFragmentManager, mFragments, R.id.home_frame, mCurIndex);
    }

    /**
     * 监听底部按钮点击事件
     */
    public void addButtonClickListener() {
        mAlphaIndicator.setOnTabChangedListner(new OnTabChangedListner() {
            @Override
            public void onTabSelected(int tabNum) {
                switch (tabNum) {
                    case 0:
                        mCurIndex = 0;
                        mCurrentTitlt = mWeiChatFragment.getTitle();
                        break;
                    case 1:
                        mCurIndex = 1;
                        mCurrentTitlt = mContactsFragment.getTitle();
                        break;
                    case 2:
                        mCurIndex = 2;
                        mCurrentTitlt = mFindFragment.getTitle();
                        break;
                    case 3:
                        mCurIndex = 3;
                        mCurrentTitlt = mMeFragment.getTitle();
                        break;
                    default:
                        break;
                }
                mmPublicToolbarTitle.setText(mCurrentTitlt);
                FragmentUtils.showHide(mCurIndex, mFragments);
                mmPublicIvMore.setVisibility(View.VISIBLE);
                if (mCurIndex == 0) {
                    mmPublicIvMore.setImageDrawable(ArmsUtils.getDrawablebyResource(mApplication, R.drawable.ic_add_black_24dp));
                } else if (mCurIndex == 1) {
                    mmPublicIvMore.setImageDrawable(ArmsUtils.getDrawablebyResource(mApplication, R.drawable.ic_person_add_black_24dp));
                } else {
                    mmPublicIvMore.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 默认显示的 页面
     */
    public void showDefFragment() {
        mAlphaIndicator.setTabCurrenItem(0);
        mAlphaIndicator.getTabView(mCurIndex).showNumber(66);
        mAlphaIndicator.getTabView(2).showPoint();
        mmPublicToolbarTitle.setText(mWeiChatFragment.getTitle());
    }

    /**
     * 标题栏右侧图标点击事件
     */
    public void addTitleMoreClickListener() {
        if (mCurIndex == 0) {
            showPopupInfo();
        } else {
            showAddFriend();
        }

    }

    private void showPopupInfo() {
        //                .setBackgroundDimEnable(true)
//                .setDimValue(0.5f)
//                .setDimColor(Color.RED)
//                .setDimView(mTitleBar)
        mCirclePop = EasyPopup.create()
                .setContext(mAppManager.getCurrentActivity())
                .setContentView(R.layout.layout_right_pop)
                .setAnimationStyle(R.style.public_RightPopAnim)
                .setOnViewListener(new EasyPopup.OnViewListener() {
                    @Override
                    public void initViews(View view, EasyPopup basePopup) {
                        View arrowView = view.findViewById(R.id.v_arrow);
                        arrowView.setBackground(new TriangleDrawable(TriangleDrawable.TOP, ArmsUtils.getColor(mApplication, R.color.black_dft)));
                    }
                })
                .setFocusAndOutsideEnable(true)
//                .setBackgroundDimEnable(true)
//                .setDimValue(0.5f)
//                .setDimColor(Color.RED)
//                .setDimView(mTitleBar)
                .apply();

        int offsetX = SizeUtils.dp2px(20) - mPublicToolbarMore.getWidth() / 2;
        int offsetY = 10;
        mCirclePop.showAtAnchorView(mPublicToolbarMore, YGravity.BELOW, XGravity.ALIGN_RIGHT, offsetX, offsetY);
        mCirclePop.findViewById(R.id.send_group_chat).setOnClickListener(this);
        mCirclePop.findViewById(R.id.tv_add_friend).setOnClickListener(this);
        mCirclePop.findViewById(R.id.tv_sao_yi_sao).setOnClickListener(this);
        mCirclePop.findViewById(R.id.tv_shou_fu_kuan).setOnClickListener(this);
    }


    private void showAddFriend() {
        Utils.navigation(RouterHub.FRIENDS_AddFriendsActivity);
    }

    public void checkIMServer() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                HXHelper.getInstance().getLoginEngine().addConnectListener(new EMConnectionListener() {
                    @Override
                    public void onConnected() {
                        emitter.onNext("IM 服务连接成功");
                        emitter.onComplete();
                        LogHelper.d(TAG, "IM 服务连接成功");
                    }

                    @Override
                    public void onDisconnected(int i) {
                        String error = HXHelper.getInstance().getLoginEngine().connectErrorCode(i);
                        LogHelper.d(TAG, "IM 服务连接失败");
                        emitter.onNext("服务连接失败：" + error);
                        emitter.onComplete();
                    }
                });
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<String>(mErrorHandler) {
                    @Override
                    public void onNext(String s) {
                        ToastUtils.showShort(s);
                    }
                });

    }

    @Override
    public void onClick(View v) {
        mCirclePop.dismiss();
        if (v.getId() == R.id.send_group_chat) {
            ARouter.getInstance().build(RouterHub.Chat_AddGroupActivity)
                    .withInt(Constants.IChat.OPEN_SELECT_USER, Constants.IChat.CREATE_GROUP).navigation();
        } else if (v.getId() == R.id.tv_add_friend) {
            Utils.navigation(RouterHub.FRIENDS_AddFriendsActivity);
        } else if (v.getId() == R.id.tv_sao_yi_sao) {

        } else if (v.getId() == R.id.tv_shou_fu_kuan) {
        } else {
        }
    }

    /**
     * 添加好友状态监听
     */
    public void addFriendManagerListener() {
        HXHelper.getInstance().getFriendEngine().setContactListener(new EMContactListener() {
            @Override
            public void onContactInvited(String username, String reason) {
                LogHelper.d(TAG, "收到好友邀请---" + username + "--收到好友邀请理由--" + reason);
                //收到好友邀请
                NewFriendsEntity newFriendsEntity = new NewFriendsEntity();
                newFriendsEntity.setContent("收到好友邀请理由:" + reason);
                newFriendsEntity.setOtherId(username);
                newFriendsEntity.setState(NewFriendsEntity.UserState.RECEIVE_FRIEND_INVITEED.ordinal());
                newFriendsEntity.setId(SPUtils.getInstance(Constants.ISP_Config.SP_NAME).getString(Constants.ISP_Config.USER_PHONE_NUMBER));
                newFriendsEntity.setTime(TimeUtils.getNowString());

                DBManager.getInstance().saveAsync(newFriendsEntity, new SaveCallback() {
                    @Override
                    public void onFinish(boolean success) {
                        LogHelper.d(TAG, success == true ? "onContactInvited-成功" : "onContactInvited-失败");
                    }
                });

            }

            @Override
            public void onFriendRequestAccepted(String username) {
                //好友请求被同意
                LogHelper.d(TAG, "收到好友请求被同意---" + username);

                NewFriendsEntity newFriendsEntity = new NewFriendsEntity();
                newFriendsEntity.setOtherId(username);
                newFriendsEntity.setContent("收到好友请求被同意");
                newFriendsEntity.setState(NewFriendsEntity.UserState.REQUEST_SUCCEED_INVITEED.ordinal());
                newFriendsEntity.setId(SPUtils.getInstance(Constants.ISP_Config.SP_NAME).getString(Constants.ISP_Config.USER_PHONE_NUMBER));
                newFriendsEntity.setTime(TimeUtils.getNowString());

                DBManager.getInstance().upAllAsync(newFriendsEntity, "userId = ? and otherId = ?", SPUtils.getInstance(Constants.ISP_Config.SP_NAME).getString(Constants.ISP_Config.USER_PHONE_NUMBER), username, new UpdateOrDeleteCallback() {
                    @Override
                    public void onFinish(int rowsAffected) {
                        LogHelper.d(TAG, rowsAffected > 0 ? "保存数据-收到好友请求被同意" : "保存数据-收到好友请求被同意");
                    }
                });
            }

            @Override
            public void onFriendRequestDeclined(String username) {
                //好友请求被拒绝
                LogHelper.d(TAG, "收到好友请求被拒绝---" + username);
                NewFriendsEntity newFriendsEntity = new NewFriendsEntity();
                newFriendsEntity.setOtherId(username);
                newFriendsEntity.setContent("收到好友请求被拒绝");
                newFriendsEntity.setState(NewFriendsEntity.UserState.REQUEST_ERROR_INVITEED.ordinal());
                newFriendsEntity.setId(SPUtils.getInstance(Constants.ISP_Config.SP_NAME).getString(Constants.ISP_Config.USER_PHONE_NUMBER));
                newFriendsEntity.setTime(TimeUtils.getNowString());

                DBManager.getInstance().upAllAsync(newFriendsEntity, "userId = ? and otherId = ?", SPUtils.getInstance(Constants.ISP_Config.SP_NAME).getString(Constants.ISP_Config.USER_PHONE_NUMBER), username, new UpdateOrDeleteCallback() {
                    @Override
                    public void onFinish(int rowsAffected) {
                        LogHelper.d(TAG, rowsAffected > 0 ? "保存数据-收到好友请求被同意" : "保存数据-收到好友请求被同意");
                    }
                });
            }

            @Override
            public void onContactDeleted(String username) {
                //被删除时回调此方法
                LogHelper.d(TAG, "被删除时回调此方法---" + username);
            }


            @Override
            public void onContactAdded(String username) {
                //增加了联系人时回调此方法
                LogHelper.d(TAG, "增加了联系人时回调此方法---" + username);
            }
        });
    }

    /**
     * 添加收到消息的监听
     */
    public void addMessageReceived() {
        HXHelper.getInstance().getMessageEngine().addRecevingMessageListener(new MessageHelper.IMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                LogHelper.d(TAG, "onMessageReceived -- 收到消息--" + (messages.size() > 0 ? messages.get(messages.size() - 1).toString() : "没有数据"));
                DispatchManager.getInstance().sendMessage(Constants.IPostMessage.ON_MESSAGE_RECEIVED, messages);
                EaseUI.getInstance().getNotifier().vibrateAndPlayTone(messages.get(0));
                EaseUI.getInstance().getNotifier().notify(messages);
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
                LogHelper.d(TAG, "onCmdMessageReceived -- 收到透传消息--" + (messages.size() > 0 ? messages.get(messages.size() - 1).toString() : "没有数据"));
                DispatchManager.getInstance().sendMessage(Constants.IPostMessage.ON_CMDMESSAGE_RECEIVED, messages);
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                //收到已读回执
                LogHelper.d(TAG, "onMessageRead -- 收到已读回执--" + (messages.size() > 0 ? messages.get(messages.size() - 1).toString() : "没有数据"));
                DispatchManager.getInstance().sendMessage(Constants.IPostMessage.ON_MESSAGE_READ, messages);
            }

            @Override
            public void onMessageDelivered(List<EMMessage> messages) {
                //收到已送达回执
                LogHelper.d(TAG, "onMessageDelivered -- 收到已送达回执--" + (messages.size() > 0 ? messages.get(messages.size() - 1).toString() : "没有数据"));
                DispatchManager.getInstance().sendMessage(Constants.IPostMessage.ON_MESSAGE_DELIVERED, messages);
            }

            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                //消息被撤回
                LogHelper.d(TAG, "onMessageRecalled -- 消息被撤回--" + (messages.size() > 0 ? messages.get(messages.size() - 1).toString() : "没有数据"));
                DispatchManager.getInstance().sendMessage(Constants.IPostMessage.ON_MESSAGE_RECALLED, messages);
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
                LogHelper.d(TAG, "onMessageChanged -- 消息状态变动--" + message.toString());
                DispatchManager.getInstance().sendMessage(Constants.IPostMessage.ON_MESSAGE_CHANGED, message);
            }
        });
    }

    public void dispatchTask() {
        DispatchManager.getInstance().initRevicesMessage(mAppManager);
    }

    /**
     * 通知栏显示
     */
    public void showNotifi() {
        EaseUI.getInstance().getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {
                //you can update title here
                return null;
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //you can update icon here
                return 0;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                // be used on notification bar, different text according the message type.
                String ticker = EaseCommonUtils.getMessageDigest(message, mApplication);
                if (message.getType() == EMMessage.Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                EaseUser user = getUserInfo(message.getFrom());
                if (user != null) {
                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format(mApplication.getString(com.hyphenate.easeui.R.string.at_your_in_group), user.getNick());
                    }
                    return user.getNick() + ": " + ticker;
                } else {
                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format(mApplication.getString(com.hyphenate.easeui.R.string.at_your_in_group), message.getFrom());
                    }
                    return message.getFrom() + ": " + ticker;
                }
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                // here you can customize the text.
                // return fromUsersNum + "contacts send " + messageNum + "messages to you";
                return null;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                // you can set what activity you want display when user click the notification
                Intent intent = new Intent(mApplication, ChatComponentActivity.class);
                // open calling activity if there is call
//                if(isVideoCalling){
//                    intent = new Intent(mApplication, VideoCallActivity.class);
//                }else if(isVoiceCalling){
//                    intent = new Intent(mApplication, VoiceCallActivity.class);
//                }else{
                EMMessage.ChatType chatType = message.getChatType();
                if (chatType == EMMessage.ChatType.Chat) { // single chat message
                    intent.putExtra(Constants.IChat.otherId, message.getFrom());
                    intent.putExtra(Constants.IChat.ChatType, Constants.IChat.OneChat);
                } else { // group chat message
                    // message.getTo() is the group id
                    if (chatType == EMMessage.ChatType.GroupChat) {
                        intent.putExtra(Constants.IChat.otherId, message.getTo());
                        intent.putExtra(Constants.IChat.ChatType, Constants.IChat.GroupChat);
                    }

                }
//                }
                return intent;
            }
        });
    }

    /**
     * 群组操作设置监听
     */
    public void addGroupStateListener() {

        HXHelper.getInstance().getGroupHelper().addGroupStateListenee(new EMGroupChangeListener() {
            @Override
            public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
                //接收到群组加入邀请
                LogHelper.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + "--接收到群组加入邀请--groupId--" + groupId + "--groupName--" + groupName + "--inviter--" + inviter + "--reason--" + reason);
            }

            @Override
            public void onRequestToJoinReceived(String groupId, String groupName, String applyer, String reason) {
                //用户申请加入群
                LogHelper.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + "--用户申请加入群--groupId--" + groupId + "--groupName--" + groupName + "--applyer--" + applyer + "--reason--" + reason);
            }

            @Override
            public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {
                //加群申请被同意
                LogHelper.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + "--加群申请被同意--groupId--" + groupId + "--groupName--" + groupName + "--accepter--" + accepter);
            }

            @Override
            public void onRequestToJoinDeclined(String groupId, String groupName, String decliner, String reason) {
                //加群申请被拒绝
                LogHelper.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + "--加群申请被拒绝--groupId--" + groupId + "--groupName--" + groupName + "--reason--" + reason);
            }

            @Override
            public void onInvitationAccepted(String groupId, String inviter, String reason) {
                //群组邀请被同意
                LogHelper.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + "--加群申请被拒绝--groupId--" + groupId + "--inviter--" + inviter + "--reason--" + reason);
            }

            @Override
            public void onInvitationDeclined(String groupId, String invitee, String reason) {
                //群组邀请被拒绝
                LogHelper.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + "--加群申请被拒绝--groupId--" + groupId + "--invitee--" + invitee + "--reason--" + reason);
            }

            @Override
            public void onUserRemoved(String s, String s1) {
            }

            @Override
            public void onGroupDestroyed(String s, String s1) {

            }

            @Override
            public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
                //接收邀请时自动加入到群组的通知
                LogHelper.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + "--接收邀请时自动加入到群组的通知--groupId--" + groupId + "--inviter--" + inviter + "--inviteMessage--" + inviteMessage);
            }

            @Override
            public void onMuteListAdded(String groupId, final List<String> mutes, final long muteExpire) {
                //成员禁言的通知
                LogHelper.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + "--成员禁言的通知--groupId--" + groupId + "--mutes--" + mutes + "--muteExpire--" + muteExpire);
            }

            @Override
            public void onMuteListRemoved(String groupId, final List<String> mutes) {
                //成员从禁言列表里移除通知
                LogHelper.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + "--成员从禁言列表里移除通知--groupId--" + groupId + "--groupId--" + groupId + "--muteExpire--" + mutes);
            }

            @Override
            public void onAdminAdded(String groupId, String administrator) {
                //增加管理员的通知
                LogHelper.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + "--增加管理员的通知--groupId--" + groupId + "--groupId--" + groupId + "--administrator--" + administrator);
            }

            @Override
            public void onAdminRemoved(String groupId, String administrator) {
                //管理员移除的通知
                LogHelper.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + "--管理员移除的通知--groupId--" + groupId + "--groupId--" + groupId + "--administrator--" + administrator);
            }

            @Override
            public void onOwnerChanged(String groupId, String newOwner, String oldOwner) {
                //群所有者变动通知
                LogHelper.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + "--群所有者变动通知--groupId--" + groupId + "--groupId--" + groupId + "--newOwner--" + newOwner+"--oldOwner--"+oldOwner);
            }

            @Override
            public void onMemberJoined(final String groupId, final String member) {
                //群组加入新成员通知
                LogHelper.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + "--群组加入新成员通知--groupId--" + groupId + "--groupId--" + groupId + "--member--" + member);
            }

            @Override
            public void onMemberExited(final String groupId, final String member) {
                //群成员退出通知
                LogHelper.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + "--群成员退出通知--groupId--" + groupId + "--groupId--" + groupId + "--member--" + member);
            }

            @Override
            public void onAnnouncementChanged(String groupId, String announcement) {
                //群公告变动通知
                LogHelper.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + "--群公告变动通知--groupId--" + groupId + "--groupId--" + groupId + "--announcement--" + announcement);
            }

            @Override
            public void onSharedFileAdded(String groupId, EMMucSharedFile sharedFile) {
                //增加共享文件的通知
                LogHelper.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + "--增加共享文件的通知--groupId--" + groupId + "--groupId--" + groupId + "--sharedFile--" + sharedFile.getFileName());
            }

            @Override
            public void onSharedFileDeleted(String groupId, String fileId) {
                //群共享文件删除通知
                LogHelper.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + "--群共享文件删除通知--groupId--" + groupId + "--groupId--" + groupId + "--fileId--" + fileId);
            }
        });
    }
}

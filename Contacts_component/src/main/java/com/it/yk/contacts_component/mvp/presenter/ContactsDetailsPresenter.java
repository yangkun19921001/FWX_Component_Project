package com.it.yk.contacts_component.mvp.presenter;

import android.app.Application;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.hyphenate.easeui.helper.HXHelper;
import com.hyphenate.exceptions.HyphenateException;
import com.it.yk.contacts_component.R;
import com.it.yk.contacts_component.mvp.contract.ContactsDetailsContract;
import com.it.yk.contacts_component.mvp.model.entity.ContactsDetailsEntity;
import com.it.yk.contacts_component.mvp.model.entity.NewFriendsEntity;
import com.it.yk.contacts_component.mvp.ui.fragment.adapter.ContactsDetailsAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yk.component.res.qmui.QMUIDialogHelper;
import com.yk.component.res.qmui.QMUITipDialogHelper;
import com.yk.component.res.status_view.StatusViewHelper;
import com.yk.component.sdk.core.Constants;
import com.yk.component.sdk.db.DBManager;
import com.yk.component.sdk.utils.LogHelper;

import org.litepal.crud.callback.SaveCallback;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import static com.yk.component.sdk.core.Constants.ISP_Config.USER_PHONE_NUMBER;


@ActivityScope
public class ContactsDetailsPresenter extends BasePresenter<ContactsDetailsContract.Model, ContactsDetailsContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;


    @Inject
    List<ContactsDetailsEntity> mList;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;

    @Inject
    ContactsDetailsAdapter mContactsDetailsAdapter;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private QMUIDialog.EditTextDialogBuilder mEditTextDialogBuilder;

    /**
     * 当前按钮显示的状态码
     */
    private int currentShowBtnText = 0;

    /**
     * 当前人员的账号信息
     */
    private String wx_number = "";
    /**
     * 当前按钮显示的具体内容信息
     */
    private String wx_btn_state = "";

    @Inject
    public ContactsDetailsPresenter(ContactsDetailsContract.Model model, ContactsDetailsContract.View rootView
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

    public void initView(View view) {
        StatusViewHelper mStatusViewHelper = new StatusViewHelper(mAppManager.getTopActivity(), view);

        mRefreshLayout = mStatusViewHelper.getRefreshLayout();
        mRefreshLayout.setEnableRefresh(false);

        mRecyclerView = mStatusViewHelper.getRecyclerView();
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mContactsDetailsAdapter);

        getDatas();
        mStatusViewHelper.showContent();
        mContactsDetailsAdapter.notifyDataSetChanged();
    }

    public List<ContactsDetailsEntity> getDatas() {
        mList.clear();
        if (mAppManager.getTopActivity().getIntent().getExtras().getString(Constants.IContacts.NEW_FRIEND) == null)
            return mList;

        switch (mAppManager.getTopActivity().getIntent().getExtras().getString(Constants.IContacts.NEW_FRIEND)) {
            case "SearchDialog":
                currentShowBtnText = 1;
                wx_number = mAppManager.getTopActivity().getIntent().getExtras().getString(USER_PHONE_NUMBER);
                wx_btn_state = "添加到通讯录";
                break;
            case Constants.IContacts.NewFriendsAdapter:
                if (mAppManager.getTopActivity().getIntent().getExtras().getParcelable(Constants.IContacts.NEW_FRIEND_DATA) != null) {
                    NewFriendsEntity newFriendsEntity = mAppManager.getTopActivity().getIntent().getExtras().getParcelable(Constants.IContacts.NEW_FRIEND_DATA);
                    wx_number = newFriendsEntity.getOtherId() + "";
                    currentShowBtnText = 2;
//                    //发送邀请
//                    REQUEST_START_INVITEED,
//                            //邀请成功
//                            REQUEST_SUCCEED_INVITEED,
//                            //邀请失败
//                            REQUEST_ERROR_INVITEED,
//
//                            //收到好友请求
//                            RECEIVE_FRIEND_INVITEED,
//                            //好友请求拒绝
//                            RECEIVE_FRIEND_ERROR_INVITEED,
//                            //好友请求成功
//                            RECEIVE_FRIEND_SUCCEED_INVITEED
                    if (newFriendsEntity.getState() == NewFriendsEntity.UserState.REQUEST_START_INVITEED.ordinal()) {
                        wx_btn_state = "邀请中";
                    } else if (newFriendsEntity.getState() == NewFriendsEntity.UserState.REQUEST_SUCCEED_INVITEED.ordinal()) {
                        wx_btn_state = "发消息";
                    } else if (newFriendsEntity.getState() == NewFriendsEntity.UserState.REQUEST_ERROR_INVITEED.ordinal()) {
                        wx_btn_state = "邀请失败";
                    } else if (newFriendsEntity.getState() == NewFriendsEntity.UserState.RECEIVE_FRIEND_INVITEED.ordinal()) {
                        wx_btn_state = "同意请求";
                    } else if (newFriendsEntity.getState() == NewFriendsEntity.UserState.RECEIVE_FRIEND_ERROR_INVITEED.ordinal()) {
                        wx_btn_state = "拒绝请求";
                    } else if (newFriendsEntity.getState() == NewFriendsEntity.UserState.RECEIVE_FRIEND_SUCCEED_INVITEED.ordinal()) {
                        wx_btn_state = "发消息";
                    }
                }
                break;
        }


        if (!StringUtils.isEmpty(wx_number))
            mList.add(new ContactsDetailsEntity(Constants.IContacts.AddFriendLayout_Top, wx_number, -1, wx_number));
        mList.add(new ContactsDetailsEntity(Constants.IContacts.AddFriendLayout_Group, "冰岛", -1, wx_number));
        mList.add(new ContactsDetailsEntity(Constants.IContacts.AddFriendLayout_Group, "来至手机搜索", -1, wx_number));
        mList.add(new ContactsDetailsEntity(Constants.IContacts.AddFriendLayout_Group, "手机", -1, wx_number));
        mList.add(new ContactsDetailsEntity(Constants.IContacts.AddFriendLayout_Button, wx_btn_state, -1, wx_number));
        return mList;
    }


    /**
     * 正式添加好友请求
     */
    public void addItemClickListener() {
        mContactsDetailsAdapter.addItemClick(new ContactsDetailsAdapter.IItemClickListener() {
            @Override
            public void addContact(String toAddUsername) {
                switch (currentShowBtnText) {
                    case 0:
                    case 1:
                        if (toAddUsername.equals(SPUtils.getInstance(Constants.ISP_Config.SP_NAME).getString(Constants.ISP_Config.USER_PHONE_NUMBER))) {
                            QMUITipDialogHelper.getInstance().showLoading(mAppManager.getTopActivity(),"不能添加自己为好友.",Constants.ILoad.LoadFail,1500,null);
                            return;
                        }
                        showNotify(toAddUsername);
                        break;
                    case 2:
                        switch (wx_btn_state) {
                            case "同意请求":
                                feedbackFriend(true, toAddUsername);
                                break;
                            case "拒绝请求":
                                feedbackFriend(false, toAddUsername);
                                break;
                            default:
                                break;
                        }
                        break;
                }
            }
        });
    }

    /**
     * 是否同意朋友的请求
     *
     * @param isAccept
     * @param toAddUsername
     */
    private void feedbackFriend(boolean isAccept, String toAddUsername) {
        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) {
                        if (isAccept) {
                            try {
                                HXHelper.getInstance().getFriendEngine().acceptInvitation(toAddUsername);
                                emitter.onNext("同意请求成功");
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                emitter.onNext(e.getMessage());
                            }
                        } else {
                            try {
                                HXHelper.getInstance().getFriendEngine().declineInvitation(toAddUsername);
                                emitter.onNext("拒绝请求成功");
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                emitter.onNext(e.getMessage());
                            }
                        }
                        emitter.onComplete();
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        QMUITipDialogHelper.getInstance().showLoading(mAppManager.getTopActivity(), "请求中...", Constants.ILoad.Loading, 0, null);

                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
//                        QMUITipDialogHelper.getInstance().showLoading(mAppManager.getTopActivity(), "完成", Constants.ILoad.LoadSuccess, 0, null);
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<String>(mErrorHandler) {
                    @Override
                    public void onNext(String aBoolean) {
                        NewFriendsEntity newFriendsEntity = new NewFriendsEntity();
                        if (aBoolean.equals("同意请求成功")) {
                            newFriendsEntity.setOtherId(toAddUsername);
                            newFriendsEntity.setState(NewFriendsEntity.UserState.REQUEST_SUCCEED_INVITEED.ordinal());
                            newFriendsEntity.setId(SPUtils.getInstance(Constants.ISP_Config.SP_NAME).getString(Constants.ISP_Config.USER_PHONE_NUMBER));
                            newFriendsEntity.setTime(TimeUtils.getNowString());
                            QMUITipDialogHelper.getInstance().showLoading(mAppManager.getTopActivity(), aBoolean, Constants.ILoad.LoadSuccess, 1500, null);
                        } else {
                            QMUITipDialogHelper.getInstance().showLoading(mAppManager.getTopActivity(), aBoolean, Constants.ILoad.LoadFail, 1500, null);
                            newFriendsEntity.setOtherId(toAddUsername);
                            newFriendsEntity.setState(NewFriendsEntity.UserState.REQUEST_ERROR_INVITEED.ordinal());
                            newFriendsEntity.setId(SPUtils.getInstance(Constants.ISP_Config.SP_NAME).getString(Constants.ISP_Config.USER_PHONE_NUMBER));
                            newFriendsEntity.setTime(TimeUtils.getNowString());
                        }
                        DBManager.getInstance().upAllAsync(newFriendsEntity, "userId = ? and otherId = ?", SPUtils.getInstance(Constants.ISP_Config.SP_NAME).getString(Constants.ISP_Config.USER_PHONE_NUMBER), toAddUsername, new UpdateOrDeleteCallback() {
                            @Override
                            public void onFinish(int rowsAffected) {
                                LogHelper.d(TAG, rowsAffected > 0 ? "保存数据-收到好友请求被同意" : "保存数据-收到好友请求被同意");
                            }
                        });
                    }
                });
    }

    /**
     * 提示邀请的信息
     *
     * @param toAddUsername 邀请的人员
     */
    private void showNotify(String toAddUsername) {
        mEditTextDialogBuilder = QMUIDialogHelper.showEditTextDialog(mAppManager.getCurrentActivity(), "提示", "请输入申请理由", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                String content = mEditTextDialogBuilder.getEditText().getText().toString().trim();
                if (StringUtils.isEmpty(content)) {
                    QMUITipDialogHelper.getInstance().showLoading(mAppManager.getCurrentActivity(), "理由不能为空，请重新输入。", Constants.ILoad.LoadFail, 1500, null);
                    return;
                }
                dialog.dismiss();
                AddFriendToContacts(toAddUsername, content);
            }
        });
    }

    /**
     * 进行添加好友
     *
     * @param addUsername
     */
    private void AddFriendToContacts(String addUsername, String toAddUsernameContent) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                try {
                    HXHelper.getInstance().getFriendEngine().addContact(addUsername, toAddUsernameContent);
                    emitter.onNext(ArmsUtils.getString(mApplication, R.string.contacts_send_successful));
                    LogHelper.i(TAG, "邀请好友发送成功---" + ArmsUtils.getString(mApplication, R.string.contacts_send_successful));
                } catch (HyphenateException e) {
                    emitter.onNext(ArmsUtils.getString(mApplication, e.getMessage()));
                    LogHelper.e(TAG, "邀请好友发送失败---" + e.getMessage());
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        QMUITipDialogHelper.getInstance().showLoading(mAppManager.getCurrentActivity(), "正在申请...", Constants.ILoad.Loading, 2000, null);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<String>(mErrorHandler) {
                    @Override
                    public void onNext(String s) {
                        if (s.equals(ArmsUtils.getString(mApplication, R.string.contacts_send_successful))) {
                            QMUITipDialogHelper.getInstance().showLoading(mAppManager.getCurrentActivity(), s, Constants.ILoad.LoadSuccess, 2000, null);
                            NewFriendsEntity newFriendsEntity = new NewFriendsEntity();
                            newFriendsEntity.setContent("发送邀请理由:" + toAddUsernameContent);
                            newFriendsEntity.setOtherId(addUsername);
                            newFriendsEntity.setState(NewFriendsEntity.UserState.REQUEST_START_INVITEED.ordinal());
                            newFriendsEntity.setId(SPUtils.getInstance(Constants.ISP_Config.SP_NAME).getString(Constants.ISP_Config.USER_PHONE_NUMBER));
                            newFriendsEntity.setTime(TimeUtils.getNowString());
                            DBManager.getInstance().saveAsync(newFriendsEntity, new SaveCallback() {
                                @Override
                                public void onFinish(boolean success) {
                                    LogHelper.i(TAG, "邀请好友数据保存成功---" + newFriendsEntity.toString());
                                }
                            });
                        } else {
                            QMUITipDialogHelper.getInstance().showLoading(mAppManager.getCurrentActivity(), s, Constants.ILoad.LoadFail, 2000, null);
                        }
                    }
                });

    }

}

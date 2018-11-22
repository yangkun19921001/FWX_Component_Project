package com.it.yk.fwx_chat_component.mvp.presenter;

import android.app.Application;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.it.yk.fwx_chat_component.R;
import com.it.yk.fwx_chat_component.mvp.contract.ChatAddGroupContract;
import com.it.yk.fwx_chat_component.mvp.model.entity.AllContactsEntity;
import com.it.yk.fwx_chat_component.mvp.ui.activity.adapter.AllContacts;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.yk.component.res.qmui.QMUIDialogHelper;
import com.yk.component.res.qmui.QMUITipDialogHelper;
import com.yk.component.res.status_view.StatusViewHelper;
import com.yk.component.sdk.core.Constants;
import com.yk.component.sdk.core.RouterHub;

import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class ChatAddGroupPresenter extends BasePresenter<ChatAddGroupContract.Model, ChatAddGroupContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;


    @Inject
    List<AllContactsEntity> mList;

    @Inject
    AllContacts mAllContactsAdapter;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    private StatusViewHelper mStatusViewHelper;
    private QMUIDialog.EditTextDialogBuilder mEditTextDialogBuilder;

    /**
     * 存储群组好友
     */
    private String[] groupMember = new String[]{};
    private StringBuilder mSb;

    @Inject
    public ChatAddGroupPresenter(ChatAddGroupContract.Model model, ChatAddGroupContract.View rootView
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

    /**
     * 初始化视图对象
     */
    public void initWindowView() {
        mStatusViewHelper = new StatusViewHelper(mAppManager.getTopActivity(), mAppManager.getTopActivity().getWindow().getDecorView());
        mStatusViewHelper.getRefreshLayout().setEnableRefresh(false);
        RecyclerView mRecyclerView = mStatusViewHelper.getRecyclerView();
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        LayoutInflater from = LayoutInflater.from(mApplication);

        mAllContactsAdapter.addHeaderView(from.inflate(R.layout.public_layout_search_view, null));
        mRecyclerView.setAdapter(mAllContactsAdapter);
    }

    /**
     * 获取所有的联系人列表
     */
    public void getALLContacts() {
        mModel.getAllContacts(mRootView)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mList.clear();
                    }
                })
                .map(new Function<List<String>, List<AllContactsEntity>>() {
                    @Override
                    public List<AllContactsEntity> apply(List<String> list) throws Exception {
                        return getALLContacts(list);
                    }
                })
                .subscribe(new ErrorHandleSubscriber<List<AllContactsEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(List<AllContactsEntity> list) {
                        if (list != null && list.size() > 0) {
                            mStatusViewHelper.showContent();
                            mAllContactsAdapter.notifyDataSetChanged();
                        } else {
                            mStatusViewHelper.showEmpty();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        ToastUtils.showShort(t.getMessage());
                        mStatusViewHelper.showError();
                    }
                });
    }

    private List<AllContactsEntity> getALLContacts(List<String> list) {

        if (list != null && list.size() > 0) {
            for (String name : list
                    ) {
                mList.add(new AllContactsEntity(false, name, -1));
            }
        }
        return mList;
    }


    @Subscriber(tag = Constants.IChat.SELECT_USER)
    public void getSelectUser(Message message) {
        if (mAllContactsAdapter.getSelectUser().size() == 0) {
            QMUITipDialogHelper.getInstance().showLoading(mAppManager.getCurrentActivity(), "请选择用户", Constants.ILoad.LoadFail, 1500, null);
            return;
        }

        if (mAppManager.getTopActivity().getIntent().getExtras().getInt(Constants.IChat.OPEN_SELECT_USER, Constants.IChat.CREATE_GROUP) != -1) {
            switch (mAppManager.getTopActivity().getIntent().getExtras().getInt(Constants.IChat.OPEN_SELECT_USER, Constants.IChat.CREATE_GROUP)) {
                case Constants.IChat.CREATE_GROUP:
                    showCreateGroupDiaLog();
                    break;
                case Constants.IChat.CREATE_Meeting:
                    showCreateMettingDiaLog();
                    break;
            }
        }


    }

    private void showCreateMettingDiaLog() {
        String[] createGroupMember = getCreateGroupMember();
        QMUIDialogHelper.showRedButtonDialog(mAppManager.getTopActivity(), "是否邀请成员：", mSb.toString() , "再想一下", "开启会议", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                ARouter.getInstance().build(RouterHub.Chat_LaunchGroupMetting).withCharSequenceArray(Constants.IChat.SELECT_USER_LIST, createGroupMember).navigation();
                mAppManager.getTopActivity().finish();
            }
        });
    }


    /**
     * 创建群组中
     */
    private void showCreateGroupDiaLog() {
        mEditTextDialogBuilder = QMUIDialogHelper.showEditTextDialog(mAppManager.getTopActivity(), "创建群组", "输入创建群组理由", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                String content = mEditTextDialogBuilder.getEditText().getText().toString().trim();
                dialog.dismiss();
                mModel.createGroup("", content, getCreateGroupMember(), content)
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                QMUITipDialogHelper.getInstance().showLoading(mAppManager.getTopActivity(), "正在创建中...", 0, 0, null);
                            }
                        }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                        .subscribe(new ErrorHandleSubscriber<String>(mErrorHandler) {
                            @Override
                            public void onNext(String groupId) {
                                if (groupId.equals("创建成功")) {
                                    QMUITipDialogHelper.getInstance().showLoading(mAppManager.getTopActivity(), groupId, 1, 1000, null);
                                    ARouter.getInstance().build(RouterHub.FRIENDS_GroupListActivity).navigation();
                                } else {
                                    QMUITipDialogHelper.getInstance().showLoading(mAppManager.getTopActivity(), groupId, 2, 1500, null);
                                }
                            }
                        });
            }
        });
    }

    /**
     * 添加群组的人员
     *
     * @return
     */
    private String[] getCreateGroupMember() {
        groupMember = null;
        mSb = new StringBuilder();
        groupMember = new String[mAllContactsAdapter.getSelectUser().size()];
        for (int i = 0; i < mAllContactsAdapter.getSelectUser().size(); i++) {
            groupMember[i] = mAllContactsAdapter.getSelectUser().get(i);
            mSb.append(groupMember[i].toString() + "\n");
        }
        return groupMember;
    }


}

package com.it.yk.contacts_component.mvp.presenter;

import android.app.Application;
import android.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPUtils;
import com.it.yk.contacts_component.R;
import com.it.yk.contacts_component.mvp.contract.AddFriendsContract;
import com.it.yk.contacts_component.mvp.model.entity.AddFriendsEntity;
import com.it.yk.contacts_component.mvp.ui.dialog.SearchDialog;
import com.it.yk.contacts_component.mvp.ui.fragment.adapter.AddFriendsAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yk.component.res.status_view.StatusViewHelper;
import com.yk.component.sdk.core.Constants;
import com.yk.component.sdk.core.RouterHub;

import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class AddFriendsPresenter extends BasePresenter<AddFriendsContract.Model, AddFriendsContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;


    @Inject
    List<AddFriendsEntity> mLists;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;

    @Inject
    AddFriendsAdapter mAddFriendsAdapter;
    private SearchDialog mSearchDialog;

    @Inject
    public AddFriendsPresenter(AddFriendsContract.Model model, AddFriendsContract.View rootView
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

        if (mSearchDialog != null)
            mSearchDialog.dismiss();
    }


    public void initView(View decorView) {
        StatusViewHelper mStatusViewHelper = new StatusViewHelper(mAppManager.getTopActivity(), decorView);

        SmartRefreshLayout mRefreshLayout = mStatusViewHelper.getRefreshLayout();
        mRefreshLayout.setEnableRefresh(false);

        RecyclerView mRecyclerView = mStatusViewHelper.getRecyclerView();
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);

        mRecyclerView.setAdapter(mAddFriendsAdapter);

        getDatas();
        mStatusViewHelper.showContent();
    }

    public List<AddFriendsEntity> getDatas() {
        mLists.clear();
        String phoneNumber = SPUtils.getInstance(Constants.ISP_Config.SP_NAME).getString(Constants.ISP_Config.USER_PHONE_NUMBER);
        //顶部搜索
        mLists.add(new AddFriendsEntity(Constants.IContacts.AddFriendLayout_Top, "我的微信号：" + phoneNumber, 0));
        //雷达添加朋友
        mLists.add(new AddFriendsEntity(Constants.IContacts.AddFriendLayout_Button, "雷达添加朋友", R.drawable.contacts_icon_radar));
        //面对面建群
        mLists.add(new AddFriendsEntity(Constants.IContacts.AddFriendLayout_Button, "创建群聊", R.drawable.contacts_icon_item_group));
        //扫一扫
        mLists.add(new AddFriendsEntity(Constants.IContacts.AddFriendLayout_Button, "扫一扫", R.drawable.contacts_icon_scan));
        return mLists;
    }

    public void setAdapterItemClick(FragmentManager fragmentManager, String tag) {
        mAddFriendsAdapter.setItemClickListener(new AddFriendsAdapter.IItemClickListener() {
            @Override
            public void showDialog() {
                mSearchDialog = new SearchDialog();
                mSearchDialog.show(fragmentManager, tag);
            }

            @Override
            public void itemClick(AddFriendsEntity item) {
                ARouter.getInstance().build(RouterHub.Chat_AddGroupActivity)
                        .withInt(Constants.IChat.OPEN_SELECT_USER,Constants.IChat.CREATE_GROUP).navigation();
            }
        });
    }


}

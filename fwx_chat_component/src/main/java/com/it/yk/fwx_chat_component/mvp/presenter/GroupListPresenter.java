package com.it.yk.fwx_chat_component.mvp.presenter;

import android.app.Application;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.blankj.utilcode.util.ToastUtils;
import com.hyphenate.chat.EMGroup;
import com.it.yk.fwx_chat_component.R;
import com.it.yk.fwx_chat_component.mvp.contract.GroupListContract;
import com.it.yk.fwx_chat_component.mvp.model.entity.GroupListEntity;
import com.it.yk.fwx_chat_component.mvp.ui.activity.adapter.GroupListAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;
import com.yk.component.res.status_view.StatusViewHelper;
import com.yk.component.sdk.utils.LogHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class GroupListPresenter extends BasePresenter<GroupListContract.Model, GroupListContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private StatusViewHelper mStatusViewHelper;
    private RecyclerView mRecyclerView;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    GroupListAdapter mGroupListAdapter;
    @Inject
    List<GroupListEntity> mGroupListEntityList;

    @Inject
    public GroupListPresenter(GroupListContract.Model model, GroupListContract.View rootView
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
    public void initView() {
        mStatusViewHelper = new StatusViewHelper(mAppManager.getTopActivity(), mAppManager.getTopActivity().getWindow().getDecorView());
        mStatusViewHelper.getRefreshLayout().setEnableRefresh(false);
        mRecyclerView = mStatusViewHelper.getRecyclerView();
        ;
        mGroupListAdapter.setHeaderView(LayoutInflater.from(mApplication).inflate(R.layout.public_layout_search_view, null));
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mGroupListAdapter);
    }


    /**
     * 获取加入的群组列表
     */
    public void getGroupLists() {
        LogHelper.i(TAG, "获取群组列表");
        mModel.getGroupLists()
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .flatMap(new Function<List<EMGroup>, ObservableSource<EMGroup>>() {
                    @Override
                    public ObservableSource<EMGroup> apply(List<EMGroup> listEntityList) throws Exception {
                        return Observable.fromIterable(listEntityList);
                    }
                }).subscribe(new ErrorHandleSubscriber<EMGroup>(mErrorHandler) {
            @Override
            public void onNext(EMGroup emGroup) {
                mGroupListEntityList.add(new GroupListEntity(emGroup.getGroupId(), emGroup.getGroupName()));
            }

            @Override
            public void onComplete() {
                super.onComplete();
                LogHelper.i(TAG, "获取群组列表成功---" + mGroupListEntityList.size());
                if (mGroupListEntityList != null && mGroupListEntityList.size() > 0)
                    mStatusViewHelper.showContent();
                else
                    mStatusViewHelper.showEmpty();
                mGroupListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable t) {
                mStatusViewHelper.showError();
                ToastUtils.showShort(t.getMessage());
            }
        });
    }

}

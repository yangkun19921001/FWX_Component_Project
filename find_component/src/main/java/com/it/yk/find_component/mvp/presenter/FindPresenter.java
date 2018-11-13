package com.it.yk.find_component.mvp.presenter;

import android.app.Application;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.it.yk.find_component.R;
import com.it.yk.find_component.mvp.contract.FindContract;
import com.it.yk.find_component.mvp.model.entity.FindEntity;
import com.it.yk.find_component.mvp.ui.fragment.adapter.FindAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yk.component.res.status_view.StatusViewHelper;
import com.yk.component.sdk.core.Constants;

import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class FindPresenter extends BasePresenter<FindContract.Model, FindContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private StatusViewHelper mStatusViewHelper;
    private RecyclerView mRecyclerView;

    @Inject
    List<FindEntity> mList;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;

    @Inject
    FindAdapter mFindAdapter;

    @Inject
    public FindPresenter(FindContract.Model model, FindContract.View rootView
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

    public void initView(View inflate, FragmentActivity activity) {
        mStatusViewHelper = new StatusViewHelper(mAppManager.getTopActivity(), inflate);
        //初始化适配器对象
        mStatusViewHelper.initView();
        SmartRefreshLayout mRefreshLayout = mStatusViewHelper.getRefreshLayout();
        //静止刷新
        mRefreshLayout.setEnableRefresh(false);
        mRecyclerView = mStatusViewHelper.getRecyclerView();
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mFindAdapter);

        //添加默认数据
        getFindData();
        mFindAdapter.notifyDataSetChanged();
        mStatusViewHelper.showContent();
    }

    /**
     *
     * @return 返回默认数据
     */
    public List<FindEntity> getFindData() {
        //朋友圈
        FindEntity findEntityCircleOfFriends = new FindEntity(Constants.IFind.CircleOfFriends, Constants.IFind.OneLayout, R.drawable.fx_find_friends);

        //扫一扫、摇一摇
        FindEntity findEntitySweep = new FindEntity(Constants.IFind.Sweep, Constants.IFind.GroupLayout, R.drawable.fx_find_erweima);
        FindEntity findEntityShakeAShake = new FindEntity(Constants.IFind.ShakeAShake, Constants.IFind.GroupLayout, R.drawable.find_yaoyiyao);

        //看一看
        FindEntity findEntityShopping = new FindEntity(Constants.IFind.Shopping, Constants.IFind.OneLayout, R.drawable.find_gouwu);
        //附近的人、漂流瓶
        FindEntity findEntityPeopleInTheVicinity = new FindEntity(Constants.IFind.PeopleInTheVicinity, Constants.IFind.GroupLayout, R.drawable.find_fujin);
        FindEntity findEntityDriftingBottle = new FindEntity(Constants.IFind.DriftingBottle, Constants.IFind.GroupLayout, R.drawable.find_piaoliuping);

        //小程序、
        FindEntity findEntitySmallProgram = new FindEntity(Constants.IFind.SmallProgram, Constants.IFind.OneLayout, R.drawable.find_small_);

        mList.add(findEntityCircleOfFriends);
        mList.add(findEntitySweep);
        mList.add(findEntityShakeAShake);
        mList.add(findEntityShopping);
        mList.add(findEntityPeopleInTheVicinity);
        mList.add(findEntityDriftingBottle);
        mList.add(findEntitySmallProgram);

        return mList;

    }
}

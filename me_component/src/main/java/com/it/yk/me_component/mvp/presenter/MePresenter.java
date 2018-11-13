package com.it.yk.me_component.mvp.presenter;

import android.app.Application;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.it.yk.me_component.R;
import com.it.yk.me_component.mvp.contract.MeContract;
import com.it.yk.me_component.mvp.model.entity.MeEntity;
import com.it.yk.me_component.mvp.ui.fragment.adapter.MeAdapter;
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
public class MePresenter extends BasePresenter<MeContract.Model, MeContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private StatusViewHelper mStatusViewHelper;


    @Inject
    List<MeEntity> mList;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;

    @Inject
    MeAdapter mMeAdapter;
    private RecyclerView mRecyclerView;

    @Inject
    public MePresenter(MeContract.Model model, MeContract.View rootView
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

    public void initView(View inflate) {
        mStatusViewHelper = new StatusViewHelper(mAppManager.getTopActivity(), inflate);
        mStatusViewHelper.initView();

        SmartRefreshLayout mRefreshLayout = mStatusViewHelper.getRefreshLayout();
        //静止刷新
        mRefreshLayout.setEnableRefresh(false);
        mRecyclerView = mStatusViewHelper.getRecyclerView();
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mMeAdapter);

        //添加默认数据
        getMyData();
        mMeAdapter.notifyDataSetChanged();
        mStatusViewHelper.showContent();
    }

    public List<MeEntity> getMyData() {
        mList.clear();
        //个人信息
        MeEntity meEntity = new MeEntity(Constants.IMe.MY_INFO, Constants.IMe.MyInfo, R.drawable.ease_default_avatar);

        //钱包
        MeEntity Entity_PURSE = new MeEntity(Constants.IMe.MY_PURSE, Constants.IMe.OneLayout, R.drawable.fx_icon_wallet);

        //收藏
        MeEntity Entity_COLLECT = new MeEntity(Constants.IMe.MY_COLLECT, Constants.IMe.GroupLayout, R.drawable.fx_icon_save);
        //相册
        MeEntity Entity_IMAGE = new MeEntity(Constants.IMe.MY_IMAGE, Constants.IMe.GroupLayout, R.drawable.fx_icon_album);
        //卡包
        MeEntity Entity_CARD = new MeEntity(Constants.IMe.MY_CARD, Constants.IMe.GroupLayout, R.drawable.fx_icon_card);

        //设置
        MeEntity Entity_SETTING = new MeEntity(Constants.IMe.MY_SETTING, Constants.IMe.OneLayout, R.drawable.fx_icon_settings);

        mList.add(meEntity);
        mList.add(Entity_PURSE);
        mList.add(Entity_COLLECT);
        mList.add(Entity_IMAGE);
        mList.add(Entity_CARD);
        mList.add(Entity_SETTING);

        return mList;
    }
}

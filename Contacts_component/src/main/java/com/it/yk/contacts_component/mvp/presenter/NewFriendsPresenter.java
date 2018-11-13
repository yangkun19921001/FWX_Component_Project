package com.it.yk.contacts_component.mvp.presenter;

import android.app.Application;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.it.yk.contacts_component.R;
import com.it.yk.contacts_component.mvp.contract.NewFriendsContract;
import com.it.yk.contacts_component.mvp.model.entity.NewFriendsEntity;
import com.it.yk.contacts_component.mvp.ui.fragment.adapter.NewFriendsAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;
import com.yk.component.res.qmui.QMUITipDialogHelper;
import com.yk.component.res.status_view.StatusViewHelper;
import com.yk.component.sdk.core.Constants;
import com.yk.component.sdk.core.RouterHub;
import com.yk.component.sdk.db.DBManager;
import com.yk.component.sdk.utils.Utils;

import org.litepal.crud.callback.FindMultiCallback;

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


@ActivityScope
public class NewFriendsPresenter extends BasePresenter<NewFriendsContract.Model, NewFriendsContract.View> implements View.OnClickListener {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    List<NewFriendsEntity> mList;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;

    @Inject
    NewFriendsAdapter mNewFriendsAdapter;
    private StatusViewHelper mStatusViewHelper;
    private View newFriendHeader;

    @Inject
    public NewFriendsPresenter(NewFriendsContract.Model model, NewFriendsContract.View rootView
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

    public void init() {
        mStatusViewHelper = new StatusViewHelper(mAppManager.getTopActivity(), mAppManager.getTopActivity().getWindow().getDecorView());
        mStatusViewHelper.getRefreshLayout().setEnableRefresh(false);
        RecyclerView mRecyclerView = mStatusViewHelper.getRecyclerView();
        newFriendHeader = ArmsUtils.inflate(mApplication, R.layout.contacts_layout_newfriend_header);
        mNewFriendsAdapter.addHeaderView(newFriendHeader);
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mNewFriendsAdapter);
        mStatusViewHelper.showContent();
        newFriendHeader.setOnClickListener(this);
    }


    public void getHistoryInviteData() {
        Observable.create(new ObservableOnSubscribe<List<NewFriendsEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<List<NewFriendsEntity>> emitter) throws Exception {
                DBManager.getInstance().queryaAllLists("userId = ?", SPUtils.getInstance(Constants.ISP_Config.SP_NAME).getString(Constants.ISP_Config.USER_PHONE_NUMBER), "time", NewFriendsEntity.class, new FindMultiCallback() {
                    @Override
                    public void onFinish(List list) {
                        if (list != null && list.size() > 0) {
                            mList.clear();
                            mList.addAll(list);
                        }
                        emitter.onNext(mList);
                        emitter.onComplete();
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        QMUITipDialogHelper.getInstance().showLoading(mAppManager.getTopActivity(), "加载中...", Constants.ILoad.Loading, 0, null);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        QMUITipDialogHelper.getInstance().showLoading(mAppManager.getTopActivity(), "加载成功.", Constants.ILoad.LoadSuccess, 0, null);
                    }
                })
                .subscribe(new ErrorHandleSubscriber<List<NewFriendsEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(List<NewFriendsEntity> newFriendsEntities) {
                        if (newFriendsEntities.size() > 0)
                            mNewFriendsAdapter.notifyDataSetChanged();
                    }
                });


    }

    @Override
    public void onClick(View v) {
        if (v == newFriendHeader) {
            Utils.navigation(RouterHub.FRIENDS_PhoneContactsActivity);
        }
    }
}

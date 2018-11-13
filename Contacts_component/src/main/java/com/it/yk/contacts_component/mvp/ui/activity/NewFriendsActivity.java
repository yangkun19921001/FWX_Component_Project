package com.it.yk.contacts_component.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.it.yk.contacts_component.di.component.DaggerNewFriendsComponent;
import com.it.yk.contacts_component.di.module.NewFriendsModule;
import com.it.yk.contacts_component.mvp.contract.NewFriendsContract;
import com.it.yk.contacts_component.mvp.presenter.NewFriendsPresenter;

import com.it.yk.contacts_component.R;
import com.yk.component.sdk.core.RouterHub;


import static com.jess.arms.utils.Preconditions.checkNotNull;

@Route(path = RouterHub.FRIENDS_NewFriendsActivity)
public class NewFriendsActivity extends BaseActivity<NewFriendsPresenter> implements NewFriendsContract.View {


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerNewFriendsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .newFriendsModule(new NewFriendsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_new_friends; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        //初始化一些基本数据
        mPresenter.init();
        //获取历史邀请或者被邀请的朋友
        mPresenter.getHistoryInviteData();

    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }


    @Override
    public Context getContext() {
        return getApplicationContext();
    }
}

package com.it.yk.fwx_chat_component.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.it.yk.fwx_chat_component.di.component.DaggerSelectFriendsComponent;
import com.it.yk.fwx_chat_component.di.module.SelectFriendsModule;
import com.it.yk.fwx_chat_component.mvp.contract.SelectFriendsContract;
import com.it.yk.fwx_chat_component.mvp.presenter.SelectFriendsPresenter;

import com.it.yk.fwx_chat_component.R;


import static com.jess.arms.utils.Preconditions.checkNotNull;


public class SelectFriendsActivity extends BaseActivity<SelectFriendsPresenter> implements SelectFriendsContract.View {


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerSelectFriendsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .selectFriendsModule(new SelectFriendsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_select_friends; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {

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


}

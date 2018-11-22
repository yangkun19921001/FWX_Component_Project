package com.it.yk.fwx_chat_component.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.it.yk.fwx_chat_component.di.component.DaggerGroupListComponent;
import com.it.yk.fwx_chat_component.di.module.GroupListModule;
import com.it.yk.fwx_chat_component.mvp.contract.GroupListContract;
import com.it.yk.fwx_chat_component.mvp.presenter.GroupListPresenter;

import com.it.yk.fwx_chat_component.R;
import com.yk.component.sdk.core.RouterHub;


import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * 加入的群组列表
 */
@Route(path = RouterHub.FRIENDS_GroupListActivity)
public class GroupListActivity extends BaseActivity<GroupListPresenter> implements GroupListContract.View {


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerGroupListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .groupListModule(new GroupListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_group_list; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mPresenter.initView();
        mPresenter. getGroupLists();
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


    public String getCurrentTitle() {
        return ArmsUtils.getString(getApplicationContext(),R.string.chat_component_group_list);
    }
}

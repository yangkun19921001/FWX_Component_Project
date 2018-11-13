package com.it.yk.contacts_component.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.it.yk.contacts_component.di.component.DaggerContactsDetailsComponent;
import com.it.yk.contacts_component.di.module.ContactsDetailsModule;
import com.it.yk.contacts_component.mvp.contract.ContactsDetailsContract;
import com.it.yk.contacts_component.mvp.presenter.ContactsDetailsPresenter;

import com.it.yk.contacts_component.R;
import com.yk.component.sdk.core.RouterHub;


import static com.jess.arms.utils.Preconditions.checkNotNull;

@Route(path = RouterHub.FRIENDS_ContactsDetailsActivity)
public class ContactsDetailsActivity extends BaseActivity<ContactsDetailsPresenter> implements ContactsDetailsContract.View {


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerContactsDetailsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .contactsDetailsModule(new ContactsDetailsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_contacts_details; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mPresenter.initView(getWindow().getDecorView());
        mPresenter.addItemClickListener();

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

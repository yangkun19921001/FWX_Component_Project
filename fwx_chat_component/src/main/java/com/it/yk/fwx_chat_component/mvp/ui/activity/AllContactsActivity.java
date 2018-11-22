package com.it.yk.fwx_chat_component.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.it.yk.fwx_chat_component.R;
import com.it.yk.fwx_chat_component.di.component.DaggerChatAddGroupComponent;
import com.it.yk.fwx_chat_component.di.module.ChatAddGroupModule;
import com.it.yk.fwx_chat_component.mvp.contract.ChatAddGroupContract;
import com.it.yk.fwx_chat_component.mvp.presenter.ChatAddGroupPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.yk.component.sdk.core.RouterHub;

import static com.jess.arms.utils.Preconditions.checkNotNull;

@Route(path = RouterHub.Chat_AddGroupActivity)
public class AllContactsActivity extends BaseActivity<ChatAddGroupPresenter> implements ChatAddGroupContract.View {



    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerChatAddGroupComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .chatAddGroupModule(new ChatAddGroupModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_chat_add_group; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        mPresenter.initWindowView();

        mPresenter.getALLContacts();
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

    public String getCurrentTitle(){
        return getString(R.string.chat_component_selecter_friends);
    }
}

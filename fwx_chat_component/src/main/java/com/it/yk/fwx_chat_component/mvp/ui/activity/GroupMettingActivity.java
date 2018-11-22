package com.it.yk.fwx_chat_component.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.it.yk.fwx_chat_component.R;
import com.it.yk.fwx_chat_component.di.component.DaggerGroupMettingComponent;
import com.it.yk.fwx_chat_component.di.module.GroupMettingModule;
import com.it.yk.fwx_chat_component.mvp.contract.GroupMettingContract;
import com.it.yk.fwx_chat_component.mvp.presenter.GroupMettingPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.yk.component.sdk.core.RouterHub;

import static com.jess.arms.utils.Preconditions.checkNotNull;

@Route(path = RouterHub.Chat_LaunchGroupMetting)
public class GroupMettingActivity extends BaseActivity<GroupMettingPresenter> implements GroupMettingContract.View {


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerGroupMettingComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .groupMettingModule(new GroupMettingModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_group_metting; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        mPresenter.initView();
        mPresenter.getMettingMember();
        mPresenter.addJoinAndCreateMettingListener();

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

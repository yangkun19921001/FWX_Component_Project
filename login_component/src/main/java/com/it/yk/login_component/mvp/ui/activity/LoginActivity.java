package com.it.yk.login_component.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.it.yk.login_component.R;
import com.it.yk.login_component.R2;
import com.it.yk.login_component.di.component.DaggerLoginComponent;
import com.it.yk.login_component.di.module.LoginModule;
import com.it.yk.login_component.mvp.contract.LoginContract;
import com.it.yk.login_component.mvp.presenter.LoginPresenter;
import com.it.yk.login_component.widget.LoginView;
import com.it.yk.login_component.widget.RegisterView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.yk.component.sdk.core.RouterHub;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;

@Route(path = RouterHub.LOGIN_LOGINACTIVITY)
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R2.id.register)
    RegisterView register;
    @BindView(R2.id.login)
    LoginView login;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerLoginComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_login; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mPresenter.initCurrentView(register,login);
        //注册监听
        mPresenter.onRegisterCallBack();
        //登录监听
        mPresenter.onLoginCallBack();

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

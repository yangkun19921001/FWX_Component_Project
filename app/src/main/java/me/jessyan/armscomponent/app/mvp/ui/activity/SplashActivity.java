package me.jessyan.armscomponent.app.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.Utils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.yk.component.sdk.core.Constants;
import com.yk.component.sdk.core.RouterHub;

import butterknife.BindView;
import me.jessyan.armscomponent.app.R;
import me.jessyan.armscomponent.app.di.component.DaggerSplashComponent;
import me.jessyan.armscomponent.app.di.module.SplashModule;
import me.jessyan.armscomponent.app.mvp.contract.SplashContract;
import me.jessyan.armscomponent.app.mvp.presenter.SplashPresenter;

@Route(path = RouterHub.APP_SPLASHACTIVITY)
public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashContract.View , View.OnClickListener {

    @BindView(R.id.splash_root)
    FrameLayout splashRoot;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    private AlphaAnimation mAnimation;
    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerSplashComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .splashModule(new SplashModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        ScreenUtils.setFullScreen(this);
        return R.layout.activity_splash; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }
    @Override
    public void initData(Bundle savedInstanceState) {
        findId();
        mAnimation = new AlphaAnimation(0.3f, 1.0f);
        mAnimation.setDuration(1500);
        splashRoot.startAnimation(mAnimation);

        mPresenter.initToLoginOrMain(rlBottom);

    }


    /**
     * 注册或者登录
     */
    private void findId() {
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                ARouter.getInstance().build(RouterHub.LOGIN_LOGINACTIVITY).
                        withInt(Constants.IOpenLogin.OPEN_LOGIN_TYPE, Constants.IOpenLogin.OPEN_LOGIN_TYPE_LOGIN).navigation(Utils.getApp());
                break;
            case R.id.btn_register:
                ARouter.getInstance().build(RouterHub.LOGIN_LOGINACTIVITY).
                        withInt(Constants.IOpenLogin.OPEN_LOGIN_TYPE, Constants.IOpenLogin.OPEN_LOGIN_TYPE_REGISTER).navigation(Utils.getApp());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {

    }

    @Override
    public void launchActivity(@NonNull Intent intent) {

    }

    @Override
    public void killMyself() {

    }
}

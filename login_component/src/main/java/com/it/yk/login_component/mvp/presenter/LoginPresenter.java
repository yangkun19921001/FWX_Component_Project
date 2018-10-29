package com.it.yk.login_component.mvp.presenter;

import android.app.Application;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPUtils;
import com.hyphenate.easeui.helper.HXHelper;
import com.it.yk.login_component.R;
import com.it.yk.login_component.mvp.contract.LoginContract;
import com.it.yk.login_component.widget.LoginView;
import com.it.yk.login_component.widget.RegisterView;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.yk.component.res.qmui.QMUITipDialogHelper;
import com.yk.component.sdk.core.Constants;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import static com.yk.component.sdk.core.RouterHub.APP_MAINACTIVITY;


@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public LoginPresenter(LoginContract.Model model, LoginContract.View rootView
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
        RegisterView.unRegisterCallBack();
        LoginView.unLoginCallBack();
    }

    public void initCurrentView(RegisterView register, LoginView login) {
        if (mAppManager.getTopActivity().getIntent() != null && mAppManager.getTopActivity().getIntent().getIntExtra(Constants.IOpenLogin.OPEN_LOGIN_TYPE, -1) != -1) {
            switch (mAppManager.getTopActivity().getIntent().getIntExtra(Constants.IOpenLogin.OPEN_LOGIN_TYPE, -1)) {
                case Constants.IOpenLogin.OPEN_LOGIN_TYPE_LOGIN:
                    register.setVisibility(View.GONE);
                    login.setVisibility(View.VISIBLE);
                    break;
                case Constants.IOpenLogin.OPEN_LOGIN_TYPE_REGISTER:
                    register.setVisibility(View.VISIBLE);
                    login.setVisibility(View.GONE);
                    break;
            }
        }
    }

    /**
     * 注册返回的成功或者失败
     */
    public void onRegisterCallBack() {
        RegisterView.addRegisterCallBack(new RegisterView.IRegisterListener() {
            @Override
            public void onRegisterSucceed(String username, String phone_number, String password) {
                QMUITipDialogHelper.getInstance().showLoading(mAppManager.getCurrentActivity(), mApplication.getString(R.string.login_register_succeed), 1, 1500, new QMUITipDialogHelper.ILoadListener() {
                    @Override
                    public void loadSucceed() {
                        SPUtils.getInstance(Constants.ISP_Config.SP_NAME).put(Constants.ISP_Config.USER_PHONE_NUMBER,phone_number);
                        SPUtils.getInstance(Constants.ISP_Config.SP_NAME).put(Constants.ISP_Config.USER_NAME,username);
                        SPUtils.getInstance(Constants.ISP_Config.SP_NAME).put(Constants.ISP_Config.USER_PASSWORD,password);
                        mAppManager.getCurrentActivity().finish();
                    }
                });
            }

            @Override
            public void onRegisterError(String meg) {
                QMUITipDialogHelper.getInstance().showLoading(mAppManager.getCurrentActivity(), meg, 2, 1500, null);
            }

            @Override
            public void onRegisterStart() {
                QMUITipDialogHelper.getInstance().showLoading(mAppManager.getCurrentActivity(), "注册中...", 0, 0, null);
            }
        });
    }

    public void onLoginCallBack() {
        LoginView.addLoginCallBack(new LoginView.ILoginListener() {
            @Override
            public void onLoginSucceed() {
                QMUITipDialogHelper.getInstance().showLoading(mAppManager.getCurrentActivity(), "登录成功!", Constants.ILoad.LoadSuccess, 1000, new QMUITipDialogHelper.ILoadListener() {
                    @Override
                    public void loadSucceed() {
                        HXHelper.getInstance().getLoginEngine().loginSucceedInit();
                        ARouter.getInstance().build(APP_MAINACTIVITY).navigation();
                        mAppManager.getCurrentActivity().finish();
                    }
                });
            }

            @Override
            public void onLoginError(String error) {
                QMUITipDialogHelper.getInstance().showLoading(mAppManager.getCurrentActivity(),error,Constants.ILoad.LoadFail,1500,null);
            }

            @Override
            public void onLoginStart() {
                QMUITipDialogHelper.getInstance().showLoading(mAppManager.getCurrentActivity(),"登录中...",Constants.ILoad.Loading,0,null);
            }
        });
    }
}

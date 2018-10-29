/*
 * Copyright 2018 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.jessyan.armscomponent.app.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.Utils;
import com.hyphenate.easeui.helper.HXHelper;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.yk.component.sdk.core.Constants;
import com.yk.component.sdk.core.RouterHub;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.armscomponent.app.R;

import static com.yk.component.sdk.core.RouterHub.APP_MAINACTIVITY;

/**
 * ================================================
 * Created by JessYan on 18/04/2018 17:03
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
@Route(path = RouterHub.APP_SPLASHACTIVITY)
public class SplashActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.splash_root)
    FrameLayout splashRoot;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    private AlphaAnimation mAnimation;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        ScreenUtils.setFullScreen(this);
        return R.layout.activity_splash; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        findId();

        mAnimation = new AlphaAnimation(0.3f, 1.0f);
        mAnimation.setDuration(1500);
        splashRoot.startAnimation(mAnimation);

        initToLoginOrMain();

    }

    /**
     * 检查是否登录
     */
    private void initToLoginOrMain() {

        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                boolean register = HXHelper.getInstance().getLoginEngine().isRegister();
                if (register) {
                    HXHelper.getInstance().getLoginEngine().loginSucceedInit();
                }
                try {
                    Thread.sleep(1500);
                    emitter.onNext(register);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean isLogin) {
                        if (isLogin) {
                            ARouter.getInstance().build(APP_MAINACTIVITY).navigation();
                            finish();
                        }else{
                            rlBottom.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

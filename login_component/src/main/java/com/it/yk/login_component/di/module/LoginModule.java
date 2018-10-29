package com.it.yk.login_component.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.it.yk.login_component.mvp.contract.LoginContract;
import com.it.yk.login_component.mvp.model.LoginModel;


@Module
public class LoginModule {
    private LoginContract.View view;

    /**
     * 构建LoginModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public LoginModule(LoginContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    LoginContract.View provideLoginView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    LoginContract.Model provideLoginModel(LoginModel model) {
        return model;
    }
}
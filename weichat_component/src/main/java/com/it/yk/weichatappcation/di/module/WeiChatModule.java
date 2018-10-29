package com.it.yk.weichatappcation.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.it.yk.weichatappcation.mvp.contract.WeiChatContract;
import com.it.yk.weichatappcation.mvp.model.WeiChatModel;


@Module
public class WeiChatModule {
    private WeiChatContract.View view;

    /**
     * 构建WeiChatModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public WeiChatModule(WeiChatContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    WeiChatContract.View provideWeiChatView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    WeiChatContract.Model provideWeiChatModel(WeiChatModel model) {
        return model;
    }
}
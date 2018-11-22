package com.it.yk.fwx_chat_component.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.it.yk.fwx_chat_component.mvp.contract.SelectFriendsContract;
import com.it.yk.fwx_chat_component.mvp.model.SelectFriendsModel;


@Module
public class SelectFriendsModule {
    private SelectFriendsContract.View view;

    /**
     * 构建SelectFriendsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SelectFriendsModule(SelectFriendsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SelectFriendsContract.View provideSelectFriendsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SelectFriendsContract.Model provideSelectFriendsModel(SelectFriendsModel model) {
        return model;
    }
}
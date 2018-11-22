package com.it.yk.fwx_chat_component.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.it.yk.fwx_chat_component.mvp.contract.GroupMettingContract;
import com.it.yk.fwx_chat_component.mvp.model.GroupMettingModel;


@Module
public class GroupMettingModule {
    private GroupMettingContract.View view;

    /**
     * 构建GroupMettingModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public GroupMettingModule(GroupMettingContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GroupMettingContract.View provideGroupMettingView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    GroupMettingContract.Model provideGroupMettingModel(GroupMettingModel model) {
        return model;
    }
}
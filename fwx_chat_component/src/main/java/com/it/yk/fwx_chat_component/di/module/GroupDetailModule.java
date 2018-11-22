package com.it.yk.fwx_chat_component.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.it.yk.fwx_chat_component.mvp.contract.GroupDetailContract;
import com.it.yk.fwx_chat_component.mvp.model.GroupDetailModel;


@Module
public class GroupDetailModule {
    private GroupDetailContract.View view;

    /**
     * 构建GroupDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public GroupDetailModule(GroupDetailContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GroupDetailContract.View provideGroupDetailView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    GroupDetailContract.Model provideGroupDetailModel(GroupDetailModel model) {
        return model;
    }
}
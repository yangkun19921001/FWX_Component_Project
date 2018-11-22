package com.it.yk.fwx_chat_component.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.it.yk.fwx_chat_component.di.module.GroupListModule;

import com.it.yk.fwx_chat_component.mvp.ui.activity.GroupListActivity;

@ActivityScope
@Component(modules = GroupListModule.class, dependencies = AppComponent.class)
public interface GroupListComponent {
    void inject(GroupListActivity activity);
}
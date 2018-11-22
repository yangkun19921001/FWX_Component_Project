package com.it.yk.fwx_chat_component.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.it.yk.fwx_chat_component.di.module.GroupDetailModule;

import com.it.yk.fwx_chat_component.mvp.ui.activity.GroupDetailActivity;

@ActivityScope
@Component(modules = GroupDetailModule.class, dependencies = AppComponent.class)
public interface GroupDetailComponent {
    void inject(GroupDetailActivity activity);
}
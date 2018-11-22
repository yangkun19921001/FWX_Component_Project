package com.it.yk.fwx_chat_component.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.it.yk.fwx_chat_component.di.module.GroupMettingModule;

import com.it.yk.fwx_chat_component.mvp.ui.activity.GroupMettingActivity;

@ActivityScope
@Component(modules = GroupMettingModule.class, dependencies = AppComponent.class)
public interface GroupMettingComponent {
    void inject(GroupMettingActivity activity);
}
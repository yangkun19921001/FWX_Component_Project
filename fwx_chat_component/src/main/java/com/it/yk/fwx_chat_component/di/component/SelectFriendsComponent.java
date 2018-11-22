package com.it.yk.fwx_chat_component.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.it.yk.fwx_chat_component.di.module.SelectFriendsModule;

import com.it.yk.fwx_chat_component.mvp.ui.activity.SelectFriendsActivity;

@ActivityScope
@Component(modules = SelectFriendsModule.class, dependencies = AppComponent.class)
public interface SelectFriendsComponent {
    void inject(SelectFriendsActivity activity);
}
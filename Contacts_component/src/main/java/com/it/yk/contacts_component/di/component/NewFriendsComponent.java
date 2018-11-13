package com.it.yk.contacts_component.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.it.yk.contacts_component.di.module.NewFriendsModule;

import com.it.yk.contacts_component.mvp.ui.activity.NewFriendsActivity;

@ActivityScope
@Component(modules = NewFriendsModule.class, dependencies = AppComponent.class)
public interface NewFriendsComponent {
    void inject(NewFriendsActivity activity);
}
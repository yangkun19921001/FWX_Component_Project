package com.it.yk.fwx_chat_component.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.it.yk.fwx_chat_component.di.module.ChatComponentModule;

import com.it.yk.fwx_chat_component.mvp.ui.activity.ChatComponentActivity;

@ActivityScope
@Component(modules = ChatComponentModule.class, dependencies = AppComponent.class)
public interface ChatComponentComponent {
    void inject(ChatComponentActivity activity);
}
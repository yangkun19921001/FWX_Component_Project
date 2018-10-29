package com.it.yk.weichatappcation.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.it.yk.weichatappcation.di.module.WeiChatModule;

import com.it.yk.weichatappcation.mvp.ui.fragment.WeiChatFragment;

@ActivityScope
@Component(modules = WeiChatModule.class, dependencies = AppComponent.class)
public interface WeiChatComponent {
    void inject(WeiChatFragment fragment);
}
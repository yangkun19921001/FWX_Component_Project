package com.it.yk.fwx_chat_component.di.component;

import com.it.yk.fwx_chat_component.mvp.ui.activity.AllContactsActivity;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.it.yk.fwx_chat_component.di.module.ChatAddGroupModule;

@ActivityScope
@Component(modules = ChatAddGroupModule.class, dependencies = AppComponent.class)
public interface ChatAddGroupComponent {
    void inject(AllContactsActivity activity);
}
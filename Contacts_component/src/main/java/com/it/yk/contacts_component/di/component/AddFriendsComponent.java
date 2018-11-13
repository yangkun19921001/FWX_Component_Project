package com.it.yk.contacts_component.di.component;

import com.it.yk.contacts_component.di.module.AddFriendsModule;
import com.it.yk.contacts_component.mvp.ui.activity.AddFriendsActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(modules = AddFriendsModule.class, dependencies = AppComponent.class)
public interface AddFriendsComponent {
    void inject(AddFriendsActivity activity);
}
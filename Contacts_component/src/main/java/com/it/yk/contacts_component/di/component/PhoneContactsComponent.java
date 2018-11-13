package com.it.yk.contacts_component.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.it.yk.contacts_component.di.module.PhoneContactsModule;

import com.it.yk.contacts_component.mvp.ui.activity.PhoneContactsActivity;

@ActivityScope
@Component(modules = PhoneContactsModule.class, dependencies = AppComponent.class)
public interface PhoneContactsComponent {
    void inject(PhoneContactsActivity activity);
}
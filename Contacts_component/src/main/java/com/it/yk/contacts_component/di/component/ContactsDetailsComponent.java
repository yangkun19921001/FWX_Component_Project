package com.it.yk.contacts_component.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.it.yk.contacts_component.di.module.ContactsDetailsModule;

import com.it.yk.contacts_component.mvp.ui.activity.ContactsDetailsActivity;

@ActivityScope
@Component(modules = ContactsDetailsModule.class, dependencies = AppComponent.class)
public interface ContactsDetailsComponent {
    void inject(ContactsDetailsActivity activity);
}
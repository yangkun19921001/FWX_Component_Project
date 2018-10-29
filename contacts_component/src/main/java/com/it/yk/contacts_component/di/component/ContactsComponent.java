package com.it.yk.contacts_component.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.it.yk.contacts_component.di.module.ContactsModule;

import com.it.yk.contacts_component.mvp.ui.fragment.ContactsFragment;

@ActivityScope
@Component(modules = ContactsModule.class, dependencies = AppComponent.class)
public interface ContactsComponent {
    void inject(ContactsFragment fragment);
}
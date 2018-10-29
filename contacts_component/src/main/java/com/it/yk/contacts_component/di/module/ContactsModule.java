package com.it.yk.contacts_component.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.it.yk.contacts_component.mvp.contract.ContactsContract;
import com.it.yk.contacts_component.mvp.model.ContactsModel;


@Module
public class ContactsModule {
    private ContactsContract.View view;

    /**
     * 构建ContactsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ContactsModule(ContactsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ContactsContract.View provideContactsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ContactsContract.Model provideContactsModel(ContactsModel model) {
        return model;
    }
}
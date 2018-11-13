package com.it.yk.contacts_component.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.it.yk.contacts_component.mvp.contract.PhoneContactsContract;
import com.it.yk.contacts_component.mvp.model.PhoneContactsModel;


@Module
public class PhoneContactsModule {
    private PhoneContactsContract.View view;

    /**
     * 构建PhoneContactsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PhoneContactsModule(PhoneContactsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PhoneContactsContract.View providePhoneContactsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PhoneContactsContract.Model providePhoneContactsModel(PhoneContactsModel model) {
        return model;
    }
}
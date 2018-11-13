package com.it.yk.contacts_component.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.it.yk.contacts_component.mvp.contract.ContactsContract;
import com.it.yk.contacts_component.mvp.model.ContactsModel;
import com.it.yk.contacts_component.mvp.model.entity.ContactsEntity;
import com.it.yk.contacts_component.mvp.ui.fragment.adapter.ContactsAdapter;
import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;


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

    /**
     * 通讯录适配
     * @return
     */
    @ActivityScope
    @Provides
    RecyclerView.LayoutManager layoutManager() {
        return new LinearLayoutManager(view.getContext());
    }

    /**
     * 集合
     *
     * @return
     */
    @ActivityScope
    @Provides
    List<ContactsEntity> list() {
        return new ArrayList<ContactsEntity>();
    }

    /**
     * 通讯录适配
     *
     * @return
     */
    @ActivityScope
    @Provides
    ContactsAdapter contactsAdapter(List<ContactsEntity> list) {
        return new ContactsAdapter(list);
    }
}
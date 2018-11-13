package com.it.yk.contacts_component.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.it.yk.contacts_component.mvp.contract.ContactsDetailsContract;
import com.it.yk.contacts_component.mvp.model.ContactsDetailsModel;
import com.it.yk.contacts_component.mvp.model.entity.ContactsDetailsEntity;
import com.it.yk.contacts_component.mvp.ui.fragment.adapter.ContactsDetailsAdapter;
import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;


@Module
public class ContactsDetailsModule {
    private ContactsDetailsContract.View view;

    /**
     * 构建ContactsDetailsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ContactsDetailsModule(ContactsDetailsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ContactsDetailsContract.View provideContactsDetailsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ContactsDetailsContract.Model provideContactsDetailsModel(ContactsDetailsModel model) {
        return model;
    }


    @ActivityScope
    @Provides
    RecyclerView.LayoutManager layoutManager() {
        return new LinearLayoutManager(view.getContext());
    }


    @ActivityScope
    @Provides
    List<ContactsDetailsEntity> list() {
        return new ArrayList<ContactsDetailsEntity>();
    }

    @ActivityScope
    @Provides
    ContactsDetailsAdapter addFriendsAdapter(List<ContactsDetailsEntity> list) {
        return new ContactsDetailsAdapter(list);
    }
}
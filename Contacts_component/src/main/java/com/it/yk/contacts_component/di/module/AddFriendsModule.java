package com.it.yk.contacts_component.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.it.yk.contacts_component.mvp.contract.AddFriendsContract;
import com.it.yk.contacts_component.mvp.model.AddFriendsModel;
import com.it.yk.contacts_component.mvp.model.entity.AddFriendsEntity;
import com.it.yk.contacts_component.mvp.ui.fragment.adapter.AddFriendsAdapter;
import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;


@Module
public class AddFriendsModule {
    private AddFriendsContract.View view;

    /**
     * 构建AddFriendsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public AddFriendsModule(AddFriendsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    AddFriendsContract.View provideAddFriendsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    AddFriendsContract.Model provideAddFriendsModel(AddFriendsModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager layoutManager() {
        return new LinearLayoutManager(view.getContext());
    }


    @ActivityScope
    @Provides
    List<AddFriendsEntity> list() {
        return new ArrayList<AddFriendsEntity>();
    }

    @ActivityScope
    @Provides
    AddFriendsAdapter addFriendsAdapter(List<AddFriendsEntity> list) {
        return new AddFriendsAdapter(list);
    }
}
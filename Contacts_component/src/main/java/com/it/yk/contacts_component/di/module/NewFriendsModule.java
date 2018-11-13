package com.it.yk.contacts_component.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.it.yk.contacts_component.mvp.contract.NewFriendsContract;
import com.it.yk.contacts_component.mvp.model.NewFriendsModel;
import com.it.yk.contacts_component.mvp.model.entity.NewFriendsEntity;
import com.it.yk.contacts_component.mvp.ui.fragment.adapter.NewFriendsAdapter;
import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;


@Module
public class NewFriendsModule {
    private NewFriendsContract.View view;

    /**
     * 构建NewFriendsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public NewFriendsModule(NewFriendsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    NewFriendsContract.View provideNewFriendsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    NewFriendsContract.Model provideNewFriendsModel(NewFriendsModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    List<NewFriendsEntity> newFriendsEntityList(){
        return new ArrayList<NewFriendsEntity>();
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager layoutManager(){
        return new LinearLayoutManager(view.getContext());
    }

    @ActivityScope
    @Provides
    NewFriendsAdapter newFriendsAdapter (List<NewFriendsEntity> list){
        return new NewFriendsAdapter(list);
    }
}
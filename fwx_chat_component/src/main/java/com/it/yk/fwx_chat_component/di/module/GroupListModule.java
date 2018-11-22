package com.it.yk.fwx_chat_component.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.Utils;
import com.it.yk.fwx_chat_component.mvp.contract.GroupListContract;
import com.it.yk.fwx_chat_component.mvp.model.GroupListModel;
import com.it.yk.fwx_chat_component.mvp.model.entity.GroupListEntity;
import com.it.yk.fwx_chat_component.mvp.ui.activity.adapter.GroupListAdapter;
import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;


@Module
public class GroupListModule {
    private GroupListContract.View view;

    /**
     * 构建GroupListModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public GroupListModule(GroupListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GroupListContract.View provideGroupListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    GroupListAdapter groupListAdapter(List<GroupListEntity> listEntityList){
        return new GroupListAdapter(listEntityList);
    }


    @ActivityScope
    @Provides
    RecyclerView.LayoutManager layoutManager(){
        return new LinearLayoutManager(Utils.getApp());
    }

    @ActivityScope
    @Provides
    List<GroupListEntity> lists(){
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    GroupListContract.Model provideGroupListModel(GroupListModel model) {
        return model;
    }
}
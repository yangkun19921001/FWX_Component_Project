package com.it.yk.fwx_chat_component.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.it.yk.fwx_chat_component.mvp.contract.ChatAddGroupContract;
import com.it.yk.fwx_chat_component.mvp.model.ChatAddGroupModel;
import com.it.yk.fwx_chat_component.mvp.model.entity.AllContactsEntity;
import com.it.yk.fwx_chat_component.mvp.ui.activity.adapter.AllContacts;
import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;


@Module
public class ChatAddGroupModule {
    private ChatAddGroupContract.View view;

    /**
     * 构建ChatAddGroupModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ChatAddGroupModule(ChatAddGroupContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ChatAddGroupContract.View provideChatAddGroupView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ChatAddGroupContract.Model provideChatAddGroupModel(ChatAddGroupModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    List<AllContactsEntity> allContactsEntities() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager mLayoutManager(){
        return new LinearLayoutManager(com.blankj.utilcode.util.Utils.getApp());
    }
    @ActivityScope
    @Provides
    AllContacts allContactsAdapter(List<AllContactsEntity> list) {
        return new AllContacts(list);
    }
}
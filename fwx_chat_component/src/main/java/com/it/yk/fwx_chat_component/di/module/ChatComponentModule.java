package com.it.yk.fwx_chat_component.di.module;

import com.iit.yk.chat_base_component.imuisample.models.MyMessage;
import com.it.yk.fwx_chat_component.mvp.contract.ChatComponentContract;
import com.it.yk.fwx_chat_component.mvp.model.ChatComponentModel;
import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;


@Module
public class ChatComponentModule {
    private ChatComponentContract.View view;

    /**
     * 构建ChatComponentModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ChatComponentModule(ChatComponentContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ChatComponentContract.View provideChatComponentView() {
        return this.view;
    }



    @ActivityScope
    @Provides
    ChatComponentContract.Model provideChatComponentModel(ChatComponentModel model) {
        return model;
    }
    @ActivityScope
    @Provides
    List<MyMessage> myMessageList(){
        return new ArrayList<>();
    };
}
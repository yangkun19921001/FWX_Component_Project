package com.it.yk.weichatappcation.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.iit.yk.chat_base_component.imuisample.models.MyMessage;
import com.it.yk.weichatappcation.mvp.contract.WeiChatContract;
import com.it.yk.weichatappcation.mvp.model.WeiChatModel;
import com.it.yk.weichatappcation.mvp.model.entity.WeiChatMessageHistoryEntity;
import com.it.yk.weichatappcation.mvp.ui.fragment.adapter.WeiChatHistoryAdapter;
import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;


@Module
public class WeiChatModule {
    private WeiChatContract.View view;

    /**
     * 构建WeiChatModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public WeiChatModule(WeiChatContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    WeiChatContract.View provideWeiChatView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    WeiChatContract.Model provideWeiChatModel(WeiChatModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    List<WeiChatMessageHistoryEntity> messageLists() {
        return new ArrayList<WeiChatMessageHistoryEntity>();
    }

    @ActivityScope
    @Provides
    List<MyMessage> myMessageList(){
        return new ArrayList<MyMessage>();
    }


    @ActivityScope
    @Provides
    RecyclerView.LayoutManager layoutManager(){
        return new LinearLayoutManager(view.getContext());
    }
    @ActivityScope
    @Provides
    WeiChatHistoryAdapter weiChatHistoryAdapter(List<WeiChatMessageHistoryEntity> list) {
        return new WeiChatHistoryAdapter(list);
    }
}
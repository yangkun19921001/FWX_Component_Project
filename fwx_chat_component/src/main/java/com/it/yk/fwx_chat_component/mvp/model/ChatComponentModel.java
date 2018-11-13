package com.it.yk.fwx_chat_component.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.it.yk.fwx_chat_component.mvp.contract.ChatComponentContract;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;


@ActivityScope
public class ChatComponentModel extends BaseModel implements ChatComponentContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public ChatComponentModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

}
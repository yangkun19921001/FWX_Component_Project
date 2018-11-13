package com.it.yk.contacts_component.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.it.yk.contacts_component.mvp.contract.NewFriendsContract;


@ActivityScope
public class NewFriendsModel extends BaseModel implements NewFriendsContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public NewFriendsModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
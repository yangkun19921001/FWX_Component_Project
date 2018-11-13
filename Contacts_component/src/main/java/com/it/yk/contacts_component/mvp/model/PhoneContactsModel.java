package com.it.yk.contacts_component.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.it.yk.contacts_component.mvp.contract.PhoneContactsContract;


@ActivityScope
public class PhoneContactsModel extends BaseModel implements PhoneContactsContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public PhoneContactsModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
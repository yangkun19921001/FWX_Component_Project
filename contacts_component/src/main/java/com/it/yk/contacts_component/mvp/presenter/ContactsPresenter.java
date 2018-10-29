package com.it.yk.contacts_component.mvp.presenter;

import android.app.Application;

import com.it.yk.contacts_component.mvp.contract.ContactsContract;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class ContactsPresenter extends BasePresenter<ContactsContract.Model, ContactsContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public ContactsPresenter(ContactsContract.Model model, ContactsContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

}

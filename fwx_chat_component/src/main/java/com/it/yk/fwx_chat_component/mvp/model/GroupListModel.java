package com.it.yk.fwx_chat_component.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.helper.HXHelper;
import com.hyphenate.exceptions.HyphenateException;
import com.it.yk.fwx_chat_component.mvp.contract.GroupListContract;
import com.it.yk.fwx_chat_component.mvp.model.entity.GroupListEntity;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


@ActivityScope
public class GroupListModel extends BaseModel implements GroupListContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    List<GroupListEntity> mList;

    @Inject
    public GroupListModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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

    @Override
    public Observable<List<EMGroup>> getGroupLists() {
        return Observable.create(new ObservableOnSubscribe<List<EMGroup>>() {
            @Override
            public void subscribe(ObservableEmitter<List<EMGroup>> emitter)  {
                try {
                    List<EMGroup> groupLists = HXHelper.getInstance().getGroupHelper().getGroupLists();
                    emitter.onNext(groupLists);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
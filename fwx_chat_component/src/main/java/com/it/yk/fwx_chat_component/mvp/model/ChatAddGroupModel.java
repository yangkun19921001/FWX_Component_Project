package com.it.yk.fwx_chat_component.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.hyphenate.easeui.helper.HXHelper;
import com.hyphenate.exceptions.HyphenateException;
import com.it.yk.fwx_chat_component.mvp.contract.ChatAddGroupContract;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


@ActivityScope
public class ChatAddGroupModel extends BaseModel implements ChatAddGroupContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public ChatAddGroupModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public Observable<List<String>> getAllContacts(ChatAddGroupContract.View view) {
        return Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) {
                try {
                    List<String> allContactsFromServer = HXHelper.getInstance().getFriendEngine().getAllContactsFromServer();
                    emitter.onNext(allContactsFromServer);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .compose(RxLifecycleUtils.bindToLifecycle(view))
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 创建群组
     */
    @Override
    public Observable<String> createGroup(String groupName, String desc, String[] allMembers, String reason) {

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                try {
                    HXHelper.getInstance().getGroupHelper().createGroup(groupName, desc, allMembers, reason);
                    emitter.onNext("创建成功");
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    emitter.onError(e);
                    emitter.onNext("创建失败--" + e.getMessage());
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
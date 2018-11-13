package com.it.yk.contacts_component.mvp.presenter;

import android.app.Application;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.easeui.helper.HXHelper;
import com.hyphenate.exceptions.HyphenateException;
import com.it.yk.contacts_component.R;
import com.it.yk.contacts_component.mvp.contract.ContactsContract;
import com.it.yk.contacts_component.mvp.model.entity.ContactsEntity;
import com.it.yk.contacts_component.mvp.ui.fragment.adapter.ContactsAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yk.component.res.status_view.StatusViewHelper;
import com.yk.component.sdk.core.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class ContactsPresenter extends BasePresenter<ContactsContract.Model, ContactsContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private IndexBar mIndexBar;
    private TextView mTvSideBarHint;

    private static final String INDEX_SEARCH_TOP = "↑";
    /**
     * 索引顶部数据
     */
    private static final String INDEX_STRING_TOP = "☆";

    /**
     * 联系人数据
     */
    @Inject
    List<ContactsEntity> mList;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;


    @Inject
    ContactsAdapter mContactsAdapter;

    /**
     * 从服务端获取的通讯录
     */
    List<ContactsEntity> mToServerFriendLists = null;
    private StatusViewHelper mStatusViewHelper;
    private SuspensionDecoration mDecoration;
    private RecyclerView mRecyclerView;

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


    /**
     * 初始化索引栏
     *
     * @param inflate
     */
    public void initView(View inflate) {
        mIndexBar = inflate.findViewById(R.id.indexBar);
        mTvSideBarHint = inflate.findViewById(R.id.tvSideBarHint);
        mStatusViewHelper = new StatusViewHelper(mAppManager.getTopActivity(), inflate);
        mStatusViewHelper.initView();

        mRecyclerView = mStatusViewHelper.getRecyclerView();
        SmartRefreshLayout refreshLayout = mStatusViewHelper.getRefreshLayout();
        refreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        //添加搜索框
        mRecyclerView.setAdapter(mContactsAdapter);

        mDecoration = new SuspensionDecoration(mApplication, mList);
        mRecyclerView.addItemDecoration(mDecoration);

        //使用indexBar
        mTvSideBarHint = (TextView) inflate.findViewById(R.id.tvSideBarHint);//HintTextView
        mIndexBar = (IndexBar) inflate.findViewById(R.id.indexBar);//IndexBar

        //indexbar初始化
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager((LinearLayoutManager) mLayoutManager);//设置RecyclerView的LayoutManager

        setTopData();


    }

    /**
     * 设置通讯录顶部数据
     */
    public void setTopData() {
        mList.add((ContactsEntity) new ContactsEntity(Constants.IContacts.NEW_FRIEND, R.drawable.contacts_new_friend,1).setTop(true).setBaseIndexTag(INDEX_SEARCH_TOP));
        mList.add((ContactsEntity) new ContactsEntity(Constants.IContacts.NEW_FRIEND, R.drawable.contacts_new_friend,0).setTop(true).setBaseIndexTag(INDEX_SEARCH_TOP));
        mList.add((ContactsEntity) new ContactsEntity(Constants.IContacts.GROUP_CHAT, R.drawable.contacts_group_cheat,0).setTop(true).setBaseIndexTag(INDEX_SEARCH_TOP));
        mList.add((ContactsEntity) new ContactsEntity(Constants.IContacts.TITLE, R.drawable.contacts_tag,0).setTop(true).setBaseIndexTag(INDEX_SEARCH_TOP));
        mList.add((ContactsEntity) new ContactsEntity(Constants.IContacts.Public_Number, R.drawable.contacts_offical,0).setTop(true).setBaseIndexTag(INDEX_SEARCH_TOP));
        //更新数据
        upAdapter();
        mStatusViewHelper.showContent();
    }

    /**
     * 更新适配器数据
     */
    public void upAdapter() {
        mContactsAdapter.notifyDataSetChanged();
        mIndexBar.setmSourceDatas(mList)//设置数据
                .invalidate();
        mDecoration.setmDatas(mList);

    }

    /**
     * 获取所有的好友列表
     */
    public void getAllContacts() {
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) {
                try {
                    emitter.onNext(HXHelper.getInstance().getFriendEngine().getAllContactsFromServer());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .map(new Function<List<String>, List<ContactsEntity>>() {
                    @Override
                    public List<ContactsEntity> apply(List<String> list)  {
                        return getAllContacts(list);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁;
                .subscribe(new ErrorHandleSubscriber<List<ContactsEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(List<ContactsEntity> contactsEntities) {
                        if (contactsEntities != null && contactsEntities.size() > 0) {
                            mList.addAll(contactsEntities);
                            upAdapter();
                        }else {
//                            mStatusViewHelper.showEmpty();
                        }

                    }
                });
    }

    /**
     * 获取我所有的好友
     *
     * @param list
     * @return
     */
    private List<ContactsEntity> getAllContacts(List<String> list) {
        if (mToServerFriendLists == null)
            mToServerFriendLists = new ArrayList<>();
        mToServerFriendLists.clear();
        for (String myFriend : list
                ) {
            ContactsEntity contactsEntity = new ContactsEntity();
            contactsEntity.setName(myFriend);
            mToServerFriendLists.add(contactsEntity);
        }
        return mToServerFriendLists;
    }
}

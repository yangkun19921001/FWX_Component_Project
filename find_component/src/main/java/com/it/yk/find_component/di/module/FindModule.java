package com.it.yk.find_component.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.it.yk.find_component.mvp.contract.FindContract;
import com.it.yk.find_component.mvp.model.FindModel;
import com.it.yk.find_component.mvp.model.entity.FindEntity;
import com.it.yk.find_component.mvp.ui.fragment.adapter.FindAdapter;
import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;


@Module
public class FindModule {
    private FindContract.View view;

    /**
     * 构建FindModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public FindModule(FindContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    FindContract.View provideFindView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    FindContract.Model provideFindModel(FindModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager layoutManager(){
        return new LinearLayoutManager(view.getContxt());
    }

    @ActivityScope
    @Provides
    List<FindEntity> findEntityList(){
        return new ArrayList<FindEntity>();
    }

    @ActivityScope
    @Provides
    FindAdapter findAdapter(List<FindEntity> list) {
        return new FindAdapter(list);
    }
}
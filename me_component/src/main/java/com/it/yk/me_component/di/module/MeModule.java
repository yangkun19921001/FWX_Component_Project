package com.it.yk.me_component.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.it.yk.me_component.mvp.contract.MeContract;
import com.it.yk.me_component.mvp.model.MeModel;
import com.it.yk.me_component.mvp.model.entity.MeEntity;
import com.it.yk.me_component.mvp.ui.fragment.adapter.MeAdapter;
import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;


@Module
public class MeModule {
    private MeContract.View view;

    /**
     * 构建MeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MeModule(MeContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MeContract.View provideMeView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MeContract.Model provideMeModel(MeModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager layoutManager(){
        return new LinearLayoutManager(view.getContxt());
    }

    @ActivityScope
    @Provides
    List<MeEntity> findEntityList(){
        return new ArrayList<MeEntity>();
    }

    @ActivityScope
    @Provides
    MeAdapter meAdapter(List<MeEntity> list) {
        return new MeAdapter(list);
    }
}
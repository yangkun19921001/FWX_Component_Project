package me.jessyan.armscomponent.app.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import me.jessyan.armscomponent.app.mvp.contract.SplashContract;
import me.jessyan.armscomponent.app.mvp.model.SplashModel;


@Module
public class SplashModule {
    private SplashContract.View view;

    /**
     * 构建SplashModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SplashModule(SplashContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SplashContract.View provideSplashView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SplashContract.Model provideSplashModel(SplashModel model) {
        return model;
    }
}
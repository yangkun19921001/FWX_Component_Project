package me.jessyan.armscomponent.app.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import me.jessyan.armscomponent.app.di.module.HomeModule;

import me.jessyan.armscomponent.app.mvp.ui.activity.HomeActivity;

@ActivityScope
@Component(modules = HomeModule.class, dependencies = AppComponent.class)
public interface HomeComponent {
    void inject(HomeActivity activity);
}
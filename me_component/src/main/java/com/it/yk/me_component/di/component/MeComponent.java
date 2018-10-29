package com.it.yk.me_component.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.it.yk.me_component.di.module.MeModule;

import com.it.yk.me_component.mvp.ui.fragment.MeFragment;

@ActivityScope
@Component(modules = MeModule.class, dependencies = AppComponent.class)
public interface MeComponent {
    void inject(MeFragment fragment);
}
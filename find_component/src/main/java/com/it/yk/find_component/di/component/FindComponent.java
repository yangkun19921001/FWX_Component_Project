package com.it.yk.find_component.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.it.yk.find_component.di.module.FindModule;

import com.it.yk.find_component.mvp.ui.fragment.FindFragment;

@ActivityScope
@Component(modules = FindModule.class, dependencies = AppComponent.class)
public interface FindComponent {
    void inject(FindFragment fragment);
}
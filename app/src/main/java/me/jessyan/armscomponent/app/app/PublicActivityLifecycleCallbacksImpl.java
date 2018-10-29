package me.jessyan.armscomponent.app.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.jess.arms.utils.ArmsUtils;

import me.jessyan.armscomponent.app.mvp.ui.activity.HomeActivity;

/**
 * Created by yangk on 2018/10/23.
 */

class PublicActivityLifecycleCallbacksImpl implements android.app.Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {



    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (activity instanceof HomeActivity){
            if (ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_title") != null) {
//                ((TextView) ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_title")).setText("微信");
            }

            if (ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_back") != null) {
                ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_back").setVisibility(View.GONE);
            }
        }else {
            if (ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_back") != null) {
                ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_back").setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}

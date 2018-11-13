package me.jessyan.armscomponent.app.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.it.yk.contacts_component.mvp.ui.activity.AddFriendsActivity;
import com.it.yk.contacts_component.mvp.ui.activity.ContactsDetailsActivity;
import com.it.yk.contacts_component.mvp.ui.activity.NewFriendsActivity;
import com.it.yk.contacts_component.mvp.ui.activity.PhoneContactsActivity;
import com.jess.arms.utils.ArmsUtils;
import com.yk.component.sdk.core.RouterHub;
import com.yk.component.sdk.utils.Utils;

import me.jessyan.armscomponent.app.R;
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

        showTab(activity);
        setTitleView(activity);
    }

    /**
     * 设置 Toolbar 上的一些属性
     *
     * @param activity
     */
    private void setTitleView(Activity activity) {
        if (activity instanceof HomeActivity) {
            if (ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_iv_more") != null) {
                ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_iv_more").setVisibility(View.VISIBLE);
            }

            if (ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_back") != null) {
                ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_back").setVisibility(View.GONE);
            }
        } else {
            if (ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_back") != null) {
                ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_back").setVisibility(View.VISIBLE);
            }

            if (ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_iv_more") != null) {
                ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_iv_more").setVisibility(View.GONE);
            }
        }

        if (activity instanceof NewFriendsActivity) {
            if (ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_title_right") != null) {
                ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_title_right").setVisibility(View.VISIBLE);
                TextView public_toolbar_title_right = ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_title_right");
                public_toolbar_title_right.setText(R.string.add_contacts);
                public_toolbar_title_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.navigation(RouterHub.FRIENDS_AddFriendsActivity);
                    }
                });
            }
        } else {
            if (ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_title_right") != null) {
                ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_title_right").setVisibility(View.GONE);
            }
        }
    }

    /**
     * 显示每个页面标题
     *
     * @param activity
     */
    private void showTab(Activity activity) {
        if (activity instanceof AddFriendsActivity) {
            if (ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_title") != null) {
                TextView public_toolbar_title = (TextView) ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_title");
                public_toolbar_title.setText(R.string.add_friend);
            }
        } else if (activity instanceof ContactsDetailsActivity) {
            if (ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_title") != null) {
                TextView public_toolbar_title = (TextView) ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_title");
                public_toolbar_title.setText(R.string.ContactsDetails);
            }
        } else if (activity instanceof NewFriendsActivity) {
            if (ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_title") != null) {
                TextView public_toolbar_title = (TextView) ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_title");
                public_toolbar_title.setText(R.string.NewFriends);
            }
        } else if (activity instanceof PhoneContactsActivity) {
            if (ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_title") != null) {
                TextView public_toolbar_title = (TextView) ArmsUtils.findViewByName(activity.getApplicationContext(), activity, "public_toolbar_title");
                public_toolbar_title.setText(R.string.PHONE_CONTACTS_FRIEND);
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

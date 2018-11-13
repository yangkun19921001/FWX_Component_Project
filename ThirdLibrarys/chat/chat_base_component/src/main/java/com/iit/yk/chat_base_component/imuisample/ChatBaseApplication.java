package com.iit.yk.chat_base_component.imuisample;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.iit.yk.chat_base_component.BuildConfig;


//import com.squareup.leakcanary.LeakCanary;


public class ChatBaseApplication extends Application {

    private ChatBaseApplication mImuiSampleApplication;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
        mImuiSampleApplication = this;
        Utils.init(getApplicationContext());
        CrashUtils.init(new CrashUtils.OnCrashListener() {
            @Override
            public void onCrash(String crashInfo, Throwable e) {
                LogUtils.e(crashInfo);
                ToastUtils.showShort(crashInfo);
            }
        });

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }
    }


    public static void init(Context application) {
        Utils.init(application);
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }
    }
}

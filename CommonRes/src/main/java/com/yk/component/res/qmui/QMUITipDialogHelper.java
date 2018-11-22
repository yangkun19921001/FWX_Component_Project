package com.yk.component.res.qmui;

import android.app.Activity;
import android.os.Handler;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.lang.ref.WeakReference;

/**
 * Created by yangk on 2018/5/17.
 */

public class QMUITipDialogHelper {

    private QMUITipDialog tipDialog;
    private WeakReference<Activity> weakReference;

    private static QMUITipDialogHelper instance;

    public static QMUITipDialogHelper getInstance() {
        if (instance == null)
            instance = new QMUITipDialogHelper();
        return instance;
    }

    public void onDetory() {
        if (instance != null)
            instance = null;
    }

    /**
     * 加载中视图
     * 0: "正在加载"
     * 1： "发送成功"
     * 2："发送失败"
     * 3："请勿重复操作"
     * 4：
     */
    public void showLoading(Activity activity, String meg, int type, long delayTime, final ILoadListener iLoadListener) {
        weakReference = new WeakReference<Activity>(activity);
        if (tipDialog != null) {
            tipDialog.dismiss();
            tipDialog = null;
        }
        switch (type) {
            case 0:
                tipDialog = new QMUITipDialog.Builder(weakReference.get())
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord(meg)
                        .create();
                break;
            case 1:
                tipDialog = new QMUITipDialog.Builder(weakReference.get())
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                        .setTipWord(meg)
                        .create();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tipDialog.dismiss();
                        if (iLoadListener != null)
                            iLoadListener.loadSucceed();
                    }
                }, delayTime);
                break;
            case 2:
                tipDialog = new QMUITipDialog.Builder(weakReference.get())
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                        .setTipWord(meg)
                        .create();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tipDialog.dismiss();
                        if (iLoadListener != null)
                            iLoadListener.loadSucceed();
                    }
                }, delayTime);
                break;
            case 3:
                tipDialog = new QMUITipDialog.Builder(weakReference.get())
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                        .setTipWord(meg)
                        .create();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tipDialog.dismiss();
                        if (iLoadListener != null)
                            iLoadListener.loadSucceed();
                    }
                }, delayTime);
                break;
            case 4:
                tipDialog = new QMUITipDialog.Builder(weakReference.get())
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                        .create();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tipDialog.dismiss();
                        if (iLoadListener != null)
                            iLoadListener.loadSucceed();
                    }
                }, delayTime);
                break;
            case 5:
                tipDialog = new QMUITipDialog.Builder(weakReference.get())
                        .setTipWord(meg)
                        .create();
                break;
            case 6:
                break;
            default:
                break;
        }
        if (tipDialog == null) return;
        tipDialog.show();
    }

    public interface ILoadListener {
        void loadSucceed();
    }
}

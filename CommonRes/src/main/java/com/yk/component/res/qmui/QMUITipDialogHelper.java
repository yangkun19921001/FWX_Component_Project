package com.yk.component.res.qmui;

import android.app.Activity;
import android.os.Handler;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

/**
 * Created by yangk on 2018/5/17.
 */

public class QMUITipDialogHelper {

    private static QMUITipDialog tipDialog;

    /**
     * 加载中视图
     * 0: "正在加载"
     * 1： "发送成功"
     * 2："发送失败"
     * 3："请勿重复操作"
     * 4：
     */
    public static void showLoading(Activity activity, String meg, int type, long delayTime) {
        switch (type) {
            case 0:
                tipDialog = new QMUITipDialog.Builder(activity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord(meg)
                        .create();
                break;
            case 1:
                if (tipDialog != null) {
                    tipDialog.dismiss();
                    tipDialog = null;
                }
                tipDialog = new QMUITipDialog.Builder(activity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                        .setTipWord(meg)
                        .create();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tipDialog.dismiss();
                    }
                }, delayTime);
                break;
            case 2:
                if (tipDialog != null) {
                    tipDialog.dismiss();
                    tipDialog = null;
                }
                tipDialog = new QMUITipDialog.Builder(activity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                        .setTipWord(meg)
                        .create();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tipDialog.dismiss();
                    }
                }, delayTime);
                break;
            case 3:
                if (tipDialog != null) {
                    tipDialog.dismiss();
                    tipDialog = null;
                }
                tipDialog = new QMUITipDialog.Builder(activity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                        .setTipWord(meg)
                        .create();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tipDialog.dismiss();
                    }
                }, delayTime);
                break;
            case 4:
                tipDialog = new QMUITipDialog.Builder(activity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                        .create();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tipDialog.dismiss();
                    }
                }, delayTime);
                break;
            case 5:
                tipDialog = new QMUITipDialog.Builder(activity)
                        .setTipWord(meg)
                        .create();
                break;
            case 6:
                break;
            default:
                if (tipDialog != null) {
                    tipDialog.dismiss();
                    tipDialog = null;
                }
        }
        if (tipDialog == null) return;
        tipDialog.show();
    }
}

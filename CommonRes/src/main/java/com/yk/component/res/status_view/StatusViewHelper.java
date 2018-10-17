package com.yk.component.res.status_view;

import android.app.Activity;
import android.view.View;

import me.jessyan.armscomponent.commonres.R;

/**
 * Created by yangk on 2018/5/17.
 */

public class StatusViewHelper {


    private static MultipleStatusView mMultipleStatusView;
    private static Activity mActivity;

    /**
     * 获的视图对象
     *
     * @return
     */
    public static MultipleStatusView getStausView(Activity mActivity) {
        mMultipleStatusView = (MultipleStatusView)mActivity.findViewById(R.id.multiple_status_view);
        StatusViewHelper.mActivity = mActivity;
        return mMultipleStatusView;
    }


    /**
     * 显示加载视图
     */
    public static void showLoading() {
        if (mMultipleStatusView != null)
            mMultipleStatusView.showLoading();
    }

    /**
     * 显示空视图
     */
    public static void showEmpty() {
        if (mMultipleStatusView != null)
            mMultipleStatusView.showEmpty();
    }

    /**
     * 显示没有网络视图
     */
    public static void showNoNetwork() {
        if (mMultipleStatusView != null)
            mMultipleStatusView.showNoNetwork();
    }

    /**
     * 显示数据视图
     */
    public static void showContent() {
        if (mMultipleStatusView != null)
            mMultipleStatusView.showContent();
    }

    /**
     * 显示失败视图
     */
    public static void showError() {
        if (mMultipleStatusView != null)
            mMultipleStatusView.showError();
    }

    /**
     * 显示重新连接视图
     *
     * @param onReConnectClickListener
     */
    public static void setOnReConnectClickListener(View.OnClickListener onReConnectClickListener) {
        if (mMultipleStatusView != null)
            mMultipleStatusView.setOnRetryClickListener(onReConnectClickListener);
    }



    public static void onDestory(){
        if (StatusViewHelper.mActivity != null)
            StatusViewHelper.mActivity = null;
    }
}

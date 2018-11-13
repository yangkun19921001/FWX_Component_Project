package com.yk.component.res.status_view;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.lang.ref.WeakReference;

import me.jessyan.armscomponent.commonres.R;

/**
 * Created by yangk on 2018/5/17.
 */

public class StatusViewHelper {


    private MultipleStatusView mMultipleStatusView;
    private View mView;
    private RecyclerView mRecyclerView;
    private WeakReference<Activity> mWeakReference;
    private View inflate;
    private SmartRefreshLayout mRefreshLayout;

    /**
     * 获的视图对象
     *
     * @return
     */
    public MultipleStatusView getStausView(Activity m_Activity, View inflate) {
        mWeakReference = new WeakReference<Activity>(m_Activity);
        this.inflate = inflate;
        if (inflate == null)
            mMultipleStatusView = (MultipleStatusView) m_Activity.findViewById(R.id.multiple_status_view);
        return mMultipleStatusView;
    }

    public StatusViewHelper(Activity m_Activity, View inflate) {
        mWeakReference = new WeakReference<Activity>(m_Activity);
        this.inflate = inflate;
        if (inflate != null)
            mMultipleStatusView = (MultipleStatusView) inflate.findViewById(R.id.multiple_status_view);
        mMultipleStatusView = (MultipleStatusView) inflate.findViewById(R.id.multiple_status_view);

        if (getRefreshLayout() == null || getRefreshLayout() == null)
            initView(inflate);
    }

    /**
     * 初始化布局中的对象
     *
     * @param inflate
     */
    public void initView() {
//        showContent();
        mRefreshLayout = inflate.findViewById(R.id.refreshLayout);
        mRecyclerView = inflate.findViewById(R.id.public_recyclerView);
    }

    /**
     * 初始化布局中的对象
     *
     * @param inflate
     */
    public void initView(View inflate) {
        mRefreshLayout = inflate.findViewById(R.id.refreshLayout);
        mRecyclerView = inflate.findViewById(R.id.public_recyclerView);
    }

    /**
     * 获取刷新控件对象
     *
     * @return
     */
    public SmartRefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }

    /**
     * 获取视图对象
     *
     * @return
     */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }


    /**
     * 显示加载视图
     */
    public void showLoading() {
        if (mMultipleStatusView != null)
            mMultipleStatusView.showLoading();
    }

    /**
     * 显示空视图
     */
    public void showEmpty() {
        if (mMultipleStatusView != null)
            mMultipleStatusView.showEmpty();
    }

    /**
     * 显示没有网络视图
     */
    public void showNoNetwork() {
        if (mMultipleStatusView != null)
            mMultipleStatusView.showNoNetwork();
    }

    /**
     * 显示数据视图
     */
    public View showContent() {
        if (mMultipleStatusView != null)
            return mMultipleStatusView.showContent();

        return null;
    }

    /**
     * 显示失败视图
     */
    public void showError() {
        if (mMultipleStatusView != null)
            mMultipleStatusView.showError();
    }

    /**
     * 显示重新连接视图
     *
     * @param onReConnectClickListener
     */
    public void setOnReConnectClickListener(View.OnClickListener onReConnectClickListener) {
        if (mMultipleStatusView != null)
            mMultipleStatusView.setOnRetryClickListener(onReConnectClickListener);
    }


    public void onDestory() {
    }
}

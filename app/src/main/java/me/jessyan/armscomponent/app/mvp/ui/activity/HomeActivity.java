package me.jessyan.armscomponent.app.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.yk.component.res.tab.AlphaTabsIndicator;
import com.yk.component.sdk.core.RouterHub;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.armscomponent.app.R;
import me.jessyan.armscomponent.app.di.component.DaggerHomeComponent;
import me.jessyan.armscomponent.app.di.module.HomeModule;
import me.jessyan.armscomponent.app.mvp.contract.HomeContract;
import me.jessyan.armscomponent.app.mvp.presenter.HomePresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;

@Route(path = RouterHub.APP_MAINACTIVITY)
public class HomeActivity extends BaseActivity<HomePresenter> implements HomeContract.View {


    @BindView(R.id.public_toolbar_title)
    TextView publicToolbarTitle;
    @BindView(R.id.home_frame)
    FrameLayout homeFrame;
    @BindView(R.id.alphaIndicator)
    AlphaTabsIndicator alphaIndicator;
    @BindView(R.id.public_iv_more)
    ImageView publicIvMore;
    @BindView(R.id.public_toolbar_more)
    RelativeLayout publicToolbarMore;
    @BindView(R.id.public_toolbar)
    Toolbar publicToolbar;
    @BindView(R.id.fl_container)
    LinearLayout flContainer;


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerHomeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .homeModule(new HomeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_home; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        //初始化主页面主要 TAG
        mPresenter.initMainTab(savedInstanceState, getSupportFragmentManager(),alphaIndicator, publicToolbarTitle,publicIvMore,publicToolbarMore);
        //监听底部按钮事件
        mPresenter.addButtonClickListener();
        //进入主页面默认进去的页面
        mPresenter.showDefFragment();
        //IM 服务状态监测
        mPresenter.checkIMServer();
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void onBackPressed() {
        //回主页面
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.addCategory(Intent.CATEGORY_HOME);
        launcherIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ArmsUtils.startActivity(launcherIntent);
    }


    @OnClick(R.id.public_toolbar_more)
    public void onViewClicked() {
        mPresenter.addTitleMoreClickListener();
    }
}

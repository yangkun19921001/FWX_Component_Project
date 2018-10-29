package me.jessyan.armscomponent.app.mvp.presenter;

import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.easeui.helper.HXHelper;
import com.it.yk.contacts_component.mvp.ui.fragment.ContactsFragment;
import com.it.yk.find_component.mvp.ui.fragment.FindFragment;
import com.it.yk.me_component.mvp.ui.fragment.MeFragment;
import com.it.yk.weichatappcation.mvp.ui.fragment.WeiChatFragment;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.yk.component.res.tab.AlphaTabsIndicator;
import com.yk.component.res.tab.OnTabChangedListner;
import com.yk.component.res.view.TriangleDrawable;
import com.yk.component.sdk.utils.EventBusTags;
import com.yk.component.sdk.utils.LogHelper;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.jessyan.armscomponent.app.R;
import me.jessyan.armscomponent.app.mvp.contract.HomeContract;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class HomePresenter extends BasePresenter<HomeContract.Model, HomeContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;


    /**
     * 主页面核心页面
     */
    private List<Fragment> mFragments;
    /**
     * 当前的页面
     */
    private int mCurIndex = 0;

    /**
     * 当前页面标签
     */
    private String mCurrentTitlt = "";

    private WeiChatFragment mWeiChatFragment;
    private ContactsFragment mContactsFragment;
    private FindFragment mFindFragment;
    private MeFragment mMeFragment;
    private AlphaTabsIndicator mAlphaIndicator;
    private TextView mmPublicToolbarTitle;
    private ImageView mmPublicIvMore;
    private RelativeLayout mPublicToolbarMore;

    @Inject
    public HomePresenter(HomeContract.Model model, HomeContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    /**
     * 初始化 TAB
     *
     * @param savedInstanceState
     * @param supportFragmentManager
     * @param alphaIndicator
     * @param mPublicToolbarTitle
     * @param mPublicIvMore
     * @param publicToolbarMore
     */
    public void initMainTab(Bundle savedInstanceState, FragmentManager supportFragmentManager, AlphaTabsIndicator alphaIndicator, TextView mPublicToolbarTitle, ImageView mPublicIvMore, RelativeLayout publicToolbarMore) {
        mAlphaIndicator = alphaIndicator;
        mmPublicToolbarTitle = mPublicToolbarTitle;
        mmPublicIvMore = mPublicIvMore;
        mPublicToolbarMore = publicToolbarMore;
        if (savedInstanceState == null) {
            mWeiChatFragment = WeiChatFragment.newInstance();
            mContactsFragment = ContactsFragment.newInstance();
            mFindFragment = FindFragment.newInstance();
            mMeFragment = MeFragment.newInstance();
        } else {
            mCurIndex = savedInstanceState.getInt(EventBusTags.ACTIVITY_FRAGMENT_REPLACE);
            FragmentManager fm = supportFragmentManager;
            mWeiChatFragment = (WeiChatFragment) FragmentUtils.findFragment(fm, WeiChatFragment.class);
            mContactsFragment = (ContactsFragment) FragmentUtils.findFragment(fm, ContactsFragment.class);
            mFindFragment = (FindFragment) FragmentUtils.findFragment(fm, FindFragment.class);
            mMeFragment = (MeFragment) FragmentUtils.findFragment(fm, MeFragment.class);
        }
        if (mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.add(mWeiChatFragment);
            mFragments.add(mContactsFragment);
            mFragments.add(mFindFragment);
            mFragments.add(mMeFragment);
        }
        FragmentUtils.add(supportFragmentManager, mFragments, R.id.home_frame, mCurIndex);
    }

    /**
     * 监听底部按钮点击事件
     */
    public void addButtonClickListener() {
        mAlphaIndicator.setOnTabChangedListner(new OnTabChangedListner() {
            @Override
            public void onTabSelected(int tabNum) {
                switch (tabNum) {
                    case 0:
                        mCurIndex = 0;
                        mCurrentTitlt = mWeiChatFragment.getTitle();
                        break;
                    case 1:
                        mCurIndex = 1;
                        mCurrentTitlt = mContactsFragment.getTitle();
                        break;
                    case 2:
                        mCurIndex = 2;
                        mCurrentTitlt = mFindFragment.getTitle();
                        break;
                    case 3:
                        mCurIndex = 3;
                        mCurrentTitlt = mMeFragment.getTitle();
                        break;
                    default:
                        break;
                }
                mmPublicToolbarTitle.setText(mCurrentTitlt);
                FragmentUtils.showHide(mCurIndex, mFragments);
                mmPublicIvMore.setVisibility(View.VISIBLE);
                if (mCurIndex == 0) {
                    mmPublicIvMore.setImageDrawable(ArmsUtils.getDrawablebyResource(mApplication, R.drawable.ic_add_black_24dp));
                } else if (mCurIndex == 1) {
                    mmPublicIvMore.setImageDrawable(ArmsUtils.getDrawablebyResource(mApplication, R.drawable.ic_person_add_black_24dp));
                } else {
                    mmPublicIvMore.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 默认显示的 页面
     */
    public void showDefFragment() {
        mAlphaIndicator.setTabCurrenItem(0);
        mAlphaIndicator.getTabView(mCurIndex).showNumber(66);
        mAlphaIndicator.getTabView(2).showPoint();
        mmPublicToolbarTitle.setText(mWeiChatFragment.getTitle());
    }

    /**
     * 标题栏右侧图标点击事件
     */
    public void addTitleMoreClickListener() {
        if (mCurIndex == 0){
            showPopupInfo();
        }else {
            showAddFriend();
        }

    }

    private void showPopupInfo() {
        EasyPopup  mCirclePop =   EasyPopup.create()
                .setContext(mAppManager.getCurrentActivity())
                .setContentView(R.layout.layout_right_pop)
                .setAnimationStyle(R.style.public_RightPopAnim)
                .setOnViewListener(new EasyPopup.OnViewListener() {
                    @Override
                    public void initViews(View view, EasyPopup basePopup) {
                        View arrowView = view.findViewById(R.id.v_arrow);
                        arrowView.setBackground(new TriangleDrawable(TriangleDrawable.TOP, ArmsUtils.getColor(mApplication,R.color.black_dft)));
                    }
                })
                .setFocusAndOutsideEnable(true)
//                .setBackgroundDimEnable(true)
//                .setDimValue(0.5f)
//                .setDimColor(Color.RED)
//                .setDimView(mTitleBar)
                .apply();

        int offsetX = SizeUtils.dp2px(20) - mPublicToolbarMore.getWidth() / 2;
        int offsetY = 10;
        mCirclePop.showAtAnchorView(mPublicToolbarMore, YGravity.BELOW, XGravity.ALIGN_RIGHT, offsetX, offsetY);
    }


    private void showAddFriend() {
    }

    public void checkIMServer() {
        HXHelper.getInstance().getLoginEngine().addConnectListener(new EMConnectionListener() {
            @Override
            public void onConnected() {
                LogHelper.d(TAG, "IM 服务连接成功");
            }


            @Override
            public void onDisconnected(int i) {
                String error = HXHelper.getInstance().getLoginEngine().connectErrorCode(i);
                ToastUtils.showShort("服务连接失败：" + error);
                LogHelper.d(TAG, "IM 服务连接失败");
            }
        });
    }
}

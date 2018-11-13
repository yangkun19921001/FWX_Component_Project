package me.jessyan.armscomponent.app.mvp.presenter;

import android.app.Application;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hyphenate.easeui.helper.HXHelper;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.armscomponent.app.mvp.contract.SplashContract;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import static com.yk.component.sdk.core.RouterHub.APP_MAINACTIVITY;


@ActivityScope
public class SplashPresenter extends BasePresenter<SplashContract.Model, SplashContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public SplashPresenter(SplashContract.Model model, SplashContract.View rootView
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
     * 检查是否登录
     *
     * @param rlBottom
     */
    public void initToLoginOrMain(RelativeLayout rlBottom) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                String name = Thread.currentThread().getName();
                boolean register = HXHelper.getInstance().getLoginEngine().isRegister();
                if (register) {
                    HXHelper.getInstance().getLoginEngine().loginSucceedInit();
                }
                emitter.onNext(register);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .delay(1800, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<Boolean>(mErrorHandler) {
                    @Override
                    public void onNext(Boolean isLogin) {
                        String name = Thread.currentThread().getName();
                        if (isLogin) {
                            ARouter.getInstance().build(APP_MAINACTIVITY).navigation();
                            mAppManager.getTopActivity().finish();
                        } else {
                            rlBottom.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}

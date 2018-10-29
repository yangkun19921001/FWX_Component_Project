package com.it.yk.login_component.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.easeui.helper.HXHelper;
import com.it.yk.login_component.R;
import com.jess.arms.utils.ArmsUtils;
import com.yk.component.sdk.core.Constants;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yangk on 2018/10/18.
 */

public class LoginView extends RelativeLayout implements View.OnClickListener {
    private static ILoginListener iLoginListener;
    private Context mContext;
    private EditText et_input_phone_number, et_input_password;

    public LoginView(Context context) {
        super(context);

        init(context);
    }


    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public LoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    /**
     * 初始化登录 View
     * @param context
     */
    /**
     * 加载注册View
     *
     * @param context
     */
    private void init(Context context) {
        mContext = context;
        LayoutInflater from = LayoutInflater.from(context);
        View inflate = from.inflate(R.layout.layout_login, this);
        inflate.findViewById(R.id.iv_finsh).setOnClickListener(this);
        inflate.findViewById(R.id.btn_register_next).setOnClickListener(this);
        inflate.findViewById(R.id.tv_other_login).setOnClickListener(this);
        inflate.findViewById(R.id.other_setting).setOnClickListener(this);
        et_input_phone_number = inflate.findViewById(R.id.et_input_phone_number);
        et_input_password = inflate.findViewById(R.id.et_input_password);

        String user = SPUtils.getInstance(Constants.ISP_Config.SP_NAME).getString(Constants.ISP_Config.USER_PHONE_NUMBER, "");
        String pwd = SPUtils.getInstance(Constants.ISP_Config.SP_NAME).getString(Constants.ISP_Config.USER_PASSWORD, "");
        if (!StringUtils.isEmpty(user) || !StringUtils.isEmpty(pwd)) {
            et_input_phone_number.setText(user);
            et_input_password.setText(pwd);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_finsh) {
            ArmsUtils.obtainAppComponentFromContext(mContext).appManager().getTopActivity().finish();
        } else if (v.getId() == R.id.btn_register_next) {
            if (StringUtils.isEmpty(et_input_phone_number.getText().toString().trim()) || StringUtils.isEmpty(et_input_password.getText().toString().trim())) {
                if (iLoginListener != null)
                    iLoginListener.onLoginError(mContext.getString(R.string.login_check_user_info));
                return;
            }

            try {
                if (!NetworkUtils.isConnected()) {
                    if (iLoginListener != null)
                        iLoginListener.onLoginError("检查网络是否连接？");
                    return;
                }
            } catch (Exception e) {
                if (iLoginListener != null)
                    iLoginListener.onLoginError(e.getMessage());
                return;
            }


            if (iLoginListener != null)
                iLoginListener.onLoginStart();

            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                    HXHelper.getInstance().getLoginEngine().onLogin(et_input_phone_number.getText().toString().trim(), et_input_password.getText().toString().trim(), new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            emitter.onNext(Constants.ILoad.LoadSuccess + "");

                            SPUtils.getInstance(Constants.ISP_Config.SP_NAME).put(Constants.ISP_Config.USER_PHONE_NUMBER,et_input_phone_number.getText().toString().trim());
                            SPUtils.getInstance(Constants.ISP_Config.SP_NAME).put(Constants.ISP_Config.USER_PASSWORD,et_input_password.getText().toString().trim());
                        }

                        @Override
                        public void onError(int i, String s) {
                            emitter.onNext(s);

                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String meg) {
                            if (meg.equals(Constants.ILoad.LoadSuccess+"")) {
                                if (iLoginListener != null)
                                    iLoginListener.onLoginSucceed();
                            } else {
                                if (iLoginListener != null)
                                    iLoginListener.onLoginError(meg);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (iLoginListener != null)
                                iLoginListener.onLoginError(e.getMessage().toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });


        } else {
            ToastUtils.showShort("暂未开通，请等待...");
        }
    }

    public static interface ILoginListener {

        void onLoginSucceed();

        void onLoginError(String string);

        void onLoginStart();
    }

    public static void addLoginCallBack(LoginView.ILoginListener iLoginListener) {
        LoginView.iLoginListener = iLoginListener;
    }

    public static void unLoginCallBack() {
        if (iLoginListener != null)
            iLoginListener = null;
    }
}

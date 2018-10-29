package com.it.yk.login_component.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.hyphenate.EMError;
import com.hyphenate.easeui.helper.HXHelper;
import com.hyphenate.exceptions.HyphenateException;
import com.it.yk.login_component.R;
import com.jess.arms.utils.ArmsUtils;

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

public class RegisterView extends RelativeLayout implements View.OnClickListener {


    private static IRegisterListener iRegisterListener;
    private Context mContext;
    private EditText et_input_phone_number;
    private EditText et_input_name;
    private EditText et_input_password;
    private String phone_number;
    private String username;
    private String password;
    private String TAG = this.getClass().getSimpleName();

    public RegisterView(Context context) {
        super(context);
        init(context);
    }


    public RegisterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RegisterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 加载注册View
     *
     * @param context
     */
    private void init(Context context) {
        mContext = context;
        LayoutInflater from = LayoutInflater.from(context);
        View inflate = from.inflate(R.layout.layout_register, this);
        inflate.findViewById(R.id.btn_register).setOnClickListener(this);
        inflate.findViewById(R.id.iv_finsh).setOnClickListener(this);
        et_input_name = inflate.findViewById(R.id.et_input_name);
        et_input_phone_number = inflate.findViewById(R.id.et_input_phone_number);
        et_input_password = inflate.findViewById(R.id.et_input_password);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_finsh) {
            ArmsUtils.obtainAppComponentFromContext(mContext).appManager().getTopActivity().finish();
        } else if (v.getId() == R.id.btn_register) {
            username = et_input_name.getText().toString().trim();
            phone_number = et_input_phone_number.getText().toString().trim();
            password = et_input_password.getText().toString().trim();

            //开始注册
            onRegister(username, phone_number, password);

        } else {
            ToastUtils.showShort("暂未开通，请等待...");
        }
    }

    private void onRegister(String username, String phone_number, String password) {
        if (TextUtils.isEmpty(username)) {
            et_input_name.requestFocus();
            if (iRegisterListener != null)
                iRegisterListener.onRegisterError(ArmsUtils.getString(mContext, R.string.login_User_name_cannot_be_empty));
            return;
        } else if (TextUtils.isEmpty(phone_number)) {
            et_input_phone_number.requestFocus();
            if (iRegisterListener != null)
                iRegisterListener.onRegisterError(ArmsUtils.getString(mContext, R.string.login_User_Number_cannot_be_empty));
            return;
        } else if (TextUtils.isEmpty(password)) {
            et_input_password.requestFocus();
            if (iRegisterListener != null)
                iRegisterListener.onRegisterError(ArmsUtils.getString(mContext, R.string.login_Password_cannot_be_empty));
            return;
        }

        if (iRegisterListener != null)
            iRegisterListener.onRegisterStart();

        Observable.create(new ObservableOnSubscribe<String>() { //被观察者
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                try {
                    HXHelper.getInstance().getLoginEngine().onRegister(phone_number, password);
                    emitter.onNext("onSucceed");
                } catch (final HyphenateException e) {
                    int errorCode = e.getErrorCode();
                    if (errorCode == EMError.NETWORK_ERROR) {
                        emitter.onNext(ArmsUtils.getString(mContext, R.string.login_network_anomalies));
                    } else if (errorCode == EMError.USER_ALREADY_EXIST) {
                        emitter.onNext(ArmsUtils.getString(mContext, R.string.login_User_already_exists));
                    } else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
                        emitter.onNext(ArmsUtils.getString(mContext, R.string.login_registration_failed_without_permission));
                    } else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
                        emitter.onNext(ArmsUtils.getString(mContext, R.string.login_illegal_user_name));
                    } else if (errorCode == EMError.EXCEED_SERVICE_LIMIT) {
                        emitter.onNext(ArmsUtils.getString(mContext, R.string.login_register_exceed_service_limit));
                    } else {
                        emitter.onNext(ArmsUtils.getString(mContext, R.string.login_Registration_failed));
                    }
                }
            }
        }).subscribeOn(Schedulers.io())//被观察者线程
                .observeOn(AndroidSchedulers.mainThread())//观察者线程
                .subscribe(new Observer<String>() { //观察者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        onRegisterCallBack(s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 注册的回调
     *
     * @param s
     */
    private void onRegisterCallBack(String s) {
        if (s.equals("onSucceed")) {
            if (iRegisterListener != null) {
                iRegisterListener.onRegisterSucceed(username, phone_number, password);
            }
        } else {
            if (iRegisterListener != null)
                iRegisterListener.onRegisterError(s);
        }
    }

    public static interface IRegisterListener {

        void onRegisterSucceed(String username, String phone_number, String password);

        void onRegisterError(String string);

        void onRegisterStart();
    }

    public static void addRegisterCallBack(IRegisterListener iRegisterListener) {
        RegisterView.iRegisterListener = iRegisterListener;
    }

    public static void unRegisterCallBack() {
        if (iRegisterListener != null)
            iRegisterListener = null;
    }
}

package com.it.yk.contacts_component.mvp.ui.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dasu.blur.DBlur;
import com.it.yk.contacts_component.R;
import com.yk.component.res.searchview.SearchEditText;
import com.yk.component.sdk.core.Constants;

import static com.yk.component.sdk.core.Constants.ISP_Config.USER_PHONE_NUMBER;
import static com.yk.component.sdk.core.RouterHub.FRIENDS_ContactsDetailsActivity;


/**
 * Created by yangk on 2018/11/5.
 */

public class SearchDialog extends DialogFragment implements View.OnClickListener {
    private View inflate;
    private TextView tv_search_name;
    private SearchEditText set_query;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.public_Dialog_FullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //设置dialog的 进出 动画
        getDialog().getWindow().setWindowAnimations(R.style.public_dialogWindowAnim);
        inflate = inflater.inflate(R.layout.contacts_fragment_full_search, container, false);
        //设置背景高斯模糊
        DBlur.source(getActivity()).intoTarget(inflate.findViewById(R.id.ll_back)).animAlpha().build().doBlurSync();
        inflate.findViewById(R.id.rl).setVisibility(View.GONE);
        tv_search_name = inflate.findViewById(R.id.tv_name);
        set_query = inflate.findViewById(R.id.set_query);

        setEditTextChangerListener(set_query);
        inflate.findViewById(R.id.rl).setOnClickListener(this);
        return inflate;
    }

    /**
     * 监听输入文字改变的监听
     *
     * @param set_query
     */
    private void setEditTextChangerListener(SearchEditText set_query) {
        set_query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 1) {
                    inflate.findViewById(R.id.rl).setVisibility(View.GONE);
                    tv_search_name.setText("");
                } else {
                    inflate.findViewById(R.id.rl).setVisibility(View.VISIBLE);
                    tv_search_name.setText(s.toString());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        ARouter.getInstance().build(FRIENDS_ContactsDetailsActivity)
                .withString(Constants.IContacts.NEW_FRIEND, SearchDialog.this.getClass().getSimpleName())
                .withString(USER_PHONE_NUMBER, set_query.getText().toString().trim()).navigation();
    }
}

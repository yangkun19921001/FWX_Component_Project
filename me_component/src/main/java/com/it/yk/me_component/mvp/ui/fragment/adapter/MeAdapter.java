package com.it.yk.me_component.mvp.ui.fragment.adapter;

import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.helper.HXHelper;
import com.it.yk.me_component.R;
import com.it.yk.me_component.mvp.model.entity.MeEntity;
import com.jess.arms.utils.ArmsUtils;
import com.yk.component.sdk.core.Constants;

import java.util.List;

/**
 * Created by yangk on 2018/11/1.
 */

public class MeAdapter extends BaseMultiItemQuickAdapter<MeEntity, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MeAdapter(List<MeEntity> data) {
        super(data);
        addItemType(Constants.IMe.MyInfo, R.layout.me_myinfo);
        addItemType(Constants.IMe.OneLayout, R.layout.me_one_layout);
        addItemType(Constants.IMe.GroupLayout, R.layout.me_group_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, MeEntity item) {
        switch (helper.getItemViewType()) {
            case Constants.IMe.MyInfo:
                showFindItem(helper, item);
                break;
            case Constants.IMe.OneLayout:
                showFindItem(helper, item);
                break;
            case Constants.IMe.GroupLayout:
                showFindItem(helper, item);
                switch (item.getContent()) {
                    case Constants.IMe.MY_COLLECT:
                    case Constants.IMe.MY_IMAGE:
                        helper.setVisible(R.id.v_visibility, true);
                        break;
                }
                break;
        }

    }

    private void showFindItem(BaseViewHolder helper, MeEntity item) {
        if (item.getContent() == Constants.IMe.MY_INFO) {
            EMOptions loginInfo = HXHelper.getInstance().getLoginEngine().getLoginInfo();
            helper.setText(R.id.tv_name, SPUtils.getInstance(Constants.ISP_Config.SP_NAME).getString(Constants.ISP_Config.USER_NAME))
                    .setText(R.id.tv_context, "微信号: " + SPUtils.getInstance(Constants.ISP_Config.SP_NAME).getString(Constants.ISP_Config.USER_PHONE_NUMBER))
                    .setImageDrawable(R.id.iv_icon, ArmsUtils.getDrawablebyResource(Utils.getApp(), item.getIcon()));
        } else {
            helper.setText(R.id.tv_name, item.getContent())
                    .setImageDrawable(R.id.iv_icon, ArmsUtils.getDrawablebyResource(Utils.getApp(), item.getIcon()));
        }
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort(item.getContent());
            }
        });
    }
}

package com.it.yk.find_component.mvp.ui.fragment.adapter;

import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.it.yk.find_component.R;
import com.it.yk.find_component.mvp.model.entity.FindEntity;
import com.jess.arms.utils.ArmsUtils;
import com.yk.component.sdk.core.Constants;

import java.util.List;

/**
 * Created by yangk on 2018/11/1.
 */

public class FindAdapter extends BaseMultiItemQuickAdapter<FindEntity, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public FindAdapter(List<FindEntity> data) {
        super(data);
        addItemType(Constants.IFind.OneLayout, R.layout.find_item_one_layout);
        addItemType(Constants.IFind.GroupLayout, R.layout.find_item_group_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, FindEntity item) {

        switch (helper.getItemViewType()) {
            case Constants.IFind.OneLayout:
                showFindItem(helper, item);
                break;
            case Constants.IFind.GroupLayout:
                showFindItem(helper, item);
                switch (item.getContent()) {
                    case Constants.IFind.Sweep:
                    case Constants.IFind.PeopleInTheVicinity:
                        helper.setVisible(R.id.v_visibility, true);
                        break;
                    default:
//                        helper.setVisible(R.id.v_visibility, false);
                        break;
                }
                break;
        }
    }

    private void showFindItem(BaseViewHolder helper, FindEntity item) {
        helper.setText(R.id.tv_name, item.getContent())
                .setImageDrawable(R.id.iv_icon, ArmsUtils.getDrawablebyResource(Utils.getApp(), item.getIcon()))
                .itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort(item.getContent());
            }
        });
    }
}

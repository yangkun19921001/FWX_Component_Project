package com.it.yk.fwx_chat_component.mvp.ui.activity.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.it.yk.fwx_chat_component.R;
import com.it.yk.fwx_chat_component.mvp.model.entity.AllContactsEntity;
import com.jess.arms.utils.ArmsUtils;
import com.yk.component.sdk.utils.LogHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yangk on 2018/11/16.
 */

public class AllContacts extends BaseQuickAdapter<AllContactsEntity, BaseViewHolder> {

    private List<String> selecUser = new ArrayList<>();

    public AllContacts(@Nullable List<AllContactsEntity> data) {
        super(R.layout.chat_component_adapter_all_contacts, data);
        selecUser.clear();
    }

    @Override
    protected void convert(BaseViewHolder helper, AllContactsEntity item) {
        helper.setText(R.id.tv_name, item.getName())
                .setImageDrawable(R.id.iv_selecter_icon, item.isSelect() ? ArmsUtils.getDrawablebyResource(Utils.getApp(), R.drawable.ic_check_circle_green_24dp) : ArmsUtils.getDrawablebyResource(Utils.getApp(), R.drawable.ic_check_circle_gray_dft_24dp))
                .itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setSelect(!item.isSelect());
                notifyItemChanged(helper.getAdapterPosition());
                addFirends(item.isSelect(), item.getName());
            }
        });
    }

    /**
     * 添加选择的朋友
     *
     * @param isSelect
     * @param user
     */
    public void addFirends(boolean isSelect, String user) {
        if (isSelect) {
            selecUser.add(user);
            LogHelper.i(TAG,"添加朋友--"+user+"--当前容器里面添加的朋友--"+selecUser.size());
        } else {
            Iterator<String> iterator = selecUser.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().equals(user)) {
                    iterator.remove();
                    LogHelper.i(TAG,"删除朋友--"+user+"--当前容器里面添加的朋友--"+selecUser.size());
                }
            }
        }

    }


    public List<String> getSelectUser() {
        return selecUser;
    }
}

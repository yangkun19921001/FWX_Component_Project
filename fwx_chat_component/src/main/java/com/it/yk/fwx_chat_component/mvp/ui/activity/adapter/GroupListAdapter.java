package com.it.yk.fwx_chat_component.mvp.ui.activity.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.it.yk.fwx_chat_component.R;
import com.it.yk.fwx_chat_component.mvp.model.entity.GroupListEntity;
import com.yk.component.sdk.core.Constants;
import com.yk.component.sdk.core.RouterHub;

import java.util.List;

/**
 * Created by yangk on 2018/11/19.
 */

public class GroupListAdapter extends BaseQuickAdapter<GroupListEntity,BaseViewHolder> {
    public GroupListAdapter(@Nullable List<GroupListEntity> data) {
        super(R.layout.chat_component_adapter_group_list,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GroupListEntity item) {
        helper.setText(R.id.tv_name,item.getGtoupName().isEmpty()?item.getGroupID():item.getGtoupName())
        .itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(RouterHub.Chat_ChatComponentActivity)
                        .withInt(Constants.IChat.ChatType, Constants.IChat.GroupChat) //聊天类型
                        .withString(Constants.IChat.otherId, item.getGroupID()) //跟谁聊天
                        .navigation();
            }
        });


    }
}

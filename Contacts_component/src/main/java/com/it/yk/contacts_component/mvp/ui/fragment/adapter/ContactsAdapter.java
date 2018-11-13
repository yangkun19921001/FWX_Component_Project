package com.it.yk.contacts_component.mvp.ui.fragment.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.it.yk.contacts_component.R;
import com.it.yk.contacts_component.mvp.model.entity.ContactsEntity;
import com.jess.arms.utils.ArmsUtils;
import com.yk.component.sdk.core.Constants;
import com.yk.component.sdk.core.RouterHub;
import com.yk.component.sdk.utils.Utils;

import java.util.List;

/**
 * Created by yangk on 2018/10/30.
 */

public class ContactsAdapter extends BaseMultiItemQuickAdapter<ContactsEntity, BaseViewHolder> {
    public ContactsAdapter(@Nullable List<ContactsEntity> data) {
        super(data);
        addItemType(0, R.layout.contacts_adapter_contacts);
        addItemType(1, R.layout.public_layout_search_view);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContactsEntity item) {
        switch (helper.getItemViewType()) {
            case 1:
                break;
            case 0:
                helper.setText(R.id.tv_name, item.getName());
                if (item.getIcon() != -1) {
                    helper.setImageDrawable(R.id.iv_icon, ArmsUtils.getDrawablebyResource(com.blankj.utilcode.util.Utils.getApp(), item.getIcon()));
                } else {
                    helper.setImageDrawable(R.id.iv_icon, ArmsUtils.getDrawablebyResource(com.blankj.utilcode.util.Utils.getApp(), R.drawable.ease_default_avatar));
                }
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (item.getName()) {
                            case Constants.IContacts.NEW_FRIEND:
                                Utils.navigation(RouterHub.FRIENDS_NewFriendsActivity);
                                break;
                            case Constants.IContacts.GROUP_CHAT:
                                break;
                            case Constants.IContacts.TITLE:
                                break;
                            case Constants.IContacts.Public_Number:
                                break;
                            default:
                                ARouter.getInstance().build(RouterHub.Chat_ChatComponentActivity)
                                        .withInt(Constants.IChat.ChatType, Constants.IChat.OneChat) //聊天类型
                                        .withString(Constants.IChat.otherId, item.getName()) //跟谁聊天
                                        .navigation();
                                break;
                        }

                    }
                });
                break;
        }

    }
}

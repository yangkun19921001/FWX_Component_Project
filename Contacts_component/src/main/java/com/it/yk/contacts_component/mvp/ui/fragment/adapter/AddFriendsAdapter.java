package com.it.yk.contacts_component.mvp.ui.fragment.adapter;

import android.view.View;

import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.it.yk.contacts_component.R;
import com.it.yk.contacts_component.mvp.model.entity.AddFriendsEntity;
import com.jess.arms.utils.ArmsUtils;
import com.yk.component.sdk.core.Constants;

import java.util.List;

/**
 * Created by yangk on 2018/11/4.
 */

public class AddFriendsAdapter extends BaseMultiItemQuickAdapter<AddFriendsEntity, BaseViewHolder> {
    private IItemClickListener itemClickListener;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public AddFriendsAdapter(List<AddFriendsEntity> data) {
        super(data);
        addItemType(Constants.IContacts.AddFriendLayout_Top, R.layout.contacts_layout_add_friends_sertch);
        addItemType(Constants.IContacts.AddFriendLayout_Button, R.layout.contacts_layout_add_friends_function);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddFriendsEntity item) {
        switch (helper.getItemViewType()) {
            case Constants.IContacts.AddFriendLayout_Top:
                helper.setText(R.id.tv_my_weixinnumber, item.getName())
                        .getView(R.id.ll_sertch_weixinnumber)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (itemClickListener != null)
                                    itemClickListener.showDialog();
                            }
                        });
                break;
            case Constants.IContacts.AddFriendLayout_Button:
                helper.setText(R.id.tv_name, item.getName())
                        .setImageDrawable(R.id.iv_icon, ArmsUtils.getDrawablebyResource(Utils.getApp(), item.getIcon()))
                        .itemView
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (itemClickListener != null)
                                    itemClickListener.itemClick(item);
                            }
                        });
                break;
        }
    }

    public interface IItemClickListener{

        void showDialog();

        void itemClick(AddFriendsEntity  item);
    }


    public void setItemClickListener(IItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}

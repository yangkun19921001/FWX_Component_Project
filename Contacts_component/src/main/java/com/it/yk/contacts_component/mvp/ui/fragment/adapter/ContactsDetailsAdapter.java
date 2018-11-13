package com.it.yk.contacts_component.mvp.ui.fragment.adapter;

import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.it.yk.contacts_component.R;
import com.it.yk.contacts_component.mvp.model.entity.ContactsDetailsEntity;
import com.yk.component.sdk.core.Constants;

import java.util.List;

/**
 * Created by yangk on 2018/11/5.
 */

public class ContactsDetailsAdapter extends BaseMultiItemQuickAdapter<ContactsDetailsEntity, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    String[] groupTag = new String[]{Constants.IContacts.AREA, Constants.IContacts.FROM, Constants.IContacts.SOCIAL_INFORMATION};
    private IItemClickListener iItemClickListener;

    public ContactsDetailsAdapter(List<ContactsDetailsEntity> data) {
        super(data);
        //顶部自己的信息
        addItemType(Constants.IContacts.AddFriendLayout_Top, R.layout.contacts_details_item_top);
        addItemType(Constants.IContacts.AddFriendLayout_Group, R.layout.contacts_details_item_group);
        addItemType(Constants.IContacts.AddFriendLayout_Button, R.layout.contacts_details_item_button);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContactsDetailsEntity item) {
        switch (helper.getItemViewType()) {
            case Constants.IContacts.AddFriendLayout_Top:
                helper.setText(R.id.tv_name, item.getName())
                        .setText(R.id.tv_wx_number, "微信号:" + item.getName())
                        .setText(R.id.tv_wx_name, "昵称:" + item.getName());
                break;
            case Constants.IContacts.AddFriendLayout_Group:
                helper.setText(R.id.tv_title, groupTag[helper.getAdapterPosition() - 1])
                        .setText(R.id.tv_name, item.getName());

                switch (item.getName()) {
                    case "冰岛":
                    case "来至手机搜索":
                        helper.getView(R.id.v_visibility).setVisibility(View.VISIBLE);
                        break;
                }
                break;
            case Constants.IContacts.AddFriendLayout_Button:
                Button button = helper.getView(R.id.btn_details);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (iItemClickListener != null)
                            iItemClickListener.addContact(item.getNumber());
                    }
                });
                button.setText(item.getName());
                break;
        }
    }

    public interface IItemClickListener {
        void addContact(String toAddUsername);
    }

    public void addItemClick(IItemClickListener iItemClickListener) {
        this.iItemClickListener = iItemClickListener;
    }
}

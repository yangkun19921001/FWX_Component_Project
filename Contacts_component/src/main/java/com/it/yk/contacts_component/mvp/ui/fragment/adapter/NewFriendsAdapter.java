package com.it.yk.contacts_component.mvp.ui.fragment.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.it.yk.contacts_component.R;
import com.it.yk.contacts_component.mvp.model.entity.NewFriendsEntity;
import com.yk.component.sdk.core.Constants;
import com.yk.component.sdk.core.RouterHub;

import java.util.List;

/**
 * Created by yangk on 2018/11/6.
 */

public class NewFriendsAdapter extends BaseQuickAdapter<NewFriendsEntity, BaseViewHolder> {
    public NewFriendsAdapter(@Nullable List<NewFriendsEntity> data) {
        super(R.layout.contacts_adapter_news_friends, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewFriendsEntity item) {
        helper.setText(R.id.tv_name, item.getOtherId() + "")
                .setText(R.id.tv_content, item.getContent())
                .getView(R.id.btn_look_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(RouterHub.FRIENDS_ContactsDetailsActivity)
                        .withString(Constants.IContacts.NEW_FRIEND, Constants.IContacts.NewFriendsAdapter)
                        .withParcelable(Constants.IContacts.NEW_FRIEND_DATA, item).navigation();
            }
        });
        Button btnLookUp = helper.getView(R.id.btn_look_up);
        btnLookUp.setText("查看");
    }
}

package com.it.yk.weichatappcation.mvp.ui.fragment.adapter;

import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.it.yk.weichatappcation.R;
import com.it.yk.weichatappcation.mvp.model.entity.WeiChatMessageHistoryEntity;
import com.yk.component.sdk.core.Constants;
import com.yk.component.sdk.core.RouterHub;

import java.util.List;

/**
 * Created by yangk on 2018/10/31.
 */

public class WeiChatHistoryAdapter extends BaseMultiItemQuickAdapter<WeiChatMessageHistoryEntity, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public WeiChatHistoryAdapter(List<WeiChatMessageHistoryEntity> data) {
        super(data);
        addItemType(Constants.IMessageType.OneChat, R.layout.weichat_layout_message_one_chat);
        addItemType(Constants.IMessageType.GroupChat, R.layout.weichat_layout_message_group_chat);
    }

    @Override
    protected void convert(BaseViewHolder helper, WeiChatMessageHistoryEntity item) {
        switch (helper.getItemViewType()) {
            case Constants.IMessageType.OneChat:
                showMessageBaseInfo(helper, item);
                break;
            case Constants.IMessageType.GroupChat:
                showMessageBaseInfo(helper, item);
                break;
        }
    }

    private void showMessageBaseInfo(BaseViewHolder helper, WeiChatMessageHistoryEntity item) {
        helper.setText(R.id.tv_name, item.getOtherId())
                .setText(R.id.tv_content, item.getLastMessageContent())
                .setText(R.id.tv_time, TimeUtils.getFriendlyTimeSpanByNow(item.getMessageTime()));

        if (item.getUnReadMessageCount() != 0) {
            helper.setVisible(R.id.recently_vis, true)
                    .setText(R.id.recently_count, item.getUnReadMessageCount());
        } else {
            helper.setVisible(R.id.recently_vis, false);
        }

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(RouterHub.Chat_ChatComponentActivity)
                        .withInt(Constants.IChat.ChatType, item.getItemType()) //聊天类型
                        .withString(Constants.IChat.otherId, item.getOtherId()) //跟谁聊天
                        .navigation();
                switch (item.getItemType()) {
                    case Constants.IMessageType.OneChat:
                        break;
                    case Constants.IMessageType.GroupChat:
                        break;
                }
            }
        });
    }
}

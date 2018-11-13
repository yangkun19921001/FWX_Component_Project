package com.iit.yk.chat_base_component.imuisample.views;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.iit.yk.chat_base_component.R;

import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.chatinput.listener.CustomMenuEventListener;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnClickEditTextListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.menu.Menu;
import cn.jiguang.imui.chatinput.menu.MenuManager;
import cn.jiguang.imui.chatinput.menu.view.MenuFeature;
import cn.jiguang.imui.chatinput.menu.view.MenuItem;
import cn.jiguang.imui.chatinput.record.RecordVoiceButton;
import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jiguang.imui.messages.ptr.PtrDefaultHeader;
import cn.jiguang.imui.messages.ptr.PullToRefreshLayout;
import cn.jiguang.imui.utils.DisplayUtil;


public class ChatView extends RelativeLayout {

    private TextView mTitle;
    private LinearLayout mTitleContainer;
    private MessageList mMsgList;
    private ChatInputView mChatInput;
    private RecordVoiceButton mRecordVoiceBtn;
//    private PullToRefreshLayout mPtrLayout;
    private ImageButton mSelectAlbumIb;

    public ChatView(Context context) {
        super(context);
    }

    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initModule() {
        mMsgList = (MessageList) findViewById(R.id.msg_list);
        mChatInput = (ChatInputView) findViewById(R.id.chat_input);
//        mPtrLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh_layout);

        /**
         * Should set menu container height once the ChatInputView has been initialized.
         * For perfect display, the height should be equals with soft input height.
         */
        mChatInput.setMenuContainerHeight(819);
        mRecordVoiceBtn = mChatInput.getRecordVoiceButton();
        mSelectAlbumIb = mChatInput.getSelectAlbumBtn();
        PtrDefaultHeader header = new PtrDefaultHeader(getContext());
        int[] colors = getResources().getIntArray(R.array.chat_base_google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new LayoutParams(-1, -2));
        header.setPadding(0, DisplayUtil.dp2px(getContext(),15), 0,
                DisplayUtil.dp2px(getContext(),10));
//        header.setPtrFrameLayout(mPtrLayout);
//        mMsgList.setDateBgColor(Color.parseColor("#FF4081"));
//        mMsgList.setDatePadding(5, 10, 10, 5);
//        mMsgList.setEventTextPadding(5);
//        mMsgList.setEventBgColor(Color.parseColor("#34A350"));
//        mMsgList.setDateBgCornerRadius(15);

        //设置接收文字的字体颜色
        mMsgList.setReceiveBubbleTextColor(Color.parseColor("#000000"));
        mMsgList.setReceiveBubbleTextSize(14);

        mMsgList.setSendBubbleTextColor(Color.parseColor("#000000"));
        mMsgList.setSendBubbleTextSize(14);

        mMsgList.setHasFixedSize(true);
//        mPtrLayout.setLoadingMinTime(1000);
//        mPtrLayout.setDurationToCloseHeader(1500);
//        mPtrLayout.setHeaderView(header);
//        mPtrLayout.addPtrUIHandler(header);
//        // 下拉刷新时，内容固定，只有 Header 变化
//        mPtrLayout.setPinContent(true);
        // set show display name or not
//        mMsgList.setShowReceiverDisplayName(true);
//        mMsgList.setShowSenderDisplayName(false);


        // add Custom Menu View
        MenuManager menuManager = mChatInput.getMenuManager();
        menuManager.addCustomMenu("MY_CUSTOM",R.layout.menu_text_item,R.layout.menu_text_feature);
        // Custom menu order
        menuManager.setMenu(Menu.newBuilder().
                customize(true).
                setRight(Menu.TAG_SEND).
                setBottom(Menu.TAG_VOICE, Menu.TAG_EMOJI, Menu.TAG_GALLERY, Menu.TAG_CAMERA,"MY_CUSTOM").
                build());

        //更多里面的View
        findViewById(R.id.aurora_chat_voice_call).setOnClickListener(moreMenuClick);
        findViewById(R.id.aurora_chat_video_call).setOnClickListener(moreMenuClick);
        findViewById(R.id.aurora_chat_location).setOnClickListener(moreMenuClick);
        findViewById(R.id.aurora_chat_file).setOnClickListener(moreMenuClick);
        findViewById(R.id.aurora_chat_rea_packet).setOnClickListener(moreMenuClick);


        menuManager.setCustomMenuClickListener(new CustomMenuEventListener() {
            @Override
            public boolean onMenuItemClick(String tag, MenuItem menuItem) {
                //Menu feature will not be show shown if return false；
                LogUtils.e(tag);
                return true;
            }

            @Override
            public void onMenuFeatureVisibilityChanged(int visibility, String tag, MenuFeature menuFeature) {
                if(visibility == View.VISIBLE){
                    // Menu feature is visible.
                }else {
                    // Menu feature is gone.
                }
            }
        });

    }

    public PullToRefreshLayout getPtrLayout() {
        return null;
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setMenuClickListener(OnMenuClickListener listener) {
        mChatInput.setMenuClickListener(listener);
    }

    public void setAdapter(MsgListAdapter adapter) {
        mMsgList.setAdapter(adapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mMsgList.setLayoutManager(layoutManager);
    }

    public void setRecordVoiceFile(String path, String fileName) {
        mRecordVoiceBtn.setVoiceFilePath(path, fileName);
    }

    public void setCameraCaptureFile(String path, String fileName) {
        mChatInput.setCameraCaptureFile(path, fileName);
    }

    public void setRecordVoiceListener(RecordVoiceListener listener) {
        mChatInput.setRecordVoiceListener(listener);
    }

    public void setOnCameraCallbackListener(OnCameraCallbackListener listener) {
        mChatInput.setOnCameraCallbackListener(listener);
    }

    public void setOnTouchListener(OnTouchListener listener) {
        mMsgList.setOnTouchListener(listener);
    }

    public void setOnTouchEditTextListener(OnClickEditTextListener listener) {
        mChatInput.setOnClickEditTextListener(listener);
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    public ChatInputView getChatInputView() {
        return mChatInput;
    }

    public MessageList getMessageListView() {
        return mMsgList;
    }

    public ImageButton getSelectAlbumBtn() {
        return this.mSelectAlbumIb;
    }


    private OnClickListener moreMenuClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.aurora_chat_voice_call){
                ToastUtils.showShort("语音电话");
            }else   if (v.getId() == R.id.aurora_chat_video_call){
                ToastUtils.showShort("视频电话");
            }else   if (v.getId() == R.id.aurora_chat_file){
                ToastUtils.showShort("文件");
            }else if (v.getId() == R.id.aurora_chat_location) {
                ToastUtils.showShort("位置");
            } else if (v.getId() == R.id.aurora_chat_rea_packet) {
                ToastUtils.showShort("红包");
            } else {

            }

        }
    };
}

package com.it.yk.fwx_chat_component.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.hyphenate.easeui.EaseUI;
import com.iit.yk.chat_base_component.imuisample.views.ChatView;
import com.it.yk.fwx_chat_component.R;
import com.it.yk.fwx_chat_component.R2;
import com.it.yk.fwx_chat_component.di.component.DaggerChatComponentComponent;
import com.it.yk.fwx_chat_component.di.module.ChatComponentModule;
import com.it.yk.fwx_chat_component.mvp.contract.ChatComponentContract;
import com.it.yk.fwx_chat_component.mvp.presenter.ChatComponentPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yk.component.res.view.TitleRightText;
import com.yk.component.sdk.core.RouterHub;

import butterknife.BindView;
import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.messages.MessageList;

import static com.jess.arms.utils.Preconditions.checkNotNull;


@Route(path = RouterHub.Chat_ChatComponentActivity)
public class ChatComponentActivity extends BaseActivity<ChatComponentPresenter> implements ChatComponentContract.View {


    @BindView(R2.id.public_toolbar_back)
    RelativeLayout publicToolbarBack;
    @BindView(R2.id.public_iv_more)
    ImageView publicIvMore;
    @BindView(R2.id.public_toolbar_title_right)
    TitleRightText publicToolbarTitleRight;
    @BindView(R2.id.public_toolbar_more)
    RelativeLayout publicToolbarMore;
    @BindView(R2.id.public_toolbar_title)
    TextView publicToolbarTitle;
    @BindView(R2.id.public_toolbar)
    Toolbar publicToolbar;
    @BindView(R2.id.msg_list)
    MessageList msgList;
    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R2.id.chat_input)
    ChatInputView chatInput;
    @BindView(R2.id.chat_view)
    ChatView chatView;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerChatComponentComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .chatComponentModule(new ChatComponentModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_chat_component; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        //TODO---------------暂时先没有做上拉刷新功能
        refreshLayout =  findViewById(R.id.refreshLayout);
        refreshLayout .setEnableRefresh(false);
        mPresenter.initChatView(publicToolbarTitle);

        mPresenter.addSendMessageListener();

        //加载数据库中的缓存数据
        mPresenter.addDBChatHistoryMessage();
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.sendCurrentChatPerson();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EaseUI.getInstance().getNotifier().reset();
    }
}

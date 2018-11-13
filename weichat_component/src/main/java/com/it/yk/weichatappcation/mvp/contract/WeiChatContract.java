package com.it.yk.weichatappcation.mvp.contract;

import android.app.Activity;

import com.iit.yk.chat_base_component.imuisample.models.MyMessage;
import com.it.yk.fwx_chat_component.mvp.model.entity.ChatMessageEntity;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;

import java.util.List;


public interface WeiChatContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        Activity getContext();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        public List<MyMessage> getHistoryList(List<ChatMessageEntity> list);
        public MyMessage getMyMessage(ChatMessageEntity chatMeg);
        public void saveChatMessage(int mCurrentChatType,String mediaFilePath,String txt,String msgID,int messageStatus,String otherID,int messageType);
    }
}

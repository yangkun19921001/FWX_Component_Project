package com.hyphenate.easeui.helper;

import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;
import java.util.Map;

import static com.hyphenate.easeui.EaseConstant.CHATTYPE_GROUP;

/**
 * Created by yangk on 2018/10/25.
 */

public class MessageHelper {


    private EMMessageListener mMsgListener;
    private String TAG = this.getClass().getSimpleName();

    /**
     * 发送文本消息
     */
    public void sendTxtMessage(int chatType, String content, String toChatUsername, EMCallBack emCallBack) {
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
//如果是群聊，设置chattype，默认是单聊
        if (chatType == CHATTYPE_GROUP)
            message.setChatType(EMMessage.ChatType.GroupChat);
//发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
        MessageState(message, emCallBack);
    }

    /**
     * 发送图片消息
     */
    public void sendImgMessage(int chatType, String imagePath, String toChatUsername, EMCallBack emCallBack) {
        //imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
//如果是群聊，设置chattype，默认是单聊
        if (chatType == CHATTYPE_GROUP)
            message.setChatType(EMMessage.ChatType.GroupChat);
        EMClient.getInstance().chatManager().sendMessage(message);
        MessageState(message, emCallBack);
    }

    /**
     * 发送视频消息
     */
    public void sendVideoMessage(int chatType, String videoPath, String thumbPath, int videoLength, String toChatUsername, EMCallBack emCallBack) {
        //videoPath为视频本地路径，thumbPath为视频预览图路径，videoLength为视频时间长度
        EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, toChatUsername);
//如果是群聊，设置chattype，默认是单聊
        if (chatType == CHATTYPE_GROUP)
            message.setChatType(EMMessage.ChatType.GroupChat);
        EMClient.getInstance().chatManager().sendMessage(message);

        MessageState(message, emCallBack);
    }

    /**
     * 发送文件消息
     */
    public void sendFileMessage(int chatType, String filePath, String toChatUsername, EMCallBack emCallBack) {
        EMMessage message = EMMessage.createFileSendMessage(filePath, toChatUsername);
// 如果是群聊，设置chattype，默认是单聊
        if (chatType == CHATTYPE_GROUP)
            message.setChatType(EMMessage.ChatType.GroupChat);
        EMClient.getInstance().chatManager().sendMessage(message);


        MessageState(message, emCallBack);
    }

    /**
     * 发送语音消息
     */
    public void sendVoiceMessage(int chatType, String filePath, int length, String toChatUsername, EMCallBack emCallBack) {
        //filePath为语音文件路径，length为录音时间(秒)
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);
//如果是群聊，设置chattype，默认是单聊
        if (chatType == CHATTYPE_GROUP)
            message.setChatType(EMMessage.ChatType.GroupChat);
        EMClient.getInstance().chatManager().sendMessage(message);

        MessageState(message, emCallBack);
    }

    /**
     * 发送地理位置信息
     */
    public void sendLocationMessage(int chatType, double latitude, double longitude, String locationAddress, String toChatUsername, EMCallBack emCallBack) {
        //latitude为纬度，longitude为经度，locationAddress为具体位置内容
        EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, toChatUsername);
        //如果是群聊，设置chattype，默认是单聊
        if (chatType == CHATTYPE_GROUP)
            message.setChatType(EMMessage.ChatType.GroupChat);
        EMClient.getInstance().chatManager().sendMessage(message);

        MessageState(message, emCallBack);
    }


    /**
     * 添加消息监听
     */
    public void addRecevingMessageListener(final IMessageListener iMessageListener) {
        mMsgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                if (iMessageListener != null)
                    iMessageListener.onMessageReceived(messages);
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
                if (iMessageListener != null)
                    iMessageListener.onCmdMessageReceived(messages);
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                //收到已读回执
                if (iMessageListener != null)
                    iMessageListener.onMessageRead(messages);
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
                //收到已送达回执
                if (iMessageListener != null)
                    iMessageListener.onMessageDelivered(message);
            }

            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                //消息被撤回
                if (iMessageListener != null)
                    iMessageListener.onMessageRecalled(messages);
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
                if (iMessageListener != null)
                    iMessageListener.onMessageChanged(message, change);
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(mMsgListener);
    }

    /**
     * 删除消息监听
     */
    public void unRemoveMessageListener() {
        if (mMsgListener != null)
            EMClient.getInstance().chatManager().removeMessageListener(mMsgListener);
    }

    /**
     * 监听消息状态
     */
    public void MessageState(EMMessage message, EMCallBack emCallBack) {
        message.setMessageStatusCallback(emCallBack);
    }

    /**
     * 获取消息记录
     */
    public List<EMMessage> getMessageHistoryLists(String username) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        //获取此会话的所有消息
        List<EMMessage> messages = conversation.getAllMessages();
        //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
        return messages;
    }

    /**
     * 获取更多的消息
     *
     * @param username
     * @param startMsgId
     * @param pagesize
     */
    public List<EMMessage> getMessageMoreList(String username, String startMsgId, int pagesize) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
        List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId, pagesize);
        return messages;
    }

    /**
     * 获取未读消息个数
     */
    public int getNotReadMessageCount(String username) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        return
                conversation.getUnreadMsgCount();
    }

    /**
     * 未读消息数清零
     */
    public void onNotReadClear(String username, String messageId) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        //指定会话消息未读数清零
        conversation.markAllMessagesAsRead();
        //把一条消息置为已读
        conversation.markMessageAsRead(messageId);
        //所有未读消息数清零
        EMClient.getInstance().chatManager().markAllConversationsAsRead();
    }

    /**
     * 获取消息总数
     */
    public int getMessageTotalCount(String username) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
//获取此会话在本地的所有的消息数量
        conversation.getAllMsgCount();
//如果只是获取当前在内存的消息数量，调用
        return conversation.getAllMessages().size();
    }

    /**
     * 消息撤回
     *
     * @param contextMenuMessage
     */
    public void recallMessage(EMMessage contextMenuMessage) {
        try {
            EMClient.getInstance().chatManager().recallMessage(contextMenuMessage);
        } catch (HyphenateException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();

        }
    }

    /**
     * 分页获取历史消息
     */
    public List<EMMessage> getHistoryMessage(int chatType, String username, String toChatUsername, int pagesize) {
        try {
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
            EMClient.getInstance().chatManager().fetchHistoryMessages(
                    toChatUsername, EaseCommonUtils.getConversationType(chatType), pagesize, "");
            final List<EMMessage> msgs = conversation.getAllMessages();
            int msgCount = msgs != null ? msgs.size() : 0;
            if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
                String msgId = null;
                if (msgs != null && msgs.size() > 0) {
                    msgId = msgs.get(0).getMsgId();
                }
                return conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
            }
//            EMClient.getInstance().chatManager().refreshSelectLast();
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取所有会话
     */
    public Map<String, EMConversation> getAllMessage() {
        return EMClient.getInstance().chatManager().getAllConversations();
    }

    /**
     * 删除会话及聊天记录
     */
    //删除和某个user会话，如果需要保留聊天记录，传false
    public void deleteChatAndGroup(String username,String msgId) {
        EMClient.getInstance().chatManager().deleteConversation(username, true);
        //删除当前会话的某条聊天记录
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        conversation.removeMessage(msgId);
    }

    /**
     * 消息监听器
     */
    public interface IMessageListener {
        public void onMessageReceived(List<EMMessage> messages);

        public void onCmdMessageReceived(List<EMMessage> messages);

        public void onMessageRead(List<EMMessage> messages);

        public void onMessageDelivered(List<EMMessage> message);

        public void onMessageRecalled(List<EMMessage> messages);

        public void onMessageChanged(EMMessage message, Object change);
    }
}

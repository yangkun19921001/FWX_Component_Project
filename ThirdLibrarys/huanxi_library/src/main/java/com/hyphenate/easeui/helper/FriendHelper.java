package com.hyphenate.easeui.helper;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

/**
 * Created by yangk on 2018/10/30.
 * <p>
 * 朋友及通讯录管理
 */

public class FriendHelper {

    /**
     * 获取所有的好友列表
     *
     * @return
     * @throws HyphenateException
     */
    public List<String> getAllContactsFromServer() throws HyphenateException {
        return EMClient.getInstance().contactManager().getAllContactsFromServer();
    }


    /**
     * @param toAddUsername 添加好友的用户名
     * @param reason        添加好友的理由
     * @throws HyphenateException
     */
    public void addContact(String toAddUsername, String reason) throws HyphenateException {
        //参数为要添加的好友的username和添加理由
        EMClient.getInstance().contactManager().addContact(toAddUsername, reason);
    }


    /**
     * 删除好友
     */
    public void deleteContact(String username) throws HyphenateException {
        EMClient.getInstance().contactManager().deleteContact(username);
    }

    /**
     * 默认好友请求是自动同意的，如果要手动同意需要在初始化SDK时调用 opptions.setAcceptInvitationAlways(false); 。
     *
     * @param username
     * @throws HyphenateException
     */
    public void acceptInvitation(String username) throws HyphenateException {
        EMClient.getInstance().contactManager().acceptInvitation(username);
    }

    /**
     * 拒绝好友请求
     */
    public void declineInvitation(String username) throws HyphenateException {
        EMClient.getInstance().contactManager().declineInvitation(username);
    }

    /**
     * 监听好友状态
     *
     * @param emContactListener
     */
    public void setContactListener(EMContactListener emContactListener) {
        EMClient.getInstance().contactManager().setContactListener(emContactListener);
/*        @Override
        public void onContactAgreed(String username) {
            //好友请求被同意
        }

        @Override
        public void onContactRefused(String username) {
            //好友请求被拒绝
        }

        @Override
        public void onContactInvited(String username, String reason) {
            //收到好友邀请
        }

        @Override
        public void onContactDeleted(String username) {
            //被删除时回调此方法
        }


        @Override
        public void onContactAdded(String username) {
            //增加了联系人时回调此方法
        }*/
    }

    /**
     * 获取黑名单列表
     *
     * @return
     * @throws HyphenateException
     */
    public List<String> getBlackListFromServer() throws HyphenateException {
        return EMClient.getInstance().contactManager().getBlackListFromServer();
    }

    /**
     * 从本地 db 获取黑名单列表
     */
    public void getBlackListUsernames() {
        EMClient.getInstance().contactManager().getBlackListUsernames();
    }

    /**
     * 把用户加入黑名单
     *
     * @param username
     * @param b
     * @throws HyphenateException
     */
    public void addUserToBlackList(String username, boolean b) throws HyphenateException {
        //第二个参数如果为true，则把用户加入到黑名单后双方发消息时对方都收不到；false，则我能给黑名单的中用户发消息，但是对方发给我时我是收不到的
        EMClient.getInstance().contactManager().addUserToBlackList(username, b);
    }

    /**
     * 把用户从黑名单中移除
     *
     * @param username
     * @throws HyphenateException
     */
    public void removeUserFromBlackList(String username) throws HyphenateException {
        EMClient.getInstance().contactManager().removeUserFromBlackList(username);
    }

    /**
     * 获取同一账号与其他端登录的 id
     * 获取到该id 后可以用于不同端登录的账号之间互发消息，比如PC端与移动端可以互发消息。
     */
    public List<String> getSelfIdsOnOtherPlatform() throws HyphenateException {
        return EMClient.getInstance().contactManager().getSelfIdsOnOtherPlatform();
    }


}

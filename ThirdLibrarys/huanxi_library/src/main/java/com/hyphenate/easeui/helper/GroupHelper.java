package com.hyphenate.easeui.helper;

import android.text.TextUtils;

import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangk on 2018/11/16.
 */

public class GroupHelper {

    /**
     * 创建群组
     *
     * @param groupName  群组名称
     * @param desc       群组简介
     * @param allMembers 群组初始成员，如果只有自己传空数组即可
     * @param reason     邀请成员加入的reason
     * @param option     群组类型选项，可以设置群组最大用户数(默认200)及群组类型@see {@link EMGroupStyle}
     *                   option.inviteNeedConfirm表示邀请对方进群是否需要对方同意，默认是需要用户同意才能加群的。
     *                   option.extField创建群时可以为群组设定扩展字段，方便个性化订制。
     * @return 创建好的group
     * @throws HyphenateException
     */
    public void createGroup(String groupName, String desc, String[] allMembers, String reason) throws HyphenateException {
        EMGroupOptions option = new EMGroupOptions();
        option.maxUsers = 200;
        option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
        EMClient.getInstance().groupManager().createGroup(groupName, desc, allMembers, reason, option);
    }

    /**
     * 添加管理员权限
     */
    public void addGroupUserAdmin(final String groupId, final String admin) throws HyphenateException {
        /**
         * 增加群组管理员，需要owner权限
         * @param groupId
         * @param admin
         * @return
         * @throws HyphenateException
         */
        EMClient.getInstance().groupManager().addGroupAdmin(groupId, admin);//
    }

    public void removeGroupUserAdmin(String groupId, String admin) throws HyphenateException {
        /**
         * 删除群组管理员，需要owner权限
         * @param groupId
         * @param admin
         * @return
         * @throws HyphenateException
         */
        EMClient.getInstance().groupManager().removeGroupAdmin(groupId, admin);//需异部处理
    }

    public void changeGroupOwner(String groupId, String newOwner) throws HyphenateException {
        /**
         * 群组所有权给他人
         * @param groupId
         * @param newOwner
         * @return
         * @throws HyphenateException
         */
        EMClient.getInstance().groupManager().changeOwner(groupId, newOwner);//需异部处理
    }

    /**
     * 群组中添加新人
     */
    public void groupAddUser(String groupId, String[] newmembers) throws HyphenateException {
        //群主加人调用此方法
        EMClient.getInstance().groupManager().addUsersToGroup(groupId, newmembers);//需异步处理
        //私有群里，如果开放了群成员邀请，群成员邀请调用下面方法
//        EMClient.getInstance().groupManager().inviteUser(groupId, newmembers, null);//需异步处理
    }

    /**
     * 踢人
     */
    public void removeUserToServer(String groupId, String username) throws HyphenateException {
        //把username从群组里删除
        EMClient.getInstance().groupManager().removeUserFromGroup(groupId, username);//需异步处理
    }

    /**
     * 退出群组
     *
     * @param groupId
     * @throws HyphenateException
     */
    public void leaveGroup(String groupId) throws HyphenateException {
        EMClient.getInstance().groupManager().leaveGroup(groupId);//需异步处理
    }

    /**
     * 解散群组
     */
    public void destroyGroup(String groupId) throws HyphenateException {
        EMClient.getInstance().groupManager().destroyGroup(groupId);//需异步处理
    }

    /**
     * 获取组成员
     */
    public List<String> getGroupUser(String groupId) throws HyphenateException {
        //如果群成员较多，需要多次从服务器获取完成
        List<String> memberList = new ArrayList<>();
        EMCursorResult<String> result = null;
        final int pageSize = 20;
        do {
            result = EMClient.getInstance().groupManager().fetchGroupMembers(groupId,
                    result != null ? result.getCursor() : "", pageSize);
            memberList.addAll(result.getData());
        } while (!TextUtils.isEmpty(result.getCursor()) && result.getData().size() == pageSize);

        return memberList;
    }

    /**
     * 获取组列表
     * @return
     * @throws HyphenateException
     */
    public List<EMGroup> getGroupLists() throws HyphenateException {
        //从服务器获取自己加入的和创建的群组列表，此api获取的群组sdk会自动保存到内存和db。
        return EMClient.getInstance().groupManager().getJoinedGroupsFromServer();//需异步处理

    }

    public void addGroupStateListenee(EMGroupChangeListener emGroupChangeListener){
        EMClient.getInstance().groupManager().addGroupChangeListener(emGroupChangeListener);
              /*  EMClient.getInstance().groupManager().addGroupChangeListener(new EMGroupChangeListener() {
            @Override
            public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
                //接收到群组加入邀请
            }

            @Override
            public void onRequestToJoinReceived(String groupId, String groupName, String applyer, String reason) {
                //用户申请加入群
            }

            @Override
            public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {
                //加群申请被同意
            }

            @Override
            public void onRequestToJoinDeclined(String groupId, String groupName, String decliner, String reason) {
                //加群申请被拒绝
            }

            @Override
            public void onInvitationAccepted(String groupId, String inviter, String reason) {
                //群组邀请被同意
            }

            @Override
            public void onInvitationDeclined(String groupId, String invitee, String reason) {
                //群组邀请被拒绝
            }

            @Override
            public void onUserRemoved(String s, String s1) {

            }

            @Override
            public void onGroupDestroyed(String s, String s1) {

            }

            @Override
            public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
                //接收邀请时自动加入到群组的通知
            }

            @Override
            public void onMuteListAdded(String groupId, final List<String> mutes, final long muteExpire) {
                //成员禁言的通知
            }

            @Override
            public void onMuteListRemoved(String groupId, final List<String> mutes) {
                //成员从禁言列表里移除通知
            }

            @Override
            public void onAdminAdded(String groupId, String administrator) {
                //增加管理员的通知
            }

            @Override
            public void onAdminRemoved(String groupId, String administrator) {
                //管理员移除的通知
            }

            @Override
            public void onOwnerChanged(String groupId, String newOwner, String oldOwner) {
                //群所有者变动通知
            }
            @Override
            public void onMemberJoined(final String groupId,  final String member){
                //群组加入新成员通知
            }
            @Override
            public void onMemberExited(final String groupId, final String member) {
                //群成员退出通知
            }

            @Override
            public void onAnnouncementChanged(String groupId, String announcement) {
                //群公告变动通知
            }

            @Override
            public void onSharedFileAdded(String groupId, EMMucSharedFile sharedFile) {
                //增加共享文件的通知
            }

            @Override
            public void onSharedFileDeleted(String groupId, String fileId) {
                //群共享文件删除通知
            }
        });*/
    }
}

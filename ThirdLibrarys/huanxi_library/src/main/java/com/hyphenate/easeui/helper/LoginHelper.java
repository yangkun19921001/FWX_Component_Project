package com.hyphenate.easeui.helper;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by yangk on 2018/10/23.
 */

public class LoginHelper {

    /**
     * 是否注册
     */
    public boolean isRegister() {
        return EMClient.getInstance().isLoggedInBefore();
    }

    /**
     * 开始注册
     */
    public void onRegister(String phone_number, String password) throws HyphenateException {
        EMClient.getInstance().createAccount(phone_number, password);
    }

    /**
     * 开始登陆
     */
    public void onLogin(String phone_number, String password, EMCallBack emCallBack) {
        EMClient.getInstance().login(phone_number, password, emCallBack);


    }

    /**
     * 获取当前用户的信息
     */
    public EMOptions getLoginInfo(){
       return EMClient.getInstance().getOptions();
    }

    /**
     * 登录成功做的一些初始化
     */
    public void loginSucceedInit() {
        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();
    }

    /**
     * 退出登录
     */
    public void loginOut(EMCallBack emCallBack) {
        EMClient.getInstance().logout(true, emCallBack);
    }

    /**
     * 连接监听
     */
    public void addConnectListener(EMConnectionListener emConnectionListener) {
        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(emConnectionListener);

    }

    /**
     * 连接监听错误或失败码
     */
    public String connectErrorCode(int error) {
        if (error == EMError.USER_REMOVED) {
            // 显示帐号已经被移除
            return "显示帐号已经被移除";
        } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
            // 显示帐号在其他设备登录
            return "显示帐号在其他设备登录";
        } else {
            //连接不到聊天服务器
            //当前网络不可用，请检查网络设置
            return "连接不到聊天服务器，或当前网络不可用。";
        }
    }

}


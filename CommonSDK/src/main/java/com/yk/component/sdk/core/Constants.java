/*
 * Copyright 2018 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yk.component.sdk.core;

import com.yk.component.sdk.R;

/**
 * ================================================
 * CommonSDK 的 Constants 可以定义公用的常量, 比如关于业务的常量或者正则表达式, 每个组件的 Constants 可以定义组件自己的私有常量
 * <p>
 * Created by JessYan on 30/03/2018 17:32
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface Constants {
    //电话号码正则
    String PHONE_REGULAR = "^1[3-9]\\d{9}$";


    public static interface IOpenLogin {
        String OPEN_LOGIN_TYPE = "OPEN_LOGIN_TYPE";

        int OPEN_LOGIN_TYPE_LOGIN = 0x001;
        int OPEN_LOGIN_TYPE_REGISTER = 0x002;
    }

    public interface ILogPath {
        String DefPath = "/T01/GL_Log";
        String Log_I_Path = "/T01/GL_Log/运行日志";
        String Log_HTTP_Path = "/T01/GL_Log/HTTP日志";
        String Log_D_Path = "/T01/GL_Log/调式日志";
        String Log_E_Path = "/T01/GL_Log/异常日志";
        String Log_Crash_Path = "/T01/GL_Log/崩溃日志";
    }

    public interface ISP_Config {
        String SP_NAME = com.blankj.utilcode.util.AppUtils.getAppName();
        String USER_PHONE_NUMBER = "USER_PHONE_NUMBER";
        String USER_NAME = "USER_NAME";
        String USER_PASSWORD = "USER_PASSWORD";
    }


    /**
     * 加载中视图
     * 0: "正在加载"
     * 1： "发送成功"
     * 2："发送失败"
     * 3："请勿重复操作"
     * 4：
     */
    public interface ILoad {
        int Loading = 0;
        int LoadSuccess = 1;
        int LoadFail = 2;
        int NotRePost = 3;
    }

    public interface IContacts {
        String NEW_FRIEND = "新的朋友";
        String GROUP_CHAT = "群聊";
        String TITLE = "标签";
        String Public_Number = "公众号";
        int AddFriendLayout_Top = 0x001;
        int AddFriendLayout_Button = 0x002;
        int AddFriendLayout_Group = 0x003;
        String AREA = "地区";
        String FROM = "来至";
        String SOCIAL_INFORMATION = "社交资料";
        String NEW_FRIEND_DATA = "NEW_FRIEND_DATA";
        String NewFriendsAdapter = "NewFriendsAdapter";
    }

    public interface IMessageType {
        int OneChat = 0x001;
        int GroupChat = 0x002;
    }

    public interface IFind {

        //布局类型
        int OneLayout = 0x001;
        int GroupLayout = 0x002;

        //布局标签
        String CircleOfFriends = "朋友圈";
        String Sweep = "扫一扫";
        String ShakeAShake = "摇一摇";
        String Shopping = "购物";
        String PeopleInTheVicinity = "附近的人";
        String DriftingBottle = "漂流瓶";
        String SmallProgram = "小程序";

        //布局标签图片
        int CircleOfFriendsIcon = R.drawable.ease_default_avatar;
        int SweepIcon = R.drawable.ease_default_avatar;
        int ShakeAShakeIcon = R.drawable.ease_default_avatar;
        int ShoppingIcon = R.drawable.ease_default_avatar;
        int PeopleInTheVicinityIcon = R.drawable.ease_default_avatar;
        int DriftingBottleIcon = R.drawable.ease_default_avatar;
        int SmallProgramIcon = R.drawable.ease_default_avatar;
    }

    public interface IMe {
        int MyInfo = 0x001;
        int OneLayout = 0x002;
        int GroupLayout = 0x003;

        String MY_INFO = "我的信息";
        String MY_PURSE = "钱包";
        String MY_COLLECT = "收藏";
        String MY_IMAGE = "相册";
        String MY_CARD = "卡包";
        String MY_SETTING = "设置";
    }

    public interface IChat {
        int OneChat = 0x001;
        int GroupChat = 0x002;
        /**
         * 聊天类型
         */
        String ChatType = "ChatType";
        /**
         * 聊天对象
         */
        String otherId = "otherId";

        //最后一条聊天的消息 ID
        //最后一条聊天的聊天消息类型
        //最后一条聊天的类型（单聊/群聊）
        //与谁聊天
        String MESSAGE_ID = "MESSAGE_ID";
        String MESSAGE_TYPE = "MESSAGE_TYPE";
        String MESSAGE_SIG_GROUP = "MESSAGE_SIG_GROUP";
        String MESSAGE_CONTENT = "MESSAGE_CONENT";
        String MESSAGE_TIME = "MESSAGE_TIME";

        int EVENT = 0;
        int SEND_TEXT = 1;
        int RECEIVE_TEXT = 2;

        int SEND_IMAGE = 3;
        int RECEIVE_IMAGE = 4;

        int SEND_VOICE = 5;
        int RECEIVE_VOICE = 6;

        int SEND_VIDEO = 7;
        int RECEIVE_VIDEO = 8;

        int SEND_LOCATION = 9;
        int RECEIVE_LOCATION = 10;

        int SEND_FILE = 11;
        int RECEIVE_FILE = 12;

        int SEND_CUSTOM = 13;
        int RECEIVE_CUSTOM = 14;


        String SessionMessageToAdapter = "SessionMessageToAdapter";
    }

    public interface IPostMessage {
        int ON_MESSAGE_RECEIVED = 0x001;
        int ON_CMDMESSAGE_RECEIVED = 0x002;
        int ON_MESSAGE_READ = 0x003;
        int ON_MESSAGE_DELIVERED = 0x004;
        int ON_MESSAGE_RECALLED = 0x005;
        int ON_MESSAGE_CHANGED = 0x006;
        int SEND_CURRENT_CHAT_PERSON = 0x007;
        int SESSION_TO_CHAT_ADAPTER = 0x008;
    }

    public interface IWeiChat {
        String UP_DB_HistorySessionMessage = "UP_DB_HistorySessionMessage";
        String UP_Adapter_HistorySessionMessage = "UP_Adapter_HistorySessionMessage";
        String RevicesMessage = "UP_DB_RevicesMessage";
    }

    public interface IHXMessageType {
        int MESSAGE_TYPE_RECV_TXT = 0;
        int MESSAGE_TYPE_SENT_TXT = 1;
        int MESSAGE_TYPE_SENT_IMAGE = 2;
        int MESSAGE_TYPE_SENT_LOCATION = 3;
        int MESSAGE_TYPE_RECV_LOCATION = 4;
        int MESSAGE_TYPE_RECV_IMAGE = 5;
        int MESSAGE_TYPE_SENT_VOICE = 6;
        int MESSAGE_TYPE_RECV_VOICE = 7;
        int MESSAGE_TYPE_SENT_VIDEO = 8;
        int MESSAGE_TYPE_RECV_VIDEO = 9;
        int MESSAGE_TYPE_SENT_FILE = 10;
        int MESSAGE_TYPE_RECV_FILE = 11;
        int MESSAGE_TYPE_SENT_EXPRESSION = 12;
        int MESSAGE_TYPE_RECV_EXPRESSION = 13;
    }
}

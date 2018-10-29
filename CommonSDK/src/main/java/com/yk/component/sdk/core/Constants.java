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
}

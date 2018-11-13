package com.zhaoshuang.weixinrecorded;

import android.content.Context;

import com.yixia.camera.VCamera;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zhaoshuang on 17/2/8.
 */

public class SDKUtil {

    public static String VIDEO_PATH =  "/sdcard/T01/video/";

    public static void initSDK(Context context) {

//        VIDEO_PATH += String.valueOf(System.currentTimeMillis());
        String format = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss",
                Locale.getDefault()).format(new Date());
        VIDEO_PATH += format;
        File file = new File(VIDEO_PATH);
        if (!file.exists()) file.mkdirs();

        //设置视频缓存路径
        VCamera.setVideoCachePath(VIDEO_PATH);

        // log输出,ffmpeg输出到logcat
        VCamera.setDebugMode(true);

        // 初始化拍摄SDK，必须
        VCamera.initialize(context);
    }
}

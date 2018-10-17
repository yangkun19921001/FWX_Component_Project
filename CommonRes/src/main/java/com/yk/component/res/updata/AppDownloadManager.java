package com.yk.component.res.updata;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.ALog;
import com.blankj.utilcode.util.*;
import com.jess.arms.utils.ArmsUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.io.File;
import java.lang.ref.WeakReference;

public class AppDownloadManager {
    public static final String TAG = "AppDownloadManager";
    private WeakReference<Activity> weakReference;
    private DownloadManager mDownloadManager;
    private DownloadChangeObserver mDownLoadChangeObserver;
    private DownloadReceiver mDownloadReceiver;
    private long mReqId;
    private OnUpdateListener mUpdateListener;

    /**
     * 下载 APK 地址
     */
    String apkFilePath = Environment.getExternalStorageDirectory() + "/T01/download/Small_DiDa_BB_PTTv1.0.1.8.apk";

    public static String HTTP_GET = "http://39.106.172.189:8000/pttUpgrade/android/SmallDiDa/pttLatest.ver";

    private static String POST_SYSTEM_INFO = "http://39.106.172.189:8000/pttUpgrade/android/SmallDiDa/Small_DiDa_BB_PTTv1.0.1.8.apk";

    public AppDownloadManager(Activity activity) {
        weakReference = new WeakReference<Activity>(activity);
        mDownloadManager = (DownloadManager) weakReference.get().getSystemService(Context.DOWNLOAD_SERVICE);
        mDownLoadChangeObserver = new DownloadChangeObserver(new Handler());
        mDownloadReceiver = new DownloadReceiver();
    }

    public void setUpdateListener(OnUpdateListener mUpdateListener) {
        this.mUpdateListener = mUpdateListener;
    }

    public void downloadApk(String apkUrl, String title, String desc) {
        // fix bug : 装不了新版本，在下载之前应该删除已有文件
        File apkFile = new File(apkFilePath);

        if (apkFile != null && apkFile.exists()) {
            apkFile.delete();
        }

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        //设置title
        request.setTitle(title);
        // 设置描述
        request.setDescription(desc);
        // 完成后显示通知栏
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setDestinationInExternalFilesDir(weakReference.get(), Environment.DIRECTORY_DOWNLOADS, "T01_dida_v1.0.2.8_release.apk");
        //在手机SD卡上创建一个download文件夹
//        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();
        //指定下载到SD卡的/download/my/目录下
        request.setDestinationInExternalPublicDir("/T01/download/", "Small_DiDa_BB_PTTv1.0.1.8.apk");

        request.setMimeType("application/vnd.android.package-archive");
        //
        mReqId = mDownloadManager.enqueue(request);

    }

    /**
     * 取消下载
     */
    public void cancel() {
        mDownloadManager.remove(mReqId);

    }

    /**
     * 对应 {@link Activity }
     */
    public void resume() {
        //设置监听Uri.parse("content://downloads/my_downloads")
        weakReference.get().getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), true,
                mDownLoadChangeObserver);
        // 注册广播，监听APK是否下载完成
        weakReference.get().registerReceiver(mDownloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    /**
     * 对应{@link Activity#onPause()} ()}
     */
    public void onPause() {
        try {
            weakReference.get().getContentResolver().unregisterContentObserver(mDownLoadChangeObserver);
            weakReference.get().unregisterReceiver(mDownloadReceiver);
        }catch (Exception e){
            ALog.i(TAG,e.getMessage());
        }
    }

    private void updateView() {
        int[] bytesAndStatus = new int[]{0, 0, 0};
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(mReqId);
        Cursor c = null;
        try {
            c = mDownloadManager.query(query);
            if (c != null && c.moveToFirst()) {
                //已经下载的字节数
                bytesAndStatus[0] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //总需下载的字节数
                bytesAndStatus[1] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                //状态所在的列索引
                bytesAndStatus[2] = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }

        if (mUpdateListener != null) {
            mUpdateListener.update(bytesAndStatus[0], bytesAndStatus[1]);
        }

        Log.i(TAG, "下载进度：" + bytesAndStatus[0] + "/" + bytesAndStatus[1] + "");
    }

    class DownloadChangeObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public DownloadChangeObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            updateView();
        }
    }

    class DownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            boolean haveInstallPermission;
            // 兼容Android 8.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                //先获取是否有安装未知来源应用的权限
                haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();
                if (!haveInstallPermission) {//没有权限
                    // 弹窗，并去设置页面授权
                    final AndroidOInstallPermissionListener listener = new AndroidOInstallPermissionListener() {
                        @Override
                        public void permissionSuccess() {
                            installApk(context, intent);
                        }

                        @Override
                        public void permissionFail() {
                            ToastUtils.showShort("授权失败，无法安装应用");
                        }
                    };

 /*                   AndroidOPermissionActivity.sListener = listener;
                    Intent intent1 = new Intent(context, AndroidOPermissionActivity.class);
                    context.startActivity(intent1);*/
                } else {
                    installApk(context, intent);
                }
            } else {
                installApk(context, intent);
            }

        }
    }


    /**
     * @param context
     * @param intent
     */
    private void installApk(Context context, Intent intent) {
        long completeDownLoadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

        ALog.i(TAG, "收到广播");
        Uri uri;
        Intent intentInstall = new Intent();
        intentInstall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentInstall.setAction(Intent.ACTION_VIEW);

        if (completeDownLoadId == mReqId) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { // 6.0以下
                uri = mDownloadManager.getUriForDownloadedFile(completeDownLoadId);
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) { // 6.0 - 7.0
                File apkFile = queryDownloadedApk(context, completeDownLoadId);
                uri = Uri.fromFile(apkFile);
            } else { // Android 7.0 以上
                uri = FileProvider.getUriForFile(context,
                        com.blankj.utilcode.util.AppUtils.getAppPackageName()+ ".fileProvider",
                        new File(apkFilePath));
                intentInstall.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }

            ALog.i("zhouwei", "下载完成了");

            intentInstall.setDataAndType(uri, "application/vnd.android.package-archive");
            context.startActivity(intentInstall);
        }
    }

    //通过downLoadId查询下载的apk，解决6.0以后安装的问题
    public static File queryDownloadedApk(Context context, long downloadId) {
        File targetApkFile = null;
        DownloadManager downloader = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        if (downloadId != -1) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cur = downloader.query(query);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    if (!TextUtils.isEmpty(uriString)) {
                        targetApkFile = new File(Uri.parse(uriString).getPath());
                    }
                }
                cur.close();
            }
        }
        return targetApkFile;
    }

    public interface OnUpdateListener {
        void update(int currentByte, int totalByte);
    }

    public interface AndroidOInstallPermissionListener {
        void permissionSuccess();

        void permissionFail();
    }

    /**
     * 根据当前的生命周期调用
     */
    public void onResumes() {
        resume();
    }

    /**
     * 根据当前的生命周期调用
     */
    public void onPauses() {
        onPause();
    }

    /**
     * 向服务器请求当前版本
     */
    public void checkAppVerstion(Context context, StringCallback callback) {
        OkGo.<String>get(HTTP_GET).tag(context).execute(callback);
    }

    /**
     * @param url
     * @param title
     * @param content
     */
    public void showUpdataNoty(String url, String title, final String content) {
        if (weakReference == null || weakReference.get() == null)
            throw new NullPointerException("请初始化 AppDownloadManager 类");
        AlertDialog.Builder builder = new AlertDialog.Builder(weakReference.get());
        builder.setMessage("检测到当前 APP 有重要更新 \n 是否前往下载!");
        builder.setTitle("下载提示");
        builder.setPositiveButton("确认前往", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                downloadApk(POST_SYSTEM_INFO, ArmsUtils.getString(com.blankj.utilcode.util.Utils.getApp(), org.litepal.R.string.app_name), "正在下载:" +content);
            }
        });
        builder.setNegativeButton("下次提示", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    ;


}

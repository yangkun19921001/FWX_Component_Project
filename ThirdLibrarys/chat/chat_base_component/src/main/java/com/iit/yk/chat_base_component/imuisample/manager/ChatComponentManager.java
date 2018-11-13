package com.iit.yk.chat_base_component.imuisample.manager;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.iit.yk.chat_base_component.R;
import com.iit.yk.chat_base_component.imuisample.messages.BrowserImageActivity;
import com.iit.yk.chat_base_component.imuisample.messages.VideoActivity;
import com.iit.yk.chat_base_component.imuisample.models.DefaultUser;
import com.iit.yk.chat_base_component.imuisample.models.MyMessage;
import com.iit.yk.chat_base_component.imuisample.views.ChatView;
import com.zhaoshuang.weixinrecorded.RecordedActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.listener.OnCheckedListener;
import com.zhihu.matisse.listener.OnSelectedListener;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.chatinput.model.VideoItem;
import cn.jiguang.imui.chatinput.photo.GifSizeFilter;
import cn.jiguang.imui.chatinput.photo.Glide4Engine;
import cn.jiguang.imui.commons.ImageLoader;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jiguang.imui.messages.ViewHolderController;
import cn.jiguang.imui.pop.MenuPopupAdapter;
import cn.jiguang.imui.pop.PopupLayout;
import cn.jiguang.imui.pop.PopupView;
import cn.jiguang.imui.pop.TestPopupAdapter;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.AUDIO_SERVICE;
import static android.content.Context.POWER_SERVICE;
import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by yangk on 2018/11/8.
 * <p>
 * 对聊天输入框使用的封装
 */

public class ChatComponentManager implements View.OnTouchListener,
        EasyPermissions.PermissionCallbacks, SensorEventListener {


    private WeakReference<Activity> mWctivityWeakReference;

    private final static String TAG = "MessageListActivity";
    private final int RC_RECORD_VOICE = 0x0001;
    private final int RC_CAMERA = 0x0002;
    private final int RC_PHOTO = 0x0003;

    private ChatView mChatView;
    private MsgListAdapter<MyMessage> mAdapter;

    private InputMethodManager mImm;
    private Window mWindow;
    private HeadsetDetectReceiver mHeadsetDetectReceiver;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    /**
     * Store all image messages' path, pass it to {@link BrowserImageActivity},
     * so that click image message can browser all images.
     */
    private ArrayList<String> mPathList = new ArrayList<>();
    private ArrayList<String> mMsgIdList = new ArrayList<>();
    //视频录制并拍照
    private static final int REQUEST_CODE_CHOOSE = 23;
    //图库选择
    private int REQUEST_CODE_RECORDED_VIDEO = 24;

    private List<String> megPopItems = new ArrayList<>();
    private IChatMessageListener iChatMessageListener;



    /**
     *
     */
    public void init(Activity activity) {
        if (activity == null)
            throw new NullPointerException("chat component is init Activity ?");
        mWctivityWeakReference = new WeakReference<Activity>(activity);
        mImm = (InputMethodManager) mWctivityWeakReference.get().getSystemService(Context.INPUT_METHOD_SERVICE);
        mWindow = mWctivityWeakReference.get().getWindow();
        registerProximitySensorListener();
        mChatView = activity.findViewById(R.id.chat_view);
        mChatView.initModule();
        initMsgAdapter();
        mHeadsetDetectReceiver = new HeadsetDetectReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        mWctivityWeakReference.get().registerReceiver(mHeadsetDetectReceiver, intentFilter);
        mChatView.setOnTouchListener(this);
        initMegPopItems();
        mChatView.setMenuClickListener(new OnMenuClickListener() {
            @Override
            public boolean onSendTextMessage(CharSequence input) {
                if (input.length() == 0) {
                    return false;
                }
                MyMessage message = new MyMessage(input.toString(), IMessage.MessageType.SEND_TEXT.ordinal());
                message.setTimeString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                message.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.ironman"));
                message.setMessageStatus(IMessage.MessageStatus.SEND_GOING);
                mAdapter.addToStart(message, true);
                if (iChatMessageListener != null)
                    iChatMessageListener.sendMessage(IMessage.MessageType.SEND_TEXT.ordinal(), message);
                return true;
            }

            @Override
            public void onSendFiles(List<FileItem> list) {
                if (list == null || list.isEmpty()) {
                    return;
                }

                MyMessage message;
                for (FileItem item : list) {
                    if (item.getType() == FileItem.Type.Image) {
                        message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE.ordinal());
                        mPathList.add(item.getFilePath());
                        mMsgIdList.add(message.getMsgId());
                    } else if (item.getType() == FileItem.Type.Video) {
                        message = new MyMessage(null, IMessage.MessageType.SEND_VIDEO.ordinal());
                        message.setDuration(((VideoItem) item).getDuration());

                    } else {
                        throw new RuntimeException("Invalid FileItem type. Must be Type.Image or Type.Video");
                    }

                    message.setTimeString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                    message.setMediaFilePath(item.getFilePath());
                    message.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.ironman"));
                    message.setMessageStatus(IMessage.MessageStatus.SEND_GOING);
                    message.setProgress("40");
                    final MyMessage fMsg = message;
                    mWctivityWeakReference.get().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.addToStart(fMsg, true);
                            if (iChatMessageListener != null)
                                iChatMessageListener.sendMessage(fMsg.getType(), fMsg);
                        }
                    });
                }
            }

            @Override
            public boolean switchToMicrophoneMode() {
                scrollToBottom();
                String[] perms = new String[]{
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };

                if (!EasyPermissions.hasPermissions(mWctivityWeakReference.get(), perms)) {
                    EasyPermissions.requestPermissions(mWctivityWeakReference.get(),
                            mWctivityWeakReference.get().getResources().getString(R.string.chat_base_rationale_record_voice),
                            RC_RECORD_VOICE, perms);
                }
                return true;
            }

            @Override
            public boolean switchToGalleryMode() {
                scrollToBottom();
                String[] perms = new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                };

                if (!EasyPermissions.hasPermissions(mWctivityWeakReference.get(), perms)) {
                    EasyPermissions.requestPermissions(mWctivityWeakReference.get(),
                            mWctivityWeakReference.get().getResources().getString(R.string.chat_base_rationale_photo),
                            RC_PHOTO, perms);
                }
                // If you call updateData, select photo view will try to update data(Last update over 30 seconds.)
                mChatView.getChatInputView().getSelectPhotoView().updateData();
                return true;
            }

            @Override
            public boolean switchToCameraMode() {
                scrollToBottom();
                String[] perms = new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                };

                if (!EasyPermissions.hasPermissions(mWctivityWeakReference.get(), perms)) {
                    EasyPermissions.requestPermissions(mWctivityWeakReference.get(),
                            mWctivityWeakReference.get().getResources().getString(R.string.chat_base_rationale_camera),
                            RC_CAMERA, perms);
                    return false;
                } else {
                    File rootDir = mWctivityWeakReference.get().getFilesDir();
                    String fileDir = rootDir.getAbsolutePath() + "/photo";
                    mChatView.setCameraCaptureFile(fileDir, new SimpleDateFormat("yyyy-MM-dd-hhmmss",
                            Locale.getDefault()).format(new Date()));
                }
                return true;
            }

            @Override
            public boolean switchToEmojiMode() {
                scrollToBottom();
                return true;
            }

            @Override
            public void onOpenCamera() {
                Intent intent = new Intent(mWctivityWeakReference.get(), RecordedActivity.class);
                mWctivityWeakReference.get().startActivityForResult(intent, REQUEST_CODE_RECORDED_VIDEO);
            }
        });

        mChatView.setRecordVoiceListener(new RecordVoiceListener() {
            @Override
            public void onStartRecord() {
                // set voice file path, after recording, audio file will save here
                String path = Environment.getExternalStorageDirectory().getPath() + "/T01/voice";
                File destDir = new File(path);
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }
                mChatView.setRecordVoiceFile(destDir.getPath(), DateFormat.format("yyyy-MM-dd-hhmmss",
                        Calendar.getInstance(Locale.CHINA)) + "");
            }

            @Override
            public void onFinishRecord(File voiceFile, int duration) {
                MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_VOICE.ordinal());
                message.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.ironman"));
                message.setMediaFilePath(voiceFile.getPath());
                message.setDuration(duration);
                message.setTimeString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                message.setMessageStatus(IMessage.MessageStatus.SEND_GOING);
                mAdapter.addToStart(message, true);
                if (iChatMessageListener != null)
                    iChatMessageListener.sendMessage(message.getType(), message);
            }

            @Override
            public void onCancelRecord() {

            }

            /**
             * In preview record voice layout, fires when click cancel button
             * Add since chatinput v0.7.3
             */
            @Override
            public void onPreviewCancel() {

            }

            /**
             * In preview record voice layout, fires when click send button
             * Add since chatinput v0.7.3
             */
            @Override
            public void onPreviewSend() {

            }
        });

        mChatView.setOnCameraCallbackListener(new OnCameraCallbackListener() {
            @Override
            public void onTakePictureCompleted(String photoPath) {
                final MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE.ordinal());
                message.setTimeString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                message.setMediaFilePath(photoPath);
                message.setMessageStatus(IMessage.MessageStatus.SEND_GOING);
                mPathList.add(photoPath);
                mMsgIdList.add(message.getMsgId());
                message.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.ironman"));
                mWctivityWeakReference.get().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addToStart(message, true);
                        if (iChatMessageListener != null)
                            iChatMessageListener.sendMessage(message.getType(), message);
                    }
                });
            }

            @Override
            public void onStartVideoRecord() {

            }

            @Override
            public void onFinishVideoRecord(String videoPath) {

            }

            @Override
            public void onCancelVideoRecord() {

            }
        });

        mChatView.getChatInputView().getInputView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollToBottom();
                return false;
            }
        });

        mChatView.getSelectAlbumBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse.from(mWctivityWeakReference.get())
                        .choose(MimeType.ofAll(), false)
                        .theme(R.style.Matisse_Dracula)
                        .countable(true)
                        .capture(true)
                        .captureStrategy(
                                new CaptureStrategy(true, "com.zhaoshuang.weixinrecordeddemo.fileprovider", "test"))
                        .maxSelectable(9)
                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .gridExpectedSize(
                                mWctivityWeakReference.get().getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f)
//                                            .imageEngine(new GlideEngine())  // for glide-V3
                        .imageEngine(new Glide4Engine())    // for glide-V4
                        .setOnSelectedListener(new OnSelectedListener() {
                            @Override
                            public void onSelected(
                                    @NonNull List<Uri> uriList, @NonNull List<String> pathList) {
                                // DO SOMETHING IMMEDIATELY HERE
                                Log.e("onSelected", "onSelected: pathList=" + pathList);

                            }
                        })
                        .originalEnable(true)
                        .maxOriginalSize(10)
                        .autoHideToolbarOnSingleTap(true)
                        .setOnCheckedListener(new OnCheckedListener() {
                            @Override
                            public void onCheck(boolean isChecked) {
                                // DO SOMETHING IMMEDIATELY HERE
                                Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                            }
                        })
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        AudioManager audioManager = (AudioManager) mWctivityWeakReference.get().getSystemService(AUDIO_SERVICE);
        try {
            if (audioManager.isBluetoothA2dpOn() || audioManager.isWiredHeadsetOn()) {
                return;
            }
            if (mAdapter.getMediaPlayer().isPlaying()) {
                float distance = event.values[0];
                if (distance >= mSensor.getMaximumRange()) {
                    mAdapter.setAudioPlayByEarPhone(0);
                    setScreenOn();
                } else {
                    mAdapter.setAudioPlayByEarPhone(2);
                    ViewHolderController.getInstance().replayVoice();
                    setScreenOff();
                }
            } else {
                if (mWakeLock != null && mWakeLock.isHeld()) {
                    mWakeLock.release();
                    mWakeLock = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ChatInputView chatInputView = mChatView.getChatInputView();
                if (chatInputView.getMenuState() == View.VISIBLE) {
                    chatInputView.dismissMenuLayout();
                }
                try {
                    View v = mWctivityWeakReference.get().getCurrentFocus();
                    if (mImm != null && v != null) {
                        mImm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        view.clearFocus();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MotionEvent.ACTION_UP:
                view.performClick();
                break;
        }
        return false;
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(mWctivityWeakReference.get(), perms)) {
            new AppSettingsDialog.Builder(mWctivityWeakReference.get()).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }




    private class HeadsetDetectReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                if (intent.hasExtra("state")) {
                    int state = intent.getIntExtra("state", 0);
                    mAdapter.setAudioPlayByEarPhone(state);
                }
            }
        }
    }

    private void registerProximitySensorListener() {
        try {
            mPowerManager = (PowerManager) mWctivityWeakReference.get().getSystemService(POWER_SERVICE);
            mWakeLock = mPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, TAG);
            mSensorManager = (SensorManager) mWctivityWeakReference.get().getSystemService(SENSOR_SERVICE);
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initMsgAdapter() {
        final float density = mWctivityWeakReference.get().getResources().getDisplayMetrics().density;
        final float MIN_WIDTH = 60 * density;
        final float MAX_WIDTH = 200 * density;
        final float MIN_HEIGHT = 60 * density;
        final float MAX_HEIGHT = 200 * density;
        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadAvatarImage(ImageView avatarImageView, String string) {
                // You can use other image load libraries.
           /*     if (string.contains("R.drawable")) {
                    Integer resId = getResources().getIdentifier(string.replace("R.drawable.", ""),
                            "drawable", getPackageName());


                } else {*/
                avatarImageView.setImageResource(R.drawable.default_avatar);
//                Glide.with(mWctivityWeakReference.get())
//                        .load(string)
//                        .apply(new RequestOptions().placeholder(R.drawable.default_avatar))
//                        .into(avatarImageView);
//                }
            }

            /**
             * Load image message
             * @param imageView Image message's ImageView.
             * @param string A file path, or a uri or url.
             */
            @Override
            public void loadImage(final ImageView imageView, String string) {
                // You can use other image load libraries.
                Glide.with(mWctivityWeakReference.get())
                        .asBitmap()
                        .load(string)
                        .apply(new RequestOptions().fitCenter().placeholder(R.drawable.aurora_picture_not_found))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                int imageWidth = resource.getWidth();
                                int imageHeight = resource.getHeight();
                                Log.d(TAG, "Image width " + imageWidth + " height: " + imageHeight);

                                // 裁剪 bitmap
                                float width, height;
                                if (imageWidth > imageHeight) {
                                    if (imageWidth > MAX_WIDTH) {
                                        float temp = MAX_WIDTH / imageWidth * imageHeight;
                                        height = temp > MIN_HEIGHT ? temp : MIN_HEIGHT;
                                        width = MAX_WIDTH;
                                    } else if (imageWidth < MIN_WIDTH) {
                                        float temp = MIN_WIDTH / imageWidth * imageHeight;
                                        height = temp < MAX_HEIGHT ? temp : MAX_HEIGHT;
                                        width = MIN_WIDTH;
                                    } else {
                                        float ratio = imageWidth / imageHeight;
                                        if (ratio > 3) {
                                            ratio = 3;
                                        }
                                        height = imageHeight * ratio;
                                        width = imageWidth;
                                    }
                                } else {
                                    if (imageHeight > MAX_HEIGHT) {
                                        float temp = MAX_HEIGHT / imageHeight * imageWidth;
                                        width = temp > MIN_WIDTH ? temp : MIN_WIDTH;
                                        height = MAX_HEIGHT;
                                    } else if (imageHeight < MIN_HEIGHT) {
                                        float temp = MIN_HEIGHT / imageHeight * imageWidth;
                                        width = temp < MAX_WIDTH ? temp : MAX_WIDTH;
                                        height = MIN_HEIGHT;
                                    } else {
                                        float ratio = imageHeight / imageWidth;
                                        if (ratio > 3) {
                                            ratio = 3;
                                        }
                                        width = imageWidth * ratio;
                                        height = imageHeight;
                                    }
                                }
                                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                                params.width = (int) width;
                                params.height = (int) height;
                                imageView.setLayoutParams(params);
                                Matrix matrix = new Matrix();
                                float scaleWidth = width / imageWidth;
                                float scaleHeight = height / imageHeight;
                                matrix.postScale(scaleWidth, scaleHeight);
                                imageView.setImageBitmap(Bitmap.createBitmap(resource, 0, 0, imageWidth, imageHeight, matrix, true));
                            }
                        });
            }

            /**
             * Load video message
             * @param imageCover Video message's image cover
             * @param uri Local path or url.
             */
            @Override
            public void loadVideo(ImageView imageCover, String uri) {
                long interval = 5000 * 1000;
                Glide.with(mWctivityWeakReference.get())
                        .asBitmap()
                        .load(uri)
                        // Resize image view by change override size.
                        .apply(new RequestOptions().frame(interval).override(200, 400))
                        .into(imageCover);
            }
        };

        // Use default layout
        MsgListAdapter.HoldersConfig holdersConfig = new MsgListAdapter.HoldersConfig();
        mAdapter = new MsgListAdapter<>("0", holdersConfig, imageLoader);

        mAdapter.setOnMsgClickListener(new MsgListAdapter.OnMsgClickListener<MyMessage>() {
            @Override
            public void onMessageClick(MyMessage message) {
                // do something
                if (message.getType() == IMessage.MessageType.RECEIVE_VIDEO.ordinal()
                        || message.getType() == IMessage.MessageType.SEND_VIDEO.ordinal()) {
                    if (!TextUtils.isEmpty(message.getMediaFilePath())) {
                        Intent intent = new Intent(mWctivityWeakReference.get(), VideoActivity.class);
                        intent.putExtra(VideoActivity.VIDEO_PATH, message.getMediaFilePath());
                        mWctivityWeakReference.get().startActivity(intent);
                    }
                } else if (message.getType() == IMessage.MessageType.RECEIVE_IMAGE.ordinal()
                        || message.getType() == IMessage.MessageType.SEND_IMAGE.ordinal()) {
                    if (mPathList == null || mPathList.size() == 0)return;
                    Intent intent = new Intent(mWctivityWeakReference.get(), BrowserImageActivity.class);
                    intent.putExtra("msgId", message.getMsgId());
                    intent.putStringArrayListExtra("pathList", mPathList);
                    intent.putStringArrayListExtra("idList", mMsgIdList);
                    mWctivityWeakReference.get().startActivity(intent);
                } else {
                    Toast.makeText(mWctivityWeakReference.get(),
                            mWctivityWeakReference.get().getString(R.string.chat_base_message_click_hint),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<MyMessage>() {
            @Override
            public void onMessageLongClick(View view, MyMessage message) {
                // do something
                PopupView popupView = getPopupView(megPopItems);
                popupView.setPopupLocation(PopupLayout.PopupLocation.TOP);
                popupView.show(view);
            }
        });

        mAdapter.setOnAvatarClickListener(new MsgListAdapter.OnAvatarClickListener<MyMessage>() {
            @Override
            public void onAvatarClick(MyMessage message) {
                DefaultUser userInfo = (DefaultUser) message.getFromUser();
                Toast.makeText(mWctivityWeakReference.get(),
                        mWctivityWeakReference.get().getString(R.string.chat_base_avatar_click_hint),
                        Toast.LENGTH_SHORT).show();
                // do something
            }
        });

        mAdapter.setMsgStatusViewClickListener(new MsgListAdapter.OnMsgStatusViewClickListener<MyMessage>() {
            @Override
            public void onStatusViewClick(MyMessage message) {
                ToastUtils.showShort("点击重发");
            }
        });

        try {
            mChatView.setAdapter(mAdapter);
            mAdapter.getLayoutManager().scrollToPosition(0);
        } catch (Exception e) {
            Log.e("init--", e.getMessage());
        }

    }

    private void initMegPopItems() {
        megPopItems.clear();
        megPopItems.add("复制");
        megPopItems.add("转发");
        megPopItems.add("提醒");
        megPopItems.add("删除");
    }

    public PopupView getPopupView(List<String> item) {
        final MenuPopupAdapter<String> adapter = new TestPopupAdapter(item);
        final PopupView popupView = new PopupView(mWctivityWeakReference.get());
        popupView.setAdapter(adapter);
        popupView.setOnPopupItemClickListener(new PopupLayout.OnPopupItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(view.getContext(), adapter.getItem(position), Toast.LENGTH_SHORT).show();
                popupView.dismiss();
            }
        });
        return popupView;
    }

    private void setScreenOn() {
        if (mWakeLock != null) {
            mWakeLock.setReferenceCounted(false);
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    private void setScreenOff() {
        if (mWakeLock == null) {
            mWakeLock = mPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, TAG);
        }
        mWakeLock.acquire();
    }

    private void scrollToBottom() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mChatView.getMessageListView().smoothScrollToPosition(0);
            }
        }, 200);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        MyMessage message = null;

        if (requestCode == REQUEST_CODE_RECORDED_VIDEO && resultCode == RESULT_OK) {
            String imagePath = data.getStringExtra("imagePath");
            String videoPath = data.getStringExtra("videoPath");
            if (!TextUtils.isEmpty(imagePath)) {
                message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE.ordinal());
                message.setMediaFilePath(imagePath);
                mPathList.add(imagePath);
                mMsgIdList.add(message.getMsgId());
                message.setTimeString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                message.setMediaFilePath(imagePath);
                message.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.ironman"));
                message.setMessageStatus(IMessage.MessageStatus.SEND_GOING);
            } else if (!TextUtils.isEmpty(videoPath)) {
                message = new MyMessage(null, IMessage.MessageType.SEND_VIDEO.ordinal());
                mPathList.add(videoPath);
                mMsgIdList.add(message.getMsgId());
                message.setTimeString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                message.setMediaFilePath(videoPath);
                message.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.ironman"));
                message.setMessageStatus(IMessage.MessageStatus.SEND_GOING);
            }
            if (message != null)
                mAdapter.addToStart(message, true);
            if (iChatMessageListener != null)
                iChatMessageListener.sendMessage(message.getType(), message);
        } else if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<String> filePath = Matisse.obtainPathResult(data);
            if (filePath != null && filePath.size() > 0) {
                for (String url : filePath) {
                    String fileExtension = MimeTypeMap.getFileExtensionFromUrl(url.toString());
                    String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
                    switch (mimeType) {
                        case "image/jpeg":
                            message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE.ordinal());
                            message.setMediaFilePath(url);
                            mPathList.add(url);
                            mMsgIdList.add(message.getMsgId());
                            message.setTimeString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                            message.setMediaFilePath(url);
                            message.setMessageStatus(IMessage.MessageStatus.SEND_GOING);
                            break;
                        case "video/mp4":
                            message = new MyMessage(null, IMessage.MessageType.SEND_VIDEO.ordinal());
                            mPathList.add(url);
                            mMsgIdList.add(message.getMsgId());
                            message.setTimeString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                            message.setMediaFilePath(url);
                            message.setMessageStatus(IMessage.MessageStatus.SEND_GOING);
                            break;
                    }

                    if (message != null)
                        mAdapter.addToStart(message, true);
                    if (iChatMessageListener != null)
                        iChatMessageListener.sendMessage(message.getType(), message);
                }
            }
        }
    }

    /**
     * 销毁活动中的任务
     */
    public void onDestory() {
        if (mHeadsetDetectReceiver != null)
            mWctivityWeakReference.get().unregisterReceiver(mHeadsetDetectReceiver);

        if (mSensorManager != null)
            mSensorManager.unregisterListener(this);

        if (iChatMessageListener != null)
            iChatMessageListener = null;
    }

    /**
     * 发送消息和接收消息等的一些处理监听
     */
    public interface IChatMessageListener {
        void sendMessage(int sendType, MyMessage message);
    }

    /**
     * 添加消息监听
     *
     * @param iChatMessageListener
     */
    public void addIChatMessageListener(IChatMessageListener iChatMessageListener) {
        this.iChatMessageListener = iChatMessageListener;
    }

    /**
     * 加载单条数据
     * @param myMessage
     */
    public void loadMessage(MyMessage myMessage) {
        mAdapter.addToStart(myMessage, true);
    }

    /**
     * 加载集合数据
     */
    public void loadListsData(List<MyMessage> list){
        mAdapter.addToEnd(list);
    }

    /**
     * 获取当前聊天数据
     * @return
     */
    public List<MyMessage> getAdapterData() {
        return mAdapter.getMessageList();
    }

    /**
     *
     * @return 返回当前适配器中的聊天数据个数
     */
    public int getCurrentAdapterMessageCount(){
        return mAdapter.getMessageList().size();
    }

    public void upAdapter(String msgId, final IMessage.MessageStatus messageStatus) {
        for (int i = 0; i < mAdapter.getMessageList().size(); i++) {
            if (msgId.equals(mAdapter.getMessageList().get(i).getMsgId())) {
                final int finalI = i;
                mAdapter.getMessageList().get(finalI).setMessageStatus(messageStatus);
                try {
                    Thread.sleep(200);
                    mAdapter.notifyItemChanged(finalI);
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.i(TAG,e.getMessage());
                }
            }
        }
    }
}

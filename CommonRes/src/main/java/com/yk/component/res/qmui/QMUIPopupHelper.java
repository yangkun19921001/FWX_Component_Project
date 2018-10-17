package com.yk.component.res.qmui;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.jessyan.armscomponent.commonres.R;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by yangk on 2018/5/17.
 */

public class QMUIPopupHelper {

    /**
     * 显示普通浮层
     */

    public static void showOrdinaryFloatPup(Activity activity, View view, String content, PopupWindow.OnDismissListener onDismissListener) {
        initNormalPopupIfNeed(activity, view, content, onDismissListener);
    }

    private static void initNormalPopupIfNeed(Activity activity, View view, String content, PopupWindow.OnDismissListener onDismissListener) {
        QMUIPopup mNormalPopup = new QMUIPopup(activity, QMUIPopup.DIRECTION_NONE);
        TextView textView = new TextView(activity);
        textView.setLayoutParams(mNormalPopup.generateLayoutParam(
                QMUIDisplayHelper.dp2px(activity, 250),
                WRAP_CONTENT
        ));
        textView.setLineSpacing(QMUIDisplayHelper.dp2px(activity, 4), 1.0f);
        int padding = QMUIDisplayHelper.dp2px(activity, 20);
        textView.setPadding(padding, padding, padding, padding);
        textView.setText(content);
        textView.setTextColor(ContextCompat.getColor(activity, R.color.app_color_description));
        mNormalPopup.setContentView(textView);
        mNormalPopup.setOnDismissListener(onDismissListener);

        mNormalPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        mNormalPopup.setPreferredDirection(QMUIPopup.DIRECTION_TOP);
        mNormalPopup.show(view);
    }

    /**
     * 显示列表浮层
     */
    public static void showListFloatPup(Activity activity, String[] listItem, View view, AdapterView.OnItemClickListener onDismissListener) {
        initListPopupIfNeed(activity, listItem, view, onDismissListener);
    }

    private static void initListPopupIfNeed(Activity activity, String[] listItems, View view, AdapterView.OnItemClickListener onDismissListener) {

        List<String> data = new ArrayList<>();

        Collections.addAll(data, listItems);

        ArrayAdapter adapter = new ArrayAdapter<>(activity, R.layout.simple_list_item, data);

        QMUIListPopup mListPopup = new QMUIListPopup(activity, QMUIPopup.DIRECTION_NONE, adapter);
        mListPopup.create(QMUIDisplayHelper.dp2px(activity, 250), QMUIDisplayHelper.dp2px(activity, 200), onDismissListener);
        mListPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        mListPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        mListPopup.setPreferredDirection(QMUIPopup.DIRECTION_TOP);
        mListPopup.show(view);
    }
}

package com.yk.component.res.qmui;

import android.app.Activity;

import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.util.List;

/**
 * Created by yangk on 2018/5/17.
 * <p>
 * 底部弹出框帮助类
 */

public class QMUIBottomSheetHelper {

    private static int isShowBottomSheetList = 0;


    /**
     * 底部文字弹出框
     *
     * @param activity
     * @param items
     * @param listItems
     * @param itemClickListener
     */
    public static void showSimpleBottomSheetList(Activity activity, String[] items, List<String> listItems, QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener itemClickListener) {
        QMUIBottomSheet.BottomListSheetBuilder bottomListSheetBuilder = new QMUIBottomSheet.BottomListSheetBuilder(activity);
        isShowBottomSheetList = 0;
        if (items == null && listItems != null && listItems.size() > 0) {
            for (String item : listItems) {
                bottomListSheetBuilder.addItem(item);
                isShowBottomSheetList = 1;
            }
        } else if (items != null && items.length > 0) {
            for (String item : items) {
                bottomListSheetBuilder.addItem(item);
                isShowBottomSheetList = 1;
            }
        }

        if (isShowBottomSheetList == 0) return;
        bottomListSheetBuilder.setOnSheetItemClickListener(itemClickListener)
                .build()
                .show();
    }

    /**
     * 底部 Grid 导航
     * @param activity
     * @param imageRes
     * @param text
     * @param itemClickListener
     */
    public static void showSimpleBottomSheetGrid(Activity activity, Integer[] imageRes, String[] text
            , QMUIBottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener itemClickListener) {
        QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet.BottomGridSheetBuilder(activity);
        if (imageRes != null && text != null && imageRes.length > 0 && text.length > 0 && imageRes.length == text.length) {
            for (int i = 0; i < imageRes.length; i++) {
                if (i < 4) {
                    builder.addItem(imageRes[i]
                            , text[i], i
                            , QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE);
                } else if (i >= 4) {
                    builder.addItem(imageRes[i]
                            , text[i], i
                            , QMUIBottomSheet.BottomGridSheetBuilder.SECOND_LINE);
                }
            }
        }
        //  int tag = (int) itemView.getTag();
        builder.setOnSheetItemClickListener(itemClickListener).build().show();
    }
}

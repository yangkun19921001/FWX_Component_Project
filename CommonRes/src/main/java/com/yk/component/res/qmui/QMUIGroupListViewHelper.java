package com.yk.component.res.qmui;

import android.app.Activity;
import android.view.View;
import android.widget.CompoundButton;

import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import me.jessyan.armscomponent.commonres.R;

/**
 * Created by yangk on 2018/5/17.
 */

public class QMUIGroupListViewHelper {
    /**
     * 显示唯一的条目
     */
    public static void showOnlyItem(Activity activity, String itemContent, View.OnClickListener onClickListener) {
        QMUIGroupListView mGroupListView = activity.findViewById(R.id.groupListView);
        QMUICommonListItemView normalItem = mGroupListView.createItemView(itemContent);
        normalItem.setOrientation(QMUICommonListItemView.VERTICAL);
        QMUIGroupListView.newSection(activity)
                .addItemView(normalItem, onClickListener)
                .addTo(mGroupListView);
    }

    /**
     * 左边右边各显示一个 Text 居中
     */
    public static void showHorizontallyItem(Activity activity, String itemContent, String rightContent, View.OnClickListener onClickListener) {
        QMUIGroupListView mGroupListView = activity.findViewById(R.id.groupListView);
        QMUICommonListItemView itemWithDetail = mGroupListView.createItemView(itemContent);
        itemWithDetail.setDetailText(rightContent);

        QMUIGroupListView.newSection(activity)
                .addItemView(itemWithDetail, onClickListener)
                .addTo(mGroupListView);
    }


    /**
     * 显示上面一个下面一个的 Item
     */
    public static void showTwoVerticalItem(Activity activity, String itemContent, String rightContent, View.OnClickListener onClickListener) {
        QMUIGroupListView mGroupListView = activity.findViewById(R.id.groupListView);
        QMUICommonListItemView itemWithDetailBelow = mGroupListView.createItemView(itemContent);
        itemWithDetailBelow.setOrientation(QMUICommonListItemView.VERTICAL);
        itemWithDetailBelow.setDetailText(rightContent);

        QMUIGroupListView.newSection(activity)
                .addItemView(itemWithDetailBelow, onClickListener)
                .addTo(mGroupListView);
    }

    /**
     * 显示带右箭头的 Item
     */
    public static void showRightArrowItem(Activity activity, String itemContent, View.OnClickListener onClickListener) {
        QMUIGroupListView mGroupListView = activity.findViewById(R.id.groupListView);
        QMUICommonListItemView itemWithChevron = mGroupListView.createItemView(itemContent);
        itemWithChevron.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUIGroupListView.newSection(activity)
                .addItemView(itemWithChevron, onClickListener)
                .addTo(mGroupListView);
    }

    /**
     * 带开关的 Item
     */
    public static void showSwitchItem(Activity activity, String itemContent, boolean isSelect, CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        QMUIGroupListView mGroupListView = activity.findViewById(R.id.groupListView);
        QMUICommonListItemView itemWithSwitch = mGroupListView.createItemView(itemContent);
        itemWithSwitch.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        itemWithSwitch.getSwitch().setChecked(isSelect);
        itemWithSwitch.getSwitch().setOnCheckedChangeListener(onCheckedChangeListener);


        QMUIGroupListView.newSection(activity)
                .addItemView(itemWithSwitch, null)
                .addTo(mGroupListView);
    }
}

package com.yk.component.res.qmui;

import android.app.Activity;
import android.content.DialogInterface;
import android.text.InputType;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

/**
 * Created by yangk on 2018/5/17.
 * <p>
 * Dialog 帮助类
 */

public class QMUIDialogHelper {

    private static int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    /**
     * 红色按钮 dialog
     */
    public static void showRedButtonDialog(Activity activity, String title, String content, String centerContent
            , String yesContent, QMUIDialogAction.ActionListener actionListener) {
        new QMUIDialog.MessageDialogBuilder(activity)
                .setTitle(title)
                .setMessage(content)
                .addAction(centerContent, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, yesContent, QMUIDialogAction.ACTION_PROP_NEGATIVE, actionListener)
                .create(mCurrentDialogStyle).show();
    }


    /**
     * 菜单类型的对话框
     */
    public static void showMenuItem(Activity activity, String[] items, DialogInterface.OnClickListener onClickListener) {

        if (items == null || items.length == 0)
            throw new NullPointerException("请检查 String [] items 是否有数据");

        new QMUIDialog.MenuDialogBuilder(activity)
                .addItems(items, onClickListener)
                .create(mCurrentDialogStyle).show();
    }


    /**
     * 带 checkBox 对话框
     */
    public static void showCheckBoxDialog(Activity activity, String title, String checkBoxContent, String centerText, String yesText, QMUIDialogAction.ActionListener listener) {
        new QMUIDialog.CheckBoxMessageDialogBuilder(activity)
                .setTitle(title)
                .setMessage(checkBoxContent)
                .setChecked(true)
                .addAction(centerText, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(yesText, listener)
                .create(mCurrentDialogStyle).show();
    }

    /**
     * 多选择 Dialog
     */
    public static void showNumerousMultiChoiceDialog(String[] items, Activity activity, QMUIDialogAction.ActionListener actionListener) {
        final QMUIDialog.MultiCheckableDialogBuilder builder = new QMUIDialog.MultiCheckableDialogBuilder(activity)
//                .setCheckedItems(new int[]{1, 3})
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.addAction("取消", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
            }
        });
        builder.addAction("提交", actionListener);
        builder.create(mCurrentDialogStyle).show();
    }

/*new QMUIDialogAction.ActionListener() {
        @Override
        public void onClick(QMUIDialog dialog, int index) {
            String result = "你选择了 ";
            for (int i = 0; i < builder.getCheckedItemIndexes().length; i++) {
                result += "" + builder.getCheckedItemIndexes()[i] + "; ";
            }
            Toast.makeText(tivity(), result, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }*/

    /**
     * 带输入框的 Dialog
     */
    public static QMUIDialog.EditTextDialogBuilder showEditTextDialog(Activity activity, String title, String content, QMUIDialogAction.ActionListener actionListener) {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(activity);
        builder.setTitle(title)
                .setPlaceholder(content)
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", actionListener)
                .create(mCurrentDialogStyle).show();
        return builder;
    }


}




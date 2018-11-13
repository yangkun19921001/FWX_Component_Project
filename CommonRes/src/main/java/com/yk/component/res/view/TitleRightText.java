package com.yk.component.res.view;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by yangk on 2018/11/7.
 */

public class TitleRightText extends AppCompatTextView {
    public TitleRightText(Context context) {
        super(context);
        init(context);
    }

    public TitleRightText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleRightText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 开启抗锯齿 和下划线
     *
     * @param context
     */
    private void init(Context context) {
        getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        getPaint().setAntiAlias(true);
    }

}

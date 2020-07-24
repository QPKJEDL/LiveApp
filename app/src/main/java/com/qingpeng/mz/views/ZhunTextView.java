package com.qingpeng.mz.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.qingpeng.mz.utils.APP;


@SuppressLint("AppCompatCustomView")
public class ZhunTextView extends TextView {
    public ZhunTextView(Context context) {
        super(context);
        //设置字体
        setTypeface(APP.getInstance().getTypeface1());
    }

    public ZhunTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //设置字体
        setTypeface(APP.getInstance().getTypeface1());
    }

    public ZhunTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置字体
        setTypeface(APP.getInstance().getTypeface1());
    }
}
package com.qingpeng.mz.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingpeng.mz.R;
import com.qingpeng.mz.utils.StringUtils;


public class MineItemView extends LinearLayout {
    private Context mContext;
    ImageView ivRight;
    TextView tvRightText;
    ImageView ivLeft;
    TextView tvText;

    public MineItemView(@NonNull Context context) {
        super(context);
        mContext = context;
        init(context, null);
    }

    public MineItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context, attrs);
    }

    public MineItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View inflate = View.inflate(context, R.layout.view_mine_item, this);
        ivRight = (ImageView) inflate.findViewById(R.id.iv_right);
        ivLeft = (ImageView) inflate.findViewById(R.id.iv_left);
        tvText = (TextView) inflate.findViewById(R.id.tv_text);
        tvRightText = (TextView) inflate.findViewById(R.id.tv_right_text);

        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.MineItemView);
        boolean showToastLeftIcon = attr.getBoolean(R.styleable.MineItemView_showToastLeftIcon, false);
        boolean showToastRightIcon = attr.getBoolean(R.styleable.MineItemView_showToastRightIcon, false);
        String text = attr.getString(R.styleable.MineItemView_itemText);
        String righTtext = attr.getString(R.styleable.MineItemView_itemRightText);

        if (StringUtils.isEmpty(righTtext)) {
            tvRightText.setVisibility(GONE);
        } else {
            tvRightText.setText(righTtext);
        }

        if (showToastLeftIcon) {
            int leftIconResourceId = attr.getResourceId(R.styleable.MineItemView_leftIcon, 0);
            if (leftIconResourceId != 0) {
                ivLeft.setBackgroundResource(leftIconResourceId);
                ivLeft.setVisibility(VISIBLE);
            } else {
                ivLeft.setVisibility(GONE);
            }
        } else {
            ivLeft.setVisibility(GONE);
        }

        if (showToastRightIcon) {
            int rightIconResourceId = attr.getResourceId(R.styleable.MineItemView_rightIcon, 0);
            if (rightIconResourceId != 0) {
                ivRight.setBackgroundResource(rightIconResourceId);
                ivRight.setVisibility(VISIBLE);
            } else {
                ivRight.setVisibility(GONE);
            }
        } else {
            ivRight.setVisibility(GONE);
        }

        tvText.setText(text);
    }


    public void setRightText(String text) {
        tvRightText.setVisibility(VISIBLE);
        tvRightText.setText(text);
    }

    public void setShowRightText() {
        tvRightText.setVisibility(GONE);
    }


    public void setLeftText(String text) {
        tvText.setText(text);
    }
}

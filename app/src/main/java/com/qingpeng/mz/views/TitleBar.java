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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingpeng.mz.R;
import com.qingpeng.mz.utils.StringUtils;


public class TitleBar extends RelativeLayout {
    private LinearLayout llCenter;
    private LinearLayout llRight;
    private LinearLayout llLeft;
    private ImageView ivLeft;
    private ImageView ivRight;
    private ImageView ivCenter;
    private TextView tvLeft;
    private TextView tvRight;
    private TextView tvCenter;
    private Context mContext;
    int textRightColor;
    int textLeftColor;

    public TitleBar(@NonNull Context context) {
        super(context);
        mContext = context;
        init(context, null);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context, attrs);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View inflate = View.inflate(context, R.layout.widget_title_bar, this);

        llCenter = (LinearLayout) inflate.findViewById(R.id.ll_center);
        llRight = (LinearLayout) inflate.findViewById(R.id.ll_right);
        llLeft = (LinearLayout) inflate.findViewById(R.id.ll_left);
        tvCenter = (TextView) inflate.findViewById(R.id.tv_center);
        tvRight = (TextView) inflate.findViewById(R.id.tv_right);
        tvLeft = (TextView) inflate.findViewById(R.id.tv_left);
        ivCenter = (ImageView) inflate.findViewById(R.id.iv_center);
        ivRight = (ImageView) inflate.findViewById(R.id.iv_right);
        ivLeft = (ImageView) inflate.findViewById(R.id.iv_left);

//        setBackgroundColor(context.getResources().getColor(R.color.c_fed943));
        tvCenter.setTextColor(getResources().getColor(R.color.c_ffffff));
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        String textTitle = a.getString(R.styleable.TitleBar_textTitle);
        String textRight = a.getString(R.styleable.TitleBar_textRight);
        String textLeft = a.getString(R.styleable.TitleBar_textLeft);

        int textTitleSrc = a.getResourceId(R.styleable.TitleBar_textTitleSrc, 0);
        int textRightSrc = a.getResourceId(R.styleable.TitleBar_textRightSrc, 0);
        int textLeftSrc = a.getResourceId(R.styleable.TitleBar_textLeftSrc, 0);

        boolean showToastTitleSrc = a.getBoolean(R.styleable.TitleBar_showToastTitleSrc, false);
        boolean showToastRightSrc = a.getBoolean(R.styleable.TitleBar_showToastRightSrc, false);
        boolean showToastLeftSrc = a.getBoolean(R.styleable.TitleBar_showToastLeftSrc, false);

        int textTitleColor = a.getColor(R.styleable.TitleBar_textTitleColor, 0);
        textRightColor = a.getColor(R.styleable.TitleBar_textRightColor, 0);
        textLeftColor = a.getColor(R.styleable.TitleBar_textLeftColor, 0);

        if (StringUtils.isEmpty(textTitle) && textTitleSrc == 0) {
            llCenter.setVisibility(GONE);
        } else {
            if (!StringUtils.isEmpty(textTitle)) {
                tvCenter.setText(textTitle);
                if (textTitleColor != 0)
                    tvCenter.setTextColor(textTitleColor);
            } else {
                tvCenter.setVisibility(GONE);
            }
            if (textTitleSrc != 0) {
                ivCenter.setImageResource(textTitleSrc);
            } else {
                ivCenter.setVisibility(GONE);
            }
            if (!showToastTitleSrc) {
                ivCenter.setVisibility(GONE);
            }
        }

        if (StringUtils.isEmpty(textRight) && textRightSrc == 0) {
            llRight.setVisibility(GONE);
        } else {
            if (!StringUtils.isEmpty(textRight)) {
                tvRight.setText(textRight);
                if (textTitleColor != 0)
                    tvRight.setTextColor(textTitleColor);
                if (textRightColor != 0)
                    tvRight.setTextColor(textRightColor);
            } else {
                tvRight.setVisibility(INVISIBLE);
            }
            if (textRightSrc != 0) {
                ivRight.setImageResource(textRightSrc);
            } else {
                ivRight.setVisibility(INVISIBLE);
            }
            if (!showToastRightSrc) {
                ivRight.setVisibility(INVISIBLE);
            }
        }

        if (StringUtils.isEmpty(textLeft) && textLeftSrc == 0) {
            llLeft.setVisibility(GONE);
        } else {
            if (!StringUtils.isEmpty(textLeft)) {
                tvLeft.setText(textLeft);
                if (textTitleColor != 0)
                    tvLeft.setTextColor(textTitleColor);
                if (textLeftColor != 0)
                    tvLeft.setTextColor(textLeftColor);
            } else {
                tvLeft.setVisibility(INVISIBLE);
            }
            if (textLeftSrc != 0) {
                ivLeft.setImageResource(textLeftSrc);
            } else {
                ivLeft.setVisibility(INVISIBLE);
            }
            if (!showToastLeftSrc) {
                ivLeft.setVisibility(INVISIBLE);
            }
        }
    }

    public void setLlCenterVisibility(int visibility) {
        llCenter.setVisibility(visibility);
    }

    public void setLlLeftVisibility(int visibility) {
        llLeft.setVisibility(visibility);
    }

    public void setLlRightVisibility(int visibility) {
        llRight.setVisibility(visibility);
    }

    public LinearLayout getLlLeft() {
        return llLeft;
    }

    public LinearLayout getLlRight() {
        return llRight;
    }

    public void setTitle(String title) {
        tvCenter.setText(title);
    }

    public void setLeftText(String leftText) {
        tvLeft.setText(leftText);
        tvLeft.setVisibility(VISIBLE);
        tvLeft.setTextColor(textLeftColor);
        llLeft.setVisibility(VISIBLE);
    }

    public void setRightText(String rightText) {
        tvRight.setText(rightText);
        tvRight.setVisibility(VISIBLE);
        tvRight.setTextColor(textRightColor);
        llRight.setVisibility(VISIBLE);
    }

//    public void showTopRightMenu(final Activity activity) {
//        TopRightMenu mTopRightMenu = new TopRightMenu(activity);
//
////添加菜单项
//        List<MenuItem> menuItems = new ArrayList<>();
//        menuItems.add(new MenuItem(R.mipmap.ic_qunliao, "发起群聊"));
//        menuItems.add(new MenuItem(R.mipmap.ic_jiaren, "添加朋友"));
//        menuItems.add(new MenuItem(R.mipmap.ic_x, "扫一扫"));
//        int w = activity.getWindowManager().getDefaultDisplay().getWidth();
//        mTopRightMenu
//                .setHeight(LayoutParams.WRAP_CONTENT)     //默认高度480
//                .setWidth(w / 3)      //默认宽度wrap_content
//                .showIcon(true)     //显示菜单图标，默认为true
//                .dimBackground(true)        //背景变暗，默认为true
//                .needAnimationStyle(true)   //显示动画，默认为true
//                .setAnimationStyle(R.style.TRM_ANIM_STYLE)
//                .addMenuList(menuItems)
//                .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
//                    @Override
//                    public void onMenuItemClick(int position) {
//                        switch (position) {
//                            case 0:
//                                mContext.startActivity(new Intent(mContext, AddGroupActivity.class));
//                                break;
//                            case 1:
//                                mContext.startActivity(new Intent(mContext, SearchActivity.class));
//                                break;
//                            case 2:
//                                mContext.startActivity(new Intent(mContext, SaoActivity.class));
//                                break;
//                        }
//                    }
//                })
//                .showAsDropDown(llRight, -30, 0);    //带偏移量
//    }
}

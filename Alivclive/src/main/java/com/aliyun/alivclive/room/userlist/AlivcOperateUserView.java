package com.aliyun.alivclive.room.userlist;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.lang.reflect.Type;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.aliyun.alivclive.R;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author Mulberry
 *         create on 2018/5/3.
 */

public class AlivcOperateUserView extends Dialog implements OnClickListener {

    /**
     * 是否显示操作用户的界面
     */
    private static final String KEY_IS_SHOW_OPERATE_USER = "isShowOperateUser";
    private static final String KEY_IS_FORBID_USER= "isForbidUser";
    /**
     * 传递用户信息的KEY
     */
    private static final String KEY_USER_INFO = "alivcUserInfo";

    private WeakReference<Context> context;
    private ImageView ivUserAvatar;
    private TextView tvUserName;
    private TextView tvUserId;
    private Button btnForbidUser;
    private Button btnKickoutUser;
    private LinearLayout alivcLayoutOperateUser;

    /**
     * 禁言和非禁言状态，true为禁言状态
     */
    private boolean isForbidUser;

    /**
     * 用户信息
     */
    private AlivcLiveUserInfo alivcLiveUserInfo;
    /**
     * 是否显示操作用户
     */
    private Boolean isShowOperateUser;
    /**
     * 传递信息的bundle
     */
    private Bundle mArguments;


    public static AlivcOperateUserView newInstance(Context context,AlivcLiveUserInfo alivcLiveUserInfo,boolean isShowOperateUser,boolean isForbidUser) {
        AlivcOperateUserView dialog = new AlivcOperateUserView(context);
        Bundle b = new Bundle();
        b.putSerializable(KEY_USER_INFO, alivcLiveUserInfo);
        b.putBoolean(KEY_IS_SHOW_OPERATE_USER,isShowOperateUser);
        b.putBoolean(KEY_IS_FORBID_USER,isForbidUser);
        dialog.setArguments(b);
        return dialog;
    }

    private void setArguments(Bundle bundle) {
        mArguments = bundle;
    }

    public Bundle getArguments() {
        return mArguments;
    }

    public AlivcOperateUserView(@NonNull Context context) {
        super(context);
        this.context = new WeakReference<Context>(context);
    }

    public AlivcOperateUserView(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = new WeakReference<Context>(context);
    }

    public AlivcOperateUserView(@NonNull Context context, boolean cancelable,
                                @Nullable
                                    OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = new WeakReference<Context>(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alivc_operate_user_view_dialog);

        alivcLiveUserInfo = (AlivcLiveUserInfo) getArguments().getSerializable(KEY_USER_INFO);
        isShowOperateUser = getArguments().getBoolean(KEY_IS_SHOW_OPERATE_USER);
        isForbidUser = getArguments().getBoolean(KEY_IS_FORBID_USER);

        ivUserAvatar = (ImageView) findViewById(R.id.iv_user_avatar);
        tvUserName  = (TextView) findViewById(R.id.tv_user_name);
        tvUserId = (TextView) findViewById(R.id.tv_userid);
        btnForbidUser = (Button) findViewById(R.id.btn_forbid_user);
        btnKickoutUser = (Button)findViewById(R.id.btn_kickout_User);
        alivcLayoutOperateUser = (LinearLayout)findViewById(R.id.alivc_layout_operate_user);

        tvUserId.setText(String.valueOf(alivcLiveUserInfo.getUserId()));

        setBtnForbidUser();
        btnForbidUser.setOnClickListener(this);
        btnKickoutUser.setOnClickListener(this);

        Glide.with(this.context.get())
            .load(R.mipmap.icon_avatar_default)
            .centerCrop()
            .placeholder(R.color.alivc_color_player_colorAccent)
            .crossFade()
            .into(ivUserAvatar);
    }

    @Override
    public void show() {
        super.show();
    }

    private void setBtnForbidUser(){
        if (!isShowOperateUser){
            alivcLayoutOperateUser.setVisibility(View.GONE);
            return;
        }

        if (isForbidUser){
            btnForbidUser.setText(context.get().getResources().getString(R.string.alivc_operate_allow_user));
        }else {
            btnForbidUser.setText(context.get().getResources().getString(R.string.alivc_operate_forbid_user));
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_forbid_user){
            onUserOperateListener.onForbidUserSendMessage(isForbidUser,alivcLiveUserInfo.getUserId());
            dismiss();
        }else if(v.getId() == R.id.btn_kickout_User){
            onUserOperateListener.onKickOutUser(alivcLiveUserInfo.getUserId());
            dismiss();
        }
    }

    OnUserOperateListener onUserOperateListener;
    public void setOnUserOperateListener(
        OnUserOperateListener onUserOperateListener) {
        this.onUserOperateListener = onUserOperateListener;
    }

    public interface OnUserOperateListener {

        /**
         *
         * @param isForbidUser
         * @param userid
         */
        void onForbidUserSendMessage(boolean isForbidUser,String userid);

        /**
         * 踢人
         * @param userid
         */
        void onKickOutUser(String userid);
    }
}

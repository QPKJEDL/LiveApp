package com.aliyun.alivclive.setting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.alivclive.R;
import com.aliyun.alivclive.utils.http.AlivcHttpManager;
import com.aliyun.alivclive.setting.manager.AlivcLiveUserManager;
import com.aliyun.alivclive.room.userlist.AlivcLiveUserInfo;
import com.aliyun.alivclive.utils.http.AlivcStsManager;
import com.aliyun.alivclive.utils.http.HttpEngine;
import com.aliyun.alivclive.utils.http.HttpResponse;
import com.aliyun.alivclive.utils.ImageUtils;
import com.aliyun.alivclive.utils.NetUtils;

import java.util.Random;

/**
 * Created by Akira on 2018/5/27.
 * <p>
 * UserInfo Activity
 */

public class AlivcUserSettingActivity extends Activity implements View.OnClickListener {

    private TextView mTvUsername, mTvUserId, mTvTitle;
    private ImageView mIvAvatar, mIvBack, mIvChangeAvatar, mIvAvatarBg;

    private AlivcLiveUserInfo mUserInfo = new AlivcLiveUserInfo();

    private int[] mAvatarBgs = {R.drawable.user_avatar_bg_01, R.drawable.user_avatar_bg_02,
            R.drawable.user_avatar_bg_03, R.drawable.user_avatar_bg_04};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        mTvUsername = findViewById(R.id.tv_username);
        mTvUserId = findViewById(R.id.tv_user_id);
        mIvAvatar = findViewById(R.id.iv_user_avatar);
        mIvAvatarBg = findViewById(R.id.iv_user_avatar_bg);

        View actionBar = findViewById(R.id.action_bar);

        mTvTitle = actionBar.findViewById(R.id.titlebar_title);
        mTvTitle.setText(R.string.alivc_user_setting);
        mIvBack = actionBar.findViewById(R.id.titlebar_back);

        mIvBack.setOnClickListener(this);
        findViewById(R.id.iv_change_avatar).setOnClickListener(this);
        findViewById(R.id.modify_userinfo).setOnClickListener(this);
        findViewById(R.id.tv_change_user).setOnClickListener(this);

        mUserInfo = AlivcLiveUserManager.getInstance().getUserInfo(getApplicationContext());
        if (mUserInfo != null) {
            updateUserView();
        } else {
            newGuest();
        }

    }

    private void updateUserView() {
        if (TextUtils.isEmpty(mUserInfo.getRoomId())) {
            mTvUserId.setVisibility(View.GONE);
        } else {
            mTvUserId.setText(String.format("ID:%s", mUserInfo.getUserId()));
        }
        mTvUsername.setText(mUserInfo.getNickName());
        mIvAvatarBg.setImageResource(mAvatarBgs[new Random().nextInt(mAvatarBgs.length)]);
        ImageUtils.loadCircleImage(this, mUserInfo.getAvatar(), mIvAvatar);
    }

    private void newGuest() {
        AlivcHttpManager.getInstance().newGuest(new HttpEngine.OnResponseCallback<HttpResponse.User>() {
            @Override
            public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.User data) {
                if (result) {
                    if (data != null) {
                        //todo AlivcUserInfoManager
                        AlivcLiveUserInfo userInfo = data.getData();
                        if (userInfo != null) {
                            mUserInfo = userInfo;
                            updateUserView();
                            AlivcLiveUserManager.getInstance().setUserInfo(AlivcUserSettingActivity.this, userInfo);
                            AlivcStsManager.getInstance().refreshStsToken(userInfo.getUserId());
                            Toast.makeText(AlivcUserSettingActivity.this, getText(R.string.alivc_user_switch_success), Toast.LENGTH_LONG).show();

                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mIvBack.getId()) {
            finish();
        } else if (id == R.id.iv_change_avatar) {
//            UserCardDialogFragment fragment = new UserCardDialogFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("avatarUrl", "http://11.165.218.233:80/heads/03.png");
//            bundle.putString("username", "霹雳小柴柴");
//            bundle.putString("userId", "12345667");
//            bundle.putBoolean("isArchor", true);
//            fragment.setArguments(bundle);
//            fragment.show(getFragmentManager(), "");

        } else if (id == R.id.modify_userinfo) {
            if (!NetUtils.isNetworkConnected(AlivcUserSettingActivity.this)) {
                Toast.makeText(AlivcUserSettingActivity.this, "No network connection", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(mUserInfo.getUserId())) {
                return;
            }

            Intent intent = new Intent();
            intent.putExtra("userId", mUserInfo.getUserId());
            intent.putExtra("nickname", mUserInfo.getNickName());
            intent.setClass(AlivcUserSettingActivity.this, AlivcModifyUserInfoActivity.class);
            startActivityForResult(intent, 1);
        } else if (id == R.id.tv_change_user) {
            if (!NetUtils.isNetworkConnected(AlivcUserSettingActivity.this)) {
                Toast.makeText(AlivcUserSettingActivity.this, "No network connection", Toast.LENGTH_SHORT).show();
                return;
            }

            newGuest();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (data != null) {
                String username = data.getStringExtra("username");
                if (!TextUtils.isEmpty(username)) {
                    mUserInfo.setNickName(username);
                    mTvUsername.setText(username);
                }
            }
        }
    }
}

package com.aliyun.alivclive.setting.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.alivclive.R;
import com.aliyun.alivclive.utils.http.AlivcHttpManager;
import com.aliyun.alivclive.setting.manager.AlivcLiveUserManager;
import com.aliyun.alivclive.room.userlist.AlivcLiveUserInfo;
import com.aliyun.alivclive.utils.http.HttpEngine;
import com.aliyun.alivclive.utils.http.HttpResponse;
import com.aliyun.alivclive.utils.LogUtils;
import com.aliyun.alivclive.utils.NetUtils;

/**
 * Created by Akira on 2018/5/28.
 */

public class AlivcModifyUserInfoActivity extends Activity implements View.OnClickListener {

    private final String TAG = "AlivcModifyUserInfoActivity";
    private TextView mTvTitle, mTvRight;
    private EditText mEtNickname;
    private ImageView mIvBack;

    private String mUserId, mUserName;

    private final int maxLen = 16;
    private InputFilter mInputFilter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            int dindex = 0;
            int count = 0;

            while (count <= maxLen && dindex < dest.length()) {
                char c = dest.charAt(dindex++);
                if (c < 128) {
                    count = count + 1;
                } else {
                    count = count + 2;
                }
            }

            if (count > maxLen) {
                return dest.subSequence(0, dindex - 1);
            }

            int sindex = 0;
            while (count <= maxLen && sindex < source.length()) {
                char c = source.charAt(sindex++);
                if (c < 128) {
                    count = count + 1;
                } else {
                    count = count + 2;
                }
            }

            if (count > maxLen) {
                sindex--;
            }

            return source.subSequence(0, sindex);
        }


    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_userinfo);
        Intent intent = getIntent();
        mUserId = intent.getStringExtra("userId");
        mUserName = intent.getStringExtra("nickname");

        View actionBar = findViewById(R.id.action_bar);

        mTvTitle = actionBar.findViewById(R.id.titlebar_title);
        mTvTitle.setVisibility(View.GONE);
        mIvBack = actionBar.findViewById(R.id.titlebar_back);
        mTvRight = actionBar.findViewById(R.id.titlebar_right);
        mTvRight.setVisibility(View.VISIBLE);

        mIvBack.setOnClickListener(this);
        mTvRight.setOnClickListener(this);

        mEtNickname = findViewById(R.id.et_nickname);
        mEtNickname.setFilters(new InputFilter[]{mInputFilter});
        mEtNickname.setText(mUserName);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == mIvBack.getId()) {
            finish();
        } else if (viewId == mTvRight.getId()) {
            if (!NetUtils.isNetworkConnected(AlivcModifyUserInfoActivity.this)) {
                Toast.makeText(AlivcModifyUserInfoActivity.this, "No network connection", Toast.LENGTH_SHORT).show();
            }

            final String nickname = mEtNickname.getText().toString();
            if (TextUtils.isEmpty(nickname)) {
                Toast.makeText(AlivcModifyUserInfoActivity.this, "Nickname is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (nickname.contentEquals("\n") || nickname.startsWith(" ")
                    || nickname.endsWith(" ") || TextUtils.isEmpty(nickname.trim())) {
                Toast.makeText(AlivcModifyUserInfoActivity.this, "illegal text", Toast.LENGTH_SHORT).show();
                return;
            }

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            boolean isOpen = imm.isActive();
            if (isOpen) {
                imm.hideSoftInputFromWindow(mEtNickname.getWindowToken(), 0);
            }

            AlivcHttpManager.getInstance().updateUser(mUserId, nickname, new HttpEngine.OnResponseCallback<HttpResponse.User>() {
                @Override
                public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.User data) {
                    if (result) {
                        Toast.makeText(AlivcModifyUserInfoActivity.this, "Success!!!", Toast.LENGTH_SHORT).show();
                        if (data != null) {
                            //todo AlivcUserInfoManager
                            AlivcLiveUserInfo userInfo = data.getData();
                            if (userInfo != null) {
                                LogUtils.d(TAG, "new guest info = " + userInfo.toString());
                                AlivcLiveUserManager.getInstance().setUserInfo(getApplicationContext(), userInfo);
                            }
                        }

                        Intent intent = new Intent();
                        intent.putExtra("username", nickname);
                        setResult(1, intent);
                        finish();
                    } else {

                    }
                }
            });
        }


    }
}

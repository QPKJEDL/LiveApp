package com.aliyun.alivclive.setting.manager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.aliyun.alivclive.room.userlist.AlivcLiveUserInfo;
import com.aliyun.alivclive.utils.http.HttpEngine;
import com.aliyun.alivclive.utils.http.HttpResponse;
import com.aliyun.alivclive.utils.LogUtils;

import com.aliyun.alivclive.utils.http.AlivcHttpManager;
import com.aliyun.alivclive.utils.http.AlivcStsManager;
import com.aliyun.pusher.core.utils.SharedPreferenceUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Akira on 2018/5/28.
 */

public class AlivcLiveUserManager {

    private final String TAG = "AlivcLiveUserManager";

    private static AlivcLiveUserManager instance;

    private AlivcLiveUserInfo mUserInfo;

    public static AlivcLiveUserManager getInstance() {
        if (instance == null) {
            synchronized (AlivcLiveUserManager.class) {
                if (instance == null) {
                    instance = new AlivcLiveUserManager();
                }
            }
        }
        return instance;
    }

    public void init(final Context context) {
        if (mUserInfo == null) {
            String dataString = SharedPreferenceUtils.getUser(context);
            if (!TextUtils.isEmpty(dataString)) {
                mUserInfo = new Gson().fromJson(dataString, new TypeToken<AlivcLiveUserInfo>() {
                }.getType());
            }

            if (mUserInfo == null) {
                newUser(context);
            } else {
                AlivcStsManager.getInstance().refreshStsToken(mUserInfo.getUserId());
                getUserDetail(context, mUserInfo.getUserId());
            }
        } else {
            AlivcStsManager.getInstance().refreshStsToken(mUserInfo.getUserId());
            getUserDetail(context, mUserInfo.getUserId());
        }
    }

    public void newUser(final Context context) {
        AlivcHttpManager.getInstance().newGuest(new HttpEngine.OnResponseCallback<HttpResponse.User>() {
            @Override
            public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.User data) {
                if (result) {
                    if (data != null) {
                        //todo AlivcUserInfoManager
                        AlivcLiveUserInfo userInfo = data.getData();
                        if (userInfo != null) {
                            LogUtils.d(TAG, "new guest info = " + userInfo.toString());
                            AlivcStsManager.getInstance().refreshStsToken(userInfo.getUserId());

                            getUserDetail(context, userInfo.getUserId());
                            setUserInfo(context, userInfo);
                        }
                    }
                } else {
                    LogUtils.d(TAG, "new guest request is failure");
                }
            }
        });
    }


    private void getUserDetail(final Context context, String uid) {
        AlivcHttpManager.getInstance().getUserDetail(uid, new HttpEngine.OnResponseCallback<HttpResponse.User>() {
            @Override
            public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.User data) {
                if (data != null) {
                    AlivcLiveUserInfo userInfo = data.getData();
                    if (userInfo != null) {
                        mUserInfo = userInfo;
                        AlivcLiveUserManager.getInstance().setUserInfo(context, userInfo);
                    }
                }
            }
        });
    }

    public AlivcLiveUserInfo getUserInfo(Context context) {
        if (mUserInfo == null) {
            String dataString = SharedPreferenceUtils.getUser(context);
            if (!TextUtils.isEmpty(dataString)) {
                mUserInfo = new Gson().fromJson(dataString, new TypeToken<AlivcLiveUserInfo>() {
                }.getType());
            }
        }
        if (mUserInfo != null) {
            LogUtils.d(TAG, "get user info = " + mUserInfo.toString());
        }
        return mUserInfo;
    }


    public void setUserInfo(Context context, AlivcLiveUserInfo userInfo) {
        if (mUserInfo == null) {
            mUserInfo = new AlivcLiveUserInfo();
        }
        mUserInfo.setUserId(userInfo.getUserId());
        mUserInfo.setNickName(userInfo.getNickName());
        mUserInfo.setAvatar(userInfo.getAvatar());
        mUserInfo.setRoomId(userInfo.getRoomId());
        String userString = new Gson().toJson(mUserInfo);
        SharedPreferenceUtils.setUser(context, userString);
    }
}

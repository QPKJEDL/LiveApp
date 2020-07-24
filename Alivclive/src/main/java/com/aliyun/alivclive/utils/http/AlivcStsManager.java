package com.aliyun.alivclive.utils.http;


import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alivc.auth.AlivcAuthManager;
import com.alivc.auth.AlivcSts;
import com.alivc.auth.AlivcToken;
import com.alivc.auth.AlivcTokenListener;
import com.aliyun.alivclive.listener.IListener;
import com.aliyun.alivclive.listener.IStsTokenListener;
import com.aliyun.alivclive.listener.ITokenListener;
import com.aliyun.alivclive.utils.LogUtils;
import com.aliyun.alivclive.setting.manager.AlivcLiveUserManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akira on 2018/6/2.
 */

public class AlivcStsManager {
    private final String TAG = "AlivcStsManager";

    private static AlivcStsManager instance;

    private AlivcSts mStsTokenInfo = new AlivcSts();

    private String mUserId;

    private List<IStsTokenListener> mListeners = new ArrayList<>();

    public static AlivcStsManager getInstance() {
        if (instance == null) {
            synchronized (AlivcStsManager.class) {
                if (instance == null) {
                    instance = new AlivcStsManager();
                }
            }
        }
        return instance;
    }

    public void registerStsListener(IStsTokenListener listener) {
        mListeners.add(listener);
    }

    public void unregisterStsListener(IStsTokenListener listener) {
        mListeners.remove(listener);
    }

    public AlivcSts getStsToken() {
        return mStsTokenInfo;
    }

    public void refreshStsToken(final String userId) {
        mUserId = userId;
        AlivcHttpManager.getInstance().newSts(userId, new HttpEngine.OnResponseCallback<HttpResponse.StsTokenBean>() {
            @Override
            public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.StsTokenBean data) {
                if (result) {
                    if (data != null && data.data != null && data.data.SecurityTokenInfo != null) {

                        if (mStsTokenInfo != null) {
                            mStsTokenInfo = null;
                            mStsTokenInfo = new AlivcSts();
                        }
                        mStsTokenInfo.setStsAccessKey(data.data.SecurityTokenInfo.AccessKeyId);
                        mStsTokenInfo.setStsSecretKey(data.data.SecurityTokenInfo.AccessKeySecret);
                        mStsTokenInfo.setStsExpireTime(data.data.SecurityTokenInfo.Expiration);
                        mStsTokenInfo.setStsSecurityToken(data.data.SecurityTokenInfo.SecurityToken);

//                        boolean debug = true;
//                        if (debug) {
//                            long dueTime = AlivcPublicParams.getDateFromUTCString(mStsTokenInfo.Expiration).getTime();
//
//                            dueTime -= 3300;
//                            mStsTokenInfo.Expiration = AlivcPublicParams.generateUTCString(dueTime);
//                            LogUtils.d(TAG, "alivc live refresh due Expiration = " + mStsTokenInfo.Expiration);
//                        }

                        //todo ! subscribe !
                        if (isValid()) {
                            LogUtils.d(TAG, "alivc live refresh sts succes info = " + mStsTokenInfo.toString());
                            subsribeToken();
                            mRefreshTokenListener.onSucces();
                            notifyStsTokenRefresh(mStsTokenInfo);
                        } else {
                            LogUtils.e(TAG, "refresh sts failure");
                            mRefreshTokenListener.onFailure();
                        }

                    }
                } else {
                    LogUtils.e(TAG, "refresh sts failure");
                    mRefreshTokenListener.onFailure();
                }
            }
        });
    }

    private void notifyStsTokenRefresh(AlivcSts sts) {
        LogUtils.d(TAG, "alivc live refresh sts notify update STS~~~");
        if (mListeners.size() > 0) {
            for (IStsTokenListener listener : mListeners) {
                if (listener != null) {
                    listener.onTokenRefresh(sts);
                }
            }
        }
    }

    public boolean isValid() {
        if (mStsTokenInfo == null || TextUtils.isEmpty(mStsTokenInfo.getStsAccessKey())
                || TextUtils.isEmpty(mStsTokenInfo.getStsSecurityToken())
                || TextUtils.isEmpty(mStsTokenInfo.getStsSecretKey())) {
            return false;
        }
        return true;
    }

    public void checkSts(Context context) {
        LogUtils.d(TAG, "sts remain time = " + mStsTokenInfo.getRemainDuration());
        if (mStsTokenInfo.getRemainDuration() < 300000) {
            if(TextUtils.isEmpty(mUserId)) {
                mUserId = AlivcLiveUserManager.getInstance().getUserInfo(context).getUserId();
            }
            refreshStsToken(mUserId);
        }
    }

    private ITokenListener mTokenListener = new ITokenListener() {
        @Override
        public void onNotifyRefreshToken() {
            refreshStsToken(mUserId);
        }
    };

    private IListener mRefreshTokenListener = new IListener() {
        @Override
        public void onSucces() {

        }

        @Override
        public void onFailure() {

        }
    };

    private AlivcTokenListener mAlivcTokenListener = new AlivcTokenListener() {
        @Override
        public void onStsTokenExpired(AlivcSts alivcSts) {

        }

        @Override
        public void onStsTokenCloseExpire(AlivcSts alivcSts, long l) {
            LogUtils.d(TAG, "alivc live onStsTokenCloseExpire");
            mTokenListener.onNotifyRefreshToken();
        }

        @Override
        public void onStsTokenExpired(AlivcToken alivcToken) {

        }

        @Override
        public void onStsTokenCloseExpire(AlivcToken alivcToken, long l) {

        }
    };

    public void subsribeToken() {
        if (!isValid()) {
            return;
        }
        try {
            LogUtils.d(TAG, "alivc subscribe sts");
            AlivcAuthManager.getInstance().subscribe(mStsTokenInfo, 300000, 1, mAlivcTokenListener);
        } catch (Exception e) {
            LogUtils.d(TAG, "alivc subscribe sts error");
        }

    }
}

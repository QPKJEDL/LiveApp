package com.aliyun.alivclive.setting.manager;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;

import com.alivc.im.AlivcUserInfo;
import com.alivc.live.room.constants.AlivcLiveRole;
import com.aliyun.alivclive.room.userlist.AlivcLiveUserInfo;
import com.aliyun.alivclive.utils.http.HttpEngine;
import com.aliyun.pusher.core.utils.SharedPreferenceUtils;


/**
 * 用户信息管理类
 *
 * @author Mulberry
 *         create on 2018/4/19.
 */
//TODO 暂时没发现怎么用 之后考虑跟 AlivcLiveUserManager 合并
public class AlivcUserInfoManager {


    protected HttpEngine mHttpEngine;

    private AlivcUserInfoManager() {
    }

    static AlivcUserInfoManager alivcUserInfoManager = null;

    static public AlivcUserInfoManager getInstance() {
        if (alivcUserInfoManager != null) {
            return alivcUserInfoManager;
        }
        synchronized (AlivcUserInfoManager.class) {
            alivcUserInfoManager = new AlivcUserInfoManager();
            return alivcUserInfoManager;
        }
    }

    /**
     * 获取STS信息
     * 如果已经获取过从缓存中获取
     *
     * @return
     */
    public void getStsToken(final Callback callback) {
//        AlivcStsToken alivcRoomUserInfo =new AlivcStsToken();
//        mHttpEngine.newSts("", new OnResponseCallback<HttpResponse.AlivcStsToken>() {
//            @Override
//            public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.Sts data) {
//                if (result == true){
//                    callback.onSuccess(data);
//                }else {
//                    callback.onFailure(retmsg);
//                }
//            }
//        });

    }

    public AlivcLiveUserInfo getCurrentUserInfo(Context context, AlivcLiveRole alivcLiveRole) {
        if (alivcLiveRole.getLivingRoleName().equals(AlivcLiveRole.ROLE_HOST.getLivingRoleName().toString())) {
            return getHostUser(context);
        } else {
            return getAudienceUser(context);
        }
    }

    /**
     * 获取自己的账户信息
     * 如果已经获取过从缓存中获取
     *
     * @return
     */
    public AlivcLiveUserInfo getAudienceUser(Context context) {

        AlivcLiveUserInfo alivcUserInfo = new AlivcLiveUserInfo();
        int randNumber = new Random().nextInt(10000000 - 100000 + 1) + 100000;

        int userId = SharedPreferenceUtils.getAudienceUser(context);
        if (userId == -1) {
            SharedPreferenceUtils.setAudienceUser(context, randNumber);
            alivcUserInfo.setUserId(String.valueOf(randNumber));
            alivcUserInfo.setNickName("testname" + String.valueOf(randNumber));
        } else {
            alivcUserInfo.setUserId(String.valueOf(userId));
            alivcUserInfo.setNickName("testname" + String.valueOf(userId));
        }

        return alivcUserInfo;
    }

    /**
     * 获取主播客户
     *
     * @return
     */
    public AlivcLiveUserInfo getHostUser(Context context) {
        AlivcLiveUserInfo alivcLiveUserInfo = new AlivcLiveUserInfo();
        int userId = SharedPreferenceUtils.getHostUser(context);

        if (userId == -1) {
            int randNumber = new Random().nextInt(10000000 - 100000 + 1) + 100000;
            SharedPreferenceUtils.setHostUser(context, randNumber);
            alivcLiveUserInfo.setUserId(String.valueOf(randNumber));
            alivcLiveUserInfo.setNickName("testname" + String.valueOf(randNumber));
        } else {
            alivcLiveUserInfo.setUserId(String.valueOf(userId));
            alivcLiveUserInfo.setNickName("testname" + String.valueOf(userId));
        }
        return alivcLiveUserInfo;
    }

    /**
     * 根据用户id获取用户信息,主要用于
     *
     * @return
     */
    public AlivcUserInfo getUserInfoByUserId() {
        AlivcUserInfo alivcUserInfo = new AlivcUserInfo();
        return alivcUserInfo;
    }

    /**
     * 根据房间id获取用户信息列表
     *
     * @param roomId
     * @return
     */
    public ArrayList<AlivcUserInfo> getUserInfoListByRoomId(String roomId) {
        ArrayList<AlivcUserInfo> userInfos = new ArrayList<>();
        return userInfos;
    }

    public interface Callback<T> {

        /**
         * 成功
         */
        void onSuccess(T data);

        /**
         * 失败
         *
         * @param msg 错误信息
         */
        void onFailure(final String msg);

    }

}

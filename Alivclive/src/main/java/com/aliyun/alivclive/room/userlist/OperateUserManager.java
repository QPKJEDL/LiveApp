package com.aliyun.alivclive.room.userlist;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;

import com.aliyun.pusher.core.utils.SharedPreferenceUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 用户操作管理
 * @author Mulberry
 *         create on 2018/5/19.
 */

public class OperateUserManager {

    private static class  OperateUserManagerHolder{
        private static  OperateUserManager  instance = new OperateUserManager();
    }

    public static OperateUserManager getInstance() {
        return OperateUserManagerHolder.instance;
    }

    /**
     * 获取已经禁言的用户列表
     * @param context
     * @return
     */
    public ArrayList<String> getForbidUser(Context context){
        Type listType = new TypeToken<ArrayList<String>>(){}.getType();

       ArrayList<String> forbidusers = new Gson().fromJson(SharedPreferenceUtils.getForbidUser(context),listType);
       return forbidusers;
    }

    /**
     * 禁言用户
     */
    public void forbidUser(Context context,String userID){
        ArrayList<String> forbidusers = getForbidUser(context);
        if (forbidusers == null){
            forbidusers = new ArrayList<>();
        }

        forbidusers.add(userID);
        String users = new Gson().toJson(forbidusers);
        SharedPreferenceUtils.setForbidUser(context,users);
    }

    /**
     * 允许用户发言
     * @param context
     * @param userID
     */
    public void allowUser(Context context,String userID){
        ArrayList<String> forbidusers = getForbidUser(context);
        if (forbidusers == null){
            return;
        }
        forbidusers.remove(userID);
        String users = new Gson().toJson(forbidusers);
        SharedPreferenceUtils.setForbidUser(context,users);
    }

    /**
     * 是否已经被禁言
     * @param context
     * @param userId
     * @return
     */
    public boolean isForbidUser(Context context,String userId){
        ArrayList<String> forbidusers = getForbidUser(context);
        if (forbidusers == null){
            return false;
        }
        for (int i= 0;i<forbidusers.size();i++ ){
            if (forbidusers.get(i).toString().equals(userId)){
                return true;
            }
        }
        return  false;
    }

}

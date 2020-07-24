package com.aliyun.alivclive.room.userlist;

import com.alivc.auth.AlivcSts;

import java.io.Serializable;

/**
 * Created by pengshuang on 23/05/2018.
 */

public class AlivcUserInfo implements Serializable{
    public String userId;
    public String userDesp;
    public String stsAccessKey;
    public String stsSecretKey;
    public String stsExpireTime;
    public String stsSecurityToken;
}

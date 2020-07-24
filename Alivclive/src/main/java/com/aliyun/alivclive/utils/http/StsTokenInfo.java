package com.aliyun.alivclive.utils.http;

/**
 * Created by Akira on 2018/6/2.
 */

public class StsTokenInfo {
    public String AccessKeySecret;
    public String SecurityToken;
    public String Expiration;
    public String AccessKeyId;

    @Override
    public String toString() {
        return "StsTokenInfo{" +
                "AccessKeySecret='" + AccessKeySecret + '\'' +
                ", SecurityToken='" + SecurityToken + '\'' +
                ", Expiration='" + Expiration + '\'' +
                ", AccessKeyId='" + AccessKeyId + '\'' +
                '}';
    }
}

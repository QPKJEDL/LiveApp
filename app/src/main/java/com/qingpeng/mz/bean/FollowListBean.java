package com.qingpeng.mz.bean;

public class FollowListBean {


    /**
     * data : [{"avater":"","creatime":"1593330880","fans":0,"nickname":"旺仔","sign":"","status":0}]
     * info : 关注列表
     * status : 1
     */
    /**
     * avater :
     * creatime : 1593330880
     * fans : 0
     * nickname : 旺仔
     * sign :
     * status : 0
     */

    private String avater;
    private String creatime;
    private int fans;
    private String nickname;
    private String sign;
    private int status;

    public String getAvater() {
        return avater;
    }

    public void setAvater(String avater) {
        this.avater = avater;
    }

    public String getCreatime() {
        return creatime;
    }

    public void setCreatime(String creatime) {
        this.creatime = creatime;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

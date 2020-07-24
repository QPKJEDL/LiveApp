package com.qingpeng.mz.bean;

public class LoginBean {

    /**
     * data : {"token":"3f59ea0d3a3a876969659a14654536db","userid":"2"}
     * info : 登陆成功!
     * status : 1
     */


    /**
     * token : 3f59ea0d3a3a876969659a14654536db
     * userid : 2
     */

    private String token;
    private String uid;


    private String game_token;
    private String game_uid;

    private String game_url;
    private String game_wsurl;

    public String getGame_url() {
        return game_url;
    }

    public void setGame_url(String game_url) {
        this.game_url = game_url;
    }

    public String getGame_wsurl() {
        return game_wsurl;
    }

    public void setGame_wsurl(String game_wsurl) {
        this.game_wsurl = game_wsurl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGame_token() {
        return game_token;
    }

    public void setGame_token(String game_token) {
        this.game_token = game_token;
    }

    public String getGame_uid() {
        return game_uid;
    }

    public void setGame_uid(String game_uid) {
        this.game_uid = game_uid;
    }
}

package com.qingpeng.mz.bean;

public class WebUrlBean {


    /**
     * data : {"game_tcpport":23001,"game_url":"http://129.211.114.135:8210","game_wsport":13892,"game_wsurl":"129.211.114.135"}
     * info : web地址
     * status : 1
     */

    /**
     * game_tcpport : 23001
     * game_url : http://129.211.114.135:8210
     * game_wsport : 13892
     * game_wsurl : 129.211.114.135
     */

    private int game_tcpport;
    private String game_url;
    private int game_wsport;
    private String game_wsurl;

    public int getGame_tcpport() {
        return game_tcpport;
    }

    public void setGame_tcpport(int game_tcpport) {
        this.game_tcpport = game_tcpport;
    }

    public String getGame_url() {
        return game_url;
    }

    public void setGame_url(String game_url) {
        this.game_url = game_url;
    }

    public int getGame_wsport() {
        return game_wsport;
    }

    public void setGame_wsport(int game_wsport) {
        this.game_wsport = game_wsport;
    }

    public String getGame_wsurl() {
        return game_wsurl;
    }

    public void setGame_wsurl(String game_wsurl) {
        this.game_wsurl = game_wsurl;
    }

}

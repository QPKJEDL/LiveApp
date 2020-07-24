package com.qingpeng.mz.bean;

import com.google.gson.annotations.SerializedName;

public class RoomInfoBean {


    /**
     * data : [{"BetMoney":0,"BootNum":1,"BootSn":"2020061709090831","CountDown":30,"DeskId":3,"DeskName":"A5","GameId":1,"GameName":"百家乐","GameStarTime":1592703257,"LeftPlay":"http://cctvalih5ca.v.myalicdn.com/live/cctv3_2/index.m3u8","MaxLimit":5000,"MinLimit":20,"PairMaxLimit":5000,"PairMinLimit":10,"PaveNum":10,"Phase":3,"RecordSn":"202006210934173110","RightPlay":"http://cctvalih5ca.v.myalicdn.com/live/cctv3_2/index.m3u8","Systime":1592815810,"TieMaxLimit":5000,"TieMinLimit":20,"WaitDown":5,"deskPeople":0,"is_alive":"1","super":"0"}]
     * info : 房间信息
     * status : 1
     */

    /**
     * BetMoney : 0
     * BootNum : 1
     * BootSn : 2020061709090831
     * CountDown : 30
     * DeskId : 3
     * DeskName : A5
     * GameId : 1
     * GameName : 百家乐
     * GameStarTime : 1592703257
     * LeftPlay : http://cctvalih5ca.v.myalicdn.com/live/cctv3_2/index.m3u8
     * MaxLimit : 5000
     * MinLimit : 20
     * PairMaxLimit : 5000
     * PairMinLimit : 10
     * PaveNum : 10
     * Phase : 3
     * RecordSn : 202006210934173110
     * RightPlay : http://cctvalih5ca.v.myalicdn.com/live/cctv3_2/index.m3u8
     * Systime : 1592815810
     * TieMaxLimit : 5000
     * TieMinLimit : 20
     * WaitDown : 5
     * deskPeople : 0
     * is_alive : 1
     * super : 0
     */

    private int BetMoney;
    private int BootNum;
    private String BootSn;
    private int CountDown;
    private int DeskId;
    private String DeskName;
    private int GameId;
    private String GameName;
    private int GameStarTime;
    private String LeftPlay;
    private int MaxLimit;
    private int MinLimit;
    private int PairMaxLimit;
    private int PairMinLimit;
    private int PaveNum;
    private int Phase;
    private String RecordSn;
    private String RightPlay;
    private int Systime;
    private int TieMaxLimit;
    private int TieMinLimit;
    private int WaitDown;
    private int deskPeople;
    private String is_alive;
    @SerializedName("super")
    private String superX;

    public boolean isIsclick() {
        return isclick;
    }

    public void setIsclick(boolean isclick) {
        this.isclick = isclick;
    }

    private boolean isclick;

    public int getBetMoney() {
        return BetMoney;
    }

    public void setBetMoney(int BetMoney) {
        this.BetMoney = BetMoney;
    }

    public int getBootNum() {
        return BootNum;
    }

    public void setBootNum(int BootNum) {
        this.BootNum = BootNum;
    }

    public String getBootSn() {
        return BootSn;
    }

    public void setBootSn(String BootSn) {
        this.BootSn = BootSn;
    }

    public int getCountDown() {
        return CountDown;
    }

    public void setCountDown(int CountDown) {
        this.CountDown = CountDown;
    }

    public int getDeskId() {
        return DeskId;
    }

    public void setDeskId(int DeskId) {
        this.DeskId = DeskId;
    }

    public String getDeskName() {
        return DeskName;
    }

    public void setDeskName(String DeskName) {
        this.DeskName = DeskName;
    }

    public int getGameId() {
        return GameId;
    }

    public void setGameId(int GameId) {
        this.GameId = GameId;
    }

    public String getGameName() {
        return GameName;
    }

    public void setGameName(String GameName) {
        this.GameName = GameName;
    }

    public int getGameStarTime() {
        return GameStarTime;
    }

    public void setGameStarTime(int GameStarTime) {
        this.GameStarTime = GameStarTime;
    }

    public String getLeftPlay() {
        return LeftPlay;
    }

    public void setLeftPlay(String LeftPlay) {
        this.LeftPlay = LeftPlay;
    }

    public int getMaxLimit() {
        return MaxLimit;
    }

    public void setMaxLimit(int MaxLimit) {
        this.MaxLimit = MaxLimit;
    }

    public int getMinLimit() {
        return MinLimit;
    }

    public void setMinLimit(int MinLimit) {
        this.MinLimit = MinLimit;
    }

    public int getPairMaxLimit() {
        return PairMaxLimit;
    }

    public void setPairMaxLimit(int PairMaxLimit) {
        this.PairMaxLimit = PairMaxLimit;
    }

    public int getPairMinLimit() {
        return PairMinLimit;
    }

    public void setPairMinLimit(int PairMinLimit) {
        this.PairMinLimit = PairMinLimit;
    }

    public int getPaveNum() {
        return PaveNum;
    }

    public void setPaveNum(int PaveNum) {
        this.PaveNum = PaveNum;
    }

    public int getPhase() {
        return Phase;
    }

    public void setPhase(int Phase) {
        this.Phase = Phase;
    }

    public String getRecordSn() {
        return RecordSn;
    }

    public void setRecordSn(String RecordSn) {
        this.RecordSn = RecordSn;
    }

    public String getRightPlay() {
        return RightPlay;
    }

    public void setRightPlay(String RightPlay) {
        this.RightPlay = RightPlay;
    }

    public int getSystime() {
        return Systime;
    }

    public void setSystime(int Systime) {
        this.Systime = Systime;
    }

    public int getTieMaxLimit() {
        return TieMaxLimit;
    }

    public void setTieMaxLimit(int TieMaxLimit) {
        this.TieMaxLimit = TieMaxLimit;
    }

    public int getTieMinLimit() {
        return TieMinLimit;
    }

    public void setTieMinLimit(int TieMinLimit) {
        this.TieMinLimit = TieMinLimit;
    }

    public int getWaitDown() {
        return WaitDown;
    }

    public void setWaitDown(int WaitDown) {
        this.WaitDown = WaitDown;
    }

    public int getDeskPeople() {
        return deskPeople;
    }

    public void setDeskPeople(int deskPeople) {
        this.deskPeople = deskPeople;
    }

    public String getIs_alive() {
        return is_alive;
    }

    public void setIs_alive(String is_alive) {
        this.is_alive = is_alive;
    }

    public String getSuperX() {
        return superX;
    }

    public void setSuperX(String superX) {
        this.superX = superX;
    }
}

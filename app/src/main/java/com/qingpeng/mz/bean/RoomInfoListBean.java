package com.qingpeng.mz.bean;

public class RoomInfoListBean {


    /**
     * data : [{"Channel":"颜值","CoverImg":"/img","CoverName":"程序员","EndTime":1593585933,"Label":"小鲜肉sss","LiveName":"旺仔","LiveUid":100001,"RoomId":10000,"StartTime":1593673652}]
     * info : 首页大厅直播间
     * status : 1
     */
    /**
     * Channel : 颜值
     * CoverImg : /img
     * CoverName : 程序员
     * EndTime : 1593585933
     * Label : 小鲜肉sss
     * LiveName : 旺仔
     * LiveUid : 100001
     * RoomId : 10000
     * StartTime : 1593673652
     */

    private String Channel;
    private String CoverImg;
    private String CoverName;
    private int EndTime;
    private String Label;
    private String LiveName;
    private int LiveUid;
    private int RoomId;
    private int StartTime;
    private int RoomCount;

    public int getRoomCount() {
        return RoomCount;
    }

    public void setRoomCount(int roomCount) {
        RoomCount = roomCount;
    }

    public String getChannel() {
        return Channel;
    }

    public void setChannel(String Channel) {
        this.Channel = Channel;
    }

    public String getCoverImg() {
        return CoverImg;
    }

    public void setCoverImg(String CoverImg) {
        this.CoverImg = CoverImg;
    }

    public String getCoverName() {
        return CoverName;
    }

    public void setCoverName(String CoverName) {
        this.CoverName = CoverName;
    }

    public int getEndTime() {
        return EndTime;
    }

    public void setEndTime(int EndTime) {
        this.EndTime = EndTime;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String Label) {
        this.Label = Label;
    }

    public String getLiveName() {
        return LiveName;
    }

    public void setLiveName(String LiveName) {
        this.LiveName = LiveName;
    }

    public int getLiveUid() {
        return LiveUid;
    }

    public void setLiveUid(int LiveUid) {
        this.LiveUid = LiveUid;
    }

    public int getRoomId() {
        return RoomId;
    }

    public void setRoomId(int RoomId) {
        this.RoomId = RoomId;
    }

    public int getStartTime() {
        return StartTime;
    }

    public void setStartTime(int StartTime) {
        this.StartTime = StartTime;
    }

}

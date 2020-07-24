package com.qingpeng.mz.bean;

public class CoverBean {


    /**
     * data : {"list":{"RoomId":10000,"RoomCount":0,"LiveUid":100001,"LiveName":"/img","Label":"小鲜肉sss","Channel":"颜值","CoverImg":"/img","CoverName":"程序员","StartTime":1594114953,"SysTime":1594114953,"EndTime":1593585933,"Push":"rtmp://666/live/100001?txSecret=927b6a801ac52c307993792848c3acce&txTime=5F2BD089","PullRtmp":"rtmp://666/live/100001?txSecret=927b6a801ac52c307993792848c3acce&txTime=5F2BD089","PullHLS":"http://666/live/100001.m3u8?txSecret=927b6a801ac52c307993792848c3acce&txTime=5F2BD089","PullFLV":"http://666/live/100001.flv?txSecret=927b6a801ac52c307993792848c3acce&txTime=5F2BD089"}}
     * info : 开启成功
     * status : 1
     */
    /**
     * list : {"RoomId":10000,"RoomCount":0,"LiveUid":100001,"LiveName":"/img","Label":"小鲜肉sss","Channel":"颜值","CoverImg":"/img","CoverName":"程序员","StartTime":1594114953,"SysTime":1594114953,"EndTime":1593585933,"Push":"rtmp://666/live/100001?txSecret=927b6a801ac52c307993792848c3acce&txTime=5F2BD089","PullRtmp":"rtmp://666/live/100001?txSecret=927b6a801ac52c307993792848c3acce&txTime=5F2BD089","PullHLS":"http://666/live/100001.m3u8?txSecret=927b6a801ac52c307993792848c3acce&txTime=5F2BD089","PullFLV":"http://666/live/100001.flv?txSecret=927b6a801ac52c307993792848c3acce&txTime=5F2BD089"}
     */

    private ListBean list;

    public ListBean getList() {
        return list;
    }

    public void setList(ListBean list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * RoomId : 10000
         * RoomCount : 0
         * LiveUid : 100001
         * LiveName : /img
         * Label : 小鲜肉sss
         * Channel : 颜值
         * CoverImg : /img
         * CoverName : 程序员
         * StartTime : 1594114953
         * SysTime : 1594114953
         * EndTime : 1593585933
         * Push : rtmp://666/live/100001?txSecret=927b6a801ac52c307993792848c3acce&txTime=5F2BD089
         * PullRtmp : rtmp://666/live/100001?txSecret=927b6a801ac52c307993792848c3acce&txTime=5F2BD089
         * PullHLS : http://666/live/100001.m3u8?txSecret=927b6a801ac52c307993792848c3acce&txTime=5F2BD089
         * PullFLV : http://666/live/100001.flv?txSecret=927b6a801ac52c307993792848c3acce&txTime=5F2BD089
         */

        private int RoomId;
        private int RoomCount;
        private int LiveUid;
        private String LiveName;
        private String Label;
        private String Channel;
        private String CoverImg;
        private String CoverName;
        private int StartTime;
        private int SysTime;
        private int EndTime;
        private String Push;
        private String PullRtmp;
        private String PullHLS;
        private String PullFLV;

        public int getRoomId() {
            return RoomId;
        }

        public void setRoomId(int RoomId) {
            this.RoomId = RoomId;
        }

        public int getRoomCount() {
            return RoomCount;
        }

        public void setRoomCount(int RoomCount) {
            this.RoomCount = RoomCount;
        }

        public int getLiveUid() {
            return LiveUid;
        }

        public void setLiveUid(int LiveUid) {
            this.LiveUid = LiveUid;
        }

        public String getLiveName() {
            return LiveName;
        }

        public void setLiveName(String LiveName) {
            this.LiveName = LiveName;
        }

        public String getLabel() {
            return Label;
        }

        public void setLabel(String Label) {
            this.Label = Label;
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

        public int getStartTime() {
            return StartTime;
        }

        public void setStartTime(int StartTime) {
            this.StartTime = StartTime;
        }

        public int getSysTime() {
            return SysTime;
        }

        public void setSysTime(int SysTime) {
            this.SysTime = SysTime;
        }

        public int getEndTime() {
            return EndTime;
        }

        public void setEndTime(int EndTime) {
            this.EndTime = EndTime;
        }

        public String getPush() {
            return Push;
        }

        public void setPush(String Push) {
            this.Push = Push;
        }

        public String getPullRtmp() {
            return PullRtmp;
        }

        public void setPullRtmp(String PullRtmp) {
            this.PullRtmp = PullRtmp;
        }

        public String getPullHLS() {
            return PullHLS;
        }

        public void setPullHLS(String PullHLS) {
            this.PullHLS = PullHLS;
        }

        public String getPullFLV() {
            return PullFLV;
        }

        public void setPullFLV(String PullFLV) {
            this.PullFLV = PullFLV;
        }
    }
}

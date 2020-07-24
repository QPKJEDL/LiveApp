package com.qingpeng.mz.bean;

public class ZhuBoRoomInfoBean {


    /**
     * data : {"isFollowed":0,"list":{"RoomId":10001,"RoomCount":0,"LiveUid":100002,"LiveName":"小八","Label":"小鲜肉sss","Channel":"颜值","CoverImg":"/img","CoverName":"程序员","StartTime":1594621666,"SysTime":1594449533,"EndTime":0,"Push":"rtmp://666/live/100002?txSecret=f782184a1b059a0cc4150f4ed0fa3258&txTime=5F30EB7D","PullRtmp":"rtmp://666/live/100002?txSecret=f782184a1b059a0cc4150f4ed0fa3258&txTime=5F30EB7D","PullHLS":"http://666/live/100002.m3u8?txSecret=f782184a1b059a0cc4150f4ed0fa3258&txTime=5F30EB7D","PullFLV":"http://666/live/100002.flv?txSecret=f782184a1b059a0cc4150f4ed0fa3258&txTime=5F30EB7D"},"liveinfo":{"UserId":100002,"Account":17888888888,"NickName":"小八","Avater":"/img","Token":"bd6ba2796b033e5410f136b0dda85bdc","Fans":0,"Followed":0,"Sex":0,"Address":"","Sign":"","LastIp":"127.0.0.1","Talk":0,"ShenFen":0,"LevelExp":{"UserLevel":0,"UserExp":0,"LiveLevel":0,"LiveExp":0},"Bank":{"Wx":"","WxName":"","Ali":"","AliName":"","CardName":"","BankName":"","BankCard":0,"Province":"","City":"","BankAddr":""}}}
     * info : 房间信息
     * status : 1
     */

    /**
     * isFollowed : 0
     * list : {"RoomId":10001,"RoomCount":0,"LiveUid":100002,"LiveName":"小八","Label":"小鲜肉sss","Channel":"颜值","CoverImg":"/img","CoverName":"程序员","StartTime":1594621666,"SysTime":1594449533,"EndTime":0,"Push":"rtmp://666/live/100002?txSecret=f782184a1b059a0cc4150f4ed0fa3258&txTime=5F30EB7D","PullRtmp":"rtmp://666/live/100002?txSecret=f782184a1b059a0cc4150f4ed0fa3258&txTime=5F30EB7D","PullHLS":"http://666/live/100002.m3u8?txSecret=f782184a1b059a0cc4150f4ed0fa3258&txTime=5F30EB7D","PullFLV":"http://666/live/100002.flv?txSecret=f782184a1b059a0cc4150f4ed0fa3258&txTime=5F30EB7D"}
     * liveinfo : {"UserId":100002,"Account":17888888888,"NickName":"小八","Avater":"/img","Token":"bd6ba2796b033e5410f136b0dda85bdc","Fans":0,"Followed":0,"Sex":0,"Address":"","Sign":"","LastIp":"127.0.0.1","Talk":0,"ShenFen":0,"LevelExp":{"UserLevel":0,"UserExp":0,"LiveLevel":0,"LiveExp":0},"Bank":{"Wx":"","WxName":"","Ali":"","AliName":"","CardName":"","BankName":"","BankCard":0,"Province":"","City":"","BankAddr":""}}
     */

    private int isFollowed;
    private ListBean list;
    private LiveinfoBean liveinfo;

    public int getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(int isFollowed) {
        this.isFollowed = isFollowed;
    }

    public ListBean getList() {
        return list;
    }

    public void setList(ListBean list) {
        this.list = list;
    }

    public LiveinfoBean getLiveinfo() {
        return liveinfo;
    }

    public void setLiveinfo(LiveinfoBean liveinfo) {
        this.liveinfo = liveinfo;
    }

    public static class ListBean {
        /**
         * RoomId : 10001
         * RoomCount : 0
         * LiveUid : 100002
         * LiveName : 小八
         * Label : 小鲜肉sss
         * Channel : 颜值
         * CoverImg : /img
         * CoverName : 程序员
         * StartTime : 1594621666
         * SysTime : 1594449533
         * EndTime : 0
         * Push : rtmp://666/live/100002?txSecret=f782184a1b059a0cc4150f4ed0fa3258&txTime=5F30EB7D
         * PullRtmp : rtmp://666/live/100002?txSecret=f782184a1b059a0cc4150f4ed0fa3258&txTime=5F30EB7D
         * PullHLS : http://666/live/100002.m3u8?txSecret=f782184a1b059a0cc4150f4ed0fa3258&txTime=5F30EB7D
         * PullFLV : http://666/live/100002.flv?txSecret=f782184a1b059a0cc4150f4ed0fa3258&txTime=5F30EB7D
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

    public static class LiveinfoBean {
        /**
         * UserId : 100002
         * Account : 17888888888
         * NickName : 小八
         * Avater : /img
         * Token : bd6ba2796b033e5410f136b0dda85bdc
         * Fans : 0
         * Followed : 0
         * Sex : 0
         * Address :
         * Sign :
         * LastIp : 127.0.0.1
         * Talk : 0
         * ShenFen : 0
         * LevelExp : {"UserLevel":0,"UserExp":0,"LiveLevel":0,"LiveExp":0}
         * Bank : {"Wx":"","WxName":"","Ali":"","AliName":"","CardName":"","BankName":"","BankCard":0,"Province":"","City":"","BankAddr":""}
         */

        private int UserId;
        private long Account;
        private String NickName;
        private String Avater;
        private String Token;
        private int Fans;
        private int Followed;
        private int Sex;
        private String Address;
        private String Sign;
        private String LastIp;
        private int Talk;
        private int ShenFen;
        private LevelExpBean LevelExp;
        private BankBean Bank;

        public int getUserId() {
            return UserId;
        }

        public void setUserId(int UserId) {
            this.UserId = UserId;
        }

        public long getAccount() {
            return Account;
        }

        public void setAccount(long Account) {
            this.Account = Account;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String NickName) {
            this.NickName = NickName;
        }

        public String getAvater() {
            return Avater;
        }

        public void setAvater(String Avater) {
            this.Avater = Avater;
        }

        public String getToken() {
            return Token;
        }

        public void setToken(String Token) {
            this.Token = Token;
        }

        public int getFans() {
            return Fans;
        }

        public void setFans(int Fans) {
            this.Fans = Fans;
        }

        public int getFollowed() {
            return Followed;
        }

        public void setFollowed(int Followed) {
            this.Followed = Followed;
        }

        public int getSex() {
            return Sex;
        }

        public void setSex(int Sex) {
            this.Sex = Sex;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String Address) {
            this.Address = Address;
        }

        public String getSign() {
            return Sign;
        }

        public void setSign(String Sign) {
            this.Sign = Sign;
        }

        public String getLastIp() {
            return LastIp;
        }

        public void setLastIp(String LastIp) {
            this.LastIp = LastIp;
        }

        public int getTalk() {
            return Talk;
        }

        public void setTalk(int Talk) {
            this.Talk = Talk;
        }

        public int getShenFen() {
            return ShenFen;
        }

        public void setShenFen(int ShenFen) {
            this.ShenFen = ShenFen;
        }

        public LevelExpBean getLevelExp() {
            return LevelExp;
        }

        public void setLevelExp(LevelExpBean LevelExp) {
            this.LevelExp = LevelExp;
        }

        public BankBean getBank() {
            return Bank;
        }

        public void setBank(BankBean Bank) {
            this.Bank = Bank;
        }

        public static class LevelExpBean {
            /**
             * UserLevel : 0
             * UserExp : 0
             * LiveLevel : 0
             * LiveExp : 0
             */

            private int UserLevel;
            private int UserExp;
            private int LiveLevel;
            private int LiveExp;

            public int getUserLevel() {
                return UserLevel;
            }

            public void setUserLevel(int UserLevel) {
                this.UserLevel = UserLevel;
            }

            public int getUserExp() {
                return UserExp;
            }

            public void setUserExp(int UserExp) {
                this.UserExp = UserExp;
            }

            public int getLiveLevel() {
                return LiveLevel;
            }

            public void setLiveLevel(int LiveLevel) {
                this.LiveLevel = LiveLevel;
            }

            public int getLiveExp() {
                return LiveExp;
            }

            public void setLiveExp(int LiveExp) {
                this.LiveExp = LiveExp;
            }
        }

        public static class BankBean {
            /**
             * Wx :
             * WxName :
             * Ali :
             * AliName :
             * CardName :
             * BankName :
             * BankCard : 0
             * Province :
             * City :
             * BankAddr :
             */

            private String Wx;
            private String WxName;
            private String Ali;
            private String AliName;
            private String CardName;
            private String BankName;
            private int BankCard;
            private String Province;
            private String City;
            private String BankAddr;

            public String getWx() {
                return Wx;
            }

            public void setWx(String Wx) {
                this.Wx = Wx;
            }

            public String getWxName() {
                return WxName;
            }

            public void setWxName(String WxName) {
                this.WxName = WxName;
            }

            public String getAli() {
                return Ali;
            }

            public void setAli(String Ali) {
                this.Ali = Ali;
            }

            public String getAliName() {
                return AliName;
            }

            public void setAliName(String AliName) {
                this.AliName = AliName;
            }

            public String getCardName() {
                return CardName;
            }

            public void setCardName(String CardName) {
                this.CardName = CardName;
            }

            public String getBankName() {
                return BankName;
            }

            public void setBankName(String BankName) {
                this.BankName = BankName;
            }

            public int getBankCard() {
                return BankCard;
            }

            public void setBankCard(int BankCard) {
                this.BankCard = BankCard;
            }

            public String getProvince() {
                return Province;
            }

            public void setProvince(String Province) {
                this.Province = Province;
            }

            public String getCity() {
                return City;
            }

            public void setCity(String City) {
                this.City = City;
            }

            public String getBankAddr() {
                return BankAddr;
            }

            public void setBankAddr(String BankAddr) {
                this.BankAddr = BankAddr;
            }
        }
    }
}

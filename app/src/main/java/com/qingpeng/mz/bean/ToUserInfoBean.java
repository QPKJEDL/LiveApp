package com.qingpeng.mz.bean;

public class ToUserInfoBean {

    /**
     * data : {"Account":13244442222,"Address":"233","Avater":"https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3669707182,1525465329&fm=26&gp=0.jpg","Fans":2,"Followed":8,"LastIp":"192.168.0.119","LevelExp":{"UserLevel":5,"UserExp":40100,"LiveLevel":1,"LiveExp":50},"NickName":"小马甲","Sex":1,"ShenFen":1,"Sign":"233","Talk":0,"UserId":100023,"isBanUser":false,"isFollowed":0,"isManager":false}
     * info : 用户信息
     * status : 1
     */

    /**
     * Account : 13244442222
     * Address : 233
     * Avater : https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3669707182,1525465329&fm=26&gp=0.jpg
     * Fans : 2
     * Followed : 8
     * LastIp : 192.168.0.119
     * LevelExp : {"UserLevel":5,"UserExp":40100,"LiveLevel":1,"LiveExp":50}
     * NickName : 小马甲
     * Sex : 1
     * ShenFen : 1
     * Sign : 233
     * Talk : 0
     * UserId : 100023
     * isBanUser : false
     * isFollowed : 0
     * isManager : false
     */

    private long Account;
    private String Address;
    private String Avater;
    private int Fans;
    private int Followed;
    private String LastIp;
    private LevelExpBean LevelExp;
    private String NickName;
    private int Sex;
    private int ShenFen;
    private String Sign;
    private int Talk;
    private int UserId;
    private boolean isBanUser;
    private int isFollowed;
    private boolean isManager;

    public long getAccount() {
        return Account;
    }

    public void setAccount(long Account) {
        this.Account = Account;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getAvater() {
        return Avater;
    }

    public void setAvater(String Avater) {
        this.Avater = Avater;
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

    public String getLastIp() {
        return LastIp;
    }

    public void setLastIp(String LastIp) {
        this.LastIp = LastIp;
    }

    public LevelExpBean getLevelExp() {
        return LevelExp;
    }

    public void setLevelExp(LevelExpBean LevelExp) {
        this.LevelExp = LevelExp;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String NickName) {
        this.NickName = NickName;
    }

    public int getSex() {
        return Sex;
    }

    public void setSex(int Sex) {
        this.Sex = Sex;
    }

    public int getShenFen() {
        return ShenFen;
    }

    public void setShenFen(int ShenFen) {
        this.ShenFen = ShenFen;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String Sign) {
        this.Sign = Sign;
    }

    public int getTalk() {
        return Talk;
    }

    public void setTalk(int Talk) {
        this.Talk = Talk;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int UserId) {
        this.UserId = UserId;
    }

    public boolean isIsBanUser() {
        return isBanUser;
    }

    public void setIsBanUser(boolean isBanUser) {
        this.isBanUser = isBanUser;
    }

    public int getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(int isFollowed) {
        this.isFollowed = isFollowed;
    }

    public boolean isIsManager() {
        return isManager;
    }

    public void setIsManager(boolean isManager) {
        this.isManager = isManager;
    }

    public static class LevelExpBean {
        /**
         * UserLevel : 5
         * UserExp : 40100
         * LiveLevel : 1
         * LiveExp : 50
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
}


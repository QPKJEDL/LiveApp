package com.qingpeng.mz.bean;

import java.util.List;

public class RankBean {


    private List<ListBean> Rank;

    public List<ListBean> getRank() {
        return Rank;
    }

    public void setRank(List<ListBean> Rank) {
        this.Rank = Rank;
    }

    public static class ListBean {
        /**
         * UserId : 100024
         * GiftMoney : 1300190
         * Nickname : 123456
         * Avater : https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3669707182,1525465329&amp;fm=26&amp;gp=0.jpg
         */

        private int UserId;
        private int GiftMoney;
        private String Nickname;
        private String Avater;

        public int getUserId() {
            return UserId;
        }

        public void setUserId(int UserId) {
            this.UserId = UserId;
        }

        public int getGiftMoney() {
            return GiftMoney;
        }

        public void setGiftMoney(int GiftMoney) {
            this.GiftMoney = GiftMoney;
        }

        public String getNickname() {
            return Nickname;
        }

        public void setNickname(String Nickname) {
            this.Nickname = Nickname;
        }

        public String getAvater() {
            return Avater;
        }

        public void setAvater(String Avater) {
            this.Avater = Avater;
        }
    }
}

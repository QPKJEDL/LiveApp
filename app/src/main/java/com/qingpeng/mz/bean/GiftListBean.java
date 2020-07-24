package com.qingpeng.mz.bean;

import java.util.List;

public class GiftListBean {


    /**
     * data : {"list":[{"UserId":100002,"GiftMoney":100000,"Nickname":"小八","Avater":"/img"},{"UserId":100006,"GiftMoney":90000,"Nickname":"小77","Avater":"/img"}]}
     * info : 礼物排行榜
     * status : 1
     */
    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * UserId : 100002
         * GiftMoney : 100000
         * Nickname : 小八
         * Avater : /img
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

package com.qingpeng.mz.bean;

public class GiftRecordBean {

    /**
     * data : [{"creatime":"1593324089","gift_id":1,"id":1,"nickname":"小七","num":1,"user_id":100006},{"creatime":"1593324089","gift_id":1,"id":2,"nickname":"小七","num":1,"user_id":100006}]
     * info : 修改成功
     * status : 1
     */
        /**
         * creatime : 1593324089
         * gift_id : 1
         * id : 1
         * nickname : 小七
         * num : 1
         * user_id : 100006
         */

        private String creatime;
        private int gift_id;
        private int id;
        private String nickname;
        private int num;
        private int user_id;

        public String getCreatime() {
            return creatime;
        }

        public void setCreatime(String creatime) {
            this.creatime = creatime;
        }

        public int getGift_id() {
            return gift_id;
        }

        public void setGift_id(int gift_id) {
            this.gift_id = gift_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

}

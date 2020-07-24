package com.qingpeng.mz.bean;

public class UserRoomBean {


    /**
     * text : 清清浅浅
     * user : {"userid":100023,"username":"爱看书和","icon":""}
     */

    private String text;
    private UserBean user;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * userid : 100023
         * username : 爱看书和
         * icon :
         */

        private String userid;
        private String username;
        private String icon;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}

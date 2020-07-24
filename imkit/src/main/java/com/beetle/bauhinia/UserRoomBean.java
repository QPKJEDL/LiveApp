package com.beetle.bauhinia;

public class UserRoomBean {

    private String text;
    private ListBean user;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ListBean getUser() {
        return user;
    }

    public void setUser(ListBean user) {
        this.user = user;
    }

    public static class ListBean {
        private String icon;
        private String userid;
        private String username;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

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
    }
}

package com.qingpeng.mz.bean;

public class RoomGameInfoBean {


    /**
     * data : {"desk_id":"2","game_type":"3","room_id":"10000"}
     * info : 房间游戏信息
     * status : 1
     */
        /**
         * desk_id : 2
         * game_type : 3
         * room_id : 10000
         */

        private String desk_id;
        private String game_type;
        private String room_id;

        public String getDesk_id() {
            return desk_id;
        }

        public void setDesk_id(String desk_id) {
            this.desk_id = desk_id;
        }

        public String getGame_type() {
            return game_type;
        }

        public void setGame_type(String game_type) {
            this.game_type = game_type;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }
}

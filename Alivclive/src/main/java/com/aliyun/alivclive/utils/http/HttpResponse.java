package com.aliyun.alivclive.utils.http;

import java.util.List;

import com.aliyun.alivclive.room.roominfo.AlivcLiveRoomInfo;
import com.aliyun.alivclive.room.roominfo.AlivcLiveRoomInfoData;
import com.aliyun.alivclive.room.userlist.AlivcLiveUserInfo;

/**
 * Created by jac on 2017/10/30.
 */

public class HttpResponse {
    public boolean result;
    public String request_id;
    public String message;

    public static class AlivcRoomList extends HttpResponse {
        public AlivcLiveRoomInfoData data;

        @Override
        public String toString() {
            return "AlivcLiveRoomInfoData{" +
                    "data=" + data +
                    '}';
        }
    }

    public static class User extends HttpResponse {
        public AlivcLiveUserInfo data;

        public AlivcLiveUserInfo getData() {
            return data;
        }

        @Override
        public String toString() {
            return "User{" +
                    "data=" + data +
                    '}';
        }
    }

    public static class Room extends HttpResponse {
        public AlivcLiveRoomInfo data;

        @Override
        public String toString() {
            return "Room{" +
                    "data=" + data +
                    '}';
        }
    }

    public static class RoomDetail extends HttpResponse {
        public AlivcRoomDetail data;

        @Override
        public String toString() {
            return "RoomDetail{" +
                    "data=" + data +
                    '}';
        }
    }

    public static class AlivcRoomDetail extends HttpResponse {
        public AlivcLiveUserInfo streamer_info;
        public AlivcLiveRoomInfo room_info;

        @Override
        public String toString() {
            return "AlivcRoomDetail{" +
                    "streamer_info=" + streamer_info +
                    ", room_info=" + room_info +
                    '}';
        }
    }

    public static class UserList extends HttpResponse {
        public List<AlivcLiveUserInfo> data;

    }

    public static class Notification extends HttpResponse {
        public String data;

    }

    public static class StsTokenBean extends HttpResponse {
        public AlivcStsToken data;

        @Override
        public String toString() {
            return "StsTokenBean{" +
                    data +
                    '}';
        }
    }

    public static class AlivcStsToken {
        public StsTokenInfo SecurityTokenInfo;

        @Override
        public String toString() {
            return "AlivcStsToken{" +
                    SecurityTokenInfo +
                    '}';
        }
    }

}

package com.aliyun.alivclive.utils.constants;

import com.aliyun.alivclive.utils.ApiConfig;

/**
 * @author Mulberry
 *         create on 2018/5/9.
 */

public class AlivcConstants {

    private static final String APP_SVR_URL = getAppSvrUrl();

//    public final static String APP_ID_SG = "sg-37nisbt8";
//    public final static String APP_ID_SH = "sh-mfo1yx7g";

    public final static String APP_ID_SG = "sg-becvqlqr";
    public final static String APP_ID_SH = "sh-hrjbxns6";

    public final static String HOST_SG = "http://live-appserver-sig.alivecdn.com/";
    public final static String HOST_SH = "http://live-appserver-sh.alivecdn.com/";
    public final static String HOST_DAILY = "http://11.165.218.233:80/";

    public static String getAppId() {
        String appId;
        switch (ApiConfig.getApiConFigCache()) {
            case 0:
                appId = APP_ID_SG;
                break;
            case 1:
                appId = APP_ID_SH;
                break;
            default:
                appId = APP_ID_SG;
                break;
        }

        return appId;
    }

    public static String getAppSvrUrl() {
        String res;
        switch (ApiConfig.getApiConFigCache()) {
            case 0:
                res = HOST_SG;
                break;
            case 1:
                res = HOST_SH;
                break;
            case 2:
                res = HOST_DAILY;
                break;
            default:
                res = HOST_SG;
                break;
        }

        return res;
    }

    public static void update() {

        URL_GET_ROOM_LIST = getAppSvrUrl() + "appserver/getroomlist";
        URL_GET_ROOM_DETAIL = getAppSvrUrl()  + "appserver/getroomdetail";
        URL_GET_USER_DETAIL = getAppSvrUrl()  + "appserver/getuserdetail";
        URL_NEW_GUEST = getAppSvrUrl()  + "appserver/newguest";
        URL_UPDATE_USER = getAppSvrUrl()  + "appserver/updateuser";
        URL_GET_USERS = getAppSvrUrl() + "appserver/getusers";
        URL_JOIN_ROOM = getAppSvrUrl()  + "appserver/joinroom";
        URL_CREATE_ROOM = getAppSvrUrl()  + "appserver/createroom";
        URL_LEAVE_ROOM = getAppSvrUrl()  + "appserver/leaveroom";
        URL_NEW_STS = getAppSvrUrl()  + "appserver/newsts";
        URL_NOTIFICATION = getAppSvrUrl()  + "appserver/notification";
        URL_START_LIVE = getAppSvrUrl()  + "appserver/startstreaming";
        URL_END_LIVE = getAppSvrUrl()  + "appserver/endstreaming";

    }

    public static String URL_GET_ROOM_LIST = getAppSvrUrl() + "appserver/getroomlist";
    public static String URL_GET_ROOM_DETAIL = getAppSvrUrl()  + "appserver/getroomdetail";
    public static String URL_GET_USER_DETAIL = getAppSvrUrl()  + "appserver/getuserdetail";
    public static String URL_NEW_GUEST = getAppSvrUrl()  + "appserver/newguest";
    public static String URL_UPDATE_USER = getAppSvrUrl()  + "appserver/updateuser";
    public static String URL_GET_USERS = getAppSvrUrl() + "appserver/getusers";
    public static String URL_JOIN_ROOM = getAppSvrUrl()  + "appserver/joinroom";
    public static String URL_CREATE_ROOM = getAppSvrUrl()  + "appserver/createroom";
    public static String URL_LEAVE_ROOM = getAppSvrUrl()  + "appserver/leaveroom";
    public static String URL_NEW_STS = getAppSvrUrl()  + "appserver/newsts";
    public static String URL_NOTIFICATION = getAppSvrUrl()  + "appserver/notification";
    public static String URL_START_LIVE = getAppSvrUrl()  + "appserver/startstreaming";
    public static String URL_END_LIVE = getAppSvrUrl()  + "appserver/endstreaming";

    public static int ALIVC_PUSHER_EVENT_RTMP_NETWORK_POOR = 0xF0030902 + 1;
    public static int ALIVC_PUSHER_EVENT_RTMP_NETWORK_RECOVERY = 0xF0030902 + 2;
    public static int ALIVC_PUSHER_EVENT_RTMP_RECONNECT_START = 0xF0030902 + 3;
    public static int ALIVC_PUSHER_EVENT_RTMP_RECONNECT_SUCCESS = 0xF0030902 + 4;


}

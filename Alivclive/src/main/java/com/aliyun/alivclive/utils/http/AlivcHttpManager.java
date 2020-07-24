package com.aliyun.alivclive.utils.http;

import com.aliyun.alivclive.utils.constants.AlivcConstants;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Akira on 2018/5/28.
 */

public class AlivcHttpManager {

    private static AlivcHttpManager sInstance;

    private HttpEngine mHttpEngine = new HttpEngine();

    public static AlivcHttpManager getInstance() {
        if (sInstance == null) {
            synchronized (AlivcHttpManager.class) {
                if (sInstance == null) {
                    sInstance = new AlivcHttpManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 生成新用户
     *
     * @param callback
     */
    public void newGuest(HttpEngine.OnResponseCallback<HttpResponse.User> callback) {
        Request request = new Request.Builder().url(AlivcConstants.URL_NEW_GUEST)
                .post(new FormBody.Builder().build())
                .build();

        mHttpEngine.request(request, HttpResponse.User.class, callback);
    }

    /**
     * 更新用户
     *
     * @param userId
     * @param callback
     */
    public void updateUser(String userId, String nickname, HttpEngine.OnResponseCallback<HttpResponse.User> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody body = builder
                .add("user_id", userId)
                .add("nickname", nickname)
                .build();
        Request request = new Request.Builder().url(AlivcConstants.URL_UPDATE_USER)
                .post(body)
                .build();

        mHttpEngine.request(request, HttpResponse.User.class, callback);
    }

    /**
     * 获取用户信息
     *
     * @param userId
     * @param callback
     */
    public void getUserDetail(String userId, HttpEngine.OnResponseCallback<HttpResponse.User> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody body = builder
                .add("user_id", userId)
                .build();
        Request request = new Request.Builder().url(AlivcConstants.URL_GET_USER_DETAIL)
                .post(body)
                .build();

        mHttpEngine.request(request, HttpResponse.User.class, callback);
    }

    /**
     * @param userId
     * @param callback
     */
    public void getUsers(String userId, HttpEngine.OnResponseCallback<HttpResponse.UserList> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody body = builder
                .add("user_id", userId)
                .build();
        Request request = new Request.Builder().url(AlivcConstants.URL_GET_USERS)
                .post(body)
                .build();

        mHttpEngine.request(request, HttpResponse.UserList.class, callback);
    }

    /**
     * @param page_size
     * @param page_index
     */
    public void getRoomList(int page_index, int page_size, HttpEngine.OnResponseCallback<HttpResponse.AlivcRoomList> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody body = builder
                .add("page_size", page_size + "")
                .add("page_index", page_index + "")
                .build();

        Request request = new Request.Builder().url(AlivcConstants.URL_GET_ROOM_LIST)
                .post(body)
                .build();

        mHttpEngine.request(request, HttpResponse.AlivcRoomList.class, callback);
    }

    public void cancel(String url) {
        if (mHttpEngine != null) {
            mHttpEngine.cancel(url);
        }
    }

    /**
     * getRoomDetail
     *
     * @param roomId
     */
    public void getRoomDetail(String roomId, HttpEngine.OnResponseCallback<HttpResponse.RoomDetail> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody body = builder
                .add("room_id", roomId)
                .build();

        Request request = new Request.Builder().url(AlivcConstants.URL_GET_ROOM_DETAIL)
                .post(body)
                .build();

        mHttpEngine.request(request, HttpResponse.RoomDetail.class, callback);
    }

    /**
     * JoinRoom
     *
     * @param roomId     房间ID
     * @param streamerId 主播ID
     * @param viewerId   观众ID(用户ID)
     */
    public void joinRoom(String roomId, String streamerId, String viewerId, HttpEngine.OnResponseCallback<HttpResponse.Room> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody body = builder
                .add("room_id", roomId)
                .add("streamer_id", streamerId)
                .add("viewer_id", viewerId)
                .build();
        Request request = new Request.Builder().url(AlivcConstants.URL_JOIN_ROOM)
                .post(body)
                .build();

        mHttpEngine.requestAsyncThread(request, HttpResponse.Room.class, callback);
    }

    /**
     * CreateRoom
     *
     * @param userId
     * @param roomTitle
     */
    public void createRoom(String userId, String roomTitle, HttpEngine.OnResponseCallback<HttpResponse.Room> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody body = builder
                .add("user_id", userId)
                .add("room_title", roomTitle)
                .build();
        Request request = new Request.Builder().url(AlivcConstants.URL_CREATE_ROOM)
                .post(body)
                .build();

        mHttpEngine.request(request, HttpResponse.Room.class, callback);
    }

    /**
     * LeaveRoom
     *
     * @param roomId 房间ID
     * @param userId 主播ID
     */
    public void leaveRoom(String userId, String roomId, HttpEngine.OnResponseCallback<HttpResponse.User> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody body = builder
                .add("user_id", userId)
                .add("room_id", roomId)
                .build();

        Request request = new Request.Builder().url(AlivcConstants.URL_LEAVE_ROOM)
                .post(body)
                .build();

        mHttpEngine.requestAsyncThread(request, HttpResponse.User.class, callback);
    }

    /**
     * newSts
     *
     * @param callback
     */
    public void newSts(String id, HttpEngine.OnResponseCallback<HttpResponse.StsTokenBean> callback) {
        Request request = new Request.Builder().url(AlivcConstants.URL_NEW_STS)
                .post(new FormBody.Builder().add("id", id).build())
                .build();

        mHttpEngine.requestAsyncThread(request, HttpResponse.StsTokenBean.class, callback);
    }

    /**
     * onEvent
     *
     * @param callback
     */
    public void notification(String userId, String roomId, String eventId, HttpEngine.OnResponseCallback<HttpResponse.Notification> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody body = builder
                .add("user_id", userId)
                .add("room_id", roomId)
                .add("event_id", eventId)
                .build();

        Request request = new Request.Builder().url(AlivcConstants.URL_NOTIFICATION)
                .post(body)
                .build();

        mHttpEngine.requestAsyncThread(request, HttpResponse.Notification.class, callback);
    }

    /**
     * @param callback
     */
    public void startLive(String userId, String roomId, HttpEngine.OnResponseCallback<HttpResponse.Notification> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody body = builder
                .add("user_id", userId)
                .add("room_id", roomId)
                .build();

        Request request = new Request.Builder().url(AlivcConstants.URL_START_LIVE)
                .post(body)
                .build();

        mHttpEngine.requestAsyncThread(request, HttpResponse.Notification.class, callback);
    }

    /**
     * onEvent
     *
     * @param callback
     */
    public void endLive(String userId, String roomId, HttpEngine.OnResponseCallback<HttpResponse.Notification> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody body = builder
                .add("user_id", userId)
                .add("room_id", roomId)
                .build();

        Request request = new Request.Builder().url(AlivcConstants.URL_END_LIVE)
                .post(body)
                .build();

        mHttpEngine.requestAsyncThread(request, HttpResponse.Notification.class, callback);
    }


}

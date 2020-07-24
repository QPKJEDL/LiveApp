package com.aliyun.alivclive.utils.http;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import android.support.annotation.Nullable;

import com.aliyun.alivclive.utils.HandleUtils;
import com.aliyun.alivclive.utils.LogUtils;
import com.aliyun.alivclive.utils.constants.AlivcConstants;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author Mulberry
 * create on 2018/5/9.
 */

public class HttpEngine {
    private final String TAG = "HttpEngine";

    private static final MediaType MEDIA_JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient okHttpClient;
    public boolean isUnittest = false;

    public HttpEngine() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

    }

    public <R extends HttpResponse> void requestAsyncThread(final Request request, final Class<R> rClass, final OnResponseCallback<R> callback) {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d(TAG, "request failure,  request url = " + request.url());
                if (callback != null) {
                    if (isUnittest) {
                        callback.onResponse(false, "网络请求超时，请检查网络", null);
                    } else {
                        callback.onResponse(false, "网络请求超时，请检查网络", null);

                    }
                }
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String body = response.body().string();
                LogUtils.d(TAG, "request url = " + request.url());
                LogUtils.d(TAG, "response body = " + body);
                Gson gson = new Gson();

                try {
                    final R resp = gson.fromJson(body, rClass);
                    if (resp == null) {
                        return;
                    }
                    String errorMessage = resp.message;
                    if (resp.result != false) {
                        errorMessage += ("[err=" + resp.message + "]");
                    }
                    if (callback != null) {
                        final String finalErrorMessage = errorMessage;
                        if (isUnittest) {
                            callback.onResponse(resp.result, finalErrorMessage, resp);
                        } else {
                            callback.onResponse(resp.result, finalErrorMessage, resp);

                        }
                    }

                } catch (JsonSyntaxException e) {
                    onFailure(call, new IOException(e.getMessage()));
                }
            }
        });
    }

    ConcurrentHashMap<String, Call> map = new ConcurrentHashMap<>();

    public <R extends HttpResponse> void request(final Request request, final Class<R> rClass, final OnResponseCallback<R> callback) {
        Call call = okHttpClient.newCall(request);
        map.put(request.url().toString(), call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d(TAG, "request failure,  request url = " + request.url());
                if (callback != null) {
                    if (isUnittest) {
                        callback.onResponse(false, "网络请求超时，请检查网络", null);
                    } else {
                        post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onResponse(false, "网络请求超时，请检查网络", null);
                            }
                        });

                    }
                }
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String body = response.body().string();
                LogUtils.d(TAG, "request url = " + request.url());
                LogUtils.d(TAG, "response body = " + body);
                Gson gson = new Gson();

                try {
                    final R resp = gson.fromJson(body, rClass);
                    if (resp == null) {
                        return;
                    }
                    String errorMessage = resp.message;
                    if (resp.result != false) {
                        errorMessage += ("[err=" + resp.message + "]");
                    }
                    if (callback != null) {
                        final String finalErrorMessage = errorMessage;
                        if (isUnittest) {
                            callback.onResponse(resp.result, finalErrorMessage, resp);
                        } else {
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onResponse(resp.result, finalErrorMessage, resp);
                                }
                            });
                        }
                    }

                } catch (JsonSyntaxException e) {
                    onFailure(call, new IOException(e.getMessage()));
                }
            }
        });
    }

    private void post(Runnable r) {
        HandleUtils.getMainThreadHandler().post(r);
    }

    public void cancel(String url) {
        if (map != null && map.containsKey(url)) {
            Call remove = map.remove(url);
            remove.cancel();
        }
    }

    /**
     * 生成新用户
     *
     * @param callback
     */
    public void newGuest(OnResponseCallback<HttpResponse.User> callback) {
        Request request = new Request.Builder().url(AlivcConstants.URL_NEW_GUEST)
                .post(new FormBody.Builder().build())
                .build();

        request(request, HttpResponse.User.class, callback);
    }

    /**
     * 更新用户
     *
     * @param userId
     * @param callback
     */
    public void updateUser(String userId, String nickname, OnResponseCallback<HttpResponse.User> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody body = builder
                .add("user_id", userId)
                .add("nickname", nickname)
                .build();
        Request request = new Request.Builder().url(AlivcConstants.URL_UPDATE_USER)
                .post(body)
                .build();

        request(request, HttpResponse.User.class, callback);
    }

    /**
     * 获取用户信息
     *
     * @param userId
     * @param callback
     */
    public void getUserDetail(String userId, OnResponseCallback<HttpResponse.User> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody body = builder
                .add("user_id", userId)
                .build();
        Request request = new Request.Builder().url(AlivcConstants.URL_GET_USER_DETAIL)
                .post(body)
                .build();

        request(request, HttpResponse.User.class, callback);
    }

    /**
     * @param userId
     * @param callback
     */
    public void getUsers(String userId, OnResponseCallback<HttpResponse.UserList> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody body = builder
                .add("user_id", userId)
                .build();
        Request request = new Request.Builder().url(AlivcConstants.URL_GET_USERS)
                .post(body)
                .build();

        request(request, HttpResponse.UserList.class, callback);
    }

    /**
     * @param page_size
     * @param page_index
     */
    public void getRoomList(int page_index, int page_size, OnResponseCallback<HttpResponse.AlivcRoomList> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody body = builder
                .add("page_size", page_size + "")
                .add("page_index", page_index + "")
                .build();

        Request request = new Request.Builder().url(AlivcConstants.URL_GET_ROOM_LIST)
                .post(body)
                .build();

        request(request, HttpResponse.AlivcRoomList.class, callback);
    }

    /**
     * getRoomDetail
     *
     * @param roomId
     */
    public void getRoomDetail(String roomId, OnResponseCallback<HttpResponse.RoomDetail> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody body = builder
                .add("room_id", roomId)
                .build();

        Request request = new Request.Builder().url(AlivcConstants.URL_GET_ROOM_DETAIL)
                .post(body)
                .build();

        request(request, HttpResponse.RoomDetail.class, callback);
    }

    /**
     * JoinRoom
     *
     * @param roomId     房间ID
     * @param streamerId 主播ID
     * @param viewerId   观众ID(用户ID)
     */
    public void joinRoom(String roomId, String streamerId, String viewerId, OnResponseCallback<HttpResponse.Room> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody body = builder
                .add("room_id", roomId)
                .add("streamer_id", streamerId)
                .add("viewer_id", viewerId)
                .build();
        Request request = new Request.Builder().url(AlivcConstants.URL_JOIN_ROOM)
                .post(body)
                .build();

        request(request, HttpResponse.Room.class, callback);
    }

    /**
     * CreateRoom
     *
     * @param userId
     * @param roomTitle
     */
    public void createRoom(String userId, String roomTitle, OnResponseCallback<HttpResponse.Room> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody body = builder
                .add("user_id", userId)
                .add("room_title", roomTitle)
                .build();
        Request request = new Request.Builder().url(AlivcConstants.URL_CREATE_ROOM)
                .post(body)
                .build();

        request(request, HttpResponse.Room.class, callback);
    }

    /**
     * LeaveRoom
     *
     * @param roomId 房间ID
     * @param userId 主播ID
     */
    public void leaveRoom(String userId, String roomId, OnResponseCallback<HttpResponse.Room> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody body = builder
                .add("user_id", userId)
                .add("room_id", roomId)
                .build();

        Request request = new Request.Builder().url(AlivcConstants.URL_LEAVE_ROOM)
                .post(body)
                .build();

        request(request, HttpResponse.Room.class, callback);
    }

    /**
     * newSts
     *
     * @param callback
     */
    public void newSts(String id, OnResponseCallback<HttpResponse.StsTokenBean> callback) {
        Request request = new Request.Builder().url(AlivcConstants.URL_NEW_STS)
                .post(new FormBody.Builder().add("id", id).build())
                .build();

        request(request, HttpResponse.StsTokenBean.class, callback);
    }

    /**
     * onEvent
     *
     * @param callback
     */
    public void notification(String userId, String roomId, String eventId, String content, HttpEngine.OnResponseCallback<HttpResponse.User> callback) {
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody body = builder
                .add("user_id", userId)
                .add("room_id", roomId)
                .add("event_id", eventId)
                .add("content", content)
                .build();

        Request request = new Request.Builder().url(AlivcConstants.URL_NOTIFICATION)
                .post(body)
                .build();

        request(request, HttpResponse.User.class, callback);
    }

    public interface OnResponseCallback<T> {
        void onResponse(final boolean result, final @Nullable String retmsg, final @Nullable T data);
    }

}

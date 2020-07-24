package com.aliyun.alivclive.utils;

import android.support.annotation.Nullable;


import com.aliyun.alivclive.room.userlist.AlivcLiveUserInfo;
import com.aliyun.alivclive.utils.http.HttpEngine;
import com.aliyun.alivclive.utils.http.HttpResponse;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Akira on 2018/5/22.
 */
public class HttpEngineTest extends TestCase {

    private HttpEngine httpEngine;

    @Before
    public void setUp() throws Exception {
        httpEngine = new HttpEngine();
        httpEngine.isUnittest = true;
        System.out.println("Ready for testing!");
    }

    @Test
    public void testNewGuest() throws Exception {
        httpEngine.newGuest(new HttpEngine.OnResponseCallback<HttpResponse.User>() {
            @Override
            public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.User data) {
                if (result) {

                    AlivcLiveUserInfo userInfo = data.getData();
                    System.out.println("ret msg = " + retmsg);
                    System.out.println("data = " + data.toString());
                } else {
                    System.out.println("request failure ret msg = " + retmsg);
                }
            }
        });
        Thread.sleep(4000);
    }

    @Test
    public void testUserDetail() throws Exception {
        httpEngine.getUserDetail("rlzzua44wugxe5yv", new HttpEngine.OnResponseCallback<HttpResponse.User>() {
            @Override
            public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.User data) {
                if (result) {
                    AlivcLiveUserInfo userInfo = data.getData();
                    System.out.println("ret msg = " + retmsg);
                    System.out.println("data = " + data.toString());
                } else {
                    System.out.println("request failure ret msg = " + retmsg);
                }
            }
        });
        Thread.sleep(3000);
    }

    @Test
    public void testUpdateUser() throws Exception {
        httpEngine.updateUser("db5e5513-8f77-4ba1-96c9-98dcfdea519c", "flashDog", new HttpEngine.OnResponseCallback<HttpResponse.User>() {
            @Override
            public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.User data) {
                if (result) {

                    AlivcLiveUserInfo userInfo = data.getData();
                    System.out.println("data = " + data.toString());
                } else {
                    System.out.println("request failure");
                }

                System.out.println("ret msg = " + retmsg);
            }
        });

        Thread.sleep(3000);
    }

    @Test
    public void testGetUsers() throws Exception {
        httpEngine.getUsers("db5e5513-8f77-4ba1-96c9-98dcfdea519c", new HttpEngine.OnResponseCallback<HttpResponse.UserList>() {
            @Override
            public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.UserList data) {
                if (result) {
                    System.out.println("data = " + data.toString());
                } else {
                    System.out.println("request failure");
                }

                System.out.println("ret msg = " + retmsg);
            }
        });

        Thread.sleep(3000);
    }

    @Test
    public void testGetRoomList() throws Exception {
        httpEngine.getRoomList(1, 12, new HttpEngine.OnResponseCallback<HttpResponse.AlivcRoomList>() {
            @Override
            public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.AlivcRoomList data) {
                if (result) {
                    System.out.println("data = " + data.toString());
                } else {
                    System.out.println("request failure");
                    System.out.println("ret msg = " + retmsg);
                }
            }
        });

        Thread.sleep(3000);
    }

    @Test
    public void testGetRoomDetail() throws Exception {
        httpEngine.getRoomDetail("c09d1fkm4unl9kgi", new HttpEngine.OnResponseCallback<HttpResponse.RoomDetail>() {
            @Override
            public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.RoomDetail data) {
                if (result) {
                    System.out.println("data = " + data.toString());
                } else {
                    System.out.println("request failure");
                }
                System.out.println("ret msg = " + retmsg);
            }
        });
        Thread.sleep(4000);
    }

    @Test
    public void testJoinRoom() throws Exception {
        httpEngine.joinRoom("miyou_room_1", "miyou", "y8tk6je9qjtqcmmv", new HttpEngine.OnResponseCallback<HttpResponse.Room>() {
            @Override
            public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.Room data) {
                if (result) {
                    System.out.println("data = " + data.toString());
                } else {
                    System.out.println("request failure");
                }
                System.out.println("ret msg = " + retmsg);
            }
        });

        Thread.sleep(4000);
    }

    @Test
    public void testCreateRoom() throws Exception {
        httpEngine.createRoom("rlzzua44wugxe5yv", "one punch man", new HttpEngine.OnResponseCallback<HttpResponse.Room>() {
            @Override
            public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.Room data) {
                if (result) {
                    System.out.println("data = " + data.toString());
                } else {
                    System.out.println("request failure");
                }
                System.out.println("ret msg = " + retmsg);
            }
        });

        Thread.sleep(13000);
    }

    @Test
    public void testLeaveRoom() throws Exception {
        httpEngine.leaveRoom("c8624b97b7b54acba4b65951c12d3c5b", "gr4sikiik9aozj6m", new HttpEngine.OnResponseCallback<HttpResponse.Room>() {
            @Override
            public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.Room data) {
                if (result) {
                    System.out.println("data = " + data.toString());
                } else {
                    System.out.println("request failure");
                }
                System.out.println("ret msg = " + retmsg);
            }
        });

        Thread.sleep(3000);
    }

    @Test
    public void testNewSts() throws Exception {
        httpEngine.newSts("rlzzua44wugxe5yv", new HttpEngine.OnResponseCallback<HttpResponse.StsTokenBean>() {
            @Override
            public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.StsTokenBean data) {
                if (result) {
                    System.out.println("data = " + data.toString());
                } else {
                    System.out.println("request failure");
                }
                System.out.println("ret msg = " + retmsg);
            }
        });

        Thread.sleep(3000);
    }

    @Test
    public void testOnEvent() throws Exception {
        httpEngine.notification("ym9pmj7gyi76zp75", "miyou_room_1", "1", "",new HttpEngine.OnResponseCallback<HttpResponse.User>() {
            @Override
            public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.User data) {
                if (result) {
                    System.out.println("data = " + data.toString());
                } else {
                    System.out.println("request failure");
                }
                System.out.println("ret msg = " + retmsg);
            }
        });

        Thread.sleep(30000);
    }


    @Test
    public void testDemo() throws Exception {
//        System.out.println("people = " + NumberUtils.formatPeopleNum(1100323));
    }

}
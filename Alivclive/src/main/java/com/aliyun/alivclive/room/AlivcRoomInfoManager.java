package com.aliyun.alivclive.room;

import com.alivc.live.room.AlivcLiveRoomFactory;
import com.alivc.live.room.model.AlivcRoomInfo;

/**
 * 互动直播信息管理类，通过该类来管理直播信息
 * 播放管理:后台进行通信，通知后台进入房间、退出房间、点赞事件
 * 直播管理类:后台进行通信，拉取直播地址、通知后台退出直播
 * @author Mulberry
 *         create on 2018/4/19.
 */

public class AlivcRoomInfoManager {

    AlivcRoomInfo alivcRoomInfo;

    private static class AlivcRoomInfoManagerHolder {
        private static AlivcRoomInfoManager instance = new AlivcRoomInfoManager();
    }

    public static AlivcRoomInfoManager getInstance() {
        return AlivcRoomInfoManagerHolder.instance;
    }

    public void setAlivcRoomInfo(AlivcRoomInfo alivcRoomInfo) {
        this.alivcRoomInfo = alivcRoomInfo;
    }

    public AlivcRoomInfo getAlivcRoomInfo() {
        //alivcRoomInfo = new AlivcRoomInfo();
        //alivcRoomInfo.setRoomName("hello");
        //alivcRoomInfo.setRoomId("148-651-5149");
        //alivcRoomInfo.setPushUrl("rtmp://video-center.alivecdn.com/alilvb/cc80664262d44be69b7da129c2f84909?vhost=qt1.alivecdn.com");
        return alivcRoomInfo;
    }



}

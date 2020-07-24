package com.aliyun.alivclive.room;

import com.alivc.auth.AlivcSts;
import com.alivc.live.room.AlivcLiveRoomFactory;
import com.alivc.live.room.interactive.impl.AlivcInteractiveLiveRoom;
import com.alivc.net.AlivcNetManager;

/**
 * 互动直播信息管理类，通过该类来管理直播信息
 * 播放管理:后台进行通信，通知后台进入房间、退出房间、点赞事件
 * 直播管理类:后台进行通信，拉取直播地址、通知后台退出直播
 * @author Mulberry
 *         create on 2018/4/19.
 */

public class AlivcLiveRoomManager {
    private static final String appid = "sg-37nisbt8";
    private static final String ak = "LTAIbuQBuOjrr9u1";
    private static final String sk = "wskx2mr1ZFxumSQk7ggKbgaAWGx7w8";

    static AlivcInteractiveLiveRoom alivcILiveRoom = null;

    static public AlivcInteractiveLiveRoom getLiveRoom() {
        if (alivcILiveRoom != null) {
            return alivcILiveRoom;
        }
        synchronized (AlivcLiveRoomManager.class) {
            alivcILiveRoom = new AlivcInteractiveLiveRoom();
            return alivcILiveRoom;
        }
    }

    static public void release(){
        synchronized (AlivcLiveRoomManager.class) {
            if (alivcILiveRoom != null){
                alivcILiveRoom = null;
            }
        }
    }

    public String getAppid() {
        return appid;
    }

    /**
     * 加入房间
     */
    public void JoinRoom(){

    }

    /**
     * 离开房间
     */
    public void leaveRoom(){

    }

    /**
     * 绑定用户
     */
    public void bindRoomUser(){

    }

    /**
     * 生成新的STS
     */
    public void newSts(){

    }

}

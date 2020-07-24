package com.aliyun.alivclive.room.roominfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mulberry
 *         create on 2018/5/9.
 */

public class AlivcLiveRoomInfoData implements Serializable {

    public boolean more;

    public List<AlivcLiveRoomInfo> rooms;

    @Override
    public String toString() {
        return "AlivcLiveRoomInfoData{" +
                "rooms=" + rooms +
                '}';
    }
}

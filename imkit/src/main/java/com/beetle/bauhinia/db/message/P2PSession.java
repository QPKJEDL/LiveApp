/*                                                                            
  Copyright (c) 2014-2019, GoBelieve     
    All rights reserved.		    				     			
 
  This source code is licensed under the BSD-style license found in the
  LICENSE file in the root directory of this source tree. An additional grant
  of patent rights can be found in the PATENTS file in the same directory.
*/


package com.beetle.bauhinia.db.message;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class P2PSession extends MessageContent {
    @SerializedName("device_id")
    public String deviceID;
    @SerializedName("channel_id")
    public String channelID;



    public static P2PSession newP2PSession(String channelID, String devicdID) {
        P2PSession s = new P2PSession();
        String uuid = UUID.randomUUID().toString();

        JsonObject content = new JsonObject();
        JsonObject textContent = new JsonObject();
        textContent.addProperty("device_id", devicdID);
        textContent.addProperty("channel_id", channelID);
        content.add(P2P_SESSION, textContent);
        content.addProperty("uuid", uuid);

        s.raw = content.toString();
        s.deviceID = devicdID;
        s.channelID = channelID;
        return s;
    }


    public MessageType getType() {
        return MessageType.MESSAGE_P2P_SESSION;
    }


}



/*                                                                            
  Copyright (c) 2014-2019, GoBelieve     
    All rights reserved.		    				     			
 
  This source code is licensed under the BSD-style license found in the
  LICENSE file in the root directory of this source tree. An additional grant
  of patent rights can be found in the PATENTS file in the same directory.
*/


package com.beetle.bauhinia.db;

import com.beetle.bauhinia.db.message.Location;

public class EPeerMessageDB extends SQLPeerMessageDB implements IMessageDB {
    public static final boolean SQL_ENGINE_DB = true;

    private static EPeerMessageDB instance = new EPeerMessageDB();

    public static EPeerMessageDB getInstance() {
        return instance;
    }


    EPeerMessageDB() {
        secret = 1;
    }


    public void saveMessageAttachment(IMessage msg, String address) {
        Location loc = (Location)msg.content;
        loc = Location.newLocation(loc.latitude, loc.longitude, address);
        this.updateContent(msg.msgLocalID, loc.getRaw());
    }

    public void saveMessage(IMessage imsg) {
        assert(imsg.isOutgoing);
        this.insertMessage(imsg, imsg.receiver);
    }

    public void removeMessage(IMessage imsg) {
        this.removeMessage(imsg.msgLocalID);
    }

    public void markMessageListened(IMessage imsg) {
        this.markMessageListened(imsg.msgLocalID);
    }

    public void markMessageFailure(IMessage imsg) {
        this.markMessageFailure(imsg.msgLocalID);
    }

    public void eraseMessageFailure(IMessage imsg) {
        this.eraseMessageFailure(imsg.msgLocalID);
    }


}

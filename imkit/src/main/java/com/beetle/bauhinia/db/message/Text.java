/*                                                                            
  Copyright (c) 2014-2019, GoBelieve     
    All rights reserved.		    				     			
 
  This source code is licensed under the BSD-style license found in the
  LICENSE file in the root directory of this source tree. An additional grant
  of patent rights can be found in the PATENTS file in the same directory.
*/


package com.beetle.bauhinia.db.message;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.UUID;

public class Text extends MessageContent {
    public String text;
    public List<Long> at;
    @SerializedName("at_name")
    public List<String> atNames;

    public static Text newText(String text) {
        Text t = new Text();
        JsonObject textContent = new JsonObject();
        textContent.addProperty(TEXT, text);
        t.raw = textContent.toString();
        t.text = text;
        return t;
    }

    public static Text newText(String text, List<Long> at, List<String> atNames) {
        Text t = new Text();
        String uuid = UUID.randomUUID().toString();

        JsonArray atArray = new JsonArray();
        JsonArray atNameArray = new JsonArray();
        if (at != null && atNames != null && at.size() == atNames.size()) {
            for (int i = 0; i < at.size(); i++) {
                atArray.add(at.get(i));
                atNameArray.add(atNames.get(i));
            }
        }

        JsonObject textContent = new JsonObject();
        textContent.addProperty(TEXT, text);
        textContent.addProperty("uuid", uuid);
        if (atArray.size() > 0) {
            textContent.add("at", atArray);
            textContent.add("at_name", atNameArray);
        }
        t.raw = textContent.toString();
        t.text = text;
        t.uuid = uuid;
        t.at = at;
        return t;
    }

    public Text(String text) {
        String uuid = UUID.randomUUID().toString();
        JsonObject textContent = new JsonObject();
        textContent.addProperty(TEXT, text);
        textContent.addProperty("uuid", uuid);
        this.raw = textContent.toString();
        this.text = text;
        this.uuid = uuid;
    }

    public Text() {

    }

    public MessageType getType() {
        return MessageType.MESSAGE_TEXT;
    }
}

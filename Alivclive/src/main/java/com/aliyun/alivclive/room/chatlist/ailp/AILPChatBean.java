package com.aliyun.alivclive.room.chatlist.ailp;

import android.graphics.Color;
import android.text.TextUtils;

import java.util.Map;

/**
 *
 * @author zhangzhiquan
 * @date 2018/3/30
 */

public class AILPChatBean {
    public static final int TYPE_DEFAULT = 0x01;
    public static final int TYPE_SHARE = 0x02;
    /**
     * 默认样式
     */
    private static final String DEFAULT = "default";
    /**
     * 分享样式
     */
    private static final String SHARE = "share";

    private String type;
    private String cellType;
    private String data;
    private String face;
    private String name;
    private String textColor;
    private String textAlpha;
    private String faceIcon;
    private String nameColor;
    private String nameAlpha;
    private String bgColor;
    private String bgAlpha;

    public AILPChatBean(){}

    public AILPChatBean(Map<String,String> parms){
        type = parms.get("type");
        data = parms.get("data");
        face = parms.get("face");
        name = parms.get("nickName");
        cellType = parms.get("cellType");
        textColor = parms.get("rgb");
        textAlpha = parms.get("alpha");
        faceIcon = parms.get("faceIcon");
        nameColor = parms.get("nickNameColor");
        nameAlpha = parms.get("nickNameAlpha");
        bgColor = parms.get("bgColor");
        bgAlpha = parms.get("bgAlpha");
    }

    public String getType() {
        return type;
    }

    public int getCellType() {
        if(TextUtils.isEmpty(cellType)){
            return TYPE_DEFAULT;
        }
        if(DEFAULT.equals(cellType)){
            return TYPE_DEFAULT;
        }else if(SHARE.equals(cellType)){
            return TYPE_SHARE;
        }
        return TYPE_DEFAULT;
    }

    public String getFaceIcon() {
        return faceIcon;
    }

    public String getData() {
        return data;
    }

    public String getFace() {
        return face;
    }

    public String getName() {
        return name;
    }

    public int getNameColor() {
       return getColor(nameAlpha,nameColor, Color.WHITE);
    }

    private int bgColorValue = -1;
    public int getBgColor(){
        if(bgColorValue != -1){
            return bgColorValue;
        }
        bgColorValue = getColor("ff",bgColor);
        return bgColorValue;
    }

    public int getBgAlpha(){
        try {
            return Integer.parseInt(bgAlpha,16);
        }catch (Exception e){
            e.printStackTrace();
            return 0xFF;
        }
    }

    public int getTextColor(){
        return getColor(textAlpha,textColor, Color.WHITE);
    }

    private int getColor(String alpha, String colorStr, int defaultColor){
        int color = getColor(alpha,colorStr);
        return color == -1 ? defaultColor : color;

    }

    private int getColor(String alpha, String color) {
        if (TextUtils.isEmpty(color)) {
            return -1;
        }
        try {
            if (TextUtils.isEmpty(alpha)) {
                return Color.parseColor("#" + color);
            } else {
                return Color.parseColor("#" + alpha + color);
            }

        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("type"+type+"\n");
        builder.append("cellType"+cellType+"\n");
        builder.append("data"+data+"\n");
        builder.append("face"+face+"\n");
        builder.append("name"+name+"\n");
        builder.append("rgb"+textColor+"\n");
        builder.append("alpha"+textAlpha+"\n");
        return builder.toString();
    }
}

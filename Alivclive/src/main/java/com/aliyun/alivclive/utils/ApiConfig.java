package com.aliyun.alivclive.utils;

import android.content.Context;

import com.aliyun.pusher.core.utils.SharedPreferenceUtils;

public class ApiConfig {
    private static int api = 0;

    public static void setApiConfig(Context context, int i) {
        SharedPreferenceUtils.setNetconfig(context, i);
        api = i;
    }

    public static int getApiConfig(Context context) {
        api = SharedPreferenceUtils.getNetconfig(context);
        return api;
    }

    public static int getApiConFigCache() {
        return api;
    }
}

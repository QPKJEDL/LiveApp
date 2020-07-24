package com.aliyun.alivclive.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Akira on 2018/5/28.
 */

public class HandleUtils {

    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public static Handler getMainThreadHandler() {
        return mHandler;
    }
}

package com.aliyun.alivclive.listener;

import com.alivc.auth.AlivcSts;

/**
 * Created by Akira on 2018/6/5.
 */

public interface IStsTokenListener {

    void onTokenRefresh(AlivcSts sts);
}

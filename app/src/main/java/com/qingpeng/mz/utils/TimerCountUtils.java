package com.qingpeng.mz.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

public class TimerCountUtils {
    public static boolean status = false;

    public static CountDownTimer getTimer(final TextView tvCountDowm) {
        return new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                tvCountDowm.setText("");
                tvCountDowm.setText("重新获取("+(l / 1000) + ")");
                if (l / 1000 == 1) {
                    tvCountDowm.setText("获取验证码");
                    status = false;
                }
                status = true;
            }

            @Override
            public void onFinish() {
                status = false;
            }
        };
    }
}

package com.aliyun.alivclive.room.music;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 *
 * 音效
 * @author Mulberry
 *         create on 2018/4/23.
 */

public class AlivcMusicEffectView extends RelativeLayout {

    public AlivcMusicEffectView(Context context) {
        super(context);
    }

    public AlivcMusicEffectView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlivcMusicEffectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface OnMusicEffectViewClickListener {
        //降噪
        //耳返
        //人身音量
        //伴奏音量
    }
}

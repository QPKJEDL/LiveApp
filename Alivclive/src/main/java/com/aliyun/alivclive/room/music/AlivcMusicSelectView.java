package com.aliyun.alivclive.room.music;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import com.aliyun.alivclive.R;

/**
 * 音乐选择View
 * @author Mulberry
 *         create on 2018/4/19.
 */

public class AlivcMusicSelectView extends RelativeLayout {


    public AlivcMusicSelectView(Context context) {
        super(context);
        initView();
    }

    public AlivcMusicSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AlivcMusicSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        LayoutInflater.from(getContext().getApplicationContext()).inflate(R.layout.alivc_control_bar_view_layout,this,false);
    }

    private OnMusicSelectClickListener onMusicSelectClickListener;
    public void setOnMusicSelectClickListener(
        OnMusicSelectClickListener onMusicSelectClickListener) {
        this.onMusicSelectClickListener = onMusicSelectClickListener;
    }

    public interface OnMusicSelectClickListener {
        void choiceMusic(String path);
    }
}

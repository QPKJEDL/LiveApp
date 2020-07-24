package com.aliyun.alivclive.room.beauty;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import com.alivc.live.room.interactive.impl.AlivcInteractiveLiveRoom;
import com.alivc.live.room.model.AlivcBeautyParams;
import com.aliyun.alivclive.R;
import com.aliyun.pusher.core.utils.SharedPreferenceUtils;


/**
 * 美颜调节View
 * @author Mulberry
 *         create on 2018/4/19.
 */

public class AlivcBeautyAdjustmentView extends Dialog {

    private static final String BEAUTY_ON = "beauty_on";
    private SeekBar mCheekPinkBar;
    private SeekBar mWhiteBar;
    private SeekBar mSkinBar;
    private SeekBar mRuddyBar;
    private SeekBar mSlimFaceBar;
    private SeekBar mShortenFaceBar;
    private SeekBar mBigEyeBar;

    private TextView mCheekPink;
    private TextView mWhite;
    private TextView mSkin;
    private TextView mRuddy;
    private TextView mSlimFace;
    private TextView mShortenFace;
    private TextView mBigEye;

    private AlivcInteractiveLiveRoom mAlivcLiveRoom = null;

    AlivcBeautyParams beautyParams;

    public AlivcBeautyAdjustmentView(@NonNull Context context) {
        super(context);
    }

    public AlivcBeautyAdjustmentView(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public AlivcBeautyAdjustmentView(@NonNull Context context, boolean cancelable,
                                     @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dpMetrics);
        WindowManager.LayoutParams p = getWindow().getAttributes();

        p.width = dpMetrics.widthPixels;
        p.height = dpMetrics.heightPixels/2;
        getWindow().setAttributes(p);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aliyun_beauty_adjustment_view_layout);
        getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        getWindow().setGravity(Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);

        beautyParams = new AlivcBeautyParams();

        mCheekPinkBar = (SeekBar) findViewById(R.id.beauty_cheekpink_seekbar);
        mWhiteBar = (SeekBar) findViewById(R.id.beauty_white_seekbar);
        mSkinBar = (SeekBar) findViewById(R.id.beauty_skin_seekbar);
        mRuddyBar = (SeekBar) findViewById(R.id.beauty_ruddy_seekbar);
        mSlimFaceBar = (SeekBar) findViewById(R.id.beauty_thinface_seekbar);
        mShortenFaceBar = (SeekBar) findViewById(R.id.beauty_shortenface_seekbar);
        mBigEyeBar = (SeekBar) findViewById(R.id.beauty_bigeye_seekbar);
        mCheekPink = (TextView) findViewById(R.id.cheekpink);
        mWhite = (TextView) findViewById(R.id.white);
        mSkin = (TextView) findViewById(R.id.skin);
        mRuddy = (TextView) findViewById(R.id.ruddy);
        mSlimFace = (TextView) findViewById(R.id.thinface);
        mShortenFace = (TextView) findViewById(R.id.shortenface);
        mBigEye = (TextView) findViewById(R.id.bigeye);
        mCheekPinkBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mWhiteBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mSkinBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mRuddyBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mSlimFaceBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mShortenFaceBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mBigEyeBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mWhite.setText(String.valueOf(SharedPreferenceUtils.getWhiteValue(getContext())));
        mWhiteBar.setProgress(SharedPreferenceUtils.getWhiteValue(getContext()));
        mSkin.setText(String.valueOf(SharedPreferenceUtils.getBuffing(getContext())));
        mSkinBar.setProgress(SharedPreferenceUtils.getBuffing(getContext()));
        mRuddy.setText(String.valueOf(SharedPreferenceUtils.getRuddy(getContext())));
        mRuddyBar.setProgress(SharedPreferenceUtils.getRuddy(getContext()));
        mCheekPink.setText(String.valueOf(SharedPreferenceUtils.getCheekpink(getContext())));
        mCheekPinkBar.setProgress(SharedPreferenceUtils.getCheekpink(getContext()));
        mSlimFace.setText(String.valueOf(SharedPreferenceUtils.getSlimFace(getContext())));
        mSlimFaceBar.setProgress(SharedPreferenceUtils.getSlimFace(getContext()));
        mShortenFace.setText(String.valueOf(SharedPreferenceUtils.getShortenFace(getContext())));
        mShortenFaceBar.setProgress(SharedPreferenceUtils.getShortenFace(getContext()));
        mBigEye.setText(String.valueOf(SharedPreferenceUtils.getBigEye(getContext())));
        mBigEyeBar.setProgress(SharedPreferenceUtils.getBigEye(getContext()));

    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            try{
                int seekBarId = seekBar.getId();

                if(mCheekPinkBar.getId() == seekBarId) {
                    //腮红
                    beautyParams.beautyCheekPink = mCheekPinkBar.getProgress();
                    mAlivcLiveRoom.setBeautyBeautyParams(beautyParams);
                    mCheekPink.setText(String.valueOf(progress));
                    SharedPreferenceUtils.setCheekPink(getContext(), progress);
                } else if(mWhiteBar.getId() == seekBarId) {
                    //美白
                    beautyParams.beautyWhite = mWhiteBar.getProgress();
                    mAlivcLiveRoom.setBeautyBeautyParams(beautyParams);
                    mWhite.setText(String.valueOf(progress));
                    SharedPreferenceUtils.setWhiteValue(getContext().getApplicationContext(), progress);
                } else if(mSkinBar.getId() == seekBarId) {
                    //磨皮
                    beautyParams.beautyBuffing = mSkinBar.getProgress();
                    mAlivcLiveRoom.setBeautyBeautyParams(beautyParams);
                    mSkin.setText(String.valueOf(progress));
                    SharedPreferenceUtils.setBuffing(getContext(), progress);
                } else if (mRuddyBar.getId() == seekBarId) {
                    //红润
                    beautyParams.beautyRuddy = mRuddyBar.getProgress();
                    mAlivcLiveRoom.setBeautyBeautyParams(beautyParams);
                    mRuddy.setText(String.valueOf(progress));
                    SharedPreferenceUtils.setRuddy(getContext(), progress);
                } else if(mSlimFaceBar.getId() == seekBarId) {
                    //瘦脸
                    beautyParams.beautySlimFace = mSlimFaceBar.getProgress();
                    mAlivcLiveRoom.setBeautyBeautyParams(beautyParams);
                    mSlimFace.setText(String.valueOf(progress));
                    SharedPreferenceUtils.setSlimFace(getContext(), progress);
                } else if(mShortenFaceBar.getId() == seekBarId) {
                    //收下巴
                    beautyParams.beautyShortenFace = mShortenFaceBar.getProgress();
                    mAlivcLiveRoom.setBeautyBeautyParams(beautyParams);
                    mShortenFace.setText(String.valueOf(progress));
                    SharedPreferenceUtils.setShortenFace(getContext(), progress);
                } else if(mBigEyeBar.getId() == seekBarId) {
                    //大眼
                    beautyParams.beautyBigEye = mBigEyeBar.getProgress();
                    mAlivcLiveRoom.setBeautyBeautyParams(beautyParams);
                    mBigEye.setText(String.valueOf(progress));
                    SharedPreferenceUtils.setBigEye(getContext(), progress);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    public void setAlivcLiveRoom(AlivcInteractiveLiveRoom alivcLivePusher) {
        this.mAlivcLiveRoom = alivcLivePusher;
    }

    public void setOnBeautyChangeClickListener(
        OnBeautyChangeClickListener onBeautyChangeClickListener) {
        this.onBeautyChangeClickListener = onBeautyChangeClickListener;
    }

    private OnBeautyChangeClickListener onBeautyChangeClickListener;
    public interface OnBeautyChangeClickListener {
        /**
         * 调节美颜参数
         * @param progress 美颜度调节
         */
        void onBeautyProgress(int  progress);

    }

}

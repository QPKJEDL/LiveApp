package com.aliyun.alivclive.room.music;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import com.alivc.live.room.interactive.impl.AlivcInteractiveLiveRoom;
import com.aliyun.alivclive.R;

public class AlivcMusicDialog extends Dialog implements View.OnClickListener {

    private Button mPause;
    private Button mStop;
    private Button mLoop;
    private Button mMute;
    private Button mEarsBack;
    private Button mAudioDenoise;

    private TextView mAccompanimentText;
    private SeekBar mAccompanimentBar;

    private TextView mVoiceText;
    private SeekBar mVoiceBar;

    private RecyclerView mMusicRecyclerView;
    private AlivcMusicAdapter mMusicAdapter;

    private AlivcInteractiveLiveRoom mAlivcLivePusher = null;
    private boolean isPause = true;
    private boolean isStop = true;
    private boolean isLoop = true;
    private boolean isMute = false;
    private boolean isEarsBack = false;
    private boolean isAudioDenoise = false;

    private int mPosition = 0;
    private AlivcMusicAdapter.MusicInfo mMusicInfo = null;

    public AlivcMusicDialog(@NonNull Context context) {
        super(context);
    }

    public AlivcMusicDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public AlivcMusicDialog(@NonNull Context context, boolean cancelable,
                       @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_music);
        mPause = (Button) findViewById(R.id.pause);
        mPause.setSelected(isPause);
        mPause.setText(isPause ? getContext().getApplicationContext().getResources().getString(R.string.pause) : getContext().getApplicationContext().getResources().getString(R.string.resume));
        mStop = (Button) findViewById(R.id.stop);
        mStop.setSelected(isStop);
        mStop.setText(isStop ? getContext().getApplicationContext().getResources().getString(R.string.stop) : getContext().getApplicationContext().getResources().getString(R.string.start));
        mLoop = (Button) findViewById(R.id.loop);
        mLoop.setSelected(isLoop);
        mLoop.setText(isLoop ? getContext().getApplicationContext().getResources().getString(R.string.close_loop) : getContext().getApplicationContext().getResources().getString(R.string.open_loop));
        mMute = (Button) findViewById(R.id.mute);
        mMute.setSelected(isMute);
        mMute.setText(isMute ? getContext().getApplicationContext().getResources().getString(R.string.close_mute) : getContext().getApplicationContext().getResources().getString(R.string.open_mute));
        mEarsBack = (Button) findViewById(R.id.ears_back);
        mEarsBack.setSelected(isEarsBack);
        mEarsBack.setText(isEarsBack ? getContext().getApplicationContext().getResources().getString(R.string.close_ears_back) : getContext().getApplicationContext().getResources().getString(R.string.open_ears_back));
        mAudioDenoise = (Button) findViewById(R.id.audio_denoise);
        mAudioDenoise.setSelected(isAudioDenoise);
        mAudioDenoise.setText(isAudioDenoise ? getContext().getApplicationContext().getResources().getString(R.string.close_audio_denoise) : getContext().getApplicationContext().getResources().getString(R.string.open_audio_denoise));

        mPause.setOnClickListener(this);
        mStop.setOnClickListener(this);
        mLoop.setOnClickListener(this);
        mMute.setOnClickListener(this);
        mEarsBack.setOnClickListener(this);
        mAudioDenoise.setOnClickListener(this);
        mAccompanimentText = (TextView) findViewById(R.id.accompaniment_text);
        mAccompanimentBar = (SeekBar) findViewById(R.id.accompaniment_seekbar);
        mVoiceText = (TextView) findViewById(R.id.voice_text);
        mVoiceBar = (SeekBar) findViewById(R.id.voice_seekbar);

        mMusicRecyclerView = (RecyclerView) findViewById(R.id.music_list);
        if(mMusicAdapter == null) {
            mMusicAdapter = new AlivcMusicAdapter(getContext());
            mMusicAdapter.setOnItemClick(mOnItemClick);
            mMusicAdapter.startDefaultMusic();
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mMusicRecyclerView.setLayoutManager(layoutManager);
        mMusicRecyclerView.setAdapter(mMusicAdapter);
        mMusicRecyclerView.addItemDecoration(new AlivcDividerItemDecoration(
            getContext(), LinearLayoutManager.HORIZONTAL));

        mAccompanimentBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mVoiceBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        updateButton(mPosition > 0 ? true : false);

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(mAlivcLivePusher == null) {
            return;
        }
        try {
            if (id == R.id.pause) {
                boolean pauseSelected = mPause.isSelected();
                mPause.setText(pauseSelected ? getContext().getApplicationContext().getResources().getString(R.string.resume) : getContext().getApplicationContext().getResources().getString(R.string.pause));
                mPause.setSelected(!pauseSelected);
                isPause = !pauseSelected;
                if (!pauseSelected) {
                    //mAlivcLivePusher.resumeBGM();
                } else {
                    //mAlivcLivePusher.pauseBGM();
                }

            } else if (id == R.id.stop) {
                boolean stopSelected = mStop.isSelected();
                mStop.setText(stopSelected ? getContext().getApplicationContext().getResources().getString(R.string.start) : getContext().getApplicationContext().getResources().getString(R.string.stop));
                mStop.setSelected(!stopSelected);
                isStop = !stopSelected;
                mPause.setEnabled(isStop);
                mPause.setText(getContext().getApplicationContext().getResources().getString(R.string.pause));
                mPause.setSelected(true);
                if (!stopSelected) {
                    //mAlivcLivePusher.startBGMAsync(mMusicInfo.getPath());
                } else {
                    //mAlivcLivePusher.stopBGMAsync();
                }

            } else if (id == R.id.loop) {
                boolean loopSelected = mLoop.isSelected();
                //mAlivcLivePusher.setBGMLoop(!loopSelected);
                mLoop.setText(loopSelected ? getContext().getApplicationContext().getResources().getString(R.string.open_loop) : getContext().getApplicationContext().getResources().getString(R.string.close_loop));
                mLoop.setSelected(!loopSelected);
                isLoop = !loopSelected;
                if (mMusicAdapter != null) {
                    mMusicAdapter.updateItemView((AlivcMusicAdapter.MusicViewHolder)mMusicRecyclerView
                        .findViewHolderForAdapterPosition(mPosition), mPosition, !loopSelected);
                }

            } else if (id == R.id.mute) {
                boolean isSelect = mMute.isSelected();
                //mAlivcLivePusher.setBGMVolume(0);
                if (mPosition == 0) {
                    mAccompanimentBar.setEnabled(false);
                    mVoiceBar.setEnabled(true);
                } else {
                    mAccompanimentBar.setEnabled(isSelect);
                    mVoiceBar.setEnabled(isSelect);
                }

                mMute.setText(isSelect ? getContext().getApplicationContext().getResources().getString(R.string.open_mute) : getContext().getApplicationContext().getResources().getString(R.string.close_mute));
                mMute.setSelected(!isSelect);
                isMute = !isSelect;

            } else if (id == R.id.ears_back) {
                boolean erasSelected = mEarsBack.isSelected();
                //mAlivcLivePusher.setBGMEarsBack(!erasSelected);
                mEarsBack.setText(
                    erasSelected ? getContext().getApplicationContext().getResources().getString(R.string.open_ears_back) : getContext().getApplicationContext().getResources().getString(R.string.close_ears_back));
                mEarsBack.setSelected(!erasSelected);
                isEarsBack = !erasSelected;

            } else if (id == R.id.audio_denoise) {
                boolean audioSelected = mAudioDenoise.isSelected();
                //mAlivcLivePusher.setAudioDenoise(!audioSelected);
                mAudioDenoise.setText(
                    audioSelected ? getContext().getApplicationContext().getResources().getString(R.string.open_audio_denoise) : getContext().getApplicationContext().getResources().getString(R.string.close_audio_denoise));
                mAudioDenoise.setSelected(!audioSelected);
                isAudioDenoise = !audioSelected;

            } else {
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            try{
                int seekBarId = seekBar.getId();

                if(mAccompanimentBar.getId() == seekBarId) {
                    mAccompanimentText.setText(String.valueOf(progress));
                    //mAlivcLivePusher.setBGMVolume(progress);
                } else if(mVoiceBar.getId() == seekBarId) {
                    mVoiceText.setText(String.valueOf(progress));
                    //mAlivcLivePusher.setMicrophoneVolume(progress);
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
        this.mAlivcLivePusher = alivcLivePusher;
    }

    public interface OnItemClick {
        void onItemClick(AlivcMusicAdapter.MusicInfo musicInfo, int position);
    }

    private OnItemClick mOnItemClick = new OnItemClick() {
        @Override
        public void onItemClick(AlivcMusicAdapter.MusicInfo musicInfo, int position) {
            mMusicInfo = musicInfo;
            mPosition = position;
            updateButtonState(position > 0 ? true : false);
            if(musicInfo.getPath() != null && !musicInfo.getPath().isEmpty()) {
                startBGMAsync(musicInfo.getPath());
            } else {
                try {
                    //mAlivcLivePusher.stopBGMAsync();
                } catch (IllegalStateException e) {

                }
            }
        }
    };

    private void updateButton(boolean bool) {
        mPause.setEnabled(bool);
        mStop.setEnabled(bool);
        mLoop.setEnabled(bool);
//        mMute.setEnabled(bool);

        if(mPosition == 0) {
            mAccompanimentBar.setEnabled(mMute.isSelected());
            mVoiceBar.setEnabled(!mMute.isSelected());
        } else {
            mAccompanimentBar.setEnabled(!mMute.isSelected());
            mVoiceBar.setEnabled(!mMute.isSelected());
        }
    }

    private void updateButtonState(boolean bool) {
        updateButton(bool);
        mPause.setText(getContext().getApplicationContext().getResources().getString(R.string.pause));
        mStop.setText(getContext().getApplicationContext().getResources().getString(R.string.stop));
        mPause.setSelected(true);
        mStop.setSelected(true);
    }

    public void updateProgress(long progress, long totalTime) {
        if(mMusicAdapter != null) {
            mMusicAdapter.updateProgress((AlivcMusicAdapter.MusicViewHolder) mMusicRecyclerView.findViewHolderForAdapterPosition(mPosition), progress, totalTime);
        }
    }

    private void startBGMAsync(String path) {
        if(mAlivcLivePusher != null) {
            try {
                //mAlivcLivePusher.startBGMAsync(path);
            } catch (IllegalStateException e) {

            }
        }
    }

}

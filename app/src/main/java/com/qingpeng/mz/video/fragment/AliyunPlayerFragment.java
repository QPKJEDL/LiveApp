package com.qingpeng.mz.video.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.qingpeng.mz.R;
import com.qingpeng.mz.base.BaseFragment;
import com.qingpeng.mz.views.AliyunVodPlayerView;
import com.qingpeng.mz.views.PlayParameter;

public class AliyunPlayerFragment extends BaseFragment {

    private AliyunVodPlayerView mAliyunVodPlayerView = null;
    private static final String URL_KEY = "url_key";

    @Override
    protected int getLayoutId() {
        return R.layout.alivc_player_layout_skin;
    }

    @Override
    protected int getRefreshId() {
        return 0;
    }

    @Override
    protected int getListViewId() {
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAliyunVodPlayerView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAliyunVodPlayerView.onDestroy();
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        super.init(view, savedInstanceState);
        mAliyunVodPlayerView = (AliyunVodPlayerView) findView(R.id.video_view);
        //保持屏幕敞亮
        mAliyunVodPlayerView.setKeepScreenOn(true);
        PlayParameter.PLAY_PARAM_URL = getArguments().getString(URL_KEY);
//        String sdDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test_save_cache";
//        mAliyunVodPlayerView.setPlayingCache(false, sdDir, 60 * 60 /*时长, s */, 300 /*大小，MB*/);
//        mAliyunVodPlayerView.setTheme(AliyunVodPlayerView.Theme.Blue);
        //mAliyunVodPlayerView.setCirclePlay(true);
//        mAliyunVodPlayerView.setAutoPlay(true);
        setPlaySource();
//        mAliyunVodPlayerView.setOnPreparedListener(new MyPrepareListener(this));
//        mAliyunVodPlayerView.setNetConnectedListener(new AliyunVodPlayerView.MyNetConnectedListener(this));
//        mAliyunVodPlayerView.setOnCompletionListener(new MyCompletionListener(this));
//        mAliyunVodPlayerView.setOnFirstFrameStartListener(new MyFrameInfoListener(this));
//        mAliyunVodPlayerView.setOnChangeQualityListener(new MyChangeQualityListener(this));
//        mAliyunVodPlayerView.setOnStoppedListener(new MyStoppedListener(this));
//        mAliyunVodPlayerView.setmOnPlayerViewClickListener(new MyPlayViewClickListener());
//        mAliyunVodPlayerView.setOrientationChangeListener(new MyOrientationChangeListener(this));
//        mAliyunVodPlayerView.setOnUrlTimeExpiredListener(new MyOnUrlTimeExpiredListener(this));
//        mAliyunVodPlayerView.setOnShowMoreClickListener(new MyShowMoreClickLisener(this));
        mAliyunVodPlayerView.enableNativeLog();
    }

    public static AliyunPlayerFragment newInstance(String url) {
        AliyunPlayerFragment aliyunPlayerFragment = new AliyunPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(URL_KEY, url);
        PlayParameter.PLAY_PARAM_TYPE = "localSource";
        aliyunPlayerFragment.setArguments(bundle);
        return aliyunPlayerFragment;
    }

    private void setPlaySource() {
        if ("localSource".equals(PlayParameter.PLAY_PARAM_TYPE)) {
            AliyunLocalSource.AliyunLocalSourceBuilder alsb = new AliyunLocalSource.AliyunLocalSourceBuilder();
            alsb.setSource(PlayParameter.PLAY_PARAM_URL);
            Uri uri = Uri.parse(PlayParameter.PLAY_PARAM_URL);
            if ("rtmp".equals(uri.getScheme())) {
                alsb.setTitle("");
            }
            AliyunLocalSource localSource = alsb.build();
            mAliyunVodPlayerView.setLocalSource(localSource);
        }
    }
}

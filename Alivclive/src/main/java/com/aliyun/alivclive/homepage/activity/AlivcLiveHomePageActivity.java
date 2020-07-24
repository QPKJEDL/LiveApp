package com.aliyun.alivclive.homepage.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alivc.im.AlivcIMLogUtil;
import com.aliyun.alivclive.R;
import com.aliyun.alivclive.homepage.adapter.HomePagerAdapter;
import com.aliyun.alivclive.homepage.fragment.LiveListFragment;
import com.aliyun.alivclive.homepage.fragment.ReplayListFragment;
import com.aliyun.alivclive.utils.http.AlivcHttpManager;
import com.aliyun.alivclive.setting.manager.AlivcLiveUserManager;
import com.aliyun.alivclive.utils.http.AlivcStsManager;
import com.aliyun.alivclive.room.LiveRoomPushActivity;
import com.aliyun.alivclive.room.userlist.AlivcLiveUserInfo;
import com.aliyun.alivclive.utils.HandleUtils;
import com.aliyun.alivclive.utils.http.HttpEngine;
import com.aliyun.alivclive.utils.http.HttpResponse;
import com.aliyun.alivclive.utils.LogUtils;
import com.aliyun.alivclive.utils.UIUtils;
import com.aliyun.alivclive.view.indicator.CommonNavigator;
import com.aliyun.alivclive.view.indicator.CommonNavigatorAdapter;
import com.aliyun.alivclive.view.indicator.CustomIndicator;
import com.aliyun.alivclive.view.indicator.IPagerIndicator;
import com.aliyun.alivclive.view.indicator.IPagerTitleView;
import com.aliyun.alivclive.view.indicator.LinePagerIndicator;
import com.aliyun.alivclive.view.indicator.SimplePagerTitleView;
import com.aliyun.alivclive.view.indicator.ViewPagerHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akira on 2018/5/27.
 * <p>
 * Home Activity
 */

public class AlivcLiveHomePageActivity extends Activity implements View.OnClickListener {

    private final String TAG = "AlivcLiveHomePageActivity";

    private ViewPager mViewPager;
    private CustomIndicator mIndicator;

    private TextView mTvTitle;
    private ImageView mIvBack;
    private ImageView mIvPrepareLivnig;

    private AlivcLiveUserInfo mUserInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        setContentView(R.layout.activity_homepage);
        AlivcIMLogUtil.enalbeDebug();
        if (!permissionCheck()) {
            if (Build.VERSION.SDK_INT >= 23) {
                ActivityCompat.requestPermissions(this, permissionManifest, PERMISSION_REQUEST_CODE);
            } else {
                showNoPermissionTip(getString(noPermissionTip[mNoPermissionIndex]));
                finish();
            }
        }
        initViews();
        initData();
    }

    //todo 放到应用最开始
    private void initData() {
        mUserInfo = AlivcLiveUserManager.getInstance().getUserInfo(this);
    }

    private void initViews() {
        mTvTitle = findViewById(R.id.titlebar_title);
        mIvBack = findViewById(R.id.titlebar_back);
        mIvPrepareLivnig = findViewById(R.id.prepare_living);
        mIvPrepareLivnig.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mTvTitle.setText(R.string.alivc_interactive_live);

        mViewPager = findViewById(R.id.view_pager);
        mIndicator = findViewById(R.id.home_indicator);

        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(new LiveListFragment());
        fragments.add(new ReplayListFragment());

        HomePagerAdapter fragmentAdapter = new HomePagerAdapter(getFragmentManager());
        fragmentAdapter.setData(fragments);
        mViewPager.setAdapter(fragmentAdapter);
        final String[] titles = {getString(R.string.alivc_live), getString(R.string.alivc_replay)};
        final int titleViewWidth = UIUtils.getScreenWidth(this) / (titles.length);
        mIndicator.setBackgroundColor(Color.TRANSPARENT);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setScrollPivotX(0.5f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return fragments == null ? 0 : fragments.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(titles[index]);
                simplePagerTitleView.setNormalColor(Color.WHITE);
                simplePagerTitleView.setSelectedColor(Color.WHITE);
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                simplePagerTitleView.setTextSize(14);
                simplePagerTitleView.setWidth(titleViewWidth);
                simplePagerTitleView.setHeight(UIUtils.dip2px(AlivcLiveHomePageActivity.this, 20));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setYOffset(UIUtils.dip2px(AlivcLiveHomePageActivity.this, 0));
                indicator.setColors(Color.parseColor("#00c1de"));
                indicator.setLineHeight(UIUtils.dip2px(AlivcLiveHomePageActivity.this, 2));
                return indicator;
            }
        });
        mIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mIndicator, mViewPager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        AlivcStsManager.getInstance().checkSts(AlivcLiveHomePageActivity.this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (mIvBack.getId() == viewId) {
            finish();
        } else if (mIvPrepareLivnig.getId() == viewId) {
            if (AlivcStsManager.getInstance().isValid()) {
                goToLive();
            } else {
                //TODO 这里不应该这做这种判定 目前保护
                AlivcStsManager.getInstance().refreshStsToken(mUserInfo.getUserId());
                HandleUtils.getMainThreadHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        goToLive();
                    }
                }, 3000);
            }

        }

    }

    public void goToLive() {
        if (TextUtils.isEmpty(mUserInfo.getRoomId())) {
            AlivcHttpManager.getInstance().createRoom(mUserInfo.getUserId(), mUserInfo.getNickName(), new HttpEngine.OnResponseCallback<HttpResponse.Room>() {
                @Override
                public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.Room data) {
                    if (result) {
                        if (data != null && data.data != null) {
                            LogUtils.d(TAG, "create room success, id = " + data.data.getRoom_id());
                            mUserInfo.setRoomId(data.data.getRoom_id());
                            AlivcLiveUserManager.getInstance().setUserInfo(AlivcLiveHomePageActivity.this, mUserInfo);
                        }
                    }

                    HandleUtils.getMainThreadHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.setClass(AlivcLiveHomePageActivity.this, LiveRoomPushActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            });
        } else {
            HandleUtils.getMainThreadHandler().post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.setClass(AlivcLiveHomePageActivity.this, LiveRoomPushActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private int mNoPermissionIndex = 0;
    private final int PERMISSION_REQUEST_CODE = 1;
    private final String[] permissionManifest = {
            Manifest.permission.CAMERA,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private final int[] noPermissionTip = {
            R.string.no_camera_permission,
            R.string.no_record_bluetooth_permission,
            R.string.no_record_audio_permission,
            R.string.no_read_phone_state_permission,
            R.string.no_write_external_storage_permission,
            R.string.no_read_external_storage_permission,
    };

    private boolean permissionCheck() {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        String permission;
        for (int i = 0; i < permissionManifest.length; i++) {
            permission = permissionManifest[i];
            mNoPermissionIndex = i;
            if (PermissionChecker.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionCheck = PackageManager.PERMISSION_DENIED;
            }
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    private void showNoPermissionTip(String tip) {
        Toast.makeText(this, tip, Toast.LENGTH_LONG).show();
    }
}

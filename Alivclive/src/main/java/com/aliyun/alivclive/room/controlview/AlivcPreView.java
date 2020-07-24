package com.aliyun.alivclive.room.controlview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.aliyun.alivclive.R;
import com.aliyun.alivclive.utils.http.AlivcHttpManager;
import com.aliyun.alivclive.setting.manager.AlivcLiveUserManager;
import com.aliyun.alivclive.room.userlist.AlivcLiveUserInfo;

import com.aliyun.alivclive.utils.HandleUtils;
import com.aliyun.alivclive.utils.http.HttpEngine;
import com.aliyun.alivclive.utils.http.HttpResponse;
import com.aliyun.alivclive.utils.LogUtils;
import com.aliyun.pusher.core.utils.AnimUitls;

/**
 * 相机控制View类:控制摄像头的切换，闪光灯的切换等
 *
 * @author Mulberry
 * create on 2018/4/19.
 */

public class AlivcPreView extends FrameLayout implements OnClickListener, RadioGroup.OnCheckedChangeListener {


    private View startLivebt;
    private RadioGroup mRadioGroup;
    public static final int QUITY_SMOOTH = 1;
    public static final int QUITY_SD = 2;
    public static final int QUITY_HD = 3;
    private View mBeautybt;
    private View mswitchCamera;
    private ImageView flashBtn;
    private View mBottomLayout;

    public AlivcPreView(Context context) {
        super(context);
        initView();
    }

    public AlivcPreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AlivcPreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext().getApplicationContext()).inflate(R.layout.alivc_preshow_layout,
                this, true);

        startLivebt = findViewById(R.id.startLivebt);
        mBottomLayout = findViewById(R.id.preview_bottom);
        mRadioGroup = findViewById(R.id.radiogroup);
        mBeautybt = findViewById(R.id.beauty_btn);
        mswitchCamera = findViewById(R.id.switch_camera);
        flashBtn = findViewById(R.id.flash_btn);
        View back = findViewById(R.id.back);
        back.setOnClickListener(this);
        startLivebt.setOnClickListener(this);
        mBeautybt.setOnClickListener(this);
        mswitchCamera.setOnClickListener(this);
        flashBtn.setOnClickListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);
        flashBtn.setAlpha(0.7f);
        flashBtn.setClickable(false);
        flashBtn.setActivated(false);
        mswitchCamera.setActivated(false);
    }



    public void showBottomLayout(boolean show) {
        if (show) {
            AnimUitls.startAppearAnimY(mBottomLayout);
            mBottomLayout.setVisibility(View.VISIBLE);
        } else {
            AnimUitls.startDisappearAnimY(mBottomLayout);
            mBottomLayout.setVisibility(View.GONE);
        }
    }

    public int getBottomLayoutVisibility() {
        return mBottomLayout.getVisibility();
    }

    @Override
    public void onClick(View v) {
        if (listener == null) {
            return;
        }

        if (v.getId() == R.id.startLivebt) {
            if (!TextUtils.isEmpty(AlivcLiveUserManager.getInstance().getUserInfo(getContext()).getRoomId())) {
                listener.startLive();
            } else {
                final AlivcLiveUserInfo userInfo = AlivcLiveUserManager.getInstance().getUserInfo(getContext());
                AlivcHttpManager.getInstance().createRoom(userInfo.getUserId()
                        , userInfo.getNickName(), new HttpEngine.OnResponseCallback<HttpResponse.Room>() {
                            @Override
                            public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.Room data) {
                                if (result) {
                                    if (data != null && data.data != null) {
                                        LogUtils.d("AlivcPreView", "create room success, id = " + data.data.getRoom_id());
                                        if (TextUtils.isEmpty(data.data.getRoom_id())) {
                                            Toast.makeText(getContext(), "room id is null", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        userInfo.setRoomId(data.data.getRoom_id());
                                        AlivcLiveUserManager.getInstance().setUserInfo(getContext(), userInfo);

                                        HandleUtils.getMainThreadHandler().post(new Runnable() {
                                            @Override
                                            public void run() {
                                                listener.startLive();
                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(getContext(), "room id is null", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        } else if (v.getId() == R.id.back) {
            listener.onclose();
        } else if (v.getId() == R.id.beauty_btn) {
            listener.onbeauty();
        } else if (v.getId() == R.id.switch_camera) {
            mswitchCamera.setActivated(!mswitchCamera.isActivated());
            listener.onSwitchCamera(mswitchCamera.isActivated());
            flashBtn.setImageResource(R.drawable.icon_flash_off);

            if (mswitchCamera.isActivated()) {
                //后置
                flashBtn.setClickable(true);
                flashBtn.setAlpha(1f);
            } else {
                //前置
                flashBtn.setAlpha(0.7f);
                flashBtn.setClickable(false);
                flashBtn.setActivated(false);
                listener.onflash(false);
            }

        } else if (v.getId() == R.id.flash_btn) {
            boolean activated = !v.isActivated();
            v.setActivated(activated);
            listener.onflash(activated);
            flashBtn.setImageResource(activated ? R.drawable.icon_flash : R.drawable.icon_flash_off);

        }
    }

    public Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (listener == null) return;
        int quity = -1;
        if (checkedId == R.id.smooth) {
            quity = QUITY_SMOOTH;
        } else if (checkedId == R.id.HD) {
            quity = QUITY_HD;

        } else if (checkedId == R.id.SD) {
            quity = QUITY_SD;

        }
        if (quity != -1) {
            listener.onQuityChange(quity);
        }
    }

    public interface Listener {
        void startLive();

        void onQuityChange(int quity);

        void onbeauty();

        void onSwitchCamera(boolean activated);

        void onflash(boolean activated);

        void onclose();
    }
}


package com.aliyun.alivclive.room;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.alivc.live.room.constants.AlivcLiveRole;
import com.aliyun.alivclive.R;
import com.aliyun.alivclive.listener.RoomActListener;
import com.aliyun.alivclive.room.roominfo.AlivcLiveRoomInfo;
import com.aliyun.alivclive.setting.manager.AlivcLiveUserManager;
import com.aliyun.alivclive.room.userlist.AlivcLiveUserInfo;
import com.aliyun.alivclive.room.userlist.AlivcUserInfo;
import com.aliyun.alivclive.utils.Base64;
import com.aliyun.alivclive.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Mulberry
 *         create on 2018/4/24.
 */

public class LiveRoomPushActivity extends AppCompatActivity {
    private static final String TAG = LiveRoomPushActivity.class.getName().toString();
    private AlivcLiveRoomView alivcChatRoomView;

    private LocalBroadcastManager mLocalBroadcastManager;

    //close button 考虑到后期 隐藏UI
    private ImageView mIvClose;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.alivc_room_pusher_layout);
        alivcChatRoomView = (AlivcLiveRoomView) findViewById(R.id.alivc_chat_room_view);
        mIvClose = findViewById(R.id.iv_close);
        mIvClose.setVisibility(View.GONE);
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        AlivcLiveUserInfo user = AlivcLiveUserManager.getInstance().getUserInfo(getApplicationContext());

        AlivcUserInfo userInfo = new AlivcUserInfo();
        userInfo.userId = user.getUserId();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("avatar", user.getAvatar());
            jsonObject.put("user_id", user.getUserId());
            jsonObject.put("nick_name", user.getNickName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonString = jsonObject.toString();
        userInfo.userDesp = Base64.encodeToString(jsonString.getBytes(), Base64.DEFAULT);

        AlivcLiveRoomInfo roomInfo = new AlivcLiveRoomInfo();
        roomInfo.setRoom_id(user.getRoomId());
        roomInfo.setStreamer_id(user.getUserId());
        LogUtils.d(TAG, "push user info  = " + userInfo.toString());
        LogUtils.d(TAG, "push room id  = " + roomInfo.getRoom_id());
        alivcChatRoomView.init(this, roomInfo, userInfo, AlivcLiveRole.ROLE_HOST);

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        IntentFilter intentFilter = new IntentFilter("closeRoom");
        mLocalBroadcastManager.registerReceiver(mReceiver, intentFilter);
        alivcChatRoomView.setOnCloseRoomClickListener(new RoomActListener() {
            @Override
            public void onClose() {
                finish();
            }

            @Override
            public void onKickOut() {

            }
        });
    }

    public void setCloseVisable() {
        if (mIvClose != null) {
            mIvClose.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPause) {
            isPause = false;
            alivcChatRoomView.onResume();
        }
    }

    private boolean isPause = false;

    @Override
    protected void onPause() {
        super.onPause();
        if (alivcChatRoomView != null) {
            try {
                if (alivcChatRoomView != null) {
                    isPause = true;
                    alivcChatRoomView.onPause();
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (alivcChatRoomView != null) {
            try {
                alivcChatRoomView.release();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        mLocalBroadcastManager.unregisterReceiver(mReceiver);
        super.onDestroy();
    }

}

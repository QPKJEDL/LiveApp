package com.aliyun.alivclive.room;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.alivc.live.room.constants.AlivcLiveRole;
import com.aliyun.alivclive.R;
import com.aliyun.alivclive.homepage.fragment.LiveListFragment;
import com.aliyun.alivclive.listener.RoomActListener;
import com.aliyun.alivclive.room.roominfo.AlivcLiveRoomInfo;
import com.aliyun.alivclive.setting.manager.AlivcLiveUserManager;
import com.aliyun.alivclive.room.userlist.AlivcLiveUserInfo;
import com.aliyun.alivclive.room.userlist.AlivcUserInfo;

import com.aliyun.alivclive.utils.Base64;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Mulberry
 *         create on 2018/4/24.
 */

public class LiveRoomPlayActivity extends AppCompatActivity {
    private static final String TAG = LiveRoomPlayActivity.class.getName().toString();
    private AlivcLiveRoomView alivcChatRoomView;
    private ImageView mIvClose;
    private AlivcLiveUserInfo mUserInfo;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.alivc_room_pusher_layout);

        String roomId = getIntent().getStringExtra("roomId");
        String streamerId = getIntent().getStringExtra("streamerId");
        alivcChatRoomView = (AlivcLiveRoomView) findViewById(R.id.alivc_chat_room_view);
        mIvClose = findViewById(R.id.iv_close);
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        AlivcUserInfo alivcUserInfo = new AlivcUserInfo();
        mUserInfo = AlivcLiveUserManager.getInstance().getUserInfo(getApplicationContext());
        alivcUserInfo.userId = mUserInfo.getUserId();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("avatar", mUserInfo.getAvatar());
            jsonObject.put("user_id", mUserInfo.getUserId());
            jsonObject.put("nick_name", mUserInfo.getNickName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonString = jsonObject.toString();
        alivcUserInfo.userDesp = Base64.encodeToString(jsonString.getBytes(), Base64.DEFAULT);
        AlivcLiveRoomInfo roomInfo = new AlivcLiveRoomInfo();
        roomInfo.setStreamer_id(streamerId);
        roomInfo.setRoom_id(roomId);
        alivcChatRoomView.init(getApplicationContext(), roomInfo, alivcUserInfo, AlivcLiveRole.ROLE_AUDIENCE);

        alivcChatRoomView.enterRoom(roomId, AlivcLiveRole.ROLE_AUDIENCE);
        alivcChatRoomView.queryLatestRoomInfo(roomId);
        alivcChatRoomView.setOnCloseRoomClickListener(new RoomActListener() {
            @Override
            public void onClose() {
                finish();
            }

            @Override
            public void onKickOut() {
                setResult(LiveListFragment.KICKOUT);
                finish();
            }
        });
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
        super.onDestroy();
    }


}

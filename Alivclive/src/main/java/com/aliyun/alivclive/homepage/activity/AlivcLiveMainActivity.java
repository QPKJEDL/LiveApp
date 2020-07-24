package com.aliyun.alivclive.homepage.activity;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.aliyun.alivclive.R;
import com.aliyun.alivclive.room.LiveRoomPlayActivity;
import com.aliyun.alivclive.room.LiveRoomPushActivity;
import com.aliyun.alivclive.room.userlist.AlivcUserInfo;
import com.aliyun.pusher.core.utils.FileUtils;
import com.aliyun.alivclive.utils.http.HttpEngine;
import com.aliyun.alivclive.utils.http.HttpResponse;

public class AlivcLiveMainActivity extends ListActivity {

    String[] modules = null;

    private AlivcUserInfo userInfo = new AlivcUserInfo();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //FileUtils.copyAsset(this);
        FileUtils.copyAll(this);
        modules = new String[]{
                getString(R.string.product_interactive_live),
                getString(R.string.product_interactive_live_play),
        };
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, modules));

        if (!permissionCheck()) {
            if (Build.VERSION.SDK_INT >= 23) {
                ActivityCompat.requestPermissions(this, permissionManifest, PERMISSION_REQUEST_CODE);
            } else {
                showNoPermissionTip(getString(noPermissionTip[mNoPermissionIndex]));
                finish();
            }
        }
        userInfo.userId = "miyou";
        userInfo.userDesp = "miyou";
        new HttpEngine().newSts(userInfo.userId, new HttpEngine.OnResponseCallback<HttpResponse.StsTokenBean>() {
            @Override
            public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.StsTokenBean data) {
                if (result) {
                    if (data != null && data.data != null && data.data.SecurityTokenInfo != null) {
                        userInfo.stsAccessKey = data.data.SecurityTokenInfo.AccessKeyId;
                        userInfo.stsSecretKey = data.data.SecurityTokenInfo.AccessKeySecret;
                        userInfo.stsSecurityToken = data.data.SecurityTokenInfo.SecurityToken;
                        userInfo.stsExpireTime = data.data.SecurityTokenInfo.Expiration;
                    }
                } else {
                    Log.w("", "request failure");
                }
            }
        });


    }

    @Override
    public void onListItemClick(ListView parent, View v, int position, long id) {
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(this, LiveRoomPushActivity.class);
                intent.putExtra("user", userInfo);
                startActivity(intent);
                break;
            case 1:
                final EditText et = new EditText(this);

                new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.alivc_enter_room_tips))
                        .setView(et)
                        .setPositiveButton(getResources().getString(R.string.alivc_enter_room_confirm), new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String input = et.getText().toString();
                                if ("".equals(input)) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.alivc_enter_room_check_null_tips) + input, Toast.LENGTH_LONG).show();
                                } else {
                                    Intent intent = new Intent();
                                    intent.putExtra("roomId", input);
                                    intent.putExtra("user", userInfo);
                                    intent.setClass(AlivcLiveMainActivity.this, LiveRoomPlayActivity.class);
                                    startActivity(intent);

                                }
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.alivc_enter_room_cancel), null)
                        .show();
                break;

            default:
                break;
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

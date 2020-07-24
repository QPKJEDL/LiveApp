package com.aliyun.alivclive.room;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import com.alivc.auth.AlivcSts;
import com.alivc.interactive.listener.IAlivcInteractiveNotifyListener;
import com.alivc.live.base.AlivcCommonError;
import com.alivc.live.base.IAlivcCallback;
import com.alivc.live.base.IAlivcErrorListener;
import com.alivc.live.room.constants.AlivcLiveRole;
import com.alivc.live.room.constants.AlivcResolutionMode;
import com.alivc.live.room.listener.IAlivcAuthListener;
import com.alivc.live.room.listener.IAlivcLiveRoomNotifyListener;
import com.alivc.live.room.listener.IAlivcNetworkListener;
import com.alivc.live.room.listener.IAlivcPlayerNotifyListener;
import com.alivc.live.room.listener.IAlivcPusherNotifyListener;
import com.alivc.live.room.model.AlivcBeautyParams;
import com.alivc.live.room.view.AlivcSurfaceView;
import com.aliyun.alivclive.R;
import com.aliyun.alivclive.utils.constants.AlivcAppMsgEvent;

import com.aliyun.alivclive.setting.fragment.UserCardDialogFragment;
import com.aliyun.alivclive.listener.IStsTokenListener;


import com.aliyun.alivclive.listener.OnOperateUserListener;

import com.aliyun.alivclive.room.roominfo.AlivcLiveRoomInfo;
import com.aliyun.alivclive.utils.http.AlivcHttpManager;
import com.aliyun.alivclive.setting.manager.AlivcLiveUserManager;
import com.aliyun.alivclive.utils.http.AlivcStsManager;

import com.aliyun.alivclive.room.chatlist.ailp.AlivcChatListView;
import com.aliyun.alivclive.room.chatlist.ailp.OnCellClickListener;
import com.aliyun.alivclive.room.comment.AlivcLiveMessageInfo;
import com.aliyun.alivclive.room.comment.AlivcLiveMessageInfo.AlivcMsgType;
import com.aliyun.alivclive.room.comment.InputDialog;
import com.aliyun.alivclive.room.controlview.AlivcControlView;
import com.aliyun.alivclive.room.controlview.AlivcControlView.OnControlClickListener;
import com.aliyun.alivclive.room.controlview.AlivcPreView;
import com.aliyun.alivclive.room.like.AlivcLikeView;
import com.aliyun.alivclive.room.like.AlivcLikeView.OnLikeClickListener;
import com.aliyun.alivclive.room.roominfo.AlivcRoomInfoView;
import com.aliyun.alivclive.room.userlist.AlivcLiveUserInfo;
import com.aliyun.alivclive.room.userlist.AlivcUserInfo;
import com.aliyun.alivclive.room.userlist.OperateUserManager;

import com.aliyun.alivclive.utils.Base64;

import com.aliyun.alivclive.utils.HandleUtils;
import com.aliyun.alivclive.utils.http.HttpEngine;
import com.aliyun.alivclive.utils.http.HttpResponse;
import com.aliyun.alivclive.utils.LogUtils;

import com.aliyun.alivclive.utils.constants.AlivcConstants;
import com.aliyun.pusher.core.listener.OnBeautyParamsChangeListener;
import com.aliyun.pusher.core.listener.OnItemClickListener;
import com.aliyun.pusher.core.listener.OnViewClickListener;
import com.aliyun.pusher.core.module.BeautyParams;
import com.aliyun.pusher.core.utils.AnimUitls;
import com.aliyun.pusher.core.utils.SharedPreferenceUtils;
import com.aliyun.pusher.core.utils.ToastUtils;
import com.aliyun.pusher.core.utils.constants.BeautyConstants;
import com.aliyun.pusher.core.view.AlivcBeautySettingView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 整体RoomView类，通过这个类来添加各个分层的view，接收view点击的事件监听
 * 包含:commentView，likeview,musicSelectView,controlview,beautyview
 *
 * @author Mulberry
 *         create on 2018/4/23.
 */

public class AlivcLiveRoomView extends AlivcLiveBaseRoomView {

    private static String SD_DIR = Environment.getExternalStorageDirectory().getPath() + File.separator;
    private static String RESOURCE_DIR = "alivc_resource";
    private static String filename = RESOURCE_DIR + File.separator + "watermark.png";
    public static final String waterMark = SD_DIR + filename;

    private static final String TAG = AlivcLiveRoomView.class.getName().toString();
    /**
     * 禁言时间 固定1分钟
     */
    private static final long forbidTime = 60 * 1000;
    /**
     * 踢人时间 固定1分钟
     */
    private static final long kickOutUserTime = 60 * 1000;

    /**
     * 美颜调节控制界面
     */
    private AlivcBeautySettingView beautySettingView;
    /**
     * 评论列表界面
     */
    private AlivcChatListView commentListView;
    /**
     * 界面控制按钮界面
     */
    private AlivcControlView controlView;
    private AlivcPreView preView;
    /**
     * 点赞动画界面
     */
    private AlivcLikeView likeView;

    /**
     * 输入框
     */
    private InputDialog alivcInputTextDialog;

    /**
     * 房间信息View:包含观众信息列表,点赞数
     */
    private AlivcRoomInfoView alivcRoomInfoView;

    private boolean isBeautyOn = false;
    private boolean hasJionRoom = false;
    private AlivcLiveRole mAlivcLiveRole;

    private AlivcLiveUserInfo mArchorInfo;
    private boolean mCameraIsBack;

    /**
     * Constructor.
     *
     * @param context the context
     */
    public AlivcLiveRoomView(Context context) {
        super(context);
    }

    /**
     * Constructor.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public AlivcLiveRoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructor.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public AlivcLiveRoomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init(Context context, AlivcLiveRoomInfo roomInfo, AlivcUserInfo alivcUserInfo, AlivcLiveRole alivcLiveRole) {
        super.init(context, roomInfo, alivcUserInfo, alivcLiveRole);
        mAlivcLiveRole = alivcLiveRole;
        copyAll(context);
        AlivcStsManager.getInstance().registerStsListener(mStsTokenListener);
        if (alivcLiveRole == AlivcLiveRole.ROLE_AUDIENCE) {
            initView();
        } else if (alivcLiveRole == AlivcLiveRole.ROLE_HOST) {
            initPreView();
            initBeautyView();
        }
    }

    public static void copyAll(Context cxt) {
        File dir = new File(SD_DIR);
        copySelf(cxt,"alivc_resource");
        dir.mkdirs();
    }

    public static void copySelf(Context cxt, String root) {
        try {
            String[] files = cxt.getAssets().list(root);
            if(files != null && files.length > 0) {
                File subdir = new File(SD_DIR + root);
                if (!subdir.exists()) {
                    subdir.mkdirs();
                }
                for (String fileName : files) {
                    if (new File(SD_DIR + root + File.separator + fileName).exists()){
                        continue;
                    }
                    copySelf(cxt,root + "/" + fileName);
                }
            }else{
                OutputStream myOutput = new FileOutputStream(SD_DIR+root);
                InputStream myInput = cxt.getAssets().open(root);
                byte[] buffer = new byte[1024 * 8];
                int length = myInput.read(buffer);
                while(length > 0)
                {
                    myOutput.write(buffer, 0, length);
                    length = myInput.read(buffer);
                }

                myOutput.flush();
                myInput.close();
                myOutput.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initPreView() {
        preView = new AlivcPreView(getContext());
        mAlivcLiveRoomConfig.setPushResolutionMode(AlivcResolutionMode.RESOLUTION_LD);
        preView.setListener(new AlivcPreView.Listener() {
            @Override
            public void startLive() {
                enterRoom(roomId, alivcLiveRole);
            }

            @Override
            public void onQuityChange(int quity) {
                if (quity == AlivcPreView.QUITY_SMOOTH) {
                    mAlivcLiveRoomConfig.setPushResolutionMode(AlivcResolutionMode.RESOLUTION_LD);
                    mSelectResolutionMode = AlivcResolutionMode.RESOLUTION_LD;
                } else if (quity == AlivcPreView.QUITY_SD) {
                    mAlivcLiveRoomConfig.setPushResolutionMode(AlivcResolutionMode.RESOLUTION_SD);
                    mSelectResolutionMode = AlivcResolutionMode.RESOLUTION_SD;
                } else if (quity == AlivcPreView.QUITY_HD) {
                    mAlivcLiveRoomConfig.setPushResolutionMode(AlivcResolutionMode.RESOLUTION_HD);
                    mSelectResolutionMode = AlivcResolutionMode.RESOLUTION_HD;
                }
            }

            @Override
            public void onbeauty() {
                showBeautyView(true);
                preView.showBottomLayout(false);
                preView.setTag(true);
            }

            @Override
            public void onSwitchCamera(boolean activated) {
                mCameraIsBack = activated;
                switchCamera();
            }

            @Override
            public void onflash(boolean activated) {
                setFlash(activated);
            }

            @Override
            public void onclose() {
                if (roomActListener != null) {
                    roomActListener.onClose();
                }
            }
        });
        addSubView(preView);
    }

    private void hidePreView() {
        if (preView != null) {
            removeView(preView);
            preView = null;
        }

        if (beautySettingView != null) {
            removeView(beautySettingView);
        }
    }

    @Override
    public void enterRoom(final String roomId, AlivcLiveRole alivcLiveRole) {
        if (alivcLiveRole == AlivcLiveRole.ROLE_HOST) {
            hidePreView();
            initView();
            if (mContext instanceof LiveRoomPushActivity) {
                LiveRoomPushActivity activity = (LiveRoomPushActivity) mContext;
                activity.setCloseVisable();
            }
        }
        super.enterRoom(roomId, alivcLiveRole);
    }

    @Override
    protected void handleRoomInfo() {
        super.handleRoomInfo();
        getLikeCount();
        getRoomInfo();
    }

    private IStsTokenListener mStsTokenListener = new IStsTokenListener() {
        @Override
        public void onTokenRefresh(AlivcSts sts) {
            LogUtils.d("AlivcStsManager", "alivcILiveRoom.refreshSts(sts); alivc live room refresh sts");
            if (alivcILiveRoom != null) {
                alivcILiveRoom.refreshSts(sts);
            }
        }
    };

    private void getRoomInfo() {
        AlivcHttpManager.getInstance().joinRoom(mRoomInfo.getRoom_id(), mRoomInfo.getStreamer_id(),
                userInfo.userId, new HttpEngine.OnResponseCallback<HttpResponse.Room>() {
                    @Override
                    public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.Room data) {
                        if (result) {
                            AlivcHttpManager.getInstance().notification(userInfo.userId, roomId, "e_enter_room", new HttpEngine.OnResponseCallback<HttpResponse.Notification>() {
                                @Override
                                public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.Notification data) {
                                    if (result) {
                                        hasJionRoom = true;
                                    } else {
                                        hasJionRoom = false;
                                    }
                                }
                            });
                        }
                    }
                });

        if (mAlivcLiveRole == AlivcLiveRole.ROLE_HOST) {
            AlivcHttpManager.getInstance().startLive(userInfo.userId, roomId, new HttpEngine.OnResponseCallback<HttpResponse.Notification>() {
                @Override
                public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.Notification data) {
                    if (result) {
                        LogUtils.d(TAG, "archor start live");
                    }
                }
            });
        }

        AlivcHttpManager.getInstance().getRoomDetail(roomId, new HttpEngine.OnResponseCallback<HttpResponse.RoomDetail>() {
            @Override
            public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.RoomDetail data) {
                if (result) {
                    if (data != null && data.data != null) {
                        mArchorInfo = data.data.streamer_info;
                        AlivcLiveRoomInfo roomInfo = data.data.room_info;

                        String streamerId = null;
                        if (mArchorInfo != null) {
                            LogUtils.d(TAG, "room detail mArchorInfo  = " + mArchorInfo.toString());
                            alivcRoomInfoView.setArchorInfo(mArchorInfo);
                            streamerId = mArchorInfo.getUserId();

                        }
                        if (roomInfo != null) {
                            LogUtils.d(TAG, "room detail roomInfo  = " + roomInfo.toString());
                            if (roomInfo.getRoom_user_list() != null) {
                                LogUtils.d(TAG, "room detail list = " + roomInfo.getRoom_user_list().size());
                                for (int i = 0; i < roomInfo.getRoom_user_list().size(); i++) {
                                    if (roomInfo.getRoom_user_list().get(i) != null
                                            && roomInfo.getRoom_user_list().get(i).getUserId().equals(streamerId)) {
                                        roomInfo.getRoom_user_list().remove(i);
                                    }
                                }
                            }
                            alivcRoomInfoView.setRoomInfo(roomInfo);
                        }

                    }
                }

            }
        });
    }

    private void getLikeCount() {

        alivcILiveRoom.getLikeCount(new IAlivcCallback<Long, AlivcCommonError>() {
            @Override
            public void onSuccess(Long alivcGetDigCountResult) {
                if (alivcGetDigCountResult != null) {
                    int count = 0;
                    if (alivcGetDigCountResult > 0) {
                        count = alivcGetDigCountResult.intValue();
                    }

                    final int finalCount = count;
                    HandleUtils.getMainThreadHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            alivcRoomInfoView.updateLikeCount(finalCount);
                        }
                    });
                }
            }

            @Override
            public void onFailure(AlivcCommonError alivcCommonError) {

            }
        });
    }


    private void initView() {
        //initAlivcLiveRoomView();
        initCommentView();
        initControlView();
        initLikeView();
//        initMusicSelectView();
        initAlivcRoomInfoView();

        //setAlivcLiveRoomCallback(alivcLiveRoomCallback);, todo ! --miyo
        setAlivcLiveRoomNotifyListener(alivcLiveAuthNotifyListener, alivclivePushNotifyListener, alivcLivePlayerListener, alivcLiveRoomNetworkListener, alivcInteractiveListener, alivcLiveRoomNotifyListener);
        setAlivcLiveRoomErrorListener(alivcLiveRoomErrorListener);
        if (AlivcLiveRole.ROLE_HOST == mAlivcLiveRole && beautySettingView != null) {
            addSubView(beautySettingView);
        }
    }

    private void initBeautyView() {
        if (mAlivcLiveRole != AlivcLiveRole.ROLE_HOST) {
            return;
        }
        beautySettingView = new AlivcBeautySettingView(getContext());

        mBeautyParams = getBeautyParams();
        if (mBeautyParams == null) {
            mBeautyParams = BeautyConstants.BEAUTY_MAP.get(SharedPreferenceUtils.getBeautyLevel(getContext()));
        }
        beautySettingView.setParams(mBeautyParams);

        beautySettingView.setBeautyParamsChangeListener(new OnBeautyParamsChangeListener() {
            @Override
            public void onBeautyChange(BeautyParams param) {
                if (param == null) {
                    isBeautyOn = false;
                    setBeautyOn(isBeautyOn);
                } else {
                    if (!isBeautyOn) {
                        isBeautyOn = true;
                        setBeautyOn(isBeautyOn);
                    }

                    AlivcBeautyParams params = new AlivcBeautyParams();
                    params.beautyWhite = param.beautyWhite;
                    params.beautyShortenFace = param.beautyShortenFace;
                    params.beautySlimFace = param.beautySlimFace;
                    params.beautyBigEye = param.beautyBigEye;
                    params.beautyCheekPink = param.beautyCheekPink;
                    params.beautyRuddy = param.beautyRuddy;
                    params.beautyBuffing = param.beautyBuffing;

                    alivcILiveRoom.setBeautyBeautyParams(params);
                }
            }
        });


        beautySettingView.setHideClickListener(new OnViewClickListener() {
            @Override
            public void onClick() {
                saveBeautyParams(mBeautyParams);
                showBeautyView(false);
                if (controlView != null) {
                    Boolean shouldShow = (Boolean) controlView.getTag();
                    if (shouldShow) {
                        controlView.setVisibility(View.VISIBLE);
                        controlView.setTag(false);
                    }
                }

                if (preView != null) {
                    Boolean shouldShow = (Boolean) preView.getTag();
                    if (shouldShow) {
                        preView.showBottomLayout(shouldShow);
                        preView.setTag(false);
                    }
                }
            }
        });
        beautySettingView.setVisibility(View.GONE);
        addSubView(beautySettingView);
    }

    @Override
    public void release() {
        super.release();
        saveBeautyParams(mBeautyParams);
        AlivcStsManager.getInstance().unregisterStsListener(mStsTokenListener);
        if (hasJionRoom) {
            AlivcHttpManager.getInstance().leaveRoom(userInfo.userId, roomId, new HttpEngine.OnResponseCallback<HttpResponse.User>() {
                @Override
                public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.User data) {
                    if (result) {
                        AlivcHttpManager.getInstance().notification(userInfo.userId, roomId, "e_leave_room", new HttpEngine.OnResponseCallback<HttpResponse.Notification>() {
                            @Override
                            public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.Notification data) {

                            }
                        });

                    }
                }
            });
        }

        if (mAlivcLiveRole == AlivcLiveRole.ROLE_HOST) {
            AlivcHttpManager.getInstance().endLive(userInfo.userId, roomId, new HttpEngine.OnResponseCallback<HttpResponse.Notification>() {
                @Override
                public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.Notification data) {
                    if (result) {
                        LogUtils.d(TAG, "archor end live");
                    }
                }
            });
        }
    }

    private void saveBeautyParams(BeautyParams beautyParams) {
        if (beautyParams != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(BeautyConstants.KEY_WHITE, beautyParams.beautyWhite);
                jsonObject.put(BeautyConstants.KEY_BUFFING, beautyParams.beautyBuffing);
                jsonObject.put(BeautyConstants.KEY_RUDDY, beautyParams.beautyRuddy);
                jsonObject.put(BeautyConstants.KEY_CHEEKPINK, beautyParams.beautyCheekPink);
                jsonObject.put(BeautyConstants.KEY_SLIMFACE, beautyParams.beautySlimFace);
                jsonObject.put(BeautyConstants.KEY_SHORTENFACE, beautyParams.beautyShortenFace);
                jsonObject.put(BeautyConstants.KEY_BIGEYE, beautyParams.beautyBigEye);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String jsonString = jsonObject.toString();

            if (!TextUtils.isEmpty(jsonString)) {
                SharedPreferenceUtils.setBeautyParams(getContext(), jsonString);
            }
        }
    }

    private BeautyParams getBeautyParams() {
        String jsonString = SharedPreferenceUtils.getBeautyParams(getContext());
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        BeautyParams params = new BeautyParams();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            params.beautyWhite = jsonObject.optInt(BeautyConstants.KEY_WHITE);
            params.beautyShortenFace = jsonObject.optInt(BeautyConstants.KEY_SHORTENFACE);
            params.beautySlimFace = jsonObject.optInt(BeautyConstants.KEY_SLIMFACE);
            params.beautyBigEye = jsonObject.optInt(BeautyConstants.KEY_BIGEYE);
            params.beautyCheekPink = jsonObject.optInt(BeautyConstants.KEY_CHEEKPINK);
            params.beautyRuddy = jsonObject.optInt(BeautyConstants.KEY_RUDDY);
            params.beautyBuffing = jsonObject.optInt(BeautyConstants.KEY_BUFFING);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    private void initAlivcLiveRoomView() {
        AlivcSurfaceView alivcLiveRoomView = new AlivcSurfaceView(getContext().getApplicationContext());
        addSubView(alivcLiveRoomView);
    }


    private void showBeautyView(boolean isShow) {
        if (beautySettingView == null) {
            return;
        }
        if (!isBeautyOn) {
            isBeautyOn = true;
            setBeautyOn(isBeautyOn);
        }

        if (isShow) {
            AnimUitls.startAppearAnimY(beautySettingView);
            beautySettingView.setVisibility(View.VISIBLE);
        } else {
            AnimUitls.startDisappearAnimY(beautySettingView);
            beautySettingView.setVisibility(View.GONE);
        }
    }

    private void initAlivcRoomInfoView() {
        alivcRoomInfoView = new AlivcRoomInfoView(getContext());

        alivcRoomInfoView.setViewerItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                final AlivcLiveUserInfo userInfo = alivcRoomInfoView.getUserInfo(position);
                if (userInfo != null) {
                    boolean isOperater = (alivcLiveRole == AlivcLiveRole.ROLE_HOST);
                    boolean isForbidUser = OperateUserManager.getInstance().isForbidUser(getContext(), userInfo.getUserId());
                    showOperateDialog(userInfo, isOperater, isForbidUser, false);
                }
            }
        });

        alivcRoomInfoView.setArchorViewClickListener(new OnViewClickListener() {
            @Override
            public void onClick() {
                if (mArchorInfo != null) {
                    showOperateDialog(mArchorInfo, false, false, false);
                }
            }
        });

        addSubView(alivcRoomInfoView);

    }

    private void initCommentView() {
        commentListView = new AlivcChatListView(getContext().getApplicationContext());
        commentListView.setOnCellClickListener(new OnCellClickListener<AlivcLiveMessageInfo>() {
            @Override
            public void onCellClick(AlivcLiveMessageInfo alivcLiveMessageInfo) {
                boolean isShowOperateUser = alivcLiveRole.getLivingRole() == ROLE_HOST;
                final AlivcLiveUserInfo roomUserInfo = new AlivcLiveUserInfo();
                roomUserInfo.setUserId(alivcLiveMessageInfo.getUserId());
                roomUserInfo.setNickName(String.valueOf(alivcLiveMessageInfo.getSendName()));
                roomUserInfo.setAvatar(String.valueOf(alivcLiveMessageInfo.getAvatar()));
                boolean isForbidUser = OperateUserManager.getInstance().isForbidUser(getContext(), roomUserInfo.getUserId());

                showOperateDialog(roomUserInfo, isShowOperateUser, isForbidUser, alivcLiveMessageInfo.isKickout());

            }
        });
        addSubView(commentListView);
    }

    private void showOperateDialog(final AlivcLiveUserInfo userInfo, boolean isOperater, final boolean isForbidUser, final boolean isKickout) {
        if (userInfo == null || TextUtils.isEmpty(userInfo.getUserId())) {
            return;
        }

        UserCardDialogFragment fragment = new UserCardDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("avatarUrl", userInfo.getAvatar());
        bundle.putString("username", userInfo.getNickName());
        bundle.putString("userId", userInfo.getUserId());
        bundle.putBoolean("isArchor", isOperater);
        bundle.putBoolean("isForbid", isForbidUser);
        bundle.putBoolean("isKickout", isKickout);
        fragment.setArguments(bundle);
        fragment.setOperateListener(new OnOperateUserListener() {
            @Override
            public void kickout() {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("avatar", userInfo.getAvatar());
                    jsonObject.put("user_id", userInfo.getUserId());
                    jsonObject.put("nick_name", userInfo.getNickName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String userData = jsonObject.toString();
//                userData = Base64.encodeToString(userData.getBytes(), Base64.DEFAULT);
                kickOutUser(userInfo.getUserId(), userData, kickOutUserTime);
            }

            @Override
            public void blacklist() {

            }

            @Override
            public void forbidChat() {
                OperateUserManager operateUserManager = OperateUserManager.getInstance();
                if (isForbidUser) {
                    allowUserSendMessage(userInfo.getUserId());
                    operateUserManager.allowUser(getContext().getApplicationContext(), userInfo.getUserId());
                } else {
                    forbidUserSendMessage(userInfo.getUserId(), forbidTime);
                    operateUserManager.forbidUser(getContext().getApplicationContext(), userInfo.getUserId());
                }
            }
        });

        if (getContext() instanceof Activity) {
            fragment.show(((Activity) getContext()).getFragmentManager(), "");
        }
    }

    /**
     * On add msg.
     *
     * @param userid       the userid
     * @param alivcMsgType the alivc msg type
     * @param msgContent   the alivc msg info
     */
    public void onAddMsg(String userid, String userDesc, AlivcMsgType alivcMsgType, String msgContent) {
        AlivcLiveMessageInfo messageInfo = new AlivcLiveMessageInfo();
        messageInfo.setUserId(userid);
        if (userDesc != null) {
            messageInfo.setSendName(userDesc);
        }
        messageInfo.setType(alivcMsgType.getMsgType());
        messageInfo.setDataContent(msgContent);
        commentListView.addMessage(messageInfo);

    }


    public void onAddMsg(String userid, AlivcMsgType alivcMsgType, String msgContent) {
        onAddMsg(userid, null, alivcMsgType, msgContent);

    }

    public void onAddMsg(int event, String content) {
        AlivcLiveMessageInfo messageInfo = new AlivcLiveMessageInfo();
        messageInfo.setType(event);
        messageInfo.setDataContent(content);
        commentListView.addMessage(messageInfo);
    }

    /**
     * 界面控制View
     */
    private void initControlView() {
        controlView = new AlivcControlView(getContext().getApplicationContext(), mCameraIsBack);
        controlView.setAlivcLiveRole(alivcLiveRole);
        controlView.setOnControlClickListener(new OnControlClickListener() {
            @Override
            public void showMessageInPut() {
                if (OperateUserManager.getInstance().isForbidUser(getContext(), userInfo.userId)) {

                    //todo 要改成接口的 liuli
                    ToastUtils.showToast(getContext().getApplicationContext(), "您已被禁言");
                    return;
                }
                alivcInputTextDialog = new InputDialog((Activity) getContext(), userInfo.userId);
                alivcInputTextDialog.setmOnTextSendListener(new InputDialog.OnTextSendListener() {
                    @Override
                    public void onTextSend(String msg) {
                        //发送消息

                        sendMessage(com.alivc.message.constant.AlivcMsgType.MSG_CHAT, msg);

                        //更新数据
                        JSONObject jsonObject = null;
                        AlivcLiveMessageInfo message = new AlivcLiveMessageInfo();
                        try {
                            if (!TextUtils.isEmpty(userInfo.userDesp)) {
                                String userData = new String(Base64.decode(userInfo.userDesp, Base64.DEFAULT));
                                jsonObject = new JSONObject(userData);
                                if (jsonObject != null) {
                                    message.setSendName(jsonObject.optString("nick_name"));
                                    message.setAvatar(jsonObject.optString("avatar"));
                                } else {
                                    message.setSendName(userInfo.userId);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        message.setUserId(userInfo.userId);
                        message.setDataContent(msg);
                        message.setType(AlivcMsgType.ALIVC_MESSAGE_TYPE_CHAT.getMsgType());
                        commentListView.addMessageLocal(message);
                    }
                });
                alivcInputTextDialog.show();
            }

            @Override
            public void onClickCamera() {
                switchCamera();
            }

            @Override
            public void onClickBeauty() {
                showBeautyView(true);
                controlView.setVisibility(View.GONE);
                controlView.setTag(true);

            }

            @Override
            public void onClickFlashLight(boolean bool) {
                setFlash(bool);

            }

            @Override
            public void onClickMusicSelect(boolean bool) {
                ToastUtils.showToast(getContext().getApplicationContext(), getContext().getString(R.string.building));
            }

            @Override
            public void onClickSilent(boolean bool) {
                setMute(bool);
            }

            @Override
            public void onClickScreenRecord(boolean bool) {
                // TODO: 2018/4/24 录屏功能
            }

            @Override
            public void onClickleave() {
                //暂时没用了 close 在外面
            }

            @Override
            public void onLike() {
                likeView.addPraiseWithCallback();
            }
        });
        controlView.setTag(true);
        addSubView(controlView);
    }

    private void initLikeView() {
        likeView = new AlivcLikeView(getContext().getApplicationContext());
        likeView.setOnLikeClickListener(new OnLikeClickListener() {
            @Override
            public void onLike() {

                sendMessage(com.alivc.message.constant.AlivcMsgType.MSG_LIKE, null);
            }
        });
        addSubView(likeView);
        likeView.onStart();
    }


    IAlivcAuthListener alivcLiveAuthNotifyListener = new IAlivcAuthListener() {

        @Override
        public void onSTSTokenExpired(Object iRoom, AlivcSts sts) {

        }

        @Override
        public void onStsTokenCloseExpire(Object iRoom, AlivcSts alivcSts) {
//            if (!TextUtils.isEmpty(AlivcLiveUserManager.getInstance().getUserInfo(getContext()).getUserId())) {
//                AlivcStsManager.getInstance().refreshStsToken(AlivcLiveUserManager.getInstance().getUserInfo(getContext()).getUserId(), new IListener() {
//                    @Override
//                    public void onSucces() {
//                        AlivcSts sts = new AlivcSts();
//                        sts.setStsAccessKey(AlivcStsManager.getInstance().getStsToken().AccessKeyId);
//                        sts.setStsExpireTime(AlivcStsManager.getInstance().getStsToken().Expiration);
//                        sts.setStsSecretKey(AlivcStsManager.getInstance().getStsToken().AccessKeySecret);
//                        sts.setStsSecurityToken(AlivcStsManager.getInstance().getStsToken().SecurityToken);
//                        if (alivcILiveRoom != null) {
//                            alivcILiveRoom.refreshSts(sts);
//                        }
//                        //TODO !
//                    }
//
//                    @Override
//                    public void onFailure() {
//
//                    }
//                });
//            }
        }
    };

    private IAlivcLiveRoomNotifyListener alivcLiveRoomNotifyListener = new IAlivcLiveRoomNotifyListener() {
        @Override
        public void onNotifyKickoutUser(Object alivcIRoom, String userId, String userData) {
            Log.e(TAG, "onNotifyKickoutUser:");
            if (userId.equals(AlivcLiveUserManager.getInstance().getUserInfo(mContext).getUserId())) {
                if (roomActListener != null) {
                    roomActListener.onKickOut();
                }
            } else {
                JSONObject jsonObject;
                final AlivcLiveUserInfo user = new AlivcLiveUserInfo();
                user.setUserId(userId);
                AlivcLiveMessageInfo message = new AlivcLiveMessageInfo();
                try {
                    if (!TextUtils.isEmpty(userData)) {
//                        userData = new String(Base64.decode(userData, Base64.DEFAULT));
                        jsonObject = new JSONObject(userData);
                        if (jsonObject != null) {
                            user.setNickName(jsonObject.optString("nick_name"));
                            user.setAvatar(jsonObject.optString("avatar"));
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (TextUtils.isEmpty(user.getNickName())) {
                    message.setSendName(user.getUserId());
                } else {
                    message.setSendName(user.getNickName());
                }

                message.setUserId(userId);
                message.setAvatar(user.getAvatar());
                message.setType(AlivcMsgType.ALIVC_MESSAGE_TYPE_KICKOUT.getMsgType());
                HandleUtils.getMainThreadHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        alivcRoomInfoView.removeUserInfo(user);
                    }
                });

                commentListView.addMessage(message);
            }

        }

        @Override
        public void onNotifyRoomDestroyed(Object iRoom) {

        }

        @Override
        public void onNotifyUserLogin(Object alivcIRoom, AlivcLiveRole alivcLiveRole, String userId) {
            //新用户进入房间 todo 为什么没有头像
            Log.e(TAG, "onNotifyUserLogin:");
        }

        @Override
        public void onNotifyUserLogout(Object alivcIRoom, AlivcLiveRole alivcLiveRole, String userId) {
            //用户离开房间 todo 为什么没有头像

        }

        @Override
        public void onNotifyUpMic(Object iRoom, String s) {
            onAddMsg(s, AlivcMsgType.ALIVC_MESSAGE_UP_MIC, s);
        }

        @Override
        public void onNotifyDownMic(Object iRoom, String s) {
            //if s not equals to anchor id show dialog
            if (s.equals(mRoomInfo.getStreamer_id())) {
                showDialog(getContext(), getContext().getString(R.string.streamoff), true);
            }
        }

        @Override
        public void onForbidStream(Object iRoom, String s) {
            if (s.equals(mRoomInfo.getStreamer_id())) {
            showDialog(getContext(), getContext().getString(R.string.streamoff), true);
        }

        }
    };

    private IAlivcPusherNotifyListener alivclivePushNotifyListener = new IAlivcPusherNotifyListener() {


        @Override
        public void onFirstFramePreviewed(Object alivcILive) {
            //首帧渲染通知
            Log.e(TAG, "onFirstFramePreviewed:");
        }

        @Override
        public void onDropFrame(Object alivcILive, int i, int i1) {
            //丢帧通知
            Log.e(TAG, "onDropFrame:");
        }

        @Override
        public void onAdjustBitRate(Object alivcILive, int i, int i1) {
            //调整码率通知
            Log.e(TAG, "onAdjustBitRate:");
        }

        @Override
        public void onAdjustFps(Object alivcILive, int i, int i1) {
            //调整帧率通知
            Log.e(TAG, "onAdjustFps:");
        }
    };

    private IAlivcNetworkListener alivcLiveRoomNetworkListener = new IAlivcNetworkListener() {

        @Override
        public void onNetworkPoor(Object alivcILive) {
            //网络差通知
            Log.e(TAG, "onNetworkPoor:");
        }

        @Override
        public void onNetworkRecovery(Object alivcILive) {
            //网络恢复
            Log.e(TAG, "onNetworkRecovery:");
        }

        @Override
        public void onReconnectStart(Object alivcILive) {
            //重连开始通知
            Log.e(TAG, "onReconnectStart:");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
            String date = sdf.format(new Date());
            onAddMsg(AlivcConstants.ALIVC_PUSHER_EVENT_RTMP_RECONNECT_START, (date) + ":Reconnect Start");
        }

        @Override
        public void onReconnectFail(Object alivcILive) {
            //重连失败通知
            Log.e(TAG, "onReconnectFail:");
            showDialog(getContext(), "Reconnect failed !", true);
        }

        @Override
        public void onReconnectSucceed(Object alivcILive) {
            //重连成功通知
            Log.e(TAG, "onReconnectSucceed:");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
            String date = sdf.format(new Date());
            onAddMsg(AlivcConstants.ALIVC_PUSHER_EVENT_RTMP_RECONNECT_SUCCESS, date + ":Reconnect Success");
        }

        @Override
        public void onSendDataTimeout(Object alivcILive) {
            //发送数据超时通知
            Log.e(TAG, "onSendDataTimeout:");
            showDialog(getContext(), "Send data Timeout !", true);
        }

        @Override
        public void onConnectFail(Object alivcILive) {
            //连接失败通知
            Log.e(TAG, "onConnectFail:");
            showDialog(getContext(), "onConnectFail", true);
        }
    };

    private IAlivcPlayerNotifyListener alivcLivePlayerListener = new IAlivcPlayerNotifyListener() {
        @Override
        public void onPlayerStarted(Object iPlayer, String s) {

        }

        @Override
        public void onPlayerStopped(Object iPlayer, String s) {

        }

        @Override
        public void onPlayerCompleted(Object iPlayer, String s) {
            //流播放停止通知
            Log.e(TAG, "onPlayerCompleted:");
            showDialog(getContext(), "onPlayerCompleted", true);
        }
    };

    private IAlivcInteractiveNotifyListener alivcInteractiveListener = new IAlivcInteractiveNotifyListener() {

        @Override
        public void onNotifyLike(Object alivcIRoom, final int likeCount) {
            Log.e(TAG, "onNotifyLike:" + "count" + likeCount);
            if (likeCount > 0) {
                HandleUtils.getMainThreadHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        alivcRoomInfoView.updateLikeCount(likeCount);
                        //仅仅只是显示增量点赞动画
                        likeView.addPraise(likeCount);
                    }
                });
            }
        }

        @Override
        public void onNotifyUserJionRoom(Object iInteractiveWidget, String s, String s1) {

        }

        @Override
        public void onNotifyNotice(Object iInteractiveWidget, String s) {

        }

        @Override
        public void onNotifyUserNotice(Object iInteractiveWidget, String s) {

        }

        /*@Override
        public void onNotifyForbidChat(Object iInteractiveWidget, String s, long l) {
            Log.e(TAG, "onNotifyForbidChat:");
            onAddMsg(s, AlivcMsgType.ALIVC_MESSAGE_TYPE_FORBIDSENDMSG, s);
            OperateUserManager.getInstance().forbidUser(getContext().getApplicationContext(), s);
        }

        @Override
        public void onNotifyAllowChat(Object iInteractiveWidget, String s) {
            Log.e(TAG, "onNotifyAllowUserSendMessage:");
            onAddMsg(s, AlivcMsgType.ALIVC_MESSAGE_TYPE_ALLOWSENDMSG, null);
            OperateUserManager.getInstance().allowUser(getContext().getApplicationContext(), s);
        }*/

        @Override
        public void onNotifyChatMsg(Object iInteractiveWidget, String userId, String content, String userData) {
            if (!AlivcLiveUserManager.getInstance().getUserInfo(mContext).getUserId().equals(userId)) {
                //非当前用户发送的消息才显示
                JSONObject jsonObject = null;
                AlivcLiveMessageInfo message = new AlivcLiveMessageInfo();
                try {
                    if (!TextUtils.isEmpty(userData)) {
                        userData = new String(Base64.decode(userData, Base64.DEFAULT));
                        jsonObject = new JSONObject(userData);
                        if (jsonObject != null) {
                            message.setSendName(jsonObject.optString("nick_name"));
                            message.setAvatar(jsonObject.optString("avatar"));
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (TextUtils.isEmpty(message.getSendName())) {
                    message.setSendName(userId);
                }

                message.setUserId(userId);
                message.setDataContent(content);
                message.setType(AlivcMsgType.ALIVC_MESSAGE_TYPE_CHAT.getMsgType());

                commentListView.addMessage(message);


            }
        }

        @Override
        public void onNotifyCustomMsg(Object iInteractiveWidget, final String content) {
            //目前只有进出房间
            LogUtils.d(TAG, "notify custom message = " + content);
            if (!TextUtils.isEmpty(content)) {
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    final String eventType = jsonObject.optString("event_type");
                    if (AlivcAppMsgEvent.LEAVE_ROOM.equals(eventType)
                            || AlivcAppMsgEvent.ENTER_ROOM.equals(eventType)) {
                        JSONObject userObject = jsonObject.optJSONObject("user_info");
                        if (userObject != null) {
                            final AlivcLiveUserInfo user = new AlivcLiveUserInfo();
                            user.setUserId(userObject.optString("user_id"));
                            user.setAvatar(userObject.optString("avatar"));
                            user.setNickName(userObject.optString("nick_name"));
                            //todo 离开房间暂时不用处理信息
                            final String contentBody = userObject.optString("content");

                            HandleUtils.getMainThreadHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    AlivcLiveMessageInfo message = new AlivcLiveMessageInfo();
                                    message.setSendName(user.getNickName());
                                    message.setUserId(user.getUserId());
                                    message.setAvatar(user.getAvatar());
                                    message.setDataContent(contentBody);
                                    if (mAlivcLiveRole == AlivcLiveRole.ROLE_HOST
                                            && user.getUserId().equals(userInfo.userId)) {
                                        if (AlivcAppMsgEvent.ENTER_ROOM.equals(eventType)) {
//                                            alivcRoomInfoView.addUserInfo(user);
                                        } else {
//                                            alivcRoomInfoView.removeUserInfo(user);
                                        }
                                        return;
                                    }
                                    if (AlivcAppMsgEvent.ENTER_ROOM.equals(eventType)) {
                                        alivcRoomInfoView.addUserInfo(user);
                                        message.setType(AlivcMsgType.ALIVC_MESSAGE_TYPE_LOGIN.getMsgType());
                                    } else {
                                        alivcRoomInfoView.removeUserInfo(user);
                                        message.setType(AlivcMsgType.ALIVC_MESSAGE_TYPE_LOGOUT_ROOM.getMsgType());
                                    }
                                    commentListView.addMessage(message);
                                }
                            });

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    };


    IAlivcErrorListener alivcLiveRoomErrorListener = new IAlivcErrorListener() {
        @Override
        public void onSystemError(Object o, AlivcCommonError alivcCommonError) {
            showDialog(getContext(), "onSystemError" + alivcCommonError.getErrorMessage(), true);
        }

        @Override
        public void onSDKError(Object o, AlivcCommonError alivcCommonError) {
            if (alivcCommonError != null) {
                showDialog(getContext(), "onSystemError" + alivcCommonError.getErrorMessage(), true);
            }
        }
    };


}
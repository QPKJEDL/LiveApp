package com.aliyun.alivclive.room;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alivc.eventreport.AlivcEventReport;
import com.alivc.im.AlivcImmediateMsgManager;
import com.alivc.interactive.impl.AlivcInteractiveWidget;
import com.alivc.interactive.listener.IAlivcInteractiveNotifyListener;
import com.alivc.live.base.AlivcCommonError;
import com.alivc.live.base.AlivcCommonSuccess;
import com.alivc.live.base.IAlivcCallback;
import com.alivc.live.base.IAlivcErrorListener;
import com.alivc.live.room.constants.AlivcResolutionMode;
import com.alivc.live.room.constants.AlivcLiveRole;
import com.alivc.live.room.impl.AlivcLiveRoom;
import com.alivc.live.room.interactive.config.AlivcInteractiveLiveRoomConfig;
import com.alivc.live.room.interactive.impl.AlivcInteractiveLiveRoom;
import com.alivc.live.room.listener.IAlivcAuthListener;
import com.alivc.live.room.listener.IAlivcLiveRoomNotifyListener;
import com.alivc.live.room.listener.IAlivcNetworkListener;
import com.alivc.live.room.listener.IAlivcPlayerNotifyListener;
import com.alivc.live.room.listener.IAlivcPusherNotifyListener;
import com.alivc.live.room.model.AlivcBeautyParams;
import com.alivc.live.room.model.AlivcRoomInfo;
import com.alivc.live.room.view.AlivcSurfaceView;
import com.alivc.message.constant.AlivcMsgType;
import com.alivc.net.AlivcNetManager;
import com.aliyun.alivclive.BuildConfig;
import com.aliyun.alivclive.R;
import com.aliyun.alivclive.listener.RoomActListener;
import com.aliyun.alivclive.room.roominfo.AlivcLiveRoomInfo;
import com.aliyun.alivclive.utils.http.AlivcStsManager;

import com.aliyun.alivclive.room.userlist.AlivcUserInfo;
import com.aliyun.alivclive.utils.constants.AlivcConstants;
import com.aliyun.alivclive.utils.HandleUtils;
import com.aliyun.alivclive.utils.LogUtils;
import com.aliyun.alivclive.utils.ApiConfig;
import com.aliyun.alivclive.utils.ThreadUtil;
import com.aliyun.pusher.core.module.BeautyParams;

import java.io.File;
import java.util.concurrent.ExecutorService;

/**
 * @author Mulberry
 *         create on 2018/4/23.
 */

public class AlivcLiveBaseRoomView extends FrameLayout {
    protected AlivcInteractiveLiveRoom alivcILiveRoom;
    protected AlivcSurfaceView alivcLiveRoomView;
    protected AlivcLiveRole alivcLiveRole;
    protected String roomId;
    protected AlivcUserInfo userInfo;
    protected AlivcLiveRoomInfo mRoomInfo;
    protected Context mContext;
    protected BeautyParams mBeautyParams;
    protected static final int ROLE_HOST = 0;

    /**
     * 异步线程池
     */
    ExecutorService executorService = ThreadUtil.newDynamicSingleThreadedExecutor();

    private static final String TAG = AlivcLiveBaseRoomView.class.getName().toString();
    protected AlivcInteractiveLiveRoomConfig mAlivcLiveRoomConfig;
    protected AlivcResolutionMode mSelectResolutionMode = AlivcResolutionMode.RESOLUTION_LD;

    public AlivcLiveBaseRoomView(Context context) {
        super(context);
        initView();
    }

    public AlivcLiveBaseRoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AlivcLiveBaseRoomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        alivcILiveRoom = AlivcLiveRoomManager.getLiveRoom();
        alivcLiveRoomView = new AlivcSurfaceView(getContext().getApplicationContext());
        addSubView(alivcLiveRoomView);
        if (BuildConfig.DEBUG) {
            int netConfig = ApiConfig.getApiConfig(getContext().getApplicationContext());
            switch (netConfig) {
                case 0:
                    break;
                case 1:
                    AlivcImmediateMsgManager.setDebugMode(false);
                    break;
                case 2:
                    AlivcNetManager.setDailyMode(true);
                    AlivcLiveRoom.setDailyMode();
                    AlivcEventReport.setDailyMode();
                    AlivcInteractiveWidget.setDailyMode();
                    AlivcImmediateMsgManager.setDebugMode(true);
                    break;


                default:
                    break;
            }
        }
    }

    /**
     * addSubView
     * 添加子view到布局中
     *
     * @param view 子view
     */
    protected void addSubView(View view) {
        //添加到布局中
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(view, params);
    }


    public void init(Context context, AlivcLiveRoomInfo roomInfo, AlivcUserInfo alivcUserInfo, AlivcLiveRole alivcLiveRole) {
        mRoomInfo = roomInfo;
        this.roomId = roomInfo.getRoom_id();
        mContext = context;
        this.userInfo = alivcUserInfo;
//<<<<<<< Updated upstream
//        if (alivcLiveRole.equals(AlivcLiveRole.ROLE_AUDIENCE)) {
//            this.userInfo.userId = "12345530";
//        } else {
//            this.userInfo.userId = "miyou";
//        }
//=======
        this.alivcLiveRole = alivcLiveRole;

        mAlivcLiveRoomConfig = new AlivcInteractiveLiveRoomConfig();
        mAlivcLiveRoomConfig.setPausePushImage(Environment.getExternalStorageDirectory().getPath() + File.separator + "alivc_resource/background_push.png");
        alivcILiveRoom.init(context, AlivcConstants.getAppId(), mAlivcLiveRoomConfig);

        try {
            alivcILiveRoom.login(AlivcStsManager.getInstance().getStsToken());
        } catch (IllegalArgumentException e) {
            //illegal sts
            AlivcStsManager.getInstance().refreshStsToken(this.userInfo.userId);
            HandleUtils.getMainThreadHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    enterRoom();
                }
            }, 3000);
            return;
        }
        enterRoom();

    }

    private void enterRoom() {
        if (alivcLiveRole.equals(AlivcLiveRole.ROLE_HOST)) {
            alivcILiveRoom.setLocalView(this.alivcLiveRoomView);
            try {
                alivcILiveRoom.startPreview(new IAlivcCallback<AlivcCommonSuccess, AlivcCommonError>() {
                    @Override
                    public void onSuccess(AlivcCommonSuccess iPusher) {
                        getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                alivcILiveRoom.setBeautyOn(true);
                                AlivcBeautyParams params = new AlivcBeautyParams();
                                params.beautyWhite = mBeautyParams.beautyWhite;
                                params.beautyShortenFace = mBeautyParams.beautyShortenFace;
                                params.beautySlimFace = mBeautyParams.beautySlimFace;
                                params.beautyBigEye = mBeautyParams.beautyBigEye;
                                params.beautyCheekPink = mBeautyParams.beautyCheekPink;
                                params.beautyRuddy = mBeautyParams.beautyRuddy;
                                params.beautyBuffing = mBeautyParams.beautyBuffing;

                                alivcILiveRoom.setBeautyBeautyParams(params);
                            }
                        });
                    }

                    @Override
                    public void onFailure(AlivcCommonError alivcCommonError) {

                    }
                });
            } catch (Exception e) {
            }
        } else {
            alivcILiveRoom.setRemoteView(null, this.alivcLiveRoomView);
        }
    }


    /**
     * 获取房间信息
     *
     * @return
     */
    public void queryLatestRoomInfo(String roomId) {
        if (alivcILiveRoom == null) {
            throw new IllegalAccessError("Please confirm called init method ");
        }

        //alivcILiveRoom.queryLatestRoomInfo(roomId);, todo ! --miyo
    }

    /**
     * 查询最近的消息
     *
     * @param pageIndex
     * @param pageSize
     */
    public void queryLatestMsg(int pageIndex, int pageSize/*, AlivcQueryOrder order*/) {
        if (alivcILiveRoom == null) {
            throw new IllegalAccessError("Please confirm called init method ");
        }

        //alivcILiveRoom.queryLatestMsg(pageIndex,pageSize,order);, todo ! --miyo
    }


    public void onResume() {
        if (alivcILiveRoom == null) {
            throw new IllegalAccessError("Please confirm called init method ");
        }
        try {
            alivcILiveRoom.resume();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onPause() {
        if (alivcILiveRoom == null) {
            throw new IllegalAccessError("Please confirm called init method ");
        }
        try {
            alivcILiveRoom.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void release() {
        if (alivcILiveRoom == null) {
            throw new IllegalAccessError("Please confirm called init method ");
        }

        alivcILiveRoom.release();
        AlivcLiveRoomManager.release();
        alivcILiveRoom = null;
    }

    /**
     * 加入房间
     *
     * @param roomId
     * @param alivcLiveRole
     */
    public void enterRoom(final String roomId, final AlivcLiveRole alivcLiveRole) {
        if (alivcILiveRoom == null) {
            throw new IllegalAccessError("Please confirm called init method ");
        }

        alivcILiveRoom.changePushResolutionMode(mSelectResolutionMode);

        this.roomId = roomId;
        //todo ! --miyo
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Enter Room");
                alivcILiveRoom.enter(roomId, userInfo.userId, userInfo.userDesp, alivcLiveRole,
                        new IAlivcCallback<AlivcRoomInfo, AlivcCommonError>() {
                            @Override
                            public void onSuccess(AlivcRoomInfo alivcEnterRoomResult) {
                                //todo ! notify
                                LogUtils.d(TAG, "enter room onSuccess, handle info");
                                handleRoomInfo();
                            }

                            @Override
                            public void onFailure(AlivcCommonError alivcCommonError) {
                                LogUtils.d(TAG, "enter room failure");
                                showDialog(getContext(), alivcCommonError.getErrorMessage(), false);

                            }
                        });
            }
        });

    }

    protected void handleRoomInfo() {

    }

    /**
     * 离开房间
     */
    public void leaveRoom() {
        if (alivcILiveRoom == null) {
            throw new IllegalAccessError("Please confirm called init method ");
        }

        alivcILiveRoom.quit(new IAlivcCallback<AlivcCommonSuccess, AlivcCommonError>() {
            @Override
            public void onSuccess(AlivcCommonSuccess alivcQuitRoomResult) {

            }

            @Override
            public void onFailure(AlivcCommonError alivcCommonError) {

            }
        });
    }

    /**
     * 销毁房间
     */
    public void destroyRoom() {
        if (alivcILiveRoom == null) {
            throw new IllegalAccessError("Please confirm called init method ");
        }

        alivcILiveRoom.release();
    }

    /**
     * 发送消息
     *
     * @param alivcMsgType 消息类型
     * @param message      消息内容
     */
    protected void sendMessage(final AlivcMsgType alivcMsgType, final String message) {
        if (alivcILiveRoom == null) {
            throw new IllegalAccessError("Please confirm called init method ");
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (alivcMsgType.equals(AlivcMsgType.MSG_CHAT)) {
                    try {
                        alivcILiveRoom.sendChatMessage(message, new IAlivcCallback<AlivcCommonSuccess, AlivcCommonError>() {
                            @Override
                            public void onSuccess(AlivcCommonSuccess o) {
                                Toast.makeText(getContext(), "send message suc", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(AlivcCommonError iError) {
                                Toast.makeText(getContext(), "send message failed", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (IllegalStateException e) {
                        showDialog(getContext(), "Send chat failed : " + e.getMessage(), false);
                    }
                } else if (alivcMsgType.equals(AlivcMsgType.MSG_LIKE)) {
                    try {
                        alivcILiveRoom.sendLike(1, new IAlivcCallback<AlivcCommonSuccess, AlivcCommonError>() {
                            @Override
                            public void onSuccess(AlivcCommonSuccess o) {

                            }

                            @Override
                            public void onFailure(AlivcCommonError iError) {

                            }
                        });
                    } catch (IllegalStateException e) {
                        showDialog(getContext(), "Send like failed : " + e.getMessage(), false);
                    }
                }
            }
        });
    }

    /**
     * 踢人
     *
     * @param userId 用户id
     * @param s     单位秒，踢出用户的持续时间，如踢出用户1000s则用户1000s之内进入不了房间,0s为永远
     */
    public void kickOutUser(final String userId, final String userData, final long ts) {
        if (alivcILiveRoom == null) {
            throw new IllegalAccessError("Please confirm called init method ");
        }

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //String opUid, String roomId, String reason, long duration,
                alivcILiveRoom.kickout(userId, userData, 15 * 60, new IAlivcCallback<AlivcCommonSuccess, AlivcCommonError>() {
                    @Override
                    public void onSuccess(AlivcCommonSuccess s) {
                        Toast.makeText(getContext(), "kickOutUser suc", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(AlivcCommonError alivcCommonError) {
                        showDialog(getContext(), "kickOutUser failed !" + alivcCommonError.getErrorMessage(), false);
                    }
                });
            }

            ;
        });

    }


    /**
     * 解除踢人
     *
     * @param userId 用户id
     * @param ts     单位毫秒，踢出用户的持续时间，如踢出用户1000ms则用户1000ms之内进入不了房间,0ms为永远
     */
    public void unkickOutUser(final String userId, final long ts) {
        if (alivcILiveRoom == null) {
            throw new IllegalAccessError("Please confirm called init method ");
        }

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //String opUid, String roomId, String reason, long duration,
                alivcILiveRoom.cancelKickout(userId, new IAlivcCallback<AlivcCommonSuccess, AlivcCommonError>() {
                    @Override
                    public void onSuccess(AlivcCommonSuccess s) {
                        Toast.makeText(getContext(), "kickOutUser suc", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(AlivcCommonError alivcCommonError) {
                        showDialog(getContext(), "kickOutUser failed !" + alivcCommonError.getErrorMessage(), false);
                    }
                });
            }

            ;
        });

    }

    /**
     * 禁言用户
     *
     * @param userId 用户id
     * @param ts     单位毫秒，禁言用户的持续时间，如禁言用户1000ms则用户1000ms之内不能再发送消息 0ms为永远
     */
    public void forbidUserSendMessage(final String userId, final long ts) {
        if (alivcILiveRoom == null) {
            throw new IllegalAccessError("Please confirm called init method ");
        }

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                /*alivcILiveRoom.forbidChat(userId, ts, new IAlivcCallback<AlivcCommonSuccess, AlivcCommonError>() {
                    @Override
                    public void onSuccess(AlivcCommonSuccess s) {
                        //showDialog(getContext(), "ForbidUserSendMessage " + userId + " suc ," + s, false);
                    }

                    @Override
                    public void onFailure(AlivcCommonError alivcCommonError) {
                        showDialog(getContext(), "ForbidUserSendMessage failed !" + alivcCommonError.getErrorMessage(), false);
                    }
                });*/
            }
        });

    }

    /**
     * 允许发言
     *
     * @param userId
     */
    public void allowUserSendMessage(final String userId) {
        if (alivcILiveRoom == null) {
            throw new IllegalAccessError("Please confirm called init method ");
        }

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                /*alivcILiveRoom.allowChat(userId, new IAlivcCallback<AlivcCommonSuccess, AlivcCommonError>() {
                    @Override
                    public void onSuccess(AlivcCommonSuccess s) {
                        //Toast.makeText(getContext(), "allowUserSendMessage suc", Toast.LENGTH_LONG).show();
                        //showDialog(getContext(), "allowUserSendMessage " + userId, false);
                    }

                    @Override
                    public void onFailure(AlivcCommonError alivcCommonError) {
                        showDialog(getContext(), "allowUserSendMessage failed !" + alivcCommonError.getErrorMessage(), false);
                    }
                });*/
            }
        });
    }

    /**
     * 禁言列表
     */
    public void forbidChatList() {
        if (alivcILiveRoom == null) {
            throw new IllegalAccessError("Please confirm called init method ");
        }

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //String opUid, String roomId, String reason, long duration,
                //alivcILiveRoom.getForbidChatUserList(1, 20,new IAlivcCallback<List<AlivcForbidUserInfo>, AlivcCommonError>);
            }

            ;
        });
    }

    /**
     * get chat histroy
     */
    public void getChatHistory() {
        if (alivcILiveRoom == null) {
            throw new IllegalAccessError("Please confirm called init method ");
        }

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //String opUid, String roomId, String reason, long duration,
                //alivcILiveRoom.getHistoryChatMessage();
            }

            ;
        });
    }

    /**
     * 切换摄像头
     */
    public void switchCamera() {
        alivcILiveRoom.switchCamera();
    }

    /**
     * 设置美颜开关
     *
     * @param beautyOn
     */
    public void setBeautyOn(boolean beautyOn) {
        alivcILiveRoom.setBeautyOn(beautyOn);
    }

    /**
     * 设置主播是否静音直播
     *
     * @param isMute
     */
    public void setMute(boolean isMute) {
        alivcILiveRoom.setMute(isMute);
        /*alivcILiveRoom.queryLikeInfo(new IAlivcCallback<AlivcGetDigCountRes, AlivcCommonError>() {
            @Override
            public void onSuccess(AlivcGetDigCountRes alivcGetDigCountRes) {

            }

            @Override
            public void onFailure(AlivcCommonError alivcCommonError) {

            }
        });
        alivcILiveRoom.roomNotification(roomId, "~~~~~~~~~~~~~~", AlivcMsgPriority.MSG_PRIORITY_HIGH,
            new IAlivcCallback<String, AlivcCommonError>() {
                @Override
                public void onSuccess(String s) {

                }

                @Override
                public void onFailure(AlivcCommonError alivcCommonError) {

                }
            });*/
    }


    /**
     * 开关闪光灯
     *
     * @param isFlash
     */
    public void setFlash(boolean isFlash) {
        alivcILiveRoom.setFlash(isFlash);
    }

    /*public void setAlivcLiveRoomCallback(AlivcLiveRoomCallback alivcLiveRoomCallback) {
        if (alivcILiveRoom == null){
            throw new IllegalAccessError("Please confirm called init method ");
        }

        alivcILiveRoom.setLiveRoomCallback(alivcLiveRoomCallback);
    }*/

    public void setAlivcLiveRoomErrorListener(IAlivcErrorListener alivcLiveRoomErrorListener) {
        if (alivcILiveRoom == null) {
            throw new IllegalAccessError("Please confirm called init method ");
        }

        alivcILiveRoom.setErrorListener(alivcLiveRoomErrorListener);
    }

    public void setAlivcLiveRoomNotifyListener(IAlivcAuthListener alivcAuthListener, IAlivcPusherNotifyListener pushListener, IAlivcPlayerNotifyListener playerListener, IAlivcNetworkListener networkListener, IAlivcInteractiveNotifyListener interactiveListener, IAlivcLiveRoomNotifyListener liveRoomNotifyListener) {
        if (alivcILiveRoom == null) {
            throw new IllegalAccessError("Please confirm called init method ");
        }

        alivcILiveRoom.setAuthListener(alivcAuthListener);
        alivcILiveRoom.setPusherNotifyListener(pushListener);
        alivcILiveRoom.setPlayerNotifyListener(playerListener);
        alivcILiveRoom.setNetworkListener(networkListener);
        alivcILiveRoom.setInteractiveNotifyListener(interactiveListener);
        alivcILiveRoom.setLiveRoomNotifyListener(liveRoomNotifyListener);

    }

    protected void showDialog(final Context context, final String message, final boolean isCloseRoom) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return;
        }

        if (message == null) {
            return;
        }
        android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle(context.getResources().getString(R.string.alivc_dialog_tips));
                dialog.setMessage(message);
                dialog.setNegativeButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isCloseRoom) {
                            if (roomActListener != null) {
                                roomActListener.onClose();
                            }
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    RoomActListener roomActListener;

    public void setOnCloseRoomClickListener(
            RoomActListener onCloseRoomListener) {
        this.roomActListener = onCloseRoomListener;
    }


}

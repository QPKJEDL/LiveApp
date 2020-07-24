package com.qingpeng.mz.video.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alivc.component.custom.AlivcLivePushCustomDetect;
import com.alivc.component.custom.AlivcLivePushCustomFilter;
import com.alivc.live.detect.TaoFaceFilter;
import com.alivc.live.filter.TaoBeautyFilter;
import com.alivc.live.pusher.AlivcLivePushConfig;
import com.alivc.live.pusher.AlivcLivePushStatsInfo;
import com.alivc.live.pusher.AlivcLivePusher;
import com.alivc.live.pusher.AlivcPreviewOrientationEnum;
import com.alivc.live.pusher.SurfaceStatus;
import com.android.mx.library.MxVideoView;
import com.beetle.bauhinia.ChatItemQuickAction;
import com.beetle.bauhinia.activity.MessageFileActivity;
import com.beetle.bauhinia.activity.OverlayActivity;
import com.beetle.bauhinia.activity.PlayerActivity;
import com.beetle.bauhinia.activity.WebActivity;
import com.beetle.bauhinia.api.IMHttpAPI;
import com.beetle.bauhinia.db.GroupMessageHandler;
import com.beetle.bauhinia.db.IMessage;
import com.beetle.bauhinia.db.PeerMessageHandler;
import com.beetle.bauhinia.db.message.Audio;
import com.beetle.bauhinia.db.message.Image;
import com.beetle.bauhinia.db.message.Link;
import com.beetle.bauhinia.db.message.Location;
import com.beetle.bauhinia.db.message.MessageContent;
import com.beetle.bauhinia.db.message.Notification;
import com.beetle.bauhinia.db.message.Text;
import com.beetle.bauhinia.db.message.VOIP;
import com.beetle.bauhinia.db.message.Video;
import com.beetle.bauhinia.tools.FileCache;
import com.beetle.bauhinia.tools.FileDownloader;
import com.beetle.bauhinia.view.InMessageView;
import com.beetle.bauhinia.view.MessageRowView;
import com.beetle.bauhinia.view.MessageTextView;
import com.beetle.im.IMMessage;
import com.beetle.im.IMService;
import com.beetle.im.IMServiceObserver;
import com.beetle.im.PeerMessageObserver;
import com.beetle.im.RoomMessage;
import com.beetle.im.RoomMessageObserver;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qingpeng.mz.R;
import com.qingpeng.mz.adapter.GameJieSuanAdapter;
import com.qingpeng.mz.api.Host;
import com.qingpeng.mz.api.RedBag;
import com.qingpeng.mz.bean.LongHuBean;
import com.qingpeng.mz.bean.RoomGameInfoBean;
import com.qingpeng.mz.bean.RoomInfoBean;
import com.qingpeng.mz.bean.ToUserInfoBean;
import com.qingpeng.mz.bean.UserRoomBean;
import com.qingpeng.mz.okhttp.APIResponse;
import com.qingpeng.mz.okhttp.MyCall;
import com.qingpeng.mz.okhttp.ResultException;
import com.qingpeng.mz.okhttp.RetrofitCreateHelper;
import com.qingpeng.mz.utils.APP;
import com.qingpeng.mz.utils.ToastUtils;
import com.qingpeng.mz.video.fragment.AliyunPlayerFragment;
import com.qingpeng.mz.video.fragment.LivePushFragment;
import com.qingpeng.mz.video.fragment.LiveRoomInfoFragment;
import com.qingpeng.mz.video.fragment.PayFragment;
import com.qingpeng.mz.video.im.MessageActivity;
import com.qingpeng.mz.video.utils.LiveBottomBar;
import com.qingpeng.mz.video.utils.NetWorkUtils;
import com.qingpeng.mz.views.XCRoundImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Response;

import static com.alivc.live.pusher.AlivcPreviewOrientationEnum.ORIENTATION_LANDSCAPE_HOME_LEFT;
import static com.alivc.live.pusher.AlivcPreviewOrientationEnum.ORIENTATION_LANDSCAPE_HOME_RIGHT;
import static com.alivc.live.pusher.AlivcPreviewOrientationEnum.ORIENTATION_PORTRAIT;

public class LivePushActivity extends MessageActivity implements PeerMessageObserver, RoomMessageObserver, IMServiceObserver {

    private static final int IN_MSG = 0;
    private static final int OUT_MSG = 1;

    protected boolean isShowUserName = true;

    private File captureFile;
    private File recordVideoFile;

    private static final String TAG = "LivePushActivity";

    private final long REFRESH_INTERVAL = 1000;
    private static final String URL_KEY = "url_key";
    private static final String DESk_ID = "desk_id";
    private static final String GAEM_ID = "game_id";
    private static final String ASYNC_KEY = "async_key";
    private static final String AUDIO_ONLY_KEY = "audio_only_key";
    private static final String VIDEO_ONLY_KEY = "video_only_key";
    private static final String ORIENTATION_KEY = "orientation_key";
    private static final String CAMERA_ID = "camera_id";
    private static final String FLASH_ON = "flash_on";
    private static final String AUTH_TIME = "auth_time";
    private static final String PRIVACY_KEY = "privacy_key";
    private static final String MIX_EXTERN = "mix_extern";
    private static final String MIX_MAIN = "mix_main";
    public static final int REQ_CODE_PUSH = 0x1112;
    public static final int CAPTURE_PERMISSION_REQUEST_CODE = 0x1123;

    public SurfaceView mPreviewView;
    BaseAdapter adapter;
    ListView listview;
    private LivePushFragment mLivePushFragment;
    public AlivcLivePushConfig mAlivcLivePushConfig;

    private AlivcLivePusher mAlivcLivePusher = null;
    private String mPushUrl = null;

    private boolean mAsync = false;
    private boolean mAudioOnly = false;
    private boolean mVideoOnly = false;
    private int mOrientation = ORIENTATION_PORTRAIT.ordinal();

    private boolean isPause = false;

    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private boolean mFlash = false;
    private boolean mMixExtern = false;
    private boolean mMixMain = false;
    AlivcLivePushStatsInfo alivcLivePushStatsInfo = null;
    TaoBeautyFilter taoBeautyFilter;

    TaoFaceFilter taoFaceFilter;

    private String mAuthTime = "";
    private String mPrivacyKey = "";

    private boolean videoThreadOn = false;
    private boolean audioThreadOn = false;

    private int mNetWork = 0;
    //直播参数
    private boolean isAudience = true; //默认为观众
    public static final String IS_AUDIENCE = "is_audience";
    private FrameLayout roomInfoLayout;

    private ViewGroup rootView;
    private LiveBottomBar liveBottomBar; //底部控制栏
    private LiveRoomInfoFragment liveRoomInfoFragment;
    private SurfaceStatus mSurfaceStatus = SurfaceStatus.UNINITED;
    private static final int FLING_MIN_DISTANCE = 50;
    private static final int FLING_MIN_VELOCITY = 0;


    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mDetector;
    private AliyunPlayerFragment audienceFragment;

    private String desk_id;
    private String game_id;
    public String pavenum;
    public String bootnum;

    private long currentUID;
    private long roomID;
    private long deskID;
    private int msgLocalID = 1;
    private EditText sendedit;
    private CountDownTimer timer;
    private TextView timetext;
    public boolean isPay = true;
    private LinearLayout llgameyuan;
    private PayFragment pay;
    private FragmentTransaction transaction;
    private boolean show = false;
    private FrameLayout llgameroom;
    private View rl_member_operate;
    private XCRoundImageView iv_avatar;
    private TextView tv_nick_name;
    private LinearLayout ll_zhubo_sttings;
    private Button btn_guanli;
    private Button btn_mute;
    private Button btn_kick;
    private Call<APIResponse<List<RoomInfoBean>>> roomtypelistinfo;
    private RoomInfoBean bean;

    private boolean guanli = false;
    private boolean jinyan = false;
    private View ll_live_finish;
    private TextView tv_operate_name;
    private XCRoundImageView iv_operate;
    private Button btn_finish_back;
    private MaterialDialog dialog;
    private MxVideoView mx_video;
    private LinearLayout ll_video;
    private IMService im;
    private TextView text_likai;
    private String msg = "欢迎来到直播间！严禁未成年人进行直播或打赏，清大家共同遵守，监督。直播间内严禁出现违法违规、低俗色情、吸烟酗酒等内容。若有违规内容请及时举报。如主播直播过程中以陪玩、送礼等方式进行诱导打赏、私下交易，清谨慎判断、以防人身或财产损失。清大家注意财产安全，谨防网络诈骗";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        mPushUrl = getIntent().getStringExtra(URL_KEY);
        roomID = Long.parseLong(getIntent().getStringExtra("roomid"));
        isAudience = getIntent().getBooleanExtra(IS_AUDIENCE, false);
        if (!isAudience) {
            mAsync = getIntent().getBooleanExtra(ASYNC_KEY, false);
            mAudioOnly = getIntent().getBooleanExtra(AUDIO_ONLY_KEY, false);
            mVideoOnly = getIntent().getBooleanExtra(VIDEO_ONLY_KEY, false);
            mOrientation = getIntent().getIntExtra(ORIENTATION_KEY, ORIENTATION_PORTRAIT.ordinal());
            mCameraId = getIntent().getIntExtra(CAMERA_ID, Camera.CameraInfo.CAMERA_FACING_FRONT);
            mFlash = getIntent().getBooleanExtra(FLASH_ON, false);
            mAuthTime = getIntent().getStringExtra(AUTH_TIME);
            mPrivacyKey = getIntent().getStringExtra(PRIVACY_KEY);
            mMixExtern = getIntent().getBooleanExtra(MIX_EXTERN, false);
            mMixMain = getIntent().getBooleanExtra(MIX_MAIN, false);
            setOrientation(mOrientation);
            initView();
            mAlivcLivePushConfig = (AlivcLivePushConfig) getIntent().getSerializableExtra(AlivcLivePushConfig.CONFIG);
            mAlivcLivePusher = new AlivcLivePusher();

            try {
                mAlivcLivePusher.init(getApplicationContext(), mAlivcLivePushConfig);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                showDialog(this, e.getMessage());
            } catch (IllegalStateException e) {
                e.printStackTrace();
                showDialog(this, e.getMessage());
            }

            mAlivcLivePusher.setCustomDetect(new AlivcLivePushCustomDetect() {
                @Override
                public void customDetectCreate() {
                    taoFaceFilter = new TaoFaceFilter(getApplicationContext());
                    taoFaceFilter.customDetectCreate();
                }

                @Override
                public long customDetectProcess(long data, int width, int height, int rotation, int format, long extra) {
                    if (taoFaceFilter != null) {
                        return taoFaceFilter.customDetectProcess(data, width, height, rotation, format, extra);
                    }
                    return 0;
                }

                @Override
                public void customDetectDestroy() {
                    if (taoFaceFilter != null) {
                        taoFaceFilter.customDetectDestroy();
                    }
                }
            });
            mAlivcLivePusher.setCustomFilter(new AlivcLivePushCustomFilter() {
                @Override
                public void customFilterCreate() {
                    taoBeautyFilter = new TaoBeautyFilter();
                    taoBeautyFilter.customFilterCreate();
                }

                @Override
                public void customFilterUpdateParam(float fSkinSmooth, float fWhiten, float fWholeFacePink, float fThinFaceHorizontal, float fCheekPink, float fShortenFaceVertical, float fBigEye) {
                    if (taoBeautyFilter != null) {
                        taoBeautyFilter.customFilterUpdateParam(fSkinSmooth, fWhiten, fWholeFacePink, fThinFaceHorizontal, fCheekPink, fShortenFaceVertical, fBigEye);
                    }
                }

                @Override
                public void customFilterSwitch(boolean on) {
                    if (taoBeautyFilter != null) {
                        taoBeautyFilter.customFilterSwitch(on);
                    }
                }

                @Override
                public int customFilterProcess(int inputTexture, int textureWidth, int textureHeight, long extra) {
                    if (taoBeautyFilter != null) {
                        return taoBeautyFilter.customFilterProcess(inputTexture, textureWidth, textureHeight, extra);
                    }
                    return inputTexture;
                }

                @Override
                public void customFilterDestroy() {
                    if (taoBeautyFilter != null) {
                        taoBeautyFilter.customFilterDestroy();
                    }
                    taoBeautyFilter = null;
                }
            });

            mDetector = new GestureDetector(getApplicationContext(), mGestureDetector);
            mScaleDetector = new ScaleGestureDetector(getApplicationContext(), mScaleGestureDetector);
//        initViewPager();
            mNetWork = NetWorkUtils.getAPNType(this);
        }
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(mChangedReceiver, filter);
        rootView = findViewById(R.id.layout_live_root);
        //进入房间游戏状态

        loadFragment(isAudience);
        initBottomBar();
        roomInfoLayout = findViewById(R.id.layout_room_info);
//        if (isAudience) {
//            //观众 直接显示聊天列表与底部控制栏
//            onStartLivingFinished();
//        }


        listview = (ListView) findViewById(R.id.list_view);
        timetext = (TextView) findViewById(R.id.time_text);
        llgameyuan = (LinearLayout) findViewById(R.id.ll_game_yuan);
        llgameroom = (FrameLayout) findViewById(R.id.layout_game_room);
        rl_member_operate = findViewById(R.id.rl_member_operate);
        ll_live_finish = findViewById(R.id.ll_live_finish);
        iv_avatar = (XCRoundImageView) findViewById(R.id.iv_avatar);
        iv_operate = (XCRoundImageView) findViewById(R.id.iv_operate);
        tv_nick_name = (TextView) findViewById(R.id.tv_nick_name);
        tv_operate_name = (TextView) findViewById(R.id.tv_operate_name);
        text_likai = (TextView) findViewById(R.id.text_likai);
        ll_zhubo_sttings = (LinearLayout) findViewById(R.id.ll_zhubo_sttings);
        ll_video = (LinearLayout) findViewById(R.id.ll_video);
        btn_guanli = (Button) findViewById(R.id.btn_guanli);
        btn_mute = (Button) findViewById(R.id.btn_mute);
        btn_kick = (Button) findViewById(R.id.btn_kick);
        btn_finish_back = (Button) findViewById(R.id.btn_finish_back);
        mx_video = (MxVideoView) findViewById(R.id.mx_video);
        adapter = new ChatAdapter();
        listview.setAdapter(adapter);

        btn_finish_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //hide keyboard
                rl_member_operate.setVisibility(View.GONE);
                return LivePushActivity.super.onTouchEvent(event);
            }
        });

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean reachBottom = false;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && reachBottom) {
                    reachBottom = false;
                    int count = loadLateData();
                    if (count > 0) {
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int visibleThreshold = 2;
                if (totalItemCount <= (firstVisibleItem + visibleItemCount + visibleThreshold)) {
                    reachBottom = true;
                }
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IMessage msg = messages.get(position);
                if (msg.sender != 0) {
                    showUserInfo(msg);
                    rl_member_operate.setVisibility(View.VISIBLE);
                }
                APP.isgame = false;
                Call<APIResponse<ToUserInfoBean>> userinfo = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).ToUserinfo(roomID + "", msg.sender + "");
                userinfo.enqueue(new MyCall<APIResponse<ToUserInfoBean>>() {
                    @Override
                    protected void onSuccess(Call<APIResponse<ToUserInfoBean>> call, Response<APIResponse<ToUserInfoBean>> response) {
                        ToUserInfoBean bean = response.body().getData();
                        tv_nick_name.setText(bean.getNickName());
                        Glide.with(LivePushActivity.this).load(bean.getAvater()).into(iv_avatar);
                        if (isAudience) {
                            if (bean.isIsManager()) {
                                ll_zhubo_sttings.setVisibility(View.INVISIBLE);
                                btn_guanli.setText("解除管理员");
                            }
                        } else {
                            if (bean.isIsManager()) {
                                btn_guanli.setText("解除管理员");
                            } else {
                                btn_guanli.setText("设置管理员");
                            }
                            if (bean.isIsBanUser()) {
                                btn_mute.setText("解除禁言");
                            } else {
                                btn_mute.setText("禁言");
                            }
                        }

                    }

                    @Override
                    protected void onError(Call<APIResponse<ToUserInfoBean>> call, Throwable t) {

                    }
                });
            }
        });

        goroom(Long.parseLong(APP.uid), roomID, APP.Token);
    }

    private void showGame() {
        APP.isgame = false;
        Call<APIResponse<RoomGameInfoBean>> call = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).GetRoomGameInfo(roomID + "");
        call.enqueue(new MyCall<APIResponse<RoomGameInfoBean>>() {
            @Override
            protected void onSuccess(Call<APIResponse<RoomGameInfoBean>> call, Response<APIResponse<RoomGameInfoBean>> response) {
                RoomGameInfoBean roomGameInfoBean = response.body().getData();
                show = false;
                if (roomGameInfoBean != null) {
                    APP.isgame = true;
                    roomtypelistinfo = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_SHIXUN).RoomTypelistinfo(roomGameInfoBean.getDesk_id());
                    roomtypelistinfo.enqueue(new MyCall<APIResponse<List<RoomInfoBean>>>() {
                        @Override
                        protected void onSuccess(Call<APIResponse<List<RoomInfoBean>>> call, Response<APIResponse<List<RoomInfoBean>>> response) {
                            bean = response.body().getData().get(0);
                            if (bean != null) {
                                long time = Long.parseLong(bean.getGameStarTime() + "") + Long.parseLong(bean.getCountDown() + "") * 1000 - Long.parseLong(bean.getSystime() + "");
                                if (bean.getPhase() == 3) {
                                    isPay = false;
                                    Time(time, "洗牌中", "洗牌中");
                                } else if (bean.getPhase() == 4) {
                                    isPay = true;
                                    Time(time, "开始下注", "待开牌");
                                } else if (bean.getPhase() == 5) {
                                    isPay = false;
                                    Time(time, "游戏结算", "游戏结算");
                                }
                                llgameyuan.setVisibility(View.VISIBLE);
                                llgameroom.setVisibility(View.VISIBLE);
                                pay.showData(bean.getDeskId() + "", bean.getGameId() + "");
                                go2Chat(Long.parseLong(APP.gameuid), Long.parseLong(bean.getDeskId() + ""), APP.gameToken);
                            }
                        }

                        @Override
                        protected void onError(Call<APIResponse<List<RoomInfoBean>>> call, Throwable t) {
                            if (t instanceof ResultException) {
                                ToastUtils.showToast(((ResultException) t).getInfo());
                            } else {
                                ToastUtils.showToast("网络请求失败,请稍后重试");
                            }
                        }
                    });
                }
            }

            @Override
            protected void onError(Call<APIResponse<RoomGameInfoBean>> call, Throwable t) {
                if (t instanceof ResultException) {
                    ToastUtils.showToast(((ResultException) t).getInfo());
                } else {
                    ToastUtils.showToast("网络请求失败,请稍后重试");
                }
            }
        });
    }

    private void showUserInfo(IMessage msg) {

        if (isAudience) {
            //观众
            ll_zhubo_sttings.setVisibility(View.INVISIBLE);
            if (guanli) {
                ll_zhubo_sttings.setVisibility(View.VISIBLE);
                //管理
                zhuboguanli(msg);
            }
        } else {
            //主播
            zhuboguanli(msg);
        }
    }

    private void zhuboguanli(IMessage msg) {
        HashMap usermap = new HashMap<String, String>();
        btn_guanli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usermap.put("room_id", msg.receiver + "");
                usermap.put("manager_id", msg.sender + "");
                Userinfo("SetRoomManager", usermap);
            }
        });

        btn_mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usermap.put("room_id", msg.receiver + "");
                usermap.put("fans_id", msg.sender + "");
                Userinfo("SetRoomManager", usermap);
            }
        });

        btn_kick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usermap.put("room_id", msg.receiver + "");
                usermap.put("fans_id", msg.sender + "");
                Userinfo("BanUser", usermap);
            }
        });
    }

    private void Userinfo(String text, HashMap<String, String> usermap) {
        APP.isgame = false;
        Call<APIResponse> manage = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).SetRoomManager(text, usermap);
        manage.enqueue(new MyCall<APIResponse>() {
            @Override
            protected void onSuccess(Call<APIResponse> call, Response<APIResponse> response) {
                ToastUtils.showToast(response.body().getInfo());
            }

            @Override
            protected void onError(Call<APIResponse> call, Throwable t) {
                if (t instanceof ResultException) {
                    ToastUtils.showToast(((ResultException) t).getInfo());
                } else {
                    ToastUtils.showToast("网络请求失败,请稍后重试");
                }
            }
        });
    }


    public void initView() {
        mPreviewView = (SurfaceView) findViewById(R.id.preview_view);
        mPreviewView.setVisibility(View.VISIBLE);
        mPreviewView.getHolder().addCallback(mCallback);
    }

    public SurfaceView getPreviewView() {
        return this.mPreviewView;
    }


    SurfaceHolder.Callback mCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            if (mSurfaceStatus == SurfaceStatus.UNINITED) {
                mSurfaceStatus = SurfaceStatus.CREATED;
                if (mAlivcLivePusher != null) {
                    try {
                        if (mAsync) {
                            mAlivcLivePusher.startPreviewAysnc(mPreviewView);
                        } else {
                            mAlivcLivePusher.startPreview(mPreviewView);
                        }
                        if (mAlivcLivePushConfig.isExternMainStream()) {
                            startYUV(getApplicationContext());
                        }
                    } catch (IllegalArgumentException e) {
                        e.toString();
                    } catch (IllegalStateException e) {
                        e.toString();
                    }
                }
            } else if (mSurfaceStatus == SurfaceStatus.DESTROYED) {
                mSurfaceStatus = SurfaceStatus.RECREATED;
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            mSurfaceStatus = SurfaceStatus.CHANGED;
            if (mLivePushFragment != null) {
                mLivePushFragment.setSurfaceView(mPreviewView);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            mSurfaceStatus = SurfaceStatus.DESTROYED;
        }
    };


    private GestureDetector.OnGestureListener mGestureDetector = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            if (mPreviewView.getWidth() > 0 && mPreviewView.getHeight() > 0) {
                float x = motionEvent.getX() / mPreviewView.getWidth();
                float y = motionEvent.getY() / mPreviewView.getHeight();
                try {
                    mAlivcLivePusher.focusCameraAtAdjustedPoint(x, y, true);
                } catch (IllegalStateException e) {

                }
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            if (motionEvent == null || motionEvent1 == null) {
                return false;
            }
            if (motionEvent.getX() - motionEvent1.getX() > FLING_MIN_DISTANCE
                    && Math.abs(v) > FLING_MIN_VELOCITY) {
                // Fling left
            } else if (motionEvent1.getX() - motionEvent.getX() > FLING_MIN_DISTANCE
                    && Math.abs(v) > FLING_MIN_VELOCITY) {
                // Fling right
            }
            return false;
        }
    };

    private float scaleFactor = 1.0f;
    private ScaleGestureDetector.OnScaleGestureListener mScaleGestureDetector = new ScaleGestureDetector.OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            if (scaleGestureDetector.getScaleFactor() > 1) {
                scaleFactor += 0.5;
            } else {
                scaleFactor -= 2;
            }
            if (scaleFactor <= 1) {
                scaleFactor = 1;
            }
            try {
                if (scaleFactor >= mAlivcLivePusher.getMaxZoom()) {
                    scaleFactor = mAlivcLivePusher.getMaxZoom();
                }
                mAlivcLivePusher.setZoom((int) scaleFactor);

            } catch (IllegalStateException e) {

            }
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {

        }
    };

    /**
     * 初始化底部控制栏布局
     */
    private void initBottomBar() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        liveBottomBar = new LiveBottomBar(LivePushActivity.this, this, isAudience, roomID + "", mAlivcLivePusher, fragmentManager);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rootView.addView(liveBottomBar, layoutParams);
        liveBottomBar.setVisibility(View.VISIBLE);
        sendedit = liveBottomBar.findViewById(R.id.send_edit);
        liveBottomBar.setMsgClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jinyan) {
                    ToastUtils.showToast("你已被禁言");
                    sendedit.setText("");
                    liveBottomBar.hide();
                } else {
                    UserRoomBean bean = new UserRoomBean();
                    bean.setText(sendedit.getText().toString());
                    RoomMessage message = new RoomMessage();
                    UserRoomBean.UserBean listBean = new UserRoomBean.UserBean();
                    listBean.setIcon(APP.userInfoBean.getInfo().getAvater());
                    listBean.setUserid(APP.uid);
                    listBean.setUsername(APP.userInfoBean.getInfo().getNickName());
                    bean.setUser(listBean);
                    String json = new Gson().toJson(bean);
                    message.sender = currentUID;
                    message.receiver = roomID;
                    message.content = json;
                    IMService im = IMService.getInstance();
                    im.sendRoomMessage(message);
                    liveBottomBar.hide();
                    sendedit.setText("");
                }
            }
        });
        if (isAudience) {
            liveBottomBar.setxiazhuClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isAudience) {
                        if (show) {
                            show = false;
                            llgameroom.setVisibility(View.GONE);
                        } else {
                            show = true;
                            llgameroom.setVisibility(View.VISIBLE);
                        }
                    } else {
                        ToastUtils.showToast("房间没有设置游戏");
                    }

                }
            });
        }


        liveBottomBar.setXClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAudience) {
                    finish();
                } else {
                    APP.isgame = false;
                    Call<APIResponse> over = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).LiveOver(roomID + "");
                    over.enqueue(new MyCall<APIResponse>() {
                        @Override
                        protected void onSuccess(Call<APIResponse> call, Response<APIResponse> response) {
                            ToastUtils.showToast(response.body().getInfo());
                            finish();
                        }

                        @Override
                        protected void onError(Call<APIResponse> call, Throwable t) {
                            if (t instanceof ResultException) {
                                ToastUtils.showToast(((ResultException) t).getInfo());
                            } else {
                                ToastUtils.showToast("网络请求失败,请稍后重试");
                            }
                        }
                    });
                }
            }
        });
    }


    /**
     * 根据是否为观众,加载不同的Fragment
     *
     * @param isAudience 是否为观众
     */
    private void loadFragment(boolean isAudience) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        if (isAudience) {
            audienceFragment = new AliyunPlayerFragment().newInstance(mPushUrl);
            transaction.replace(R.id.layout_main_content, audienceFragment);
            pay = new PayFragment().newInstance();
            transaction.replace(R.id.layout_game_room, pay);
            showGame();
        } else {
            mLivePushFragment = new LivePushFragment().newInstance(mPushUrl, mAsync, mAudioOnly, mVideoOnly, mCameraId, mFlash, mAlivcLivePushConfig.getQualityMode().getQualityMode(), mAuthTime, mPrivacyKey, mMixExtern, mMixMain);
            mLivePushFragment.setAlivcLivePusher(mAlivcLivePusher);
            mLivePushFragment.setStateListener(mStateListener);
            transaction.replace(R.id.layout_main_content, mLivePushFragment);
        }
        liveRoomInfoFragment = LiveRoomInfoFragment.getInstance(isAudience, roomID + "");
        transaction.replace(R.id.layout_room_info, liveRoomInfoFragment);
        transaction.commit();
    }

    /**
     * 直播开始完成的回调
     */
    public void onStartLivingFinished() {
//        isLiveStart = true;
//        chatLayout.setVisibility(View.VISIBLE);
//        liveBottomBar.setVisibility(View.VISIBLE);
        roomInfoLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 直播断开时的回调
     */
    public void onLiveDisconnect() {
//        isLiveStart = false;
//        chatLayout.setVisibility(View.GONE);
//        liveBottomBar.setVisibility(View.GONE);
        roomInfoLayout.setVisibility(View.GONE);
    }


    private void setOrientation(int orientation) {
        if (orientation == ORIENTATION_PORTRAIT.ordinal()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (orientation == ORIENTATION_LANDSCAPE_HOME_RIGHT.ordinal()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (orientation == ORIENTATION_LANDSCAPE_HOME_LEFT.ordinal()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        }
    }


    public static void startActivity(Activity activity, AlivcLivePushConfig alivcLivePushConfig, String url, boolean async, boolean audioOnly, boolean videoOnly, AlivcPreviewOrientationEnum orientation, int cameraId, boolean isFlash, String authTime, String privacyKey, boolean mixExtern, boolean mixMain, boolean isAudience, String roomid) {
        Intent intent = new Intent(activity, LivePushActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AlivcLivePushConfig.CONFIG, alivcLivePushConfig);
        bundle.putString(URL_KEY, url);
        bundle.putBoolean(ASYNC_KEY, async);
        bundle.putBoolean(AUDIO_ONLY_KEY, audioOnly);
        bundle.putBoolean(VIDEO_ONLY_KEY, videoOnly);
        bundle.putInt(ORIENTATION_KEY, orientation.ordinal());
        bundle.putInt(CAMERA_ID, cameraId);
        bundle.putBoolean(FLASH_ON, isFlash);
        bundle.putString(AUTH_TIME, authTime);
        bundle.putString(PRIVACY_KEY, privacyKey);
        bundle.putBoolean(MIX_EXTERN, mixExtern);
        bundle.putBoolean(MIX_MAIN, mixMain);
        bundle.putBoolean(IS_AUDIENCE, isAudience);
        bundle.putString("roomid", roomid);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, REQ_CODE_PUSH);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAlivcLivePusher != null) {
            try {
                if (!isPause) {
                    if (mAsync) {
                        mAlivcLivePusher.resumeAsync();
                    } else {
                        mAlivcLivePusher.resume();
                    }
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        if (!isAudience) {
            APP.isgame = false;
            Call<APIResponse> call = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).LiveReturnRoom(roomID + "");
            call.enqueue(new MyCall<APIResponse>() {
                @Override
                protected void onSuccess(Call<APIResponse> call, Response<APIResponse> response) {

                }

                @Override
                protected void onError(Call<APIResponse> call, Throwable t) {
                    if (t instanceof ResultException) {
                        ToastUtils.showToast(((ResultException) t).getInfo());
                    } else {
                        ToastUtils.showToast("网络请求失败,请稍后重试");
                    }
                }
            });
        }

//        if(mViewPager.getCurrentItem() != 1) {
//            mHandler.post(mRunnable);
//        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAlivcLivePusher != null) {
            mAlivcLivePusher.pause();
        }
        if (!isAudience) {
            APP.isgame = false;
            Call<APIResponse> call = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).LiveLeaveRoom(roomID + "");
            call.enqueue(new MyCall<APIResponse>() {
                @Override
                protected void onSuccess(Call<APIResponse> call, Response<APIResponse> response) {

                }

                @Override
                protected void onError(Call<APIResponse> call, Throwable t) {
                    if (t instanceof ResultException) {
                        ToastUtils.showToast(((ResultException) t).getInfo());
                    } else {
                        ToastUtils.showToast("网络请求失败,请稍后重试");
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        videoThreadOn = false;
        audioThreadOn = false;

        if (mAlivcLivePusher != null) {
            try {
                mAlivcLivePusher.destroy();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
//        if(mHandler != null) {
//            mHandler.removeCallbacks(mRunnable);
//            mHandler = null;
//        }
//        unregisterReceiver(mChangedReceiver);
        mLivePushFragment = null;
        mAlivcLivePushConfig = null;
        mDetector = null;
        mScaleDetector = null;
        mAlivcLivePusher = null;

//        mHandler = null;
        alivcLivePushStatsInfo = null;

        IMService.getInstance().leaveRoom(deskID);
        IMService.getInstance().removePeerObserver(this);
        IMService.getInstance().removeRoomObserver(this);
        IMService.getInstance().removeObserver(this);
        im.getInstance().leaveRoom(roomID);
        im.getInstance().removePeerObserver(this);
        im.getInstance().removeRoomObserver(this);
        im.getInstance().removeObserver(this);
        im.getInstance().stop();
        IMService.getInstance().stop();
        super.onDestroy();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        AlivcPreviewOrientationEnum orientationEnum;
        if (mAlivcLivePusher != null) {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientationEnum = ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientationEnum = ORIENTATION_LANDSCAPE_HOME_RIGHT;
                    break;
                case Surface.ROTATION_270:
                    orientationEnum = ORIENTATION_LANDSCAPE_HOME_LEFT;
                    break;
                default:
                    orientationEnum = ORIENTATION_PORTRAIT;
                    break;
            }
            mAlivcLivePusher.setPreviewOrientation(orientationEnum);
        }
    }

    public AlivcLivePusher getLivePusher() {
        return this.mAlivcLivePusher;
    }


    private void showDialog(Context context, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(getString(R.string.dialog_title));
        dialog.setMessage(message);
        dialog.setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    public interface PauseState {
        void updatePause(boolean state);
    }

    private PauseState mStateListener = new PauseState() {
        @Override
        public void updatePause(boolean state) {
            isPause = state;
        }
    };

    class ConnectivityChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                if (mNetWork != NetWorkUtils.getAPNType(context)) {
                    mNetWork = NetWorkUtils.getAPNType(context);
                    if (mAlivcLivePusher != null) {
                        if (mAlivcLivePusher.isPushing()) {
                            try {
                                mAlivcLivePusher.reconnectPushAsync(null);
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

        }
    }

    public void startYUV(final Context context) {
        new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            private AtomicInteger atoInteger = new AtomicInteger(0);

            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("LivePushActivity-readYUV-Thread" + atoInteger.getAndIncrement());
                return t;
            }
        }).execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                videoThreadOn = true;
                byte[] yuv;
                InputStream myInput = null;
                try {
                    File f = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "alivc_resource/capture0.yuv");
                    myInput = new FileInputStream(f);
                    byte[] buffer = new byte[1280 * 720 * 3 / 2];
                    int length = myInput.read(buffer);
                    //发数据
                    while (length > 0 && videoThreadOn) {
                        mAlivcLivePusher.inputStreamVideoData(buffer, 720, 1280, 1280 * 720 * 3 / 2, System.nanoTime() / 1000, 0);
                        try {
                            Thread.sleep(40);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //发数据
                        length = myInput.read(buffer);
                        if (length <= 0) {
                            myInput.close();
                            myInput = new FileInputStream(f);
                            length = myInput.read(buffer);
                        }
                    }
                    myInput.close();
                    videoThreadOn = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void stopYUV() {
        videoThreadOn = false;
    }

    private void stopPcm() {
        audioThreadOn = false;
    }


    @Override
    public void onRoomMessage(RoomMessage msg) {
//        if (msg.receiver != deskID) {
//            return;
//        }
        Log.i(TAG, "recv1 msg:" + msg.content);
        if (msg.content.contains("Cmd")) {
            JsonObject addMap = new JsonParser().parse(msg.content).getAsJsonObject();
            if (Integer.parseInt(addMap.get("Cmd").toString()) == 16) {
                return;
            }
            desk_id = addMap.get("DeskId").toString();
            bootnum = addMap.get("Boot_num").toString();
            pavenum = addMap.get("Pave_num").toString();
            if (!addMap.get("DeskId").toString().equals(desk_id)) {
                return;
            }
            if (Integer.parseInt(addMap.get("Cmd").toString()) == 3) {
                long time = Long.parseLong(addMap.get("GameStarTime").toString()) + Long.parseLong(addMap.get("CountDown").toString()) * 1000 - Long.parseLong(addMap.get("Systime").toString());
                //洗牌中
                Time(time, "洗牌中", "洗牌中");
                isPay = false;
                pay.isShow();
            } else if (Integer.parseInt(addMap.get("Cmd").toString()) == 4) {
                long time = Long.parseLong(addMap.get("GameStarTime").toString()) + Long.parseLong(addMap.get("CountDown").toString()) * 1000 - Long.parseLong(addMap.get("Systime").toString());
                //开始游戏
                Time(time, "开始下注", "待开牌");
                isPay = true;
                pay.isShow();
                llgameroom.setVisibility(View.VISIBLE);
                llgameyuan.setVisibility(View.VISIBLE);
            } else if (Integer.parseInt(addMap.get("Cmd").toString()) == 5) {
                long time = Long.parseLong(addMap.get("GameStarTime").toString()) + Long.parseLong(addMap.get("CountDown").toString()) * 1000 - Long.parseLong(addMap.get("Systime").toString());
                //游戏结算
                Time(time, "游戏结算", "游戏结算");
                isPay = false;
                pay.isShow();
            } else if (Integer.parseInt(addMap.get("Cmd").toString()) == 6) {
                //游戏结算结果
                int type = pay.getType();
                isPay = false;
                JsonObject s = new JsonParser().parse(msg.content).getAsJsonObject();
                String c = s.get("Winner").toString();
                if (c.equals("作废")) {
                    showfei();
                    return;
                }
                if (type == 1) {
                    //百家乐
                    LongHuBean bean = new LongHuBean();
                    bean.setId(1 + "");
                    bean.setTitle("和");
                    bean.setClick(false);
                    LongHuBean bean1 = new LongHuBean();
                    bean1.setId(4 + "");
                    bean1.setTitle("闲");
                    bean1.setClick(false);
                    LongHuBean bean2 = new LongHuBean();
                    bean2.setId(7 + "");
                    bean2.setTitle("庄");
                    bean2.setClick(false);
                    List<LongHuBean> data = new ArrayList<>();
                    JsonObject json = new JsonParser().parse(msg.content).getAsJsonObject();
                    String a = json.get("Winner").toString();
                    String changedString = a.replaceAll("\\\\", "");
                    String l = changedString.substring(1, changedString.length() - 1);
                    JsonObject tujosn = new JsonParser().parse(l).getAsJsonObject();
                    String game = tujosn.get("game").toString();
                    String banker = tujosn.get("bankerPair").toString();
                    String player = tujosn.get("playerPair").toString();
                    LongHuBean bean3 = new LongHuBean();
                    bean3.setId("bankerPair");
                    bean3.setTitle("庄对");
                    if (!banker.equals("0")) {
                        bean3.setClick(true);
                    } else {
                        bean3.setClick(false);
                    }
                    LongHuBean bean4 = new LongHuBean();
                    bean4.setId("playerPair");
                    bean4.setTitle("闲对");
                    if (!player.equals("0")) {
                        bean4.setClick(true);
                    } else {
                        bean4.setClick(false);
                    }
                    data.add(bean);
                    data.add(bean1);
                    data.add(bean2);
                    data.add(bean3);
                    data.add(bean4);
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getId().equals(game)) {
                            data.get(i).setClick(true);
                        }
                    }
                    showJiesuan(data, type);
                } else if (type == 2) {
                    //龙虎
                    LongHuBean bean = new LongHuBean();
                    bean.setId(1 + "");
                    bean.setTitle("和");
                    bean.setClick(false);
                    LongHuBean bean1 = new LongHuBean();
                    bean1.setId(4 + "");
                    bean1.setTitle("虎");
                    bean1.setClick(false);
                    LongHuBean bean2 = new LongHuBean();
                    bean2.setId(7 + "");
                    bean2.setTitle("龙");
                    bean2.setClick(false);
                    List<LongHuBean> data = new ArrayList<>();
                    JsonObject json = new JsonParser().parse(msg.content).getAsJsonObject();
                    String game = json.get("Winner").toString();
                    data.add(bean);
                    data.add(bean1);
                    data.add(bean2);
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getId().equals(game)) {
                            data.get(i).setClick(true);
                        }
                    }
                    showJiesuan(data, type);
                } else if (type == 3) {
                    //三公
                    LongHuBean bean = new LongHuBean();
                    bean.setId("x1win");
                    bean.setTitle("闲1");
                    bean.setClick(false);
                    LongHuBean bean1 = new LongHuBean();
                    bean1.setId("x2win");
                    bean1.setTitle("闲2");
                    bean1.setClick(false);
                    LongHuBean bean2 = new LongHuBean();
                    bean2.setId("x3win");
                    bean2.setTitle("闲3");
                    bean2.setClick(false);
                    LongHuBean bean3 = new LongHuBean();
                    bean3.setId("x4win");
                    bean3.setTitle("闲4");
                    bean3.setClick(false);
                    LongHuBean bean4 = new LongHuBean();
                    bean4.setId("x5win");
                    bean4.setTitle("闲5");
                    bean4.setClick(false);
                    LongHuBean bean5 = new LongHuBean();
                    bean5.setId("x6win");
                    bean5.setTitle("闲6");
                    bean5.setClick(false);
                    LongHuBean bean6 = new LongHuBean();
                    bean6.setId("x6win");
                    bean6.setTitle("闲6");
                    bean6.setClick(false);
                    LongHuBean bean7 = new LongHuBean();
                    bean7.setId("win");
                    bean7.setTitle("庄");
                    bean7.setClick(false);
                    List<LongHuBean> data = new ArrayList<>();
                    JsonObject json = new JsonParser().parse(msg.content).getAsJsonObject();
                    String a = json.get("Winner").toString();
                    String win[] = a.split(",");
                    data.add(bean);
                    data.add(bean1);
                    data.add(bean2);
                    data.add(bean3);
                    data.add(bean4);
                    data.add(bean5);
                    data.add(bean6);
                    data.add(bean7);
                    for (int i = 0; i < win.length; i++) {
                        for (int j = 0; j < data.size(); j++) {
                            if (data.get(j).getId().equals(win[i])) {
                                data.get(j).setClick(true);
                            }
                        }
                    }
                    showJiesuan(data, type);
                } else if (type == 4) {
                    //牛牛
                    LongHuBean bean = new LongHuBean();
                    bean.setId("x1win");
                    bean.setTitle("闲1");
                    bean.setClick(false);
                    LongHuBean bean1 = new LongHuBean();
                    bean1.setId("x2win");
                    bean1.setTitle("闲2");
                    bean1.setClick(false);
                    LongHuBean bean2 = new LongHuBean();
                    bean2.setId("x3win");
                    bean2.setTitle("闲3");
                    bean2.setClick(false);
                    LongHuBean bean7 = new LongHuBean();
                    bean7.setId("win");
                    bean7.setTitle("庄");
                    bean7.setClick(false);
                    List<LongHuBean> data = new ArrayList<>();
                    JsonObject json = new JsonParser().parse(msg.content).getAsJsonObject();
                    String a = json.get("Winner").toString();
                    String win[] = a.split(",");
                    data.add(bean);
                    data.add(bean1);
                    data.add(bean2);
                    data.add(bean7);
                    for (int i = 0; i < win.length; i++) {
                        for (int j = 0; j < data.size(); j++) {
                            if (data.get(j).getId().equals(win[i])) {
                                data.get(j).setClick(true);
                            }
                        }
                    }
                    showJiesuan(data, type);
                } else if (type == 5) {
                    //a98
                    LongHuBean bean = new LongHuBean();
                    bean.setId("ShunMenwin");
                    bean.setTitle("顺门");
                    bean.setClick(false);
                    LongHuBean bean1 = new LongHuBean();
                    bean1.setId("TianMenwin");
                    bean1.setTitle("天门");
                    bean1.setClick(false);
                    LongHuBean bean2 = new LongHuBean();
                    bean2.setId("FanMenwin");
                    bean2.setTitle("反门");
                    bean2.setClick(false);
                    LongHuBean bean7 = new LongHuBean();
                    bean7.setId("Bankerwin");
                    bean7.setTitle("庄");
                    bean7.setClick(false);
                    List<LongHuBean> data = new ArrayList<>();
                    JsonObject json = new JsonParser().parse(msg.content).getAsJsonObject();
                    String a = json.get("Winner").toString();
                    String win[] = a.split(",");
                    data.add(bean);
                    data.add(bean1);
                    data.add(bean2);
                    data.add(bean7);
                    for (int i = 0; i < win.length; i++) {
                        for (int j = 0; j < data.size(); j++) {
                            if (data.get(j).getId().equals(win[i])) {
                                data.get(j).setClick(true);
                            }
                        }
                    }
                    showJiesuan(data, type);
                }

            }
        } else if (msg.content.contains("text")) {
            final IMessage imsg = new IMessage();
            imsg.timestamp = now();
            imsg.msgLocalID = msgLocalID++;
            imsg.sender = msg.sender;
            imsg.receiver = msg.receiver;
            imsg.setContent(msg.content);
            loadUserName(imsg);
            insertMessage(imsg);
        } else if (msg.content.contains("Lmd")) {
            JsonObject json = new JsonParser().parse(msg.content).getAsJsonObject();
            if (json.get("Lmd").getAsInt() == 6) {
                //礼物排行
//                RankBean rank = new Gson().fromJson(json.get("Rank").toString(),RankBean.class);
            } else if (json.get("Lmd").getAsInt() == 5) {
                //礼物通知
                liveBottomBar.showGift(msg.content);
                ll_video.setVisibility(View.VISIBLE);
                mx_video.setVideoFromAssets("1.mp4");
            } else if (json.get("Lmd").getAsInt() == 9) {
                //设定游戏
                roomID = Long.parseLong(json.get("RoomId").toString());
                game_id = json.get("GameId").toString();
                desk_id = json.get("DeskId").toString();
                if (isAudience) {
                    pay.showData(desk_id, game_id);
                }
                showGame();
            } else if (json.get("Lmd").getAsInt() == 10) {
                //关播
                ll_live_finish.setVisibility(View.VISIBLE);
                liveBottomBar.setVisibility(View.GONE);
                if (mAlivcLivePusher != null) {
                    try {
                        mAlivcLivePusher.destroy();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            } else if (json.get("Lmd").getAsInt() == 1) {
                //进房间

            } else if (json.get("Lmd").getAsInt() == 11) {
                //离开房间
                if (!isAudience) {
                    text_likai.setVisibility(View.VISIBLE);
                    if (mAlivcLivePusher != null) {
                        mAlivcLivePusher.pause();
                    }
                }
            } else if (json.get("Lmd").getAsInt() == 12) {
                //回到房间
                if (!isAudience) {
                    text_likai.setVisibility(View.GONE);
                    if (mAlivcLivePusher != null) {
                        try {
                            if (!isPause) {
                                if (mAsync) {
                                    mAlivcLivePusher.resumeAsync();
                                } else {
                                    mAlivcLivePusher.resume();
                                }
                            }
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }


    }

    @Override
    public void onConnectState(IMService.ConnectState state) {
        if (state == IMService.ConnectState.STATE_CONNECTED) {
            enableSend();
        } else {
            disableSend();
        }
    }

    protected void saveMessage(IMessage imsg) {
        imsg.msgLocalID = msgLocalID++;
    }


    @Override
    protected boolean sendMessage(IMessage imsg) {
        RoomMessage rm = new RoomMessage();
        rm.sender = imsg.sender;
        rm.receiver = imsg.receiver;
        rm.content = imsg.content.getRaw();
        IMService im = IMService.getInstance();
        return im.sendRoomMessage(rm);
    }

    public void sendTextMessage(String text) {
        IMessage imsg = new IMessage();
        imsg.sender = Long.parseLong(APP.uid);
        imsg.receiver = roomID;
        MessageContent content = Text.newText(text);
        imsg.setContent(content);
        imsg.timestamp = now();
        imsg.isOutgoing = true;
        saveMessage(imsg);
        loadUserName(imsg);
        boolean r = sendMessage(imsg);
        if (!r) {
            imsg.setFailure(true);
        } else {
            imsg.setAck(true);
        }
    }

    private void go2Chat(long sender, long receiver, String token) {
        im = new IMService();
        im.stop();
        im.setHost(Host.SOCKE_URL);
        im.setPORT(Host.SOCKE_DUANKOU);
        PeerMessageHandler.getInstance().setUID(sender);
        GroupMessageHandler.getInstance().setUID(sender);

        IMHttpAPI.setToken(token);
        im.setToken(token);
        im.setDeviceID(sender + "");
        im.start();


        currentUID = sender;
        deskID = receiver;
        im.addRoomObserver(this);
        im.addPeerObserver(this);
        im.addObserver(this);
        im.enterRoom(deskID);
    }


    private void goroom(long sender, long receiver, String token) {
        IMService.getInstance().stop();
        IMService.getInstance().setHost(Host.SOCKE_URL);
        IMService.getInstance().setPORT(23002);
        PeerMessageHandler.getInstance().setUID(sender);
        GroupMessageHandler.getInstance().setUID(sender);

        IMHttpAPI.setToken(token);
        IMService.getInstance().setToken(token);
        IMService.getInstance().setDeviceID(sender + "");
        IMService.getInstance().start();


        currentUID = sender;
        roomID = receiver;
        IMService.getInstance().addRoomObserver(this);
        IMService.getInstance().addPeerObserver(this);
        IMService.getInstance().addObserver(this);
        IMService.getInstance().enterRoom(roomID);


        UserRoomBean bean = new UserRoomBean();
        bean.setText(msg);
        RoomMessage message = new RoomMessage();
        UserRoomBean.UserBean listBean = new UserRoomBean.UserBean();
        listBean.setIcon("");
        listBean.setUserid(APP.uid);
        listBean.setUsername("");
        bean.setUser(listBean);
        String json = new Gson().toJson(bean);
        message.sender = 0;
        message.receiver = roomID;
        message.content = json;
        final IMessage imsg = new IMessage();
        imsg.timestamp = now();
        imsg.msgLocalID = msgLocalID++;
        imsg.sender = message.sender;
        imsg.receiver = message.receiver;
        imsg.setContent(message.content);
        loadUserName(imsg);
        insertMessage(imsg);
    }


    @Override
    protected void insertMessage(IMessage imsg) {
        super.insertMessage(imsg);
        adapter.notifyDataSetChanged();
        listview.smoothScrollToPosition(messages.size() - 1);
    }

    @Override
    protected void replaceMessage(IMessage imsg, IMessage other) {
        super.replaceMessage(imsg, other);
        adapter.notifyDataSetChanged();
    }

    static interface ContentTypes {
        public static int UNKNOWN = 0;
        public static int AUDIO = 2;
        public static int IMAGE = 4;
        public static int LOCATION = 6;
        public static int TEXT = 8;
        public static int NOTIFICATION = 10;
        public static int LINK = 12;
        public static int VOIP = 14;
        public static int FILE = 16;
        public static int VIDEO = 18;
    }

    private static int VIEW_TYPE_COUNT = 20;

    class ChatAdapter extends BaseAdapter implements ContentTypes {
        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public Object getItem(int position) {
            return messages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            final int basic;
//            if (messages.get(position).isOutgoing) {
//                basic = IN_MSG;
//            } else {
            basic = IN_MSG;
//            }
            return getMediaType(position) + basic;
        }

        int getMediaType(int position) {
            IMessage msg = messages.get(position);
            final int media;
            if (msg.content instanceof Text) {
                media = TEXT;
            } else if (msg.content instanceof Image) {
                media = IMAGE;
            } else if (msg.content instanceof Audio) {
                media = AUDIO;
            } else if (msg.content instanceof Location) {
                media = LOCATION;
            } else if (msg.content instanceof Notification) {
                media = NOTIFICATION;
            } else if (msg.content instanceof Link) {
                media = LINK;
            } else if (msg.content instanceof VOIP) {
                media = VOIP;
            } else if (msg.content instanceof com.beetle.bauhinia.db.message.File) {
                media = FILE;
            } else if (msg.content instanceof Video) {
                media = VIDEO;
            } else {
                media = UNKNOWN;
            }

            return media;
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE_COUNT;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            IMessage msg = messages.get(position);
            MessageRowView rowView = (MessageRowView) convertView;
            if (rowView == null) {
                MessageContent.MessageType msgType = msg.content.getType();
//                if (msg.content instanceof Notification) {
//                    rowView = new MiddleMessageView(LivePushActivity.this, msgType);
//                }else {
                rowView = new InMessageView(LivePushActivity.this, msgType, isShowUserName);
//                }
                if (rowView != null) {
                    View contentView = rowView.getContentView();
                    if (msgType == MessageContent.MessageType.MESSAGE_TEXT) {
                        MessageTextView messageTextView = (MessageTextView) rowView.getContentView();
                        contentView = messageTextView.getTextView();
                    }

                    contentView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            IMessage im = (IMessage) v.getTag();
                            Log.i(TAG, "im:" + im.msgLocalID);
                            LivePushActivity.this.onMessageClicked(im);
                        }
                    });
                    contentView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            final IMessage im = (IMessage) v.getTag();

                            ArrayList<ChatItemQuickAction.ChatQuickAction> actions = new ArrayList<ChatItemQuickAction.ChatQuickAction>();

                            if (im.isFailure()) {
                                actions.add(ChatItemQuickAction.ChatQuickAction.RESEND);
                            }

                            if (im.content.getType() == MessageContent.MessageType.MESSAGE_TEXT) {
                                actions.add(ChatItemQuickAction.ChatQuickAction.COPY);
                            }

                            int now = now();
                            if (now >= im.timestamp && (now - im.timestamp) < (REVOKE_EXPIRE - 10) && im.isOutgoing) {
                                actions.add(ChatItemQuickAction.ChatQuickAction.REVOKE);
                            }

                            if (actions.size() == 0) {
                                return true;
                            }

                            ChatItemQuickAction.showAction(LivePushActivity.this,
                                    actions.toArray(new ChatItemQuickAction.ChatQuickAction[actions.size()]),
                                    new ChatItemQuickAction.ChatQuickActionResult() {

                                        @Override
                                        public void onSelect(ChatItemQuickAction.ChatQuickAction action) {
                                            switch (action) {
                                                case COPY:
                                                    ClipboardManager clipboard =
                                                            (ClipboardManager) LivePushActivity.this
                                                                    .getSystemService(Context.CLIPBOARD_SERVICE);
                                                    clipboard.setText(((Text) im.content).text);
                                                    break;
                                                case RESEND:
                                                    LivePushActivity.this.resend(im);
                                                    break;
                                                case REVOKE:
                                                    LivePushActivity.this.revoke(im);
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                    }
                            );
                            return true;
                        }
                    });
                    if (msgType == MessageContent.MessageType.MESSAGE_TEXT) {
                        MessageTextView messageTextView = (MessageTextView) rowView.getContentView();
                        messageTextView.setDoubleTapListener(new MessageTextView.DoubleTapListener() {
                            @Override
                            public void onDoubleTap(MessageTextView v) {
                                Text t = (Text) v.getMessage().content;
                                Log.i(TAG, "double click:" + t.text);

                                Intent intent = new Intent();
                                intent.setClass(LivePushActivity.this, OverlayActivity.class);
                                intent.putExtra("text", t.text);
                                LivePushActivity.this.startActivity(intent);
                            }
                        });
                    }
                }
            }
            rowView.setMessage(msg);
            return rowView;
        }
    }


    void onMessageClicked(IMessage message) {
        if (message.content instanceof Audio) {
            Audio audio = (Audio) message.content;
            if (FileCache.getInstance().isCached(audio.url)) {
                play(message);
            } else {
                FileDownloader.getInstance().download(message);
            }
        } else if (message.content instanceof Image) {
            navigateToViewImage(message);
        } else if (message.content.getType() == MessageContent.MessageType.MESSAGE_LOCATION) {
            Log.i(TAG, "location message clicked");
            Location loc = (Location) message.content;
//            startActivity(MapActivity.newIntent(this, loc.longitude, loc.latitude));
        } else if (message.content.getType() == MessageContent.MessageType.MESSAGE_LINK) {
            Link link = (Link) message.content;
            Intent intent = new Intent();
            intent.putExtra("url", link.url);
            intent.setClass(this, WebActivity.class);
            startActivity(intent);
        } else if (message.getType() == MessageContent.MessageType.MESSAGE_VOIP) {
            if (message.isOutgoing) {
                VOIP voip = (VOIP) message.content;
                if (voip.videoEnabled) {
                    callVideo();
                } else {
                    callVoice();
                }
            }
        } else if (message.getType() == MessageContent.MessageType.MESSAGE_FILE) {
            com.beetle.bauhinia.db.message.File f = (com.beetle.bauhinia.db.message.File) message.content;
            Intent intent = new Intent();
            intent.putExtra("url", f.url);
            intent.putExtra("size", f.size);
            intent.putExtra("filename", f.filename);
            intent.setClass(this, MessageFileActivity.class);
            startActivity(intent);
        } else if (message.getType() == MessageContent.MessageType.MESSAGE_VIDEO) {
            Video v = (Video) message.content;
            Intent intent = new Intent();
            intent.putExtra("url", v.url);
            intent.putExtra("sender", message.sender);
            intent.putExtra("secret", message.secret);
            intent.setClass(this, PlayerActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onPeerMessage(IMMessage msg) {
        Log.i(TAG, "recv msg:" + msg.content);
        if (msg.content.contains("Lmd")) {
            JsonObject json = new JsonParser().parse(msg.content).getAsJsonObject();
            if (json.get("Lmd").getAsInt() == 4) {
                //设置管理
                guanli = true;
                ToastUtils.showToast("你已成为管理员");
            } else if (json.get("Lmd").getAsInt() == 3) {
                //踢人
                ToastUtils.showToast("你被踢出房间");
                finish();
            } else if (json.get("Lmd").getAsInt() == 2) {
                //禁言
                jinyan = true;
                ToastUtils.showToast("你已被禁言");
            } else if (json.get("Lmd").getAsInt() == 7) {
                //解除禁言
                jinyan = false;
                ToastUtils.showToast("你已被解除禁言");
            } else if (json.get("Lmd").getAsInt() == 8) {
                //取消管理员
                guanli = false;
                ToastUtils.showToast("你已被取消管理员");
            }
        }


    }

    private void showJiesuan(List<LongHuBean> data, int type) {
        if (dialog == null) {
            View inflate = View.inflate(LivePushActivity.this, R.layout.dialog_jiesuan, null);
            RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
            GameJieSuanAdapter adapter = new GameJieSuanAdapter(R.layout.item_dialog_jiesuan, type);
            recyclerView.setLayoutManager(new LinearLayoutManager(LivePushActivity.this));
            recyclerView.setAdapter(adapter);
            adapter.setNewData(data);
            dialog = new MaterialDialog.Builder(LivePushActivity.this).customView(inflate, false).show();
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            } else {
                dialog.show();
            }
        }
    }

    private void showfei() {
        if (dialog == null) {
            View inflate = View.inflate(LivePushActivity.this, R.layout.dialog_fei, null);
            dialog = new MaterialDialog.Builder(LivePushActivity.this).customView(inflate, false).show();
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            } else {
                dialog.show();
            }
        }
    }

    @Override
    public void onPeerSecretMessage(IMMessage msg) {

    }

    @Override
    public void onPeerMessageACK(IMMessage msg) {

    }

    @Override
    public void onPeerMessageFailure(IMMessage msg) {

    }

    private void Time(long time, String name, String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timer = new CountDownTimer(time, 1000) {
                    @Override
                    public void onTick(long l) {
                        timetext.setText(name + "\n" + (l / 1000));
                    }

                    @Override
                    public void onFinish() {
                        timetext.setText(text + "\n" + 0);
                        if (pay != null) {
                            pay.isShow();
                        }
                        isPay = false;
                    }
                };
                timer.start();
            }
        });
    }


    public boolean showPay() {
        return isPay;
    }

    public void settextname(String s, String url) {
        tv_operate_name.setText(s);
        Glide.with(LivePushActivity.this).load(url).into(iv_operate);
    }
}

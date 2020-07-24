package com.qingpeng.mz.video.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alivc.live.pusher.AlivcLivePusher;
import com.aliyun.pusher.core.module.BeautyParams;
import com.aliyun.pusher.core.utils.AnimUitls;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qingpeng.mz.R;
import com.qingpeng.mz.adapter.DialogGameAdapter;
import com.qingpeng.mz.adapter.RoomGuanLiAdapter;
import com.qingpeng.mz.adapter.SendGiftListAdapter;
import com.qingpeng.mz.api.Host;
import com.qingpeng.mz.api.RedBag;
import com.qingpeng.mz.bean.GiftBean;
import com.qingpeng.mz.bean.GuanLiBean;
import com.qingpeng.mz.bean.RoomInfoBean;
import com.qingpeng.mz.okhttp.APIResponse;
import com.qingpeng.mz.okhttp.MyCall;
import com.qingpeng.mz.okhttp.ResultException;
import com.qingpeng.mz.okhttp.RetrofitCreateHelper;
import com.qingpeng.mz.utils.APP;
import com.qingpeng.mz.utils.ToastUtils;
import com.qingpeng.mz.video.activity.LivePushActivity;
import com.qingpeng.mz.video.myview.DialogVisibleListener;
import com.qingpeng.mz.video.myview.MusicDialog;
import com.qingpeng.mz.video.myview.PushBeautyDialog;
import com.qingpeng.mz.views.PeriscopeLayout;
import com.qingpeng.mz.views.XCRoundImageView;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import retrofit2.Call;
import retrofit2.Response;

import static com.alivc.live.pusher.AlivcLivePushCameraTypeEnum.CAMERA_TYPE_BACK;
import static com.alivc.live.pusher.AlivcLivePushCameraTypeEnum.CAMERA_TYPE_FRONT;

/**
 * Created by zhukkun on 1/6/17.
 */
public class LiveBottomBar extends RelativeLayout {

    private final LivePushActivity activity;
    private FragmentManager mFragmentManager;
    private AlivcLivePusher mAlivcLivePusher;
    private View btn_msg;
    private View btn_gift;
    private View btn_capture;
    private View btn_share;

    private View btn_filter;
    private View btn_music;
    private View btn_like;
    private View btn_send_gift;


    private int[] GiftIcon = new int[]{R.drawable.zem72,
            R.drawable.zem70,
            R.drawable.zem68,
            R.drawable.zem63};
    private PeriscopeLayout periscopeLayout; // 点赞爱心布局.

    //分享布局
    private RelativeLayout rl_share;
    private LinearLayout ll_share_content;
    private Button btn_http;
    private Button btn_hls;
    private Button btn_rtmp;
    private Button btn_share_cancel;

    // 发送礼物频率控制使用
    private long lastClickTime = 0;
    boolean isAudience;
    private String roomId;
    private int giftPosition = -1;
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private ImageView mMusic;
    private ImageView mFlash;
    ScheduledExecutorService mExecutorService = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());
    private boolean flashState = true;
    private ImageView mBeautyButton;


    private LinearLayout messageLayout;
    private TextView messageText;
    private EditText sendedit;
    private View contentView;
    private Context context;
    private PopupWindow popupWindow;
    private ImageView live_dian;
    private LinearLayout mBottomMenu;
    Vector<Integer> mDynamicals = new Vector<>();
    private ImageView mCamera;
    private ImageView live_x;
    private LinearLayout ima_youxi;
    private ImageView audience_share;
    private List<RoomInfoBean> data;
    private MaterialDialog alertDialog;
    private ImageView ima_xiazhu;
    private RoomInfoBean bean;

    private FrameLayout ll_gift_group;
    private TranslateAnimation outAnim;
    private TranslateAnimation inAnim;
    private Call<APIResponse<List<GiftBean>>> gift;
    private List<GiftBean> giftbean;
    private ImageView ima_guanli;

    public LiveBottomBar(LivePushActivity activity, Context context, boolean isAudience, String roomId, AlivcLivePusher mAlivcLivePusher, FragmentManager fragmentManager) {
        super(context);
        this.context = context;
        this.activity = activity;
        this.isAudience = isAudience;
        this.roomId = roomId;
        this.mAlivcLivePusher = mAlivcLivePusher;
        this.mFragmentManager = fragmentManager;
        int resourceId = isAudience ? R.layout.layout_live_audience_bottom_bar : R.layout.layout_live_captrue_bottom_bar;
        LayoutInflater.from(context).inflate(resourceId, this, true);
        initView();
    }


    private void initView() {
        if (isAudience) {
            APP.isgame = false;
            gift = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).GetGift();
            gift.enqueue(new MyCall<APIResponse<List<GiftBean>>>() {
                @Override
                protected void onSuccess(Call<APIResponse<List<GiftBean>>> call, Response<APIResponse<List<GiftBean>>> response) {
                    giftbean = response.body().getData();
                    APP.gif = giftbean;
                }

                @Override
                protected void onError(Call<APIResponse<List<GiftBean>>> call, Throwable t) {
                    if (t instanceof ResultException) {
                        ToastUtils.showToast(((ResultException) t).getInfo());
                    } else {
                        ToastUtils.showToast("网络请求失败,请稍后重试");
                    }
                }
            });
        }
        bindView();
        initGiftLayout();
        clickView();
        showPopwindow(isAudience);
        clearTiming(); // 开启定时清理礼物列表
        initAnim(); // 初始化动画


    }


    private void bindView() {
        // 点赞的爱心布局
        periscopeLayout = findView(R.id.periscope);
        rl_share = findView(R.id.share_layout);
        ll_share_content = findView(R.id.ll_share_content);
        btn_http = findView(R.id.btn_share_http);

        btn_rtmp = findView(R.id.btn_share_rtmp);

        ll_gift_group = findView(R.id.ll_gift_group);
        messageLayout = findView(R.id.layout_send_message);
        messageText = findView(R.id.text_send_message);
        sendedit = findView(R.id.send_edit);

        btn_share_cancel = findView(R.id.btn_share_cancel);
        if (isAudience) {
            btn_msg = findView(R.id.audience_message);
            btn_gift = findView(R.id.audience_gift);
            btn_share = findView(R.id.audience_share);
            btn_like = findView(R.id.audience_like);
            live_dian = findView(R.id.live_dian);
            live_x = findView(R.id.live_x);
        } else {
            btn_gift = findView(R.id.live_gift);
            btn_msg = findView(R.id.live_message);
            live_dian = findView(R.id.live_dian);
            btn_share = findView(R.id.audience_share);
            live_x = findView(R.id.live_x);

        }
    }


    /**
     * 显示popupWindow
     */
    private void showPopwindow(boolean type) {
        //加载弹出框的布局
        if (type) {
            //观众
            contentView = LayoutInflater.from(context).inflate(
                    R.layout.dian_guanzhong_popupwindow, null);
        } else {
            //直播
            contentView = LayoutInflater.from(context).inflate(
                    R.layout.dian_popupwindow, null);
        }
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        popupWindow.setOutsideTouchable(true);
        //设置可以点击
        popupWindow.setTouchable(true);
        //进入退出的动画，指定刚才定义的style
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);


        showData(type);
        // 按下android回退物理键 PopipWindow消失解决.

    }

    private void showData(boolean type) {
        if (type) {
            ima_xiazhu = contentView.findViewById(R.id.ima_xiazhu);
            audience_share = contentView.findViewById(R.id.audience_share);
        } else {
            mBeautyButton = contentView.findViewById(R.id.beauty_button);
            mBeautyButton.setSelected(SharedPreferenceUtils.isBeautyOn(getContext()));
            mMusic = contentView.findViewById(R.id.music);
            mFlash = contentView.findViewById(R.id.flash);
            mCamera = contentView.findViewById(R.id.camera);
            ima_guanli = contentView.findViewById(R.id.ima_guanli);
            ima_youxi = contentView.findViewById(R.id.ima_youxi);
            audience_share = contentView.findViewById(R.id.audience_share);

            ima_guanli.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    gameGuanli();
                }
            });

            ima_youxi.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity.showPay()) {
                        ToastUtils.showToast("请游戏结束后才切换游戏房间");
                    } else {
                        gamePop();
                    }
                    popupWindow.dismiss();
                }
            });
            mCamera.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCameraId == CAMERA_TYPE_FRONT.getCameraId()) {
                        mCameraId = CAMERA_TYPE_BACK.getCameraId();
                    } else {
                        mCameraId = CAMERA_TYPE_FRONT.getCameraId();
                    }
                    mAlivcLivePusher.switchCamera();
                    mFlash.post(new Runnable() {
                        @Override
                        public void run() {
                            mFlash.setClickable(mCameraId == CAMERA_TYPE_FRONT.getCameraId() ? false : true);
                            if (mCameraId == CAMERA_TYPE_FRONT.getCameraId()) {
                                mFlash.setSelected(false);
                            } else {
                                mFlash.setSelected(flashState);
                            }
                        }
                    });
                    popupWindow.dismiss();
                }
            });

            mBeautyButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    PushBeautyDialog pushBeautyDialog = PushBeautyDialog.newInstance(mBeautyButton.isSelected());
//                            pushBeautyDialog.setVisibleListener(dialogVisibleListener);
                    pushBeautyDialog.setAlivcLivePusher(mAlivcLivePusher);
                    pushBeautyDialog.setBeautyListener(mBeautyListener);
                    pushBeautyDialog.show(mFragmentManager, "beautyDialog");
                    popupWindow.dismiss();
                }
            });
            mMusic.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMusicDialog == null) {
                        mMusicDialog = MusicDialog.newInstance();
                        mMusicDialog.setAlivcLivePusher(mAlivcLivePusher);
//                                mMusicDialog.setVisibleListener(dialogVisibleListener);
                    }
                    if (!mMusicDialog.isAdded()) {
                        mMusicDialog.show(mFragmentManager, "beautyDialog");
                    }
                    popupWindow.dismiss();
                }
            });
            mFlash.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlivcLivePusher.setFlash(!mFlash.isSelected());
                    flashState = !mFlash.isSelected();
                    mFlash.post(new Runnable() {
                        @Override
                        public void run() {
                            mFlash.setSelected(!mFlash.isSelected());
                        }
                    });
                    popupWindow.dismiss();
                }
            });
        }
    }

    private void gameGuanli() {
        APP.isgame = false;
        Call<APIResponse<GuanLiBean>> call = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).RoomLiveManager(roomId + "");
        call.enqueue(new MyCall<APIResponse<GuanLiBean>>() {
            @Override
            protected void onSuccess(Call<APIResponse<GuanLiBean>> call, Response<APIResponse<GuanLiBean>> response) {
                GuanLiBean guanli = response.body().getData();
                if (guanli.getManagerList().size() > 0) {
                    if (alertDialog == null) {
                        View inflate = View.inflate(context, R.layout.dialog_guanlilist, null);
                        TextView tvCansle = (TextView) inflate.findViewById(R.id.tv_cansle);
                        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
                        RoomGuanLiAdapter adapter = new RoomGuanLiAdapter(R.layout.item_dialog_guanli);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setAdapter(adapter);
                        adapter.setNewData(guanli.getManagerList());
                        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                switch (view.getId()) {
                                    case R.id.text_room:
                                        GuanLiBean.ManagerListBean bean = guanli.getManagerList().get(position);
                                        APP.isgame = false;
                                        Call<APIResponse> call = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).RelieveRoomManager(roomId + "", bean.getUserId() + "");
                                        call.enqueue(new MyCall<APIResponse>() {
                                            @Override
                                            protected void onSuccess(Call<APIResponse> call, Response<APIResponse> response) {
                                                ToastUtils.showToast(response.body().getInfo());
                                                adapter.setNewData(data);
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

                                        break;
                                }
                            }
                        });
                        tvCansle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (alertDialog != null) {
                                    alertDialog.dismiss();
                                }
                            }
                        });
                        alertDialog = new MaterialDialog.Builder(context).customView(inflate, false).show();
                    } else {
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        } else {
                            alertDialog.show();
                        }
                    }
                }
            }

            @Override
            protected void onError(Call<APIResponse<GuanLiBean>> call, Throwable t) {
                if (t instanceof ResultException) {
                    ToastUtils.showToast(((ResultException) t).getInfo());
                } else {
                    ToastUtils.showToast("网络请求失败,请稍后重试");
                }
            }
        });
    }

    private void gamePop() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.game_popupwindow, null);
        PopupWindow pop = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        pop.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        pop.setOutsideTouchable(true);
        //设置可以点击
        pop.setTouchable(true);
        //进入退出的动画，指定刚才定义的style
        pop.setAnimationStyle(R.style.mypopwindow_anim_style);

        view.findViewById(R.id.ll_baijiale).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showGameListRoom("1");
            }
        });
        view.findViewById(R.id.ll_longhu).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showGameListRoom("2");
            }
        });
        view.findViewById(R.id.ll_niuniu).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showGameListRoom("3");
            }
        });
        view.findViewById(R.id.ll_sangong).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showGameListRoom("4");
            }
        });
        view.findViewById(R.id.ll_a89).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showGameListRoom("5");
            }
        });

        pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    private void showGameListRoom(String s) {
        APP.isgame = true;
        Call<APIResponse<List<RoomInfoBean>>> room = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_SHIXUN).RoomTypelist(s);
        room.enqueue(new MyCall<APIResponse<List<RoomInfoBean>>>() {
            @Override
            protected void onSuccess(Call<APIResponse<List<RoomInfoBean>>> call, Response<APIResponse<List<RoomInfoBean>>> response) {
                data = response.body().getData();
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setIsclick(false);
                }
                if (alertDialog == null) {
                    View inflate = View.inflate(context, R.layout.dialog_game, null);
                    TextView tvCansle = (TextView) inflate.findViewById(R.id.tv_cansle);
                    TextView tvOk = (TextView) inflate.findViewById(R.id.tv_ok);
                    RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
                    DialogGameAdapter adapter = new DialogGameAdapter(R.layout.item_dialog_game);
                    recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
                    recyclerView.setAdapter(adapter);
                    adapter.setNewData(data);
                    adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                            switch (view.getId()) {
                                case R.id.text_room:
                                    bean = data.get(position);
                                    for (int i = 0; i < data.size(); i++) {
                                        if (data.get(i) == data.get(position)) {
                                            data.get(i).setIsclick(true);
                                        } else {
                                            data.get(i).setIsclick(false);
                                        }
                                    }
                                    adapter.setNewData(data);
                                    break;
                            }
                        }
                    });
                    tvCansle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (alertDialog != null) {
                                alertDialog.dismiss();
                            }
                        }
                    });
                    tvOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (alertDialog != null) {
                                alertDialog.dismiss();
                            }
                            HashMap map = new HashMap<String, String>();
                            map.put("room_id", roomId);
                            map.put("desk_id", bean.getDeskId() + "");
                            map.put("game_id", bean.getGameId() + "");
                            APP.isgame = false;
                            Call<APIResponse> call = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).RoomGameInfo(map);
                            call.enqueue(new MyCall<APIResponse>() {
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
                    });
                    alertDialog = new MaterialDialog.Builder(context).customView(inflate, false).show();
                } else {
                    if (alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    } else {
                        alertDialog.show();
                    }
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

    private MusicDialog mMusicDialog = null;

    public interface BeautyListener {
        void onBeautySwitch(boolean beauty);

        void onBeautySettingChange(BeautyParams params);
    }

    private BeautyListener mBeautyListener = new BeautyListener() {
        @Override
        public void onBeautySwitch(boolean beauty) {
            if (mBeautyButton != null) {
                mBeautyButton.setSelected(beauty);
            }
        }

        @Override
        public void onBeautySettingChange(BeautyParams params) {
            if (mAlivcLivePusher != null) {
                mAlivcLivePusher.setBeautyWhite(params.beautyWhite);
                mAlivcLivePusher.setBeautyBuffing(params.beautyBuffing);
                mAlivcLivePusher.setBeautyCheekPink(params.beautyCheekPink);
                mAlivcLivePusher.setBeautyRuddy(params.beautyRuddy);
                mAlivcLivePusher.setBeautySlimFace(params.beautySlimFace);
                mAlivcLivePusher.setBeautyShortenFace(params.beautyShortenFace);
                mAlivcLivePusher.setBeautyBigEye(params.beautyBigEye);
            }
        }

    };

    // 初始化礼物布局
    protected void initGiftLayout() {

    }

    //强制显示或者关闭系统键盘
    public static void KeyBoard(final EditText txtSearchKey, final String status) {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager)
                        txtSearchKey.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (status.equals("open")) {
                    m.showSoftInput(txtSearchKey, InputMethodManager.SHOW_FORCED);
                } else {
                    m.hideSoftInputFromWindow(txtSearchKey.getWindowToken(), 0);
                }
            }
        }, 300);
    }

    public void hide() {
        messageLayout.setVisibility(GONE);
        KeyBoard(sendedit, "11");
    }

    private void clickView() {
        live_dian.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);

            }
        });
        btn_msg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                messageLayout.setVisibility(VISIBLE);
            }
        });

        sendedit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoard(sendedit, "open");
            }
        });
//        btn_share.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rl_share.setVisibility(VISIBLE);
//            }
//        });

        btn_gift.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showGiftLayout();
            }
        });

        if (isAudience) {
            btn_like.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isFastClick()) {
                        periscopeLayout.addHeart();
//                        sendLike();
                    }
                }
            });
        }
    }

    private void shareUrl(String url) {
        try {
            ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            // 将文本内容放到系统剪贴板里。
            cm.setText(url);
            Toast.makeText(getContext(), "直播地址已复制,请到第三方播放器打开,或分享给好友", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 显示礼物列表
    private void showGiftLayout() {
        View view = LayoutInflater.from(activity).inflate(
                R.layout.gift_layout, null);
        PopupWindow pop = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        pop.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        pop.setOutsideTouchable(true);
        //设置可以点击
        pop.setTouchable(true);
        //进入退出的动画，指定刚才定义的style
        pop.setAnimationStyle(R.style.mypopwindow_anim_style);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        SendGiftListAdapter adapter = new SendGiftListAdapter(R.layout.gift_item);
        recyclerView.setAdapter(adapter);
        adapter.setNewData(giftbean);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ima_xiazhu:
//                            showGift("gift01");
                        HashMap map = new HashMap<String, String>();
                        map.put("room_id", roomId + "");
                        map.put("live_id", APP.liveid + "");
                        map.put("gift_type", giftbean.get(position).getId() + "");
                        map.put("one_price", giftbean.get(position).getPrice() + "");
                        map.put("gift_num", "1");
                        APP.isgame = false;
                        Call<APIResponse> g = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).send_gift(map);
                        g.enqueue(new MyCall<APIResponse>() {
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
                        break;
                }
            }
        });
        pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    private NumberAnim giftNumberAnim;

    /**
     * 初始化动画
     */
    private void initAnim() {
        giftNumberAnim = new NumberAnim(); // 初始化数字动画
        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.gift_in); // 礼物进入时动画
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.gift_out); // 礼物退出时动画
    }

    public class NumberAnim {
        private Animator lastAnimator;

        public void showAnimator(View v) {

            if (lastAnimator != null) {
                lastAnimator.removeAllListeners();
                lastAnimator.cancel();
                lastAnimator.end();
            }
            ObjectAnimator animScaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.3f, 1.0f);
            ObjectAnimator animScaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.3f, 1.0f);
            AnimatorSet animSet = new AnimatorSet();
            animSet.playTogether(animScaleX, animScaleY);
            animSet.setDuration(200);
            lastAnimator = animSet;
            animSet.start();
        }
    }

    /**
     * 刷礼物的方法
     */
    public void showGift(String tag) {
        JsonObject json = new JsonParser().parse(tag).getAsJsonObject();
        String a = json.get("UserId").toString();
        View newGiftView = ll_gift_group.findViewWithTag(a);
        // 是否有该tag类型的礼物
        if (newGiftView == null) {
            // 判断礼物列表是否已经有3个了，如果有那么删除掉一个没更新过的, 然后再添加新进来的礼物，始终保持只有3个
            if (ll_gift_group.getChildCount() >= 3) {
                // 获取前2个元素的最后更新时间
                View giftView01 = ll_gift_group.getChildAt(0);
                ImageView iv_gift01 = giftView01.findViewById(R.id.iv_gift);
                long lastTime1 = (long) iv_gift01.getTag();

                View giftView02 = ll_gift_group.getChildAt(1);
                ImageView iv_gift02 = giftView02.findViewById(R.id.iv_gift);
                long lastTime2 = (long) iv_gift02.getTag();

                if (lastTime1 > lastTime2) { // 如果第二个View显示的时间比较长
                    removeGiftView(1);
                } else { // 如果第一个View显示的时间长
                    removeGiftView(0);
                }
            }

            // 获取礼物
            newGiftView = getNewGiftView(tag);
            ll_gift_group.addView(newGiftView);

            // 播放动画
            newGiftView.startAnimation(inAnim);
            final MagicTextView mtv_giftNum = newGiftView.findViewById(R.id.mtv_giftNum);
            inAnim.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    giftNumberAnim.showAnimator(mtv_giftNum);
                }
            });
        } else {
            // 如果列表中已经有了该类型的礼物，则不再新建，直接拿出
            // 更新标识，记录最新修改的时间，用于回收判断
            ImageView iv_gift = newGiftView.findViewById(R.id.iv_gift);
            iv_gift.setTag(System.currentTimeMillis());

            // 更新标识，更新记录礼物个数
            MagicTextView mtv_giftNum = newGiftView.findViewById(R.id.mtv_giftNum);
            int giftCount = (int) mtv_giftNum.getTag() + 1; // 递增
            mtv_giftNum.setText("x" + giftCount);
            mtv_giftNum.setTag(giftCount);
            giftNumberAnim.showAnimator(mtv_giftNum);
        }
    }

    /**
     * 获取礼物
     */
    private View getNewGiftView(String tag) {
        JsonObject json = new JsonParser().parse(tag).getAsJsonObject();
        String name = json.get("UserNickname").toString();
        String giftType = json.get("GiftType").toString();
        String avater = json.get("Avater").toString();
        // 添加标识, 该view若在layout中存在，就不在生成（用于findViewWithTag判断是否存在）
        View giftView = LayoutInflater.from(getContext()).inflate(R.layout.item_gift, null);
        giftView.setTag(tag);

        // 添加标识, 记录生成时间，回收时用于判断是否是最新的，回收最老的
        ImageView iv_gift = giftView.findViewById(R.id.iv_gift);
        iv_gift.setTag(System.currentTimeMillis());

        // 添加标识，记录礼物个数
        MagicTextView mtv_giftNum = giftView.findViewById(R.id.mtv_giftNum);
        mtv_giftNum.setTag(1);
        mtv_giftNum.setText("x1");


        TextView text_name = giftView.findViewById(R.id.text_name);
        XCRoundImageView ima = giftView.findViewById(R.id.ima_touxiang);
        text_name.setText(name);
        Glide.with(activity).load(avater).into(ima);
        for (int i = 0; i < giftbean.size(); i++) {
            if (giftbean.get(i).getId() == Integer.parseInt(giftType)) {
//                Glide.with(activity).load(giftbean.get(i).getImgurl()).into(iv_gift);
                Picasso.with(activity)
                        .load(giftbean.get(i).getImgurl())
                        .into(iv_gift);
            }
        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 10;
        giftView.setLayoutParams(lp);

        return giftView;
    }

    /**
     * 移除礼物列表里的giftView
     */
    private void removeGiftView(final int index) {
        // 移除列表，外加退出动画
        final View removeGiftView = ll_gift_group.getChildAt(index);
        outAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_gift_group.removeViewAt(index);
            }
        });

        // 开启动画，因为定时原因，所以可能是在子线程
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeGiftView.startAnimation(outAnim);
            }
        });
    }

    /**
     * 定时清理礼物列表信息
     */
    private void clearTiming() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                int childCount = ll_gift_group.getChildCount();
                long nowTime = System.currentTimeMillis();
                for (int i = 0; i < childCount; i++) {

                    View childView = ll_gift_group.getChildAt(i);
                    ImageView iv_gift = (ImageView) childView.findViewById(R.id.iv_gift);
                    long lastUpdateTime = (long) iv_gift.getTag();

                    // 更新超过3秒就刷新
                    if (nowTime - lastUpdateTime >= 3000) {
                        removeGiftView(i);
                    }
                }
            }
        }, 0, 3000);
    }


    public void setMsgClickListener(OnClickListener onClickListener) {
        messageText.setOnClickListener(onClickListener);
    }

    public void setXClickListener(OnClickListener onClickListener) {
        live_x.setOnClickListener(onClickListener);
    }

    public void setxiazhuClickListener(OnClickListener onClickListener) {
        popupWindow.dismiss();
        ima_xiazhu.setOnClickListener(onClickListener);
    }

    /*************************
     * 点赞爱心
     ********************************/


    // 发送爱心频率控制
    private boolean isFastClick() {
        long currentTime = System.currentTimeMillis();
        long time = currentTime - lastClickTime;
        if (time > 0 && time < 1000) {
            return true;
        }
        lastClickTime = currentTime;
        return false;
    }


    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }


    DialogVisibleListener dialogVisibleListener = new DialogVisibleListener() {
        @Override
        public void isDialogVisible(boolean isVisible) {

            showBottomMenu(!isVisible);

        }
    };

    public void showBottomMenu(boolean isShow) {

        if (isShow) {

            if (mBottomMenu != null && mBottomMenu.getVisibility() != View.VISIBLE) {

                AnimUitls.startAppearAnimY(mBottomMenu);
                mBottomMenu.setVisibility(View.VISIBLE);
            }
        } else {

            if (mBottomMenu != null && mBottomMenu.getVisibility() == View.VISIBLE) {

                AnimUitls.startDisappearAnimY(mBottomMenu);
                mBottomMenu.setVisibility(View.GONE);
            }
        }
    }
}

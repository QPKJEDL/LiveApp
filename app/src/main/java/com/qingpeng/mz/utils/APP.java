package com.qingpeng.mz.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.beetle.im.IMService;
import com.bravin.btoast.BToast;
import com.qingpeng.mz.R;
import com.qingpeng.mz.bean.CoverBean;
import com.qingpeng.mz.bean.GiftBean;
import com.qingpeng.mz.bean.UserInfoBean;
import com.qingpeng.mz.bean.ZhuBoRoomInfoBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.List;


public class APP extends MultiDexApplication {
    private static final String LOG_TAG = "FRAME_LOGGER";
    public static boolean isgame;
    public static int liveid;
    public static List<ZhuBoRoomInfoBean> ListRoom;
    public static List<GiftBean> gif;
    public static ZhuBoRoomInfoBean roominfo;
    protected static Context mContext;
    protected static Handler mHandler;
    protected static int mainThreadId;
    private static APP instance;
    private Typeface typeface;
    private Typeface typeface1;
    public static String uid;
    public static String Token = "";
    public static UserInfoBean userInfoBean;
    public static String gameuid;
    public static String gameToken = "";

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.c_9698a7);//全局设置主题颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Scale);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }


    //    public static List<String> host;
//    public static List<String> socket;
    @Override
    public void onCreate() {
        super.onCreate();

        APP.ListRoom = new ArrayList<>();
        mContext = getApplicationContext();
        //app可以单独部署服务器，给予第三方应用更多的灵活性
//        mIMService.setHost("imnode2.gobelieve.io");
//        IMHttpAPI.setAPIURL("http://api.gobelieve.io");
        MultiDex.install(this);

        //监听网路状态变更
        IMService.getInstance().registerConnectivityChangeReceiver(getApplicationContext());
        mHandler = new Handler();
        mainThreadId = android.os.Process.myTid();
//        host = new ArrayList<>();
//        host.add("http://hongbao.webziti.com");
//        host.add("http://game2.zllmqw.com/");
//        host.add("http://game3.zllmqw.com/");
//        socket = new ArrayList<>();
//        socket.add("ws://47.93.238.220:8238");
//        socket.add("ws://39.98.64.12:8238");
//        socket.add("ws://39.98.64.12:8238");
        BToast.Config.getInstance()
//                .setAnimate() // Whether to startAnimation. default is fasle;
//                .setAnimationDuration()// Animation duration. default is 800 millisecond
//                .setAnimationGravity()// Animation entering position. default is BToast.ANIMATION_GRAVITY_TOP
//                .setDuration()// toast duration  is Either BToast.DURATION_SHORT or BToast.DURATION_LONG
//                .setTextColor()// textcolor. default is white
//                .setErrorColor()// error style background Color default is red
//                .setInfoColor()// info style background Color default is blue
//                .setSuccessColor()// success style background Color default is green
//                .setWarningColor()// waring style background Color default is orange
//                .setLayoutGravity()// whan show an toast with target, coder can assgin position relative to target. default is BToast.LAYOUT_GRAVITY_BOTTOM
//                .setLongDurationMillis()// long duration. default is 4500 millisecond
//                .setRadius()// radius. default is half of view's height. coder can assgin a positive value
//                .setRelativeGravity()// whan show an toast with target, coder can assgin position relative to toastself(like relativeLayout start end center), default is BToast.RELATIVE_GRAVITY_CENTER
//                .setSameLength()// sameLength.  whan layoutGravity is BToast.LAYOUT_GRAVITY_TOP or BToast.LAYOUT_GRAVITY_BOTTOM,sameLength mean toast's width is as same as target,otherwise is same height
//                .setShortDurationMillis()// short duration. default is 3000 millisecond
//                .setShowIcon()// show or hide icon
//                .setTextSize()// text size. sp unit
                .apply(this);// must call
        init();
        instance = (APP) getApplicationContext();
        typeface = Typeface.createFromAsset(instance.getAssets(), "fonts/cu.TTF");//下载的字体
        typeface1 = Typeface.createFromAsset(instance.getAssets(), "fonts/zhun.TTF");//下载的字体
    }

    public static APP getInstance() {
        return instance;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public Typeface getTypeface1() {
        return typeface1;
    }

    private void init() {

    }

    public static Context getContext() {
        return mContext;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }

}

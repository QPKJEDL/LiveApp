package com.qingpeng.mz.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingpeng.mz.R;
import com.qingpeng.mz.adapter.HuaTiAdapter;
import com.qingpeng.mz.api.Host;
import com.qingpeng.mz.api.RedBag;
import com.qingpeng.mz.base.BaseActivity;
import com.qingpeng.mz.bean.CoverBean;
import com.qingpeng.mz.bean.HuaTiBean;
import com.qingpeng.mz.okhttp.APIResponse;
import com.qingpeng.mz.okhttp.MyCall;
import com.qingpeng.mz.okhttp.ResultException;
import com.qingpeng.mz.okhttp.RetrofitCreateHelper;
import com.qingpeng.mz.utils.APP;
import com.qingpeng.mz.utils.ResizeAbleSurfaceView;
import com.qingpeng.mz.utils.ToastUtils;
import com.qingpeng.mz.video.activity.PushConfigActivity;
import com.qingpeng.mz.views.XCRoundImageView;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class CreateLiveActivity extends BaseActivity implements SurfaceHolder.Callback {

    @BindView(R.id.ed_room_name)
    EditText edRoomName;
    @BindView(R.id.ima_touxiang)
    XCRoundImageView imaTouxiang;
    @BindView(R.id.text_huati)
    TextView textHuati;
    @BindView(R.id.text_pindao)
    TextView textPindao;
    @BindView(R.id.camera_surface)
    ResizeAbleSurfaceView cameraSurface;
    private PopupWindow popupWindow;
    private View contentView;
    private Call<APIResponse<List<HuaTiBean>>> huati;
    private int type;
    private HashMap<String, String> map;
    private Call<APIResponse<CoverBean>> cover;
    public static final String IS_AUDIENCE = "is_audience";
    private static final String URL_KEY = "url_key";
    Intent intent;
    private SurfaceHolder holder;
    private Camera camera;
    private static final int FRONT = 1;//前置摄像头标记
    private static final int BACK = 0;//后置摄像头标记
    private int currentCameraType = BACK;//当前打开的摄像头标记
    private Point size;
    private Point mScreenResolution;
    private Point previewSizeOnScreen;
    private Point pictureSizeOnScreen;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_live;
    }

    @Override
    protected int getRootViewId() {
        return 0;
    }

    @Override
    protected int getRefreshId() {
        return 0;
    }

    @Override
    protected int getListViewId() {
        return 0;
    }


    @Override
    protected void onResume() {
        super.onResume();
        cameraSurface.getHolder().addCallback(this);
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher_moren)//图片加载出来前，显示的图片
                .fallback(R.mipmap.ic_launcher_moren) //url为空的时候,显示的图片
                .error(R.mipmap.ic_launcher_moren);//图片加载失败后，显示的图片
        Glide.with(mContext).load(APP.userInfoBean.getInfo().getAvater()).apply(options).into(imaTouxiang);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.release();
    }

    public int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) (context.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) (context.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open(FRONT);
//        cameraSurface.resize(camera.getParameters().getPreviewSize().height, camera.getParameters().getPreviewSize().width);
//        Point size = getBestCameraResolution(camera.getParameters(), getScreenMetrics(mContext));
//        cameraSurface.resize(cameraSurface.getWidth(),cameraSurface.getHeight());

        setCameraSize(camera, cameraSurface.getHeight(), cameraSurface.getWidth());
        try {
            camera.setDisplayOrientation(90);//将预览旋转90度
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setCameraSize(Camera camera, float needW, float needH) {
        if (null == camera) return;
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> list = parameters.getSupportedPreviewSizes();
        /**
         * 这个返回的是所有camera支持的尺寸，需要注意的是并不是所有我们需要的尺寸摄像头都支持，
         * 比如我现在的画布尺寸是宽230高120，这个尺寸摄像头是绝对不支持的所以我们需要在摄像头
         * 支持的所有尺寸中选择以一个最接近我们目标的
         */
        float needRatio = needW / needH;
        Log.e("我需要的宽高比为", String.valueOf(needRatio));
        LinkedHashMap<Float, Camera.Size> map = new LinkedHashMap<>();
        float bestRatio = 0;
        for (Camera.Size size : list) {
            Log.e("Camera.Size", size.width + "," + size.height + "," + (float) size.width / size.height);
            /**
             * 先把所有的尺寸打出来让大家有一个认识
             */
            /**
             * 将当前宽高比的首位，存入map
             */
            if (!map.containsKey((float) size.width / size.height))
                map.put((float) size.width / size.height, size);

            if (bestRatio == 0 || Math.abs(needRatio - (float) size.width / size.height) < Math.abs(needRatio - bestRatio)) {
                bestRatio = (float) size.width / size.height;
            }
        }
        Camera.Size beatSize = map.get(bestRatio);
        Log.e("最佳的Camera.Size", beatSize.width + "---" + beatSize.height);

        parameters.setPreviewSize(beatSize.width, beatSize.height);
        camera.setParameters(parameters);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.release();
    }

    /**
     * 显示popupWindow
     */
    private void showPopwindow(String s) {
        //加载弹出框的布局
        contentView = LayoutInflater.from(CreateLiveActivity.this).inflate(
                R.layout.huati_popupwindow, null);

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


        showData(s);
        // 按下android回退物理键 PopipWindow消失解决

    }

    private void showData(String s) {
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new HuaTiAdapter(R.layout.item_huati, type);
        recyclerView.setAdapter(mAdapter);
        APP.isgame = false;
        huati = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).huatipindao(s);
        huati.enqueue(new MyCall<APIResponse<List<HuaTiBean>>>() {

            @Override
            protected void onSuccess(Call<APIResponse<List<HuaTiBean>>> call, Response<APIResponse<List<HuaTiBean>>> response) {
                List<HuaTiBean> data = response.body().getData();
                mAdapter.setNewData(data);
                mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        switch (view.getId()) {
                            case R.id.text_canyu:
                                if (type == 1) {
                                    textHuati.setText(data.get(position).getLabel());
                                } else {
                                    textPindao.setText(data.get(position).getChannel());
                                }
                                popupWindow.dismiss();
                                break;
                        }
                    }
                });
                popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
            }

            @Override
            protected void onError(Call<APIResponse<List<HuaTiBean>>> call, Throwable t) {
                if (t instanceof ResultException) {
                    ToastUtils.showToast(((ResultException) t).getInfo());
                } else {
                    ToastUtils.showToast("网络请求失败,请稍后重试");
                }
            }
        });


    }


    @OnClick({R.id.ima_touxiang, R.id.text_huati, R.id.text_pindao, R.id.pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ima_touxiang:
                break;
            case R.id.text_huati:
                type = 1;
                showPopwindow("GetLabel");
                break;
            case R.id.text_pindao:
                type = 2;
                showPopwindow("GetHasChannel");
                break;
            case R.id.pay:
                map = new HashMap<>();
                map.put("covername", edRoomName.getText().toString());
                map.put("label", textHuati.getText().toString());
                map.put("channel", textPindao.getText().toString());
                APP.isgame = false;
                cover = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).SetCover(map);
                cover.enqueue(new MyCall<APIResponse<CoverBean>>() {
                    @Override
                    protected void onSuccess(Call<APIResponse<CoverBean>> call, Response<APIResponse<CoverBean>> response) {
                        CoverBean data = response.body().getData();
                        intent = new Intent(mContext, PushConfigActivity.class).putExtra(IS_AUDIENCE, false)
                                .putExtra(URL_KEY, data.getList().getPush())
                                .putExtra("roomid", data.getList().getRoomId() + "");
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    protected void onError(Call<APIResponse<CoverBean>> call, Throwable t) {
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
}

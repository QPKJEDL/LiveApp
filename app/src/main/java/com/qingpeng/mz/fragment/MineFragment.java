package com.qingpeng.mz.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qingpeng.mz.R;
import com.qingpeng.mz.activity.GiftActivity;
import com.qingpeng.mz.activity.LiveAccountActivity;
import com.qingpeng.mz.activity.UserLookActivity;
import com.qingpeng.mz.activity.UserSttingActivity;
import com.qingpeng.mz.api.Host;
import com.qingpeng.mz.api.RedBag;
import com.qingpeng.mz.base.BaseFragment;
import com.qingpeng.mz.bean.AvaterBean;
import com.qingpeng.mz.okhttp.APIResponse;
import com.qingpeng.mz.okhttp.MyCall;
import com.qingpeng.mz.okhttp.ResultException;
import com.qingpeng.mz.okhttp.RetrofitCreateHelper;
import com.qingpeng.mz.utils.APP;
import com.qingpeng.mz.utils.ToastUtils;
import com.qingpeng.mz.views.CuTextView;
import com.qingpeng.mz.views.XCRoundImageView;
import com.qingpeng.mz.views.ZhunTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class MineFragment extends BaseFragment {

    public static final String IS_AUDIENCE = "is_audience";
    private static final String URL_KEY = "url_key";
    Intent intent;
    @BindView(R.id.mine_touxiang)
    XCRoundImageView mineTouxiang;
    @BindView(R.id.text_name)
    CuTextView textName;
    @BindView(R.id.text_id)
    ZhunTextView textId;
    @BindView(R.id.text_guanzhu)
    ZhunTextView textGuanzhu;
    @BindView(R.id.text_fensi)
    ZhunTextView textFensi;
    @BindView(R.id.text_huozan)
    ZhunTextView textHuozan;


    private List<LocalMedia> list;
    private List<String> paths;
    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine_fragment;
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
    public void onResume() {
        super.onResume();
        if (APP.userInfoBean != null) {
            textFensi.setText("粉丝: " + APP.userInfoBean.getInfo().getFans());
            textGuanzhu.setText("关注: " + APP.userInfoBean.getInfo().getFollowed());
            textName.setText(APP.userInfoBean.getInfo().getNickName());
            textId.setText("直播Id: " + APP.userInfoBean.getInfo().getUserId());
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.ic_launcher_moren)//图片加载出来前，显示的图片
                    .fallback(R.mipmap.ic_launcher_moren) //url为空的时候,显示的图片
                    .error(R.mipmap.ic_launcher_moren);//图片加载失败后，显示的图片
            Glide.with(mContext).load(APP.userInfoBean.getInfo().getAvater()).apply(options).into(mineTouxiang);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    list = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isCompressed()) {
                            paths = new ArrayList<>();
                            paths.add(list.get(i).getCompressPath());
                        }
                    }
                    final File file = new File(paths.get(0));
                    RequestBody requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), file);

                    // MultipartBody.Part is used to send also the actual file name
                    MultipartBody.Part MultipartFile =
                            MultipartBody.Part.createFormData("avater", file.getName(), requestFile);
                    APP.isgame = false;
                    Call<APIResponse<AvaterBean>> upavater = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_TOU).upavater(MultipartFile);
                    upavater.enqueue(new MyCall<APIResponse<AvaterBean>>() {
                        @Override
                        protected void onSuccess(Call<APIResponse<AvaterBean>> call, Response<APIResponse<AvaterBean>> response) {
                            AvaterBean bean = response.body().getData();
                            RequestOptions options = new RequestOptions()
                                    .placeholder(R.mipmap.ic_launcher_moren)//图片加载出来前，显示的图片
                                    .fallback(R.mipmap.ic_launcher_moren) //url为空的时候,显示的图片
                                    .error(R.mipmap.ic_launcher_moren);//图片加载失败后，显示的图片
                            Glide.with(mContext).load(bean.getAvater()).apply(options).into(mineTouxiang);
                        }

                        @Override
                        protected void onError(Call<APIResponse<AvaterBean>> call, Throwable t) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }

    private void show() {
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .compress(true)// 是否压缩 true or false
                .selectionMedia(list)// 是否传入已选图片 List<LocalMedia> list
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .enableCrop(true)// 是否裁剪 true or false
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @OnClick({R.id.ima_sttings, R.id.mine_touxiang, R.id.mine_kanguo, R.id.mine_zhanghu, R.id.mine_huiyuan, R.id.mine_huodong, R.id.mine_zhubo, R.id.mine_fensi, R.id.mine_bangzhu, R.id.mine_sixin, R.id.mine_renwu, R.id.mine_qianbao, R.id.mine_dengji})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_touxiang:
                show();
                break;
            case R.id.mine_kanguo:
                startActivity(UserLookActivity.class);
                break;
            case R.id.mine_zhanghu:
                startActivity(LiveAccountActivity.class);
                break;
            case R.id.mine_huiyuan:
                break;
            case R.id.mine_huodong:
                break;
            case R.id.mine_zhubo:
                break;
            case R.id.mine_fensi:
                startActivity(GiftActivity.class, new Intent().putExtra("type", "1"));
                break;
            case R.id.mine_bangzhu:
                break;
            case R.id.mine_sixin:

                break;
            case R.id.mine_renwu:

                break;
            case R.id.mine_qianbao:
                startActivity(LiveAccountActivity.class);
                break;
            case R.id.mine_dengji:
                break;
            case R.id.ima_sttings:
                startActivity(UserSttingActivity.class);
                break;
        }
    }

}

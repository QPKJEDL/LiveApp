package com.qingpeng.mz.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.chaychan.library.BottomBarLayout;
import com.qingpeng.mz.R;
import com.qingpeng.mz.adapter.MainFragmentAdapter;
import com.qingpeng.mz.api.Host;
import com.qingpeng.mz.api.RedBag;
import com.qingpeng.mz.base.BaseActivity;
import com.qingpeng.mz.bean.WebUrlBean;
import com.qingpeng.mz.okhttp.APIResponse;
import com.qingpeng.mz.okhttp.MyCall;
import com.qingpeng.mz.okhttp.ResultException;
import com.qingpeng.mz.okhttp.RetrofitCreateHelper;
import com.qingpeng.mz.utils.APP;
import com.qingpeng.mz.utils.PermissionManager;
import com.qingpeng.mz.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;


public class MainActivity extends BaseActivity {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.bbl)
    BottomBarLayout bbl;
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
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
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        viewPager.setAdapter(new MainFragmentAdapter(getSupportFragmentManager()));
        bbl.setViewPager(viewPager);

        APP.isgame = false;
        Call<APIResponse<WebUrlBean>> call = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).weburl();
        call.enqueue(new MyCall<APIResponse<WebUrlBean>>() {
            @Override
            protected void onSuccess(Call<APIResponse<WebUrlBean>> call, Response<APIResponse<WebUrlBean>> response) {
                WebUrlBean bean = response.body().getData();
                Host.HOST_SHIXUN=bean.getGame_url();
                Host.SOCKE_URL=bean.getGame_wsurl();
                Host.SOCKE_DUANKOU=bean.getGame_tcpport();
            }

            @Override
            protected void onError(Call<APIResponse<WebUrlBean>> call, Throwable t) {
                if (t instanceof ResultException) {
                    ToastUtils.showToast(((ResultException) t).getInfo());
                } else {
                    ToastUtils.showToast("网络请求失败,请稍后重试");
                }
            }
        });
    }


    @OnClick(R.id.tab_main_center)
    public void onViewClicked() {
        if (!PermissionManager.checkCameraPermission(this, this, REQUEST_PERMISSION_CODE)) {
            startActivity(CreateLiveActivity.class);
        }else {
            ToastUtils.showToast("获取权限后开播");
        }
    }
}
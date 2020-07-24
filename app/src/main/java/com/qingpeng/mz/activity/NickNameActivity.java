package com.qingpeng.mz.activity;

import android.view.View;
import android.widget.EditText;

import com.qingpeng.mz.R;
import com.qingpeng.mz.api.Host;
import com.qingpeng.mz.api.RedBag;
import com.qingpeng.mz.base.BaseActivity;
import com.qingpeng.mz.okhttp.APIResponse;
import com.qingpeng.mz.okhttp.MyCall;
import com.qingpeng.mz.okhttp.ResultException;
import com.qingpeng.mz.okhttp.RetrofitCreateHelper;
import com.qingpeng.mz.utils.APP;
import com.qingpeng.mz.utils.StringUtils;
import com.qingpeng.mz.utils.ToastUtils;
import com.qingpeng.mz.views.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class NickNameActivity extends BaseActivity {


    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.ed_name)
    EditText edName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_nick_name;
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
        titleBar.getLlLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.btn_ok)
    public void onViewClicked() {
        if (!StringUtils.isEmpty(edName.getText().toString())) {
            APP.isgame = false;
            call = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).change_nickname(edName.getText().toString());
            call.enqueue(new MyCall<APIResponse>() {
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
}

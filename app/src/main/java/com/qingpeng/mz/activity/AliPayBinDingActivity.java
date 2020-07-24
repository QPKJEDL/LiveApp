package com.qingpeng.mz.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qingpeng.mz.R;
import com.qingpeng.mz.api.Host;
import com.qingpeng.mz.api.RedBag;
import com.qingpeng.mz.base.BaseActivity;
import com.qingpeng.mz.okhttp.APIResponse;
import com.qingpeng.mz.okhttp.MyCall;
import com.qingpeng.mz.okhttp.ResultException;
import com.qingpeng.mz.okhttp.RetrofitCreateHelper;
import com.qingpeng.mz.utils.APP;
import com.qingpeng.mz.utils.ToastUtils;
import com.qingpeng.mz.views.TitleBar;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class AliPayBinDingActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.text_1)
    TextView text1;
    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.text_2)
    TextView text2;
    @BindView(R.id.ed_phone)
    EditText edPhone;
    private String type;
    private HashMap<String, String> map;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ali_pay_bin_ding;
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
        titleBar.getLlLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       type = getIntent().getStringExtra("type");

       if (type.equals("1")){
           //支付宝
           titleBar.setTitle("支付宝绑定");
           text1.setText("支付宝昵称");
           text2.setText("支付宝账户");
           edName.setHint("请输入支付宝昵称");
           edPhone.setHint("请输入支付宝账户");
       }else if (type.equals("2")){
           //微信
           titleBar.setTitle("微信绑定");
           text1.setText("微信昵称");
           text2.setText("微信账户");
           edName.setHint("请输入微信昵称");
           edPhone.setHint("请输入微信账户");
       }else if (type.equals("3")){
           //修改密码
           titleBar.setTitle("修改登录密码");
           text1.setText("旧密码");
           text2.setText("新密码");
           edName.setHint("请输入旧密码");
           edPhone.setHint("请输入新密码");
       }else if (type.equals("4")){
           //修改提现密码
           titleBar.setTitle("修改提现密码");
           text1.setText("旧密码");
           text2.setText("新密码");
           edName.setHint("请输入旧密码");
           edPhone.setHint("请输入新密码");
       }
       map=new HashMap<>();
    }

    @OnClick(R.id.btn_ok)
    public void onViewClicked() {
        if (type.equals("1")){
            //支付宝
            map.put("ali",edPhone.getText().toString());
            map.put("ali_name",edName.getText().toString());
            showData("ali_bind");
        }else if (type.equals("2")){
            //微信
            map.put("wx",edPhone.getText().toString());
            map.put("wx_name",edName.getText().toString());
            showData("wx_bind");
        }else if (type.equals("3")){
            //修改密码
            map.put("new_pwd",edPhone.getText().toString());
            map.put("old_pwd",edName.getText().toString());
            showData("login_pwd");
        }else if (type.equals("4")){
            //修改提现密码
            map.put("new_pwd",edPhone.getText().toString());
            map.put("old_pwd",edName.getText().toString());
            showData("draw_pwd");
        }
    }

    private void showData(String s) {
        APP.isgame=false;
        call = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).xiugaibangding(s, map);
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
}

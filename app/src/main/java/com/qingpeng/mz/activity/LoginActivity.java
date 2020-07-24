package com.qingpeng.mz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingpeng.mz.R;
import com.qingpeng.mz.api.Host;
import com.qingpeng.mz.api.RedBag;
import com.qingpeng.mz.bean.LoginBean;
import com.qingpeng.mz.okhttp.APIResponse;
import com.qingpeng.mz.okhttp.MyCall;
import com.qingpeng.mz.okhttp.ResultException;
import com.qingpeng.mz.okhttp.RetrofitCreateHelper;
import com.qingpeng.mz.utils.APP;
import com.qingpeng.mz.utils.SpUtils;
import com.qingpeng.mz.utils.StringUtils;
import com.qingpeng.mz.utils.TimerCountUtils;
import com.qingpeng.mz.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.ed_account)
    EditText edAccount;
    @BindView(R.id.ed_pwd)
    EditText edPwd;
    @BindView(R.id.login)
    TextView login;
    @BindView(R.id.registe)
    TextView registe;
    @BindView(R.id.ll_pwd)
    LinearLayout llPwd;
    @BindView(R.id.ed_yzm)
    EditText edYzm;
    @BindView(R.id.btn_yzm)
    TextView btnYzm;
    @BindView(R.id.ll_yzm)
    LinearLayout llYzm;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;
    @BindView(R.id.registe_account)
    EditText registeAccount;
    @BindView(R.id.registe_name)
    EditText registeName;
    @BindView(R.id.registe_pwd)
    EditText registePwd;
    @BindView(R.id.registe_pwds)
    EditText registePwds;
    @BindView(R.id.ll_registe)
    LinearLayout llRegiste;
    @BindView(R.id.ima_left)
    ImageView imaLeft;
    @BindView(R.id.ima_right)
    ImageView imaRight;

    private boolean isLogin;
    private boolean islogin = false;
    private CountDownTimer timer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        login();
    }

    public void login() {
        isLogin = SpUtils.getBoolean(LoginActivity.this, "islogin", false);
        if (isLogin) {
            String uid = SpUtils.getString(LoginActivity.this, "uid", "");
            String toke = SpUtils.getString(LoginActivity.this, "toke", "");
            String game_uid = SpUtils.getString(LoginActivity.this, "game_uid", "");
            String game_token = SpUtils.getString(LoginActivity.this, "game_token", "");
            if (!StringUtils.isEmpty(uid) && !StringUtils.isEmpty(toke)) {
                APP.uid = uid;
                APP.Token = toke;
                APP.gameToken = game_token;
                APP.gameuid = game_uid;
                startActivity(new Intent().setClass(LoginActivity.this, MainActivity.class));
                LoginActivity.this.finish();
            }
        }
    }


    @OnClick({R.id.login, R.id.registe, R.id.ll_pwd, R.id.btn_yzm, R.id.islogin, R.id.pay, R.id.registe_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                login.setTextSize(25);
                registe.setTextSize(20);
                imaLeft.setVisibility(View.VISIBLE);
                imaRight.setVisibility(View.INVISIBLE);
                llLogin.setVisibility(View.VISIBLE);
                llRegiste.setVisibility(View.GONE);
                break;
            case R.id.registe:
                imaRight.setVisibility(View.VISIBLE);
                imaLeft.setVisibility(View.INVISIBLE);
                login.setTextSize(20);
                registe.setTextSize(25);
                llLogin.setVisibility(View.GONE);
                llRegiste.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_yzm:
                if (StringUtils.isEmpty(edAccount.getText().toString())) {
                    ToastUtils.showToast("手机号不能为空");
                    return;
                }
                if (!StringUtils.isPhoneNumberValid(edAccount.getText().toString())) {
                    ToastUtils.showToast("手机号格式不正确");
                    return;
                }
                if (!TimerCountUtils.status) {
                    timer = TimerCountUtils.getTimer(edYzm);
                    timer.start();
//                    Call<APIResponse> smsCode = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST).getCode(etUsername.getText().toString());
//                    smsCode.enqueue(new MyCall<APIResponse>() {
//                        @Override
//                        protected void onSuccess(Call<APIResponse> call, Response<APIResponse> response) {
//                            APIResponse body = response.body();
//                            ToastUtils.showToast(body.getInfo());
//                        }
//
//                        @Override
//                        protected void onError(Call<APIResponse> call, Throwable t) {
//                            if (t instanceof ResultException) {
//                                ToastUtils.showToast(((ResultException) t).getInfo());
//                            } else {
//                                ToastUtils.showToast("网络请求失败，请稍后再试");
//                            }
//                        }
//                    });
                }
                break;
            case R.id.islogin:
                if (islogin) {
                    islogin = false;
                    llPwd.setVisibility(View.GONE);
                    llYzm.setVisibility(View.VISIBLE);
                } else {
                    islogin = true;
                    llYzm.setVisibility(View.GONE);
                    llPwd.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.pay:
                if (!islogin) {
                    APP.isgame = false;
                    if (!StringUtils.isEmpty(edAccount.getText().toString())
                            && !StringUtils.isEmpty(edPwd.getText().toString())) {
                        APP.isgame = false;
                        Call<APIResponse<LoginBean>> login = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).login(edAccount.getText().toString(), edPwd.getText().toString());
                        login.enqueue(new MyCall<APIResponse<LoginBean>>() {
                            @Override
                            protected void onSuccess(Call<APIResponse<LoginBean>> call, Response<APIResponse<LoginBean>> response) {
                                LoginBean bean = response.body().getData();
                                ToastUtils.showToast(response.body().getInfo());
                                APP.Token = bean.getToken();
                                APP.uid = bean.getUid();
                                APP.gameuid = bean.getGame_uid();
                                APP.gameToken = bean.getGame_token();
                                SpUtils.putBoolean(LoginActivity.this, "islogin", true);
                                SpUtils.putString(LoginActivity.this, "uid", bean.getUid());
                                SpUtils.putString(LoginActivity.this, "toke", bean.getToken());
                                SpUtils.putString(LoginActivity.this, "game_uid", bean.getGame_uid());
                                SpUtils.putString(LoginActivity.this, "game_token", bean.getGame_token());
                                startActivity(new Intent().setClass(LoginActivity.this, MainActivity.class));
                            }

                            @Override
                            protected void onError(Call<APIResponse<LoginBean>> call, Throwable t) {
                                if (t instanceof ResultException) {
                                    ToastUtils.showToast(((ResultException) t).getInfo());
                                } else {
                                    ToastUtils.showToast("网络请求失败,请稍后重试");
                                }
                            }
                        });
                    } else {
                        ToastUtils.showToast("账号密码不可为空");
                    }
                } else {

                }
                break;
            case R.id.registe_pay:
                APP.isgame = false;
                if (!StringUtils.isEmpty(registeAccount.getText().toString())
                        && !StringUtils.isEmpty(registeName.getText().toString())
                        && !StringUtils.isEmpty(registePwd.getText().toString())
                        && !StringUtils.isEmpty(registePwds.getText().toString())) {
                    if (registePwd.getText().toString().equals(registePwd.getText().toString())) {
                        APP.isgame = false;
                        Call<APIResponse> call = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).register(registeAccount.getText().toString(), registeName.getText().toString(), registePwd.getText().toString());
                        call.enqueue(new MyCall<APIResponse>() {
                            @Override
                            protected void onSuccess(Call<APIResponse> call, Response<APIResponse> response) {
                                ToastUtils.showToast(response.body().getInfo());
                                APP.isgame = false;
                                Call<APIResponse<LoginBean>> login = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).login(registeAccount.getText().toString(), registePwd.getText().toString());
                                login.enqueue(new MyCall<APIResponse<LoginBean>>() {
                                    @Override
                                    protected void onSuccess(Call<APIResponse<LoginBean>> call, Response<APIResponse<LoginBean>> response) {
                                        LoginBean bean = response.body().getData();
                                        ToastUtils.showToast(response.body().getInfo());
                                        APP.Token = bean.getToken();
                                        APP.uid = bean.getUid();
                                        APP.gameuid = bean.getGame_uid();
                                        APP.gameToken = bean.getGame_token();
                                        SpUtils.putBoolean(LoginActivity.this, "islogin", true);
                                        SpUtils.putString(LoginActivity.this, "uid", bean.getUid());
                                        SpUtils.putString(LoginActivity.this, "toke", bean.getToken());
                                        SpUtils.putString(LoginActivity.this, "game_uid", bean.getGame_uid());
                                        SpUtils.putString(LoginActivity.this, "game_token", bean.getGame_token());
                                        startActivity(new Intent().setClass(LoginActivity.this, MainActivity.class));
                                    }

                                    @Override
                                    protected void onError(Call<APIResponse<LoginBean>> call, Throwable t) {
                                        if (t instanceof ResultException) {
                                            ToastUtils.showToast(((ResultException) t).getInfo());
                                        } else {
                                            ToastUtils.showToast("网络请求失败,请稍后重试");
                                        }
                                    }
                                });
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
                    } else {
                        ToastUtils.showToast("两次密码不相同");
                    }
                } else {
                    ToastUtils.showToast("注册资料不能为空");
                }
                break;
        }
    }
}

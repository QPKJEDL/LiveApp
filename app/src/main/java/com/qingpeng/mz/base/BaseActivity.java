package com.qingpeng.mz.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.android.tu.loadingdialog.LoadingDailog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingpeng.mz.R;
import com.qingpeng.mz.activity.LoginActivity;
import com.qingpeng.mz.api.Host;
import com.qingpeng.mz.api.RedBag;
import com.qingpeng.mz.bean.UserInfoBean;
import com.qingpeng.mz.okhttp.APIResponse;
import com.qingpeng.mz.okhttp.MyCall;
import com.qingpeng.mz.okhttp.ResultException;
import com.qingpeng.mz.okhttp.RetrofitCreateHelper;
import com.qingpeng.mz.utils.APP;
import com.qingpeng.mz.utils.AppManager;
import com.qingpeng.mz.utils.SpUtils;
import com.qingpeng.mz.utils.StatusBarUtil;
import com.qingpeng.mz.utils.ToastUtils;
import com.qingpeng.mz.utils.WaitPorgressDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Response;


public abstract class BaseActivity<T> extends AppCompatActivity {
    protected SwipeRefreshLayout mRefreshView;
    protected RecyclerView mListView;
    protected String TAG;
    protected Unbinder mKnife;
    protected Call<APIResponse> call;
    protected WaitPorgressDialog mWaitPorgressDialog;
    protected BaseQuickAdapter mAdapter;
    protected ViewGroup layoutRootView;
    protected Context mContext;
//    protected LoadingDailog mDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mContext = BaseActivity.this;
        mKnife = ButterKnife.bind(this);
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(this).setMessage("加载中...")
                .setCancelable(false)
                .setCancelOutside(false);
//        mDialog = loadBuilder.create();
        TAG = getClass().getSimpleName();
        init(savedInstanceState);
        initView(savedInstanceState);
        layoutRootView = (ViewGroup) findViewById(getRootViewId());
        if (getListViewId() > 0) {
            mListView = (RecyclerView) findViewById(getListViewId());
            mListView.setLayoutManager(new LinearLayoutManager(this));
            initAdapter();
        }

        if (getRefreshId() > 0) {
            mRefreshView = (SwipeRefreshLayout) findViewById(getRefreshId());
        }
        if (mRefreshView != null) {
            mRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (mListView != null) {
                        mListView.setEnabled(false);
                    }
                    mRefreshView.setRefreshing(true);
                    onRefreshData();
                }
            });
        }
        onRefreshData();
        StatusBarUtil.transparencyBar(this);
        AppManager.getAppManager().addActivity(this);
    }


    protected abstract int getLayoutId();

    protected abstract int getRootViewId();

    protected abstract int getRefreshId();

    protected abstract int getListViewId();

    protected void init(Bundle savedInstanceState) {

    }

    protected void initView(Bundle savedInstanceState) {
        mWaitPorgressDialog = new WaitPorgressDialog(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        APP.isgame = false;
        Call<APIResponse<UserInfoBean>> user = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).Userinfo();
        user.enqueue(new MyCall<APIResponse<UserInfoBean>>() {
            @Override
            protected void onSuccess(Call<APIResponse<UserInfoBean>> call, Response<APIResponse<UserInfoBean>> response) {
                UserInfoBean data = response.body().getData();
                APP.userInfoBean = data;
            }

            @Override
            protected void onError(Call<APIResponse<UserInfoBean>> call, Throwable t) {
                if (t instanceof ResultException) {
                    if (((ResultException) t).getStatus() == 2) {
                        SpUtils.putBoolean(APP.getContext(), "islogin", false);
                        AppManager.getAppManager().finishAllActivity();
                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                    }
                    ToastUtils.showToast(((ResultException) t).getInfo());
                } else {
                    ToastUtils.showToast("网络请求失败,请稍后重试");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mKnife.unbind();
        if (call != null) {
            call.cancel();
        }
        AppManager.getAppManager().finishActivity(this);
    }

    protected void onRefreshData() {

    }

    protected void finishRefreshing() {
        if (mRefreshView != null) {
            mRefreshView.setRefreshing(false);
        }
    }

    protected void onLoadMore() {
        if (mAdapter != null) {
            mAdapter.setEnableLoadMore(false);
        }
        if (mRefreshView != null) {
            mRefreshView.setEnabled(false);
        }
    }


    protected void loadMoreSuccess() {
        if (mAdapter != null) {
            setMoreData();
            mAdapter.loadMoreComplete();
            mAdapter.setEnableLoadMore(true);
        }
        if (mRefreshView != null) {
            mRefreshView.setRefreshing(false);
        }
    }

    protected void setMoreData() {

    }

    protected void loadMoreFail() {
        if (mAdapter != null) {
            ToastUtils.showToast("数据请求失败");
            mAdapter.loadMoreFail();
            mAdapter.setEnableLoadMore(true);
        }
        if (mRefreshView != null) {
            mRefreshView.setRefreshing(false);
        }
    }

    protected void loadMoreEnd() {
        if (mAdapter != null) {
            ToastUtils.showToast("数据全部加载完毕");
            mAdapter.loadMoreEnd();
            mAdapter.setEnableLoadMore(false);
        }
        if (mRefreshView != null) {
            mRefreshView.setRefreshing(false);
        }
    }

    public boolean isNeedLoadMore() {
        return true;
    }

    protected void initAdapter() {
        if (mAdapter != null) {
            mAdapter.setEmptyView(getEmptyView());
            mListView.setAdapter(mAdapter);
            if (isNeedLoadMore()) {
                mAdapter.setEnableLoadMore(true);
                mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        onLoadMore();
                    }
                }, mListView);
            } else {
                mAdapter.setEnableLoadMore(false);
            }
        }
    }

    protected View getEmptyView() {
        View emptyView = View.inflate(this, R.layout.view_empty, null);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRefreshData();
            }
        });
        return emptyView;
    }

    /**
     * 显示提示框
     *
     * @param msg 提示框内容字符串
     */
    protected void showToastProgressDialog(String msg) {
        if (mWaitPorgressDialog.isShowing()) {
            mWaitPorgressDialog.dismiss();
        }
        mWaitPorgressDialog.setMessage(msg);
        mWaitPorgressDialog.show();
    }

    /**
     * 隐藏提示框
     */
    protected void hideProgressDialog() {
        if (mWaitPorgressDialog != null) {
            mWaitPorgressDialog.dismiss();
        }
    }


    /**
     * 隐藏键盘
     *
     * @return 隐藏键盘结果
     * <p>
     * true:隐藏成功
     * <p>
     * false:隐藏失败
     */
    protected boolean hiddenKeyboard() {
        //点击空白位置 隐藏软键盘
        InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService
                (INPUT_METHOD_SERVICE);
        return mInputMethodManager.hideSoftInputFromWindow(this
                .getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * [页面跳转]
     *
     * @param clz 要跳转的Activity
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(this, clz));
    }

    /**
     * [页面跳转]
     *
     * @param clz    要跳转的Activity
     * @param intent intent
     */
    public void startActivity(Class<?> clz, Intent intent) {
        intent.setClass(this, clz);
        startActivity(intent);
    }

    /**
     * [携带数据的页面跳转]
     *
     * @param clz    要跳转的Activity
     * @param bundle bundel数据
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * [含有Bundle通过Class打开编辑界面]
     *
     * @param clz         要跳转的Activity
     * @param bundle      bundel数据
     * @param requestCode requestCode
     */
    public void startActivityForResult(Class<?> clz, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }
}

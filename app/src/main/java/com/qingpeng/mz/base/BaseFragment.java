package com.qingpeng.mz.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

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


public abstract class BaseFragment<T> extends Fragment {
    protected String TAG;
    protected Unbinder mKnife;
    protected WaitPorgressDialog mWaitPorgressDialog;
    protected RecyclerView mListView;
    protected SwipeRefreshLayout mRefreshView;
    protected Context mContext;
    protected Activity mActivity;
    protected BaseQuickAdapter mAdapter;
    public Call<T> call;
    protected LinearLayoutManager ms;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mContext = activity;
    }

    protected <T extends View> T findView(int resId) {
        return (T) (getView().findViewById(resId));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        UIUtils.setCustomDesity(mActivity, (APP) mActivity.getApplication());
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TAG = getClass().getSimpleName();

        if (getListViewId() > 0) {
            mListView = (RecyclerView) view.findViewById(getListViewId());
            ms = new LinearLayoutManager(mActivity);
            mListView.setLayoutManager(ms);
            initAdapter();
        }
        if (getRefreshId() > 0) {
            mRefreshView = (SwipeRefreshLayout) view.findViewById(getRefreshId());
        }
        mKnife = ButterKnife.bind(this, view);
        init(view, savedInstanceState);
        initView(view, savedInstanceState);
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
        StatusBarUtil.transparencyBar(mActivity);
        StatusBarUtil.StatusBarLightMode(mActivity);
    }

    /**
     * [页面跳转]
     *
     * @param clz 要跳转的Activity
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(mActivity, clz));
    }

    /**
     * [页面跳转]
     *
     * @param clz    要跳转的Activity
     * @param intent intent
     */
    public void startActivity(Class<?> clz, Intent intent) {
        intent.setClass(mContext, clz);
        startActivity(intent);
    }

    protected abstract int getLayoutId();

    protected abstract int getRefreshId();

    protected abstract int getListViewId();

    protected void init(View view, Bundle savedInstanceState) {

    }

    protected void initView(View view, Bundle savedInstanceState) {
        mWaitPorgressDialog = new WaitPorgressDialog(mActivity);
    }

    protected void onRefreshData() {

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
        View emptyView = View.inflate(mActivity, R.layout.view_empty, null);
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
        InputMethodManager mInputMethodManager = (InputMethodManager) mActivity.getSystemService
                (mActivity.INPUT_METHOD_SERVICE);
        return mInputMethodManager.hideSoftInputFromWindow(mActivity
                .getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call != null)
            call.cancel();
        mKnife.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
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
}

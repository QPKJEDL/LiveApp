package com.qingpeng.mz.fragment;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingpeng.mz.R;
import com.qingpeng.mz.adapter.FollowListAdapter;
import com.qingpeng.mz.api.Host;
import com.qingpeng.mz.api.RedBag;
import com.qingpeng.mz.base.BaseFragment;
import com.qingpeng.mz.bean.FollowListBean;
import com.qingpeng.mz.okhttp.APIResponse;
import com.qingpeng.mz.okhttp.MyCall;
import com.qingpeng.mz.okhttp.ResultException;
import com.qingpeng.mz.okhttp.RetrofitCreateHelper;
import com.qingpeng.mz.utils.APP;
import com.qingpeng.mz.utils.ToastUtils;
import com.qingpeng.mz.video.activity.LivePushActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class TrendsFragment extends BaseFragment {
    private Call<APIResponse<List<FollowListBean>>> follow;
    private List<FollowListBean> data;
    public static final String IS_AUDIENCE = "is_audience";
    private static final String URL_KEY = "url_key";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_trends_fragment;
    }

    @Override
    protected int getRefreshId() {
        return 0;
    }

    @Override
    protected int getListViewId() {
        return R.id.recyclerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        APP.isgame = false;
        follow = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).follow_list();
        follow.enqueue(new MyCall<APIResponse<List<FollowListBean>>>() {
            @Override
            protected void onSuccess(Call<APIResponse<List<FollowListBean>>> call, Response<APIResponse<List<FollowListBean>>> response) {
                data = (List<FollowListBean>) response.body().getData();
                initAdapter();
            }

            @Override
            protected void onError(Call<APIResponse<List<FollowListBean>>> call, Throwable t) {
                if (t instanceof ResultException) {
                    ToastUtils.showToast(((ResultException) t).getInfo());
                } else {
                    ToastUtils.showToast("网络请求失败,请稍后重试");
                }
            }
        });
    }

    @Override
    protected void initAdapter() {
        mAdapter = new FollowListAdapter(R.layout.item_follow_list, mContext);
        mAdapter.setNewData(data);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_kanguo:
                        Intent intent = new Intent(mContext, LivePushActivity.class).putExtra(IS_AUDIENCE, true)
                                .putExtra(URL_KEY, APP.ListRoom.get(position).getList().getPush())
                                .putExtra("roomid", APP.ListRoom.get(position).getList().getRoomId() + "");
                        startActivity(intent);
                        break;
                }
            }
        });
        super.initAdapter();
    }
}

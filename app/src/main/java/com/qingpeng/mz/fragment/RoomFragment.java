package com.qingpeng.mz.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bravin.btoast.BToast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingpeng.mz.R;
import com.qingpeng.mz.adapter.RoomListAdapter;
import com.qingpeng.mz.api.Host;
import com.qingpeng.mz.api.RedBag;
import com.qingpeng.mz.base.BaseFragment;
import com.qingpeng.mz.bean.RoomInfoListBean;
import com.qingpeng.mz.bean.ZhuBoRoomInfoBean;
import com.qingpeng.mz.okhttp.APIResponse;
import com.qingpeng.mz.okhttp.MyCall;
import com.qingpeng.mz.okhttp.ResultException;
import com.qingpeng.mz.okhttp.RetrofitCreateHelper;
import com.qingpeng.mz.utils.APP;
import com.qingpeng.mz.utils.ToastUtils;
import com.qingpeng.mz.video.activity.LivePushActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class RoomFragment extends BaseFragment {
    public static final String IS_AUDIENCE = "is_audience";
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private static final String URL_KEY = "url_key";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private Call<APIResponse<List<RoomInfoListBean>>> room;
    private List<RoomInfoListBean> data = new ArrayList<>();
    private Intent intent;
    private Call<APIResponse<ZhuBoRoomInfoBean>> roomurl;
    private String sn = "0";
    private String channe;
    private boolean isCreated;
    private RoomListAdapter adapter;

    public RoomFragment(String s) {
        this.channe = s;
    }

    public RoomFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_room_list;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCreated = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isCreated) {
            return;
        } else {
            //进行刷新操作
            APP.isgame = false;
            data.clear();
            showData(channe, "0");
        }
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        super.init(view, savedInstanceState);
        APP.isgame = false;
        data.clear();
        showData(channe, "0");
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (recyclerView != null) {
                    recyclerView.setEnabled(false);
                }
                refreshLayout.finishRefresh(true);
                onRefreshData();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (data.size() < 20) {
                    BToast.success(mContext)
                            .text("数据全部加载完毕")
                            .show();
                    refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                } else {
                    sn = data.get(data.size() - 1).getStartTime() + "";
                    APP.isgame = false;
                    showData(channe, sn);
                    refreshLayout.finishLoadMore();
                }
            }
        });


    }

    private void showData(String channe, String sn) {
        APP.isgame = false;
        room = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).RoomList(channe, sn);
        room.enqueue(new MyCall<APIResponse<List<RoomInfoListBean>>>() {
            @Override
            protected void onSuccess(Call<APIResponse<List<RoomInfoListBean>>> call, Response<APIResponse<List<RoomInfoListBean>>> response) {
                data = response.body().getData();
                MyinitAdapter();
            }

            @Override
            protected void onError(Call<APIResponse<List<RoomInfoListBean>>> call, Throwable t) {
                if (t instanceof ResultException) {
                    ToastUtils.showToast(((ResultException) t).getInfo());
                } else {
                    ToastUtils.showToast("网络请求失败,请稍后重试");
                }
            }
        });
    }


    @Override
    protected void onRefreshData() {
        super.onRefreshData();
        sn = "0";
        if (adapter != null) {
            adapter.setNewData(null);
        }
        APP.isgame = false;
        showData(channe, sn);
    }

    private void MyinitAdapter() {
        adapter = new RoomListAdapter(R.layout.item_main);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        if (!sn.equals("0")) {
            adapter.addData(data);
        } else {
            adapter.setNewData(data);
        }
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.rl_room:
                        APP.isgame = false;
                        roomurl = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).RoomInfo(data.get(position).getRoomId() + "");
                        roomurl.enqueue(new MyCall<APIResponse<ZhuBoRoomInfoBean>>() {
                            @Override
                            protected void onSuccess(Call<APIResponse<ZhuBoRoomInfoBean>> call, Response<APIResponse<ZhuBoRoomInfoBean>> response) {
                                ZhuBoRoomInfoBean bean = response.body().getData();
                                APP.liveid = data.get(position).getLiveUid();
                                APP.ListRoom.add(bean);
                                intent = new Intent(getActivity(), LivePushActivity.class).putExtra(IS_AUDIENCE, true)
                                        .putExtra(URL_KEY, bean.getList().getPullRtmp())
                                        .putExtra("roomid", data.get(position).getRoomId() + "");
                                startActivity(intent);
                            }

                            @Override
                            protected void onError(Call<APIResponse<ZhuBoRoomInfoBean>> call, Throwable t) {
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
        });
    }

}

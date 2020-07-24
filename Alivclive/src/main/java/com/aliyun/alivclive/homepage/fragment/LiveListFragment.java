package com.aliyun.alivclive.homepage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aliyun.alivclive.R;
import com.aliyun.alivclive.homepage.adapter.LiveListAdapter;

import com.aliyun.alivclive.room.roominfo.AlivcLiveRoomInfo;
import com.aliyun.alivclive.utils.http.AlivcHttpManager;
import com.aliyun.alivclive.setting.manager.AlivcLiveUserManager;
import com.aliyun.alivclive.utils.http.AlivcStsManager;
import com.aliyun.alivclive.room.LiveRoomPlayActivity;
import com.aliyun.alivclive.utils.constants.AlivcConstants;
import com.aliyun.alivclive.utils.HandleUtils;
import com.aliyun.alivclive.utils.http.HttpEngine;
import com.aliyun.alivclive.utils.http.HttpResponse;
import com.aliyun.alivclive.utils.LogUtils;
import com.aliyun.alivclive.utils.UIUtils;
import com.aliyun.alivclive.view.KickOutDialog;
import com.aliyun.pusher.core.listener.OnItemClickListener;

/**
 * Created by Akira on 2018/5/27.
 */

public class LiveListFragment extends BaseFragment {

    public static final int REQCODE = 1000;
    public static final int KICKOUT = 2000;
    private final String TAG = "LiveListFragment";

    private SwipeRefreshLayout mRefreshView;
    private RecyclerView mRecyclerView;
    private LiveListAdapter mAdapter;

    private boolean hasMore = false;

    private final int PAGE_SIZE = 20;
    private int mPageIndex = 1;
    private boolean isLoading = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alivc_home_list, container, false);
        initViews(view);
        initData();
        return view;
    }

    private void initData() {
        mRefreshView.setRefreshing(false);
        getRoomInfos(mPageIndex, true);

    }

    private void getRoomInfos(int pageIndex, final boolean isLoadMore) {
        AlivcHttpManager.getInstance().getRoomList(pageIndex, PAGE_SIZE, new HttpEngine.OnResponseCallback<HttpResponse.AlivcRoomList>() {
            @Override
            public void onResponse(boolean result, @Nullable String retmsg, @Nullable HttpResponse.AlivcRoomList data) {
                if (mRefreshView.isRefreshing()) {
                    mRefreshView.setRefreshing(false);
                }
                isLoading = false;

                if (isDetached()) {
                    return;
                }

                if (result) {
                    if (data != null && data.data != null) {
                        if (isLoadMore) {
                            mAdapter.addData(data.data.rooms);
                        } else {
                            mAdapter.getData().clear();
                            mAdapter.addData(data.data.rooms);
                        }
                        hasMore = data.data.more;
                        mPageIndex++;
                        LogUtils.d(TAG, "isLoadMore = " + isLoadMore
                                + " ||||    get room list, size = " + data.data.rooms.size()
                                + "      adapter size = " + mAdapter.getData().size()
                                + "       hasMore = " + hasMore);
                    }
                } else {

                }

            }
        });
    }

    private void initViews(View view) {
        mRefreshView = view.findViewById(R.id.swipe_refresh_view);
        mRecyclerView = view.findViewById(R.id.recycler_view);
//        int right = UIUtils.dip2px(getActivity(), 10);
//        int left = UIUtils.dip2px(getActivity(), 6);
//        int bottom = UIUtils.dip2px(getActivity(), 5);
        int gap = UIUtils.dip2px(getActivity(), 5.5);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.addItemDecoration(new HomeListItemDecoration(gap, gap, gap, gap));
        mAdapter = new LiveListAdapter(getActivity().getApplicationContext(), mItemClickListener);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshView.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        mRefreshView.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);

        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        mRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageIndex = 0;
                getRoomInfos(mPageIndex, false);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                int visibleItemCount = recyclerView.getChildCount();

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItemPosition > totalItemCount - 4
                        && visibleItemCount > 0) {
                    if (hasMore && !isLoading) {
                        isLoading = true;
                        loadMore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private OnItemClickListener mItemClickListener = new OnItemClickListener() {
        @Override
        public void onClick(int position) {
            if (mAdapter.getData().size() > position) {
                final AlivcLiveRoomInfo roomInfo = mAdapter.getData().get(position);
                LogUtils.d(TAG, "room item click , room id = " + roomInfo.getRoom_id()
                        + "     streamer id = " + roomInfo.getStreamer_id());

                if (AlivcStsManager.getInstance().isValid()) {
                    Intent intent = new Intent();
                    intent.putExtra("roomId", roomInfo.getRoom_id());
                    intent.putExtra("streamerId", roomInfo.getStreamer_id());
                    intent.setClass(getActivity(), LiveRoomPlayActivity.class);
                    startActivityForResult(intent, REQCODE);
                } else {
                    //todo 这里不应该判定 临时保护
                    AlivcStsManager.getInstance().refreshStsToken(AlivcLiveUserManager.getInstance().getUserInfo(getActivity()).getUserId());
                    HandleUtils.getMainThreadHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.putExtra("roomId", roomInfo.getRoom_id());
                            intent.putExtra("streamerId", roomInfo.getStreamer_id());
                            intent.setClass(getActivity(), LiveRoomPlayActivity.class);
                            startActivityForResult(intent, REQCODE);
                        }
                    }, 3000);
                }
            }

        }
    };

    KickOutDialog kickOutDialog;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQCODE && resultCode == KICKOUT) {
            if (kickOutDialog == null) {
                kickOutDialog = new KickOutDialog(getActivity());
                kickOutDialog.setOnConfirmClickListener(new KickOutDialog.onConfirmClickListener() {
                    @Override
                    public void onConfirm() {
                        kickOutDialog.dismiss();
                    }
                });
            }
            if (!kickOutDialog.isShowing()) {
                kickOutDialog.show();
            }

        }
    }

    private void loadMore() {
        getRoomInfos(mPageIndex, true);
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    protected void lazyLoadOnlyOne() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AlivcHttpManager.getInstance().cancel(AlivcConstants.URL_GET_ROOM_LIST);
    }
}

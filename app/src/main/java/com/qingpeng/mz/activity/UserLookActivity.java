package com.qingpeng.mz.activity;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingpeng.mz.R;
import com.qingpeng.mz.adapter.UserLookAdapter;
import com.qingpeng.mz.base.BaseActivity;
import com.qingpeng.mz.utils.APP;
import com.qingpeng.mz.video.activity.LivePushActivity;
import com.qingpeng.mz.views.TitleBar;

import butterknife.BindView;

public class UserLookActivity extends BaseActivity {


    @BindView(R.id.title_bar)
    TitleBar titleBar;
    public static final String IS_AUDIENCE = "is_audience";
    private static final String URL_KEY = "url_key";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_look;
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
        return R.id.recyclerView;
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
        initAdapter();
    }

    @Override
    protected void initAdapter() {
        super.initAdapter();
        mAdapter = new UserLookAdapter(R.layout.item_follow_list);
        mAdapter.setNewData(APP.ListRoom);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_kanguo:
                        Intent intent = new Intent(UserLookActivity.this, LivePushActivity.class).putExtra(IS_AUDIENCE, true)
                                .putExtra(URL_KEY, APP.ListRoom.get(position).getList().getPush())
                                .putExtra("roomid", APP.ListRoom.get(position).getList().getRoomId() + "");
                        startActivity(intent);
                        break;
                }
            }
        });
    }
}

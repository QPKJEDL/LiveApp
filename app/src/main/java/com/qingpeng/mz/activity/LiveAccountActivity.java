package com.qingpeng.mz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.qingpeng.mz.R;
import com.qingpeng.mz.base.BaseActivity;
import com.qingpeng.mz.views.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

public class LiveAccountActivity extends BaseActivity {


    @BindView(R.id.title_bar)
    TitleBar titleBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_account;
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
    }

    @OnClick({R.id.text_tixian, R.id.text_chongzhi, R.id.mine_tixian, R.id.mine_chongzhi, R.id.mine_shou, R.id.mine_song})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_tixian:
                break;
            case R.id.text_chongzhi:
                startActivity(WithdrawActivity.class);
                break;
            case R.id.mine_tixian:
                break;
            case R.id.mine_chongzhi:
                break;
            case R.id.mine_shou:
                startActivity(GiftActivity.class, new Intent().putExtra("type", "1"));
                break;
            case R.id.mine_song:
                startActivity(GiftActivity.class, new Intent().putExtra("type", "2"));
                break;
        }
    }
}

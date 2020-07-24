package com.qingpeng.mz.activity;

import android.content.Intent;
import android.view.View;

import com.qingpeng.mz.R;
import com.qingpeng.mz.base.BaseActivity;

import butterknife.OnClick;

public class UserBinDingActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_bin_ding;
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


    @OnClick({R.id.mine_bank, R.id.mine_alipay, R.id.mine_wechat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_bank:
                startActivity(BankBinDingActivity.class);
                break;
            case R.id.mine_alipay:
                startActivity(AliPayBinDingActivity.class,new Intent().putExtra("type","1"));
                break;
            case R.id.mine_wechat:
                startActivity(AliPayBinDingActivity.class,new Intent().putExtra("type","2"));
                break;
        }
    }
}

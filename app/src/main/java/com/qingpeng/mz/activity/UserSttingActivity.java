package com.qingpeng.mz.activity;

import android.content.Intent;
import android.view.View;

import com.qingpeng.mz.R;
import com.qingpeng.mz.base.BaseActivity;
import com.qingpeng.mz.utils.APP;
import com.qingpeng.mz.utils.AppManager;
import com.qingpeng.mz.utils.SpUtils;

import butterknife.OnClick;

public class UserSttingActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_stting;
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


    @OnClick({R.id.mine_zhanghu, R.id.mine_denglu, R.id.mine_tixian, R.id.mine_xiaoxi, R.id.text_tuichu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_zhanghu:
                startActivity(UserBinDingActivity.class);
                break;
            case R.id.mine_denglu:
                startActivity(AliPayBinDingActivity.class, new Intent().putExtra("type", "3"));
                break;
            case R.id.mine_tixian:
                startActivity(AliPayBinDingActivity.class, new Intent().putExtra("type", "4"));
                break;
            case R.id.mine_xiaoxi:
                break;
            case R.id.text_tuichu:
                APP.Token = "";
                APP.uid = "";
                APP.gameuid = "";
                APP.gameToken = "";
//                APP.userInfo = null;
                SpUtils.putBoolean(mContext, "islogin", false);
                SpUtils.putString(mContext, "uid", "");
                SpUtils.putString(mContext, "toke", "");
                SpUtils.putString(mContext, "game_uid", "");
                SpUtils.putString(mContext, "game_token", "");
                SpUtils.putString(mContext, "userInfo", "");
                AppManager.getAppManager().finishAllActivity();
                APP.ListRoom.clear();
                startActivity(LoginActivity.class);
                this.finish();
                break;
        }
    }
}

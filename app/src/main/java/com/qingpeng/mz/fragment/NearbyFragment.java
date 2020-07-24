package com.qingpeng.mz.fragment;

import com.qingpeng.mz.R;
import com.qingpeng.mz.base.BaseFragment;

public class NearbyFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_nearby_fragment;
    }

    @Override
    protected int getRefreshId() {
        return 0;
    }

    @Override
    protected int getListViewId() {
        return 0;
    }
}

package com.qingpeng.mz.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qingpeng.mz.fragment.MainFragment;
import com.qingpeng.mz.fragment.MineFragment;
import com.qingpeng.mz.fragment.NearbyFragment;
import com.qingpeng.mz.fragment.TrendsFragment;

import java.util.ArrayList;
import java.util.List;

public class MainFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();

    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(new MainFragment());
        fragments.add(new TrendsFragment());
        fragments.add(new NearbyFragment());
        fragments.add(new MineFragment());
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}

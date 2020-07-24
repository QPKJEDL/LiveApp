package com.aliyun.alivclive.view.indicator;

public interface IPagerNavigator {

    // ViewPager的3个回调
    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    void onPageSelected(int position);

    void onPageScrollStateChanged(int state);

    /**
     * 当IPagerNavigator被添加到Indicator时调用
     */
    void onAttachToMagicIndicator();

    /**
     * 当IPagerNavigator从Indicator上移除时调用
     */
    void onDetachFromMagicIndicator();

    /**
     * ViewPager内容改变时需要先调用此方法
     */
    void notifyDataSetChanged();
}

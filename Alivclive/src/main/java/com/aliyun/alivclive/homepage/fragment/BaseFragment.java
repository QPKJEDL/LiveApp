package com.aliyun.alivclive.homepage.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

public abstract class BaseFragment extends Fragment {

    protected boolean isVisible;
    protected final String TAG = getClass().getSimpleName();
    private boolean isPrepared; // 标志位，标志控件已经初始化完成。
    protected boolean isFirst;//标志第一次加载

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepared = true;
        isFirst=true;
    }

    /**
     * 在这里实现Fragment数据的缓加载.
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            if (isPrepared) {
                lazyLoad();
                if (isFirst) {
                    isFirst = false;
                    lazyLoadOnlyOne();
                }
            }
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isPrepared && getUserVisibleHint()) {
            lazyLoad();
            if (isFirst) {
                isFirst = false;
                lazyLoadOnlyOne();
            }
        }
    }

    protected abstract void lazyLoad();

    /**
     * 懒加载，有且加载一次
     */
    protected abstract void lazyLoadOnlyOne();

    protected void onInvisible() {
    }

    /**
     * 通过泛型来简化findViewById
     * <E extends View>申明此方法为范型方法
     */
    protected final <E extends View> E getView(View view, int id) {
        try {
            return (E) view.findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }
}

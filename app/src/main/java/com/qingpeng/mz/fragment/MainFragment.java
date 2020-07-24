package com.qingpeng.mz.fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingpeng.mz.R;
import com.qingpeng.mz.api.Host;
import com.qingpeng.mz.api.RedBag;
import com.qingpeng.mz.base.BaseFragment;
import com.qingpeng.mz.bean.HuaTiBean;
import com.qingpeng.mz.okhttp.APIResponse;
import com.qingpeng.mz.okhttp.MyCall;
import com.qingpeng.mz.okhttp.ResultException;
import com.qingpeng.mz.okhttp.RetrofitCreateHelper;
import com.qingpeng.mz.utils.APP;
import com.qingpeng.mz.utils.ToastUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

public class MainFragment extends BaseFragment {

    @BindView(R.id.text_notice)
    TextView textNotice;
    @BindView(R.id.tablayout)
    MagicIndicator magicIndicator;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    //标题
    private String[] mTitles = {"女神", "热门", "音乐", "舞蹈", "交友", "新人"};
    private ArrayList<Fragment> mFagments;
    private InnerPagerAdapter mInnerPagerAdapter;
    private Call<APIResponse<List<HuaTiBean>>> pindao;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_fragment;
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
    public void onResume() {
        super.onResume();
        APP.isgame = false;
        pindao = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).huatipindao("GetHasChannel");
        pindao.enqueue(new MyCall<APIResponse<List<HuaTiBean>>>() {
            @Override
            protected void onSuccess(Call<APIResponse<List<HuaTiBean>>> call, Response<APIResponse<List<HuaTiBean>>> response) {
                List<HuaTiBean> bean = response.body().getData();
                //Fragment集合
                mFagments = new ArrayList<>();
                mTitles = new String[bean.size()];
                for (int i = 0; i < bean.size(); i++) {
                    //添加Fragment
                    mTitles[i] = bean.get(i).getChannel();
                    mFagments.add(new RoomFragment(bean.get(i).getId() + ""));
                }
                mInnerPagerAdapter = new InnerPagerAdapter(getChildFragmentManager(), mFagments);
                viewPager.setAdapter(mInnerPagerAdapter);
                CommonNavigator commonNavigator = new CommonNavigator(mContext);
                commonNavigator.setAdjustMode(true);
                commonNavigator.setAdapter(new CommonNavigatorAdapter() {

                    @Override
                    public int getCount() {
                        return mTitles == null ? 0 : mTitles.length;
                    }

                    @Override
                    public IPagerTitleView getTitleView(Context context, final int index) {
                        ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                        colorTransitionPagerTitleView.setText(mTitles[index]);
                        colorTransitionPagerTitleView.setTextSize(20);
                        colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                        colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                        colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                viewPager.setCurrentItem(index);
                            }
                        });
                        return colorTransitionPagerTitleView;
                    }

                    @Override
                    public IPagerIndicator getIndicator(Context context) {
                        return null;
                    }
                });
                magicIndicator.setNavigator(commonNavigator);
                ViewPagerHelper.bind(magicIndicator, viewPager);
            }

            @Override
            protected void onError(Call<APIResponse<List<HuaTiBean>>> call, Throwable t) {
                if (t instanceof ResultException) {
                    ToastUtils.showToast(((ResultException) t).getInfo());
                } else {
                    ToastUtils.showToast("网络请求失败,请稍后重试");
                }
            }
        });
    }

    class InnerPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments = new ArrayList<>();

        public InnerPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 覆写destroyItem并且空实现,这样每个Fragment中的视图就不会被销毁
             super.destroyItem(container, position, object);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }
}

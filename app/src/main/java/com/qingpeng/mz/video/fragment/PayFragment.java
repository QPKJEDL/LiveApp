package com.qingpeng.mz.video.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingpeng.mz.R;
import com.qingpeng.mz.adapter.GameAdapter;
import com.qingpeng.mz.adapter.GameAdapterBai;
import com.qingpeng.mz.adapter.MoneyPayFragmentAdapter;
import com.qingpeng.mz.api.Host;
import com.qingpeng.mz.api.RedBag;
import com.qingpeng.mz.base.BaseFragment;
import com.qingpeng.mz.bean.LongHuBean;
import com.qingpeng.mz.bean.PayMoneyBean;
import com.qingpeng.mz.bean.RoomInfoBean;
import com.qingpeng.mz.bean.UserBetsBean;
import com.qingpeng.mz.okhttp.APIResponse;
import com.qingpeng.mz.okhttp.MyCall;
import com.qingpeng.mz.okhttp.ResultException;
import com.qingpeng.mz.okhttp.RetrofitCreateHelper;
import com.qingpeng.mz.utils.APP;
import com.qingpeng.mz.utils.ToastUtils;
import com.qingpeng.mz.video.activity.LivePushActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class PayFragment extends BaseFragment {
    @BindView(R.id.game_longhu)
    RecyclerView gameLonghu;
    @BindView(R.id.game_a89)
    RecyclerView gameA89;
    @BindView(R.id.game_baijiale)
    RecyclerView gameBaijiale;
    @BindView(R.id.recycler_sangong)
    RecyclerView recyclerSangong;
    @BindView(R.id.recycler_niuniu)
    RecyclerView recyclerNiuniu;
    @BindView(R.id.btn_on)
    ImageView btnOn;
    @BindView(R.id.btn_off)
    ImageView btnOff;
    @BindView(R.id.text_xianhong)
    TextView textXianhong;
    @BindView(R.id.ll_queding)
    LinearLayout llQueding;

    //台桌id
    private String desk_id;
    private String game_id;
    private Call<APIResponse<List<RoomInfoBean>>> roomtypelistinfo;
    private RoomInfoBean bean;
    private int type;
    private ArrayList<PayMoneyBean> titles;
    private int money;
    int[] a = {10, 50, 100, 200, 500, 1000};
    private HashMap<String, String> longmap;
    private HashMap<String, String> cancel;
    private String pavenum;
    private String bootnum;
    private LivePushActivity activity;
    private Call<APIResponse<UserBetsBean>> userbets;
    private UserBetsBean userbean;
    private List<LongHuBean> data;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_game_room;
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
    public void onResume() {
        super.onResume();
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);

        titles = new ArrayList();
        titles.add(new PayMoneyBean());
        titles.add(new PayMoneyBean());
        titles.add(new PayMoneyBean());
        titles.add(new PayMoneyBean());
        titles.add(new PayMoneyBean());
        titles.add(new PayMoneyBean());
        for (int i = 0; i < titles.size(); i++) {
            titles.get(i).setIsok(false);
            titles.get(i).setMoney(a[i]);
        }
        activity = (LivePushActivity) getActivity();
    }

    public void showData(String desk_id, String game_id) {
        this.desk_id = desk_id;
        this.game_id = game_id;
        APP.isgame = true;
        roomtypelistinfo = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_SHIXUN).RoomTypelistinfo(desk_id);
        roomtypelistinfo.enqueue(new MyCall<APIResponse<List<RoomInfoBean>>>() {
            @Override
            protected void onSuccess(Call<APIResponse<List<RoomInfoBean>>> call, Response<APIResponse<List<RoomInfoBean>>> response) {
                bean = response.body().getData().get(0);
                APP.isgame = true;
                userbets = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_SHIXUN).UserBetsFee(game_id);
                userbets.enqueue(new MyCall<APIResponse<UserBetsBean>>() {
                    @Override
                    protected void onSuccess(Call<APIResponse<UserBetsBean>> call, Response<APIResponse<UserBetsBean>> response) {
                        userbean = response.body().getData();
                        if (bean.getGameName().equals("百家乐")) {
                            gameBaijiale.setVisibility(View.VISIBLE);
                            gameLonghu.setVisibility(View.GONE);
                            recyclerSangong.setVisibility(View.GONE);
                            recyclerNiuniu.setVisibility(View.GONE);
                            gameA89.setVisibility(View.GONE);
                            type = 1;
                            longmap = new HashMap<>();
                            cancel = new HashMap<>();
                            textXianhong.setText(bean.getMinLimit() + "~" + bean.getMaxLimit());
                            initbaijiale();
                        } else if (bean.getGameName().equals("龙虎")) {
                            gameLonghu.setVisibility(View.VISIBLE);
                            gameBaijiale.setVisibility(View.GONE);
                            recyclerSangong.setVisibility(View.GONE);
                            recyclerNiuniu.setVisibility(View.GONE);
                            gameA89.setVisibility(View.GONE);
                            type = 2;
                            longmap = new HashMap<>();
                            cancel = new HashMap<>();
                            textXianhong.setText(bean.getMinLimit() + "~" + bean.getMaxLimit());
                            initlonghu();
                        } else if (bean.getGameName().equals("三公")) {
                            recyclerSangong.setVisibility(View.VISIBLE);
                            gameLonghu.setVisibility(View.GONE);
                            gameBaijiale.setVisibility(View.GONE);
                            recyclerNiuniu.setVisibility(View.GONE);
                            gameA89.setVisibility(View.GONE);
                            type = 3;
                            longmap = new HashMap<>();
                            cancel = new HashMap<>();
                            textXianhong.setText(bean.getMinLimit() + "~" + bean.getMaxLimit());
                            initsangong();
                        } else if (bean.getGameName().equals("牛牛")) {
                            recyclerNiuniu.setVisibility(View.VISIBLE);
                            recyclerSangong.setVisibility(View.GONE);
                            gameLonghu.setVisibility(View.GONE);
                            gameBaijiale.setVisibility(View.GONE);
                            gameA89.setVisibility(View.GONE);
                            type = 4;
                            longmap = new HashMap<>();
                            cancel = new HashMap<>();
                            textXianhong.setText(bean.getMinLimit() + "~" + bean.getMaxLimit());
                            initniuniu();
                        } else if (bean.getGameName().equals("A89")) {
                            recyclerNiuniu.setVisibility(View.GONE);
                            recyclerSangong.setVisibility(View.GONE);
                            gameLonghu.setVisibility(View.GONE);
                            gameBaijiale.setVisibility(View.GONE);
                            gameA89.setVisibility(View.VISIBLE);
                            type = 5;
                            longmap = new HashMap<>();
                            cancel = new HashMap<>();
                            textXianhong.setText(bean.getMinLimit() + "~" + bean.getMaxLimit());
                            inita89();
                        }
                        initAdapter();
                    }

                    @Override
                    protected void onError(Call<APIResponse<UserBetsBean>> call, Throwable t) {
                        if (t instanceof ResultException) {
                            ToastUtils.showToast(((ResultException) t).getInfo());
                        } else {
                            ToastUtils.showToast("网络请求失败,请稍后重试");
                        }
                    }
                });
            }

            @Override
            protected void onError(Call<APIResponse<List<RoomInfoBean>>> call, Throwable t) {
                if (t instanceof ResultException) {
                    ToastUtils.showToast(((ResultException) t).getInfo());
                } else {
                    ToastUtils.showToast("网络请求失败,请稍后重试");
                }
            }
        });


    }


    @Override
    protected void initAdapter() {
        mAdapter = new MoneyPayFragmentAdapter(R.layout.item_money);
        mAdapter.setNewData(titles);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_money:
                        money = titles.get(position).getMoney();
                        for (int i = 0; i < titles.size(); i++) {
                            if (i == position) {
                                titles.get(i).setIsok(true);
                            } else {
                                titles.get(i).setIsok(false);
                            }
                        }
                        mAdapter.setNewData(titles);
                        break;
                }
            }
        });
        super.initAdapter();
    }

    public static PayFragment newInstance() {
        PayFragment payFragment = new PayFragment();
        return payFragment;
    }


    public void isShow() {
        if (activity.showPay()) {
            llQueding.setVisibility(View.VISIBLE);
        } else {
            llQueding.setVisibility(View.INVISIBLE);
        }
    }


    public int getType() {
        return type;
    }


    @OnClick({R.id.btn_on, R.id.btn_off})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_on:
                pavenum = activity.bootnum;
                bootnum = activity.pavenum;
                if (activity.showPay()) {
                    if (type == 1) {
                        //百家乐
                        longHu("bjl_set_bet");
                    } else if (type == 2) {
                        //龙虎
                        longHu("lh_set_bet");
                    } else if (type == 3) {
                        //三公
                        longHu("SgBets");
                    } else if (type == 4) {
                        //牛牛
                        longHu("bets");
                    } else if (type == 5) {
                        //a98
                        longHu("A89Bets");
                    }
                } else {
                    ToastUtils.showToast("停止下注");
                }
                break;
            case R.id.btn_off:
                pavenum = activity.bootnum;
                bootnum = activity.pavenum;
                if (activity.showPay()) {
                    if (type == 1) {
                        //百家乐
                        cancel("bjl_quit_bet");
                    } else if (type == 2) {
                        //龙虎
                        cancel("lh_quit_bet");
                    } else if (type == 3) {
                        //三公
                        cancel("SgcancelBets");
                    } else if (type == 4) {
                        //牛牛
                        cancel("NncancelBets");
                    } else if (type == 5) {
                        //a98
                        cancel("A89cancelBets");
                    }
                } else {
                    ToastUtils.showToast("不能取消下注");
                }
                break;
        }
    }

    private void cancel(String s) {
        cancel.put("pave_num", pavenum + "");
        cancel.put("boot_num", bootnum + "");
        cancel.put("desk_id", bean.getDeskId() + "");
        APP.isgame = true;
        call = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_SHIXUN).cancel(s, cancel);
        call.enqueue(new MyCall<APIResponse>() {
            @Override
            protected void onSuccess(Call<APIResponse> call, Response<APIResponse> response) {
                ToastUtils.showToast(response.body().getInfo());
                moneyClear();
            }

            @Override
            protected void onError(Call<APIResponse> call, Throwable t) {
                if (t instanceof ResultException) {
                    ToastUtils.showToast(((ResultException) t).getInfo());
                } else {
                    ToastUtils.showToast("网络请求失败,请稍后重试");
                }
            }
        });
    }

    private void longHu(String s) {
        longmap.put("pave_num", pavenum + "");
        longmap.put("boot_num", bootnum + "");
        longmap.put("desk_id", bean.getDeskId() + "");
        for (int i = 0; i < data.size(); i++) {
            longmap.put(data.get(i).getId(), data.get(i).getMoney() + "");
        }
        APP.isgame = true;
        call = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_SHIXUN).xiazhu(s, longmap);
        call.enqueue(new MyCall<APIResponse>() {
            @Override
            protected void onSuccess(Call<APIResponse> call, Response<APIResponse> response) {
                ToastUtils.showToast(response.body().getInfo());
                moneyClear();
            }

            @Override
            protected void onError(Call<APIResponse> call, Throwable t) {
                if (t instanceof ResultException) {
                    ToastUtils.showToast(((ResultException) t).getInfo());
                } else {
                    ToastUtils.showToast("网络请求失败,请稍后重试");
                }
            }
        });
    }

    private void moneyClear() {
        for (int i = 0; i < titles.size(); i++) {
            titles.get(i).setIsok(false);
        }
        mAdapter.setNewData(titles);
        money = 0;
    }

    private void initniuniu() {
        data = new ArrayList<>();
        LongHuBean b = new LongHuBean();
        b.setType(type);
        b.setTitle("闲1平倍");
        b.setId("x1_equal");
        b.setOddType((userbean.getEqual() / 100) + "");
        b.setClick(false);
        b.setMoney(0);

        LongHuBean b1 = new LongHuBean();
        b1.setType(type);
        b1.setTitle("闲1翻倍");
        b1.setId("x1_double");
        b1.setOddType((userbean.getDouble() / 100) + "");
        b1.setClick(false);
        b1.setMoney(0);

        LongHuBean b2 = new LongHuBean();
        b2.setType(type);
        b2.setTitle("闲1超倍");
        b2.setId("x1_SuperDouble");
        b2.setOddType((userbean.getSuperDouble() / 100) + "");
        b2.setClick(false);
        b2.setMoney(0);


        LongHuBean b3 = new LongHuBean();
        b3.setType(type);
        b3.setTitle("闲2平倍");
        b3.setId("x2_equal");
        b3.setOddType((userbean.getEqual() / 100) + "");
        b3.setClick(false);
        b3.setMoney(0);

        LongHuBean b4 = new LongHuBean();
        b4.setType(type);
        b4.setTitle("闲2翻倍");
        b4.setId("x2_double");
        b4.setOddType((userbean.getDouble() / 100) + "");
        b4.setClick(false);
        b4.setMoney(0);

        LongHuBean b5 = new LongHuBean();
        b5.setType(type);
        b5.setTitle("闲2超倍");
        b5.setId("x2_SuperDouble");
        b5.setOddType((userbean.getSuperDouble() / 100) + "");
        b5.setClick(false);
        b5.setMoney(0);


        LongHuBean b6 = new LongHuBean();
        b6.setType(type);
        b6.setTitle("闲3平倍");
        b6.setId("x3_equal");
        b6.setOddType((userbean.getEqual() / 100) + "");
        b6.setClick(false);
        b6.setMoney(0);

        LongHuBean b7 = new LongHuBean();
        b7.setType(type);
        b7.setTitle("闲3翻倍");
        b7.setId("x3_double");
        b7.setOddType((userbean.getDouble() / 100) + "");
        b7.setClick(false);
        b7.setMoney(0);

        LongHuBean b8 = new LongHuBean();
        b8.setType(type);
        b8.setTitle("闲3超倍");
        b8.setId("x3_SuperDouble");
        b8.setOddType((userbean.getSuperDouble() / 100) + "");
        b8.setClick(false);
        b8.setMoney(0);


        data.add(b);
        data.add(b1);
        data.add(b2);
        data.add(b3);
        data.add(b4);
        data.add(b5);
        data.add(b6);
        data.add(b7);
        data.add(b8);

        GameAdapter myAdapter = new GameAdapter(R.layout.item_game_sanniu);
        recyclerNiuniu.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerNiuniu.setAdapter(myAdapter);
        myAdapter.setNewData(data);
        myAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_longhu:
                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i) == data.get(position)) {
                                data.get(i).setClick(true);
                                data.get(i).setMoney((data.get(i).getMoney() + money));
                            } else {
                                data.get(i).setClick(false);
                            }
                        }
                        myAdapter.setNewData(data);
                        break;
                }
            }
        });
    }

    private void initsangong() {
        data = new ArrayList<>();
        LongHuBean b = new LongHuBean();
        b.setType(type);
        b.setTitle("闲1平倍");
        b.setId("x1_equal");
        b.setOddType((userbean.getEqual() / 100) + "");
        b.setClick(false);
        b.setMoney(0);

        LongHuBean b1 = new LongHuBean();
        b1.setType(type);
        b1.setTitle("闲1翻倍");
        b1.setId("x1_double");
        b1.setOddType((userbean.getDouble() / 100) + "");
        b1.setClick(false);
        b1.setMoney(0);

        LongHuBean b2 = new LongHuBean();
        b2.setType(type);
        b2.setTitle("闲1超倍");
        b2.setId("x1_SuperDouble");
        b2.setOddType((userbean.getSuperDouble() / 100) + "");
        b2.setClick(false);
        b2.setMoney(0);


        LongHuBean b3 = new LongHuBean();
        b3.setType(type);
        b3.setTitle("闲2平倍");
        b3.setId("x2_equal");
        b3.setOddType((userbean.getEqual() / 100) + "");
        b3.setClick(false);
        b3.setMoney(0);

        LongHuBean b4 = new LongHuBean();
        b4.setType(type);
        b4.setTitle("闲2翻倍");
        b4.setId("x2_double");
        b4.setOddType((userbean.getDouble() / 100) + "");
        b4.setClick(false);
        b4.setMoney(0);

        LongHuBean b5 = new LongHuBean();
        b5.setType(type);
        b5.setTitle("闲2超倍");
        b5.setId("x2_SuperDouble");
        b5.setOddType((userbean.getSuperDouble() / 100) + "");
        b5.setClick(false);
        b5.setMoney(0);


        LongHuBean b6 = new LongHuBean();
        b6.setType(type);
        b6.setTitle("闲3平倍");
        b6.setId("x3_equal");
        b6.setOddType((userbean.getEqual() / 100) + "");
        b6.setClick(false);
        b6.setMoney(0);

        LongHuBean b7 = new LongHuBean();
        b7.setType(type);
        b7.setTitle("闲3翻倍");
        b7.setId("x3_double");
        b7.setOddType((userbean.getDouble() / 100) + "");
        b7.setClick(false);
        b7.setMoney(0);

        LongHuBean b8 = new LongHuBean();
        b8.setType(type);
        b8.setTitle("闲3超倍");
        b8.setId("x3_SuperDouble");
        b8.setOddType((userbean.getSuperDouble() / 100) + "");
        b8.setClick(false);
        b8.setMoney(0);


        LongHuBean b9 = new LongHuBean();
        b9.setType(type);
        b9.setTitle("闲4平倍");
        b9.setId("x4_equal");
        b9.setOddType((userbean.getEqual() / 100) + "");
        b9.setClick(false);
        b9.setMoney(0);

        LongHuBean b10 = new LongHuBean();
        b10.setType(type);
        b10.setTitle("闲4翻倍");
        b10.setId("x4_double");
        b10.setOddType((userbean.getDouble() / 100) + "");
        b10.setClick(false);
        b10.setMoney(0);

        LongHuBean b11 = new LongHuBean();
        b11.setType(type);
        b11.setTitle("闲4超倍");
        b11.setId("x4_SuperDouble");
        b11.setOddType((userbean.getSuperDouble() / 100) + "");
        b11.setClick(false);
        b11.setMoney(0);


        LongHuBean b12 = new LongHuBean();
        b12.setType(type);
        b12.setTitle("闲5平倍");
        b12.setId("x5_equal");
        b12.setOddType((userbean.getEqual() / 100) + "");
        b12.setClick(false);
        b12.setMoney(0);

        LongHuBean b13 = new LongHuBean();
        b13.setType(type);
        b13.setTitle("闲5翻倍");
        b13.setId("x5_double");
        b13.setOddType((userbean.getDouble() / 100) + "");
        b13.setClick(false);
        b13.setMoney(0);

        LongHuBean b14 = new LongHuBean();
        b14.setType(type);
        b14.setTitle("闲5超倍");
        b14.setId("x5_SuperDouble");
        b14.setOddType((userbean.getSuperDouble() / 100) + "");
        b14.setClick(false);
        b14.setMoney(0);


        LongHuBean b15 = new LongHuBean();
        b15.setType(type);
        b15.setTitle("闲6平倍");
        b15.setId("x6_equal");
        b15.setOddType((userbean.getEqual() / 100) + "");
        b15.setClick(false);
        b15.setMoney(0);

        LongHuBean b16 = new LongHuBean();
        b16.setType(type);
        b16.setTitle("闲6翻倍");
        b16.setId("x6_double");
        b16.setOddType((userbean.getDouble() / 100) + "");
        b16.setClick(false);
        b16.setMoney(0);

        LongHuBean b17 = new LongHuBean();
        b17.setType(type);
        b17.setTitle("闲6超倍");
        b17.setId("x6_SuperDouble");
        b17.setOddType((userbean.getSuperDouble() / 100) + "");
        b17.setClick(false);
        b17.setMoney(0);


        data.add(b);
        data.add(b1);
        data.add(b2);
        data.add(b3);
        data.add(b4);
        data.add(b5);
        data.add(b6);
        data.add(b7);
        data.add(b8);
        data.add(b9);
        data.add(b10);
        data.add(b11);
        data.add(b12);
        data.add(b13);
        data.add(b14);
        data.add(b15);
        data.add(b16);
        data.add(b17);

        GameAdapter myAdapter = new GameAdapter(R.layout.item_game_sanniu);
        recyclerSangong.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerSangong.setAdapter(myAdapter);
        myAdapter.setNewData(data);
        myAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_longhu:
                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i) == data.get(position)) {
                                data.get(i).setClick(true);
                                data.get(i).setMoney((data.get(i).getMoney() + money));
                            } else {
                                data.get(i).setClick(false);
                            }
                        }
                        myAdapter.setNewData(data);
                        break;
                }
            }
        });
    }

    private void initlonghu() {
        data = new ArrayList<>();
        LongHuBean b = new LongHuBean();
        b.setType(type);
        b.setTitle("龙");
        b.setId("dragon");
        b.setOddType((userbean.getDragon() / 100) + "");
        b.setClick(false);
        b.setMoney(0);

        LongHuBean b1 = new LongHuBean();
        b1.setType(type);
        b1.setTitle("虎");
        b1.setId("tiger");
        b1.setOddType((userbean.getTiger() / 100) + "");
        b1.setClick(false);
        b1.setMoney(0);

        LongHuBean b2 = new LongHuBean();
        b2.setType(type);
        b2.setTitle("和");
        b2.setId("tie");
        b2.setOddType((userbean.getTie() / 100) + "");
        b2.setClick(false);
        b2.setMoney(0);

        data.add(b);
        data.add(b1);
        data.add(b2);

        GameAdapter myAdapter = new GameAdapter(R.layout.item_longhu);
        gameLonghu.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        gameLonghu.setAdapter(myAdapter);
        myAdapter.setNewData(data);
        myAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_longhu:
                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i) == data.get(position)) {
                                data.get(i).setClick(true);
                                data.get(i).setMoney((data.get(i).getMoney() + money));
                            } else {
                                data.get(i).setClick(false);
                            }
                        }
                        myAdapter.setNewData(data);
                        break;
                }
            }
        });
    }

    private void inita89() {
        data = new ArrayList<>();
        LongHuBean b = new LongHuBean();
        b.setType(type);
        b.setTitle("顺门");
        b.setId("ShunMen_equal");
        b.setOddType((userbean.getEqual() / 100) + "");
        b.setClick(false);
        b.setMoney(0);

        LongHuBean b1 = new LongHuBean();
        b1.setType(type);
        b1.setTitle("天门");
        b1.setId("TianMen_equal");
        b1.setOddType((userbean.getEqual() / 100) + "");
        b1.setClick(false);
        b1.setMoney(0);

        LongHuBean b2 = new LongHuBean();
        b2.setType(type);
        b2.setTitle("反门");
        b2.setId("FanMen_equal");
        b2.setOddType((userbean.getEqual() / 100) + "");
        b2.setClick(false);
        b2.setMoney(0);


        LongHuBean b3 = new LongHuBean();
        b3.setType(type);
        b3.setTitle("顺门超倍");
        b3.setId("ShunMen_Super_Double");
        b3.setOddType((userbean.getSuperDouble() / 100) + "");
        b3.setClick(false);
        b3.setMoney(0);


        LongHuBean b4 = new LongHuBean();
        b4.setType(type);
        b4.setTitle("天门超倍");
        b4.setId("TianMen_Super_Double");
        b4.setOddType((userbean.getSuperDouble() / 100) + "");
        b4.setClick(false);
        b4.setMoney(0);

        LongHuBean b5 = new LongHuBean();
        b5.setType(type);
        b5.setTitle("反门超倍");
        b5.setId("FanMen_Super_Double");
        b5.setOddType((userbean.getSuperDouble() / 100) + "");
        b5.setClick(false);
        b5.setMoney(0);


        data.add(b);
        data.add(b1);
        data.add(b2);
        data.add(b3);
        data.add(b4);
        data.add(b5);

        GameAdapter myAdapter = new GameAdapter(R.layout.item_game_sanniu);
        gameA89.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        gameA89.setAdapter(myAdapter);
        myAdapter.setNewData(data);
        myAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_longhu:
                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i) == data.get(position)) {
                                data.get(i).setClick(true);
                                data.get(i).setMoney((data.get(i).getMoney() + money));
                            } else {
                                data.get(i).setClick(false);
                            }
                        }
                        myAdapter.setNewData(data);
                        break;
                }
            }
        });
    }


    private void initbaijiale() {
        data = new ArrayList<>();
        LongHuBean b = new LongHuBean();
        b.setType(type);
        b.setTitle("庄");
        b.setId("banker");
        b.setOddType((userbean.getBanker() / 100) + "");
        b.setClick(false);
        b.setMoney(0);

        LongHuBean b1 = new LongHuBean();
        b1.setType(type);
        b1.setTitle("和");
        b1.setId("tie");
        b1.setOddType((userbean.getTie() / 100) + "");
        b1.setClick(false);
        b1.setMoney(0);

        LongHuBean b2 = new LongHuBean();
        b2.setType(type);
        b2.setTitle("闲");
        b2.setId("player");
        b2.setOddType((userbean.getPlayer() / 100) + "");
        b2.setClick(false);
        b2.setMoney(0);


        LongHuBean b3 = new LongHuBean();
        b3.setType(type);
        b3.setTitle("庄对");
        b3.setId("bankerPair");
        b3.setOddType((userbean.getBankerPair() / 100) + "");
        b3.setClick(false);
        b3.setMoney(0);


        LongHuBean b4 = new LongHuBean();
        b4.setType(type);
        b4.setTitle("闲对");
        b4.setId("playerPair");
        b4.setOddType((userbean.getPlayerPair() / 100) + "");
        b4.setClick(false);
        b4.setMoney(0);


        data.add(b);
        data.add(b1);
        data.add(b2);
        data.add(b3);
        data.add(b4);

        GameAdapterBai myAdapter = new GameAdapterBai(R.layout.item_game_sanniu);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 6);
        gameBaijiale.setAdapter(myAdapter);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (data.get(position).getTitle().equals("庄对")) {
                    return 3;
                } else if (data.get(position).getTitle().equals("闲对")) {
                    return 3;
                }
                return 2;
            }
        });
        gameBaijiale.setLayoutManager(manager);
        myAdapter.setNewData(data);
        myAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_longhu:
                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i) == data.get(position)) {
                                data.get(i).setClick(true);
                                data.get(i).setMoney((data.get(i).getMoney() + money));
                            } else {
                                data.get(i).setClick(false);
                            }
                        }
                        myAdapter.setNewData(data);
                        break;
                }
            }
        });
    }

}

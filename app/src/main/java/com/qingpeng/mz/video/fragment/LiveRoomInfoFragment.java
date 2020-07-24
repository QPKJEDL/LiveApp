package com.qingpeng.mz.video.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qingpeng.mz.R;
import com.qingpeng.mz.adapter.GiftListAdapter;
import com.qingpeng.mz.api.Host;
import com.qingpeng.mz.api.RedBag;
import com.qingpeng.mz.base.BaseFragment;
import com.qingpeng.mz.bean.CoverBean;
import com.qingpeng.mz.bean.GiftListBean;
import com.qingpeng.mz.bean.ZhuBoRoomInfoBean;
import com.qingpeng.mz.okhttp.APIResponse;
import com.qingpeng.mz.okhttp.MyCall;
import com.qingpeng.mz.okhttp.ResultException;
import com.qingpeng.mz.okhttp.RetrofitCreateHelper;
import com.qingpeng.mz.utils.APP;
import com.qingpeng.mz.utils.ToastUtils;
import com.qingpeng.mz.video.activity.LivePushActivity;
import com.qingpeng.mz.views.XCRoundImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by zhukkun on 1/9/17.
 */
public class LiveRoomInfoFragment extends BaseFragment {

    public static final String EXTRA_IS_AUDIENCE = "is_audence";
    @BindView(R.id.user_head)
    XCRoundImageView userHead;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_fensi)
    TextView userFensi;
    @BindView(R.id.text_people)
    TextView textPeople;
    @BindView(R.id.ima_guanzhu)
    ImageView imaGuanzhu;
    private Call<APIResponse<ZhuBoRoomInfoBean>> room;
    private MaterialDialog alertDialog;
    private Call<APIResponse<GiftListBean>> gift;
    private List<GiftListBean.ListBean> data;
    private String roomid;
    private int type;
    private ZhuBoRoomInfoBean bean;
    private CoverBean user;

    public static LiveRoomInfoFragment getInstance(boolean isAudience, String roomid) {
        LiveRoomInfoFragment fragment = new LiveRoomInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_IS_AUDIENCE, isAudience);
        bundle.putString("roomid", roomid);
        fragment.setArguments(bundle);
        return fragment;
    }

    boolean isAudience;

    @Override
    protected int getLayoutId() {
        isAudience = getArguments().getBoolean(EXTRA_IS_AUDIENCE, true);
        roomid = getArguments().getString("roomid");
        return isAudience ? R.layout.layout_live_audience_room_info : R.layout.layout_live_captrue_room_info;

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
        APP.isgame = false;
        gift = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).GetGiftRankList(roomid);
        gift.enqueue(new MyCall<APIResponse<GiftListBean>>() {
            @Override
            protected void onSuccess(Call<APIResponse<GiftListBean>> call, Response<APIResponse<GiftListBean>> response) {
                data = response.body().getData().getList();
                if (data.size() > 0) {
                    initAdapter();
                } else {
                    mListView.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(Call<APIResponse<GiftListBean>> call, Throwable t) {
                if (t instanceof ResultException) {
                    ToastUtils.showToast(((ResultException) t).getInfo());
                } else {
                    ToastUtils.showToast("网络请求失败,请稍后重试");
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isAudience) {
            bean = APP.ListRoom.get(APP.ListRoom.size() - 1);
            textPeople.setText(bean.getList().getRoomCount() + "");
            userName.setText(bean.getList().getLiveName());
            userFensi.setText("粉丝: " + bean.getLiveinfo().getFans());
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.ic_launcher_moren)//图片加载出来前，显示的图片
                    .fallback(R.mipmap.ic_launcher_moren) //url为空的时候,显示的图片
                    .error(R.mipmap.ic_launcher_moren);//图片加载失败后，显示的图片
            Glide.with(mContext)
                    .load(bean.getLiveinfo().getAvater())
                    .apply(options)
                    .into(userHead);
            LivePushActivity activity = (LivePushActivity) getActivity();
            activity.settextname(bean.getList().getLiveName(), bean.getLiveinfo().getAvater());
        } else {
            APP.isgame = false;
            room = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).RoomInfo(roomid);
            room.enqueue(new MyCall<APIResponse<ZhuBoRoomInfoBean>>() {
                @Override
                protected void onSuccess(Call<APIResponse<ZhuBoRoomInfoBean>> call, Response<APIResponse<ZhuBoRoomInfoBean>> response) {
                    bean = response.body().getData();
                    textPeople.setText(bean.getList().getRoomCount() + "");
                    userName.setText(bean.getList().getLiveName());
                    userFensi.setText("粉丝: " + bean.getLiveinfo().getFans());
                    Glide.with(mContext)
                            .load(bean.getLiveinfo().getAvater())
//                .apply(RequestOptions.bitmapTransform(new RoundedCorners(5)))
                            .into(userHead);
                }

                @Override
                protected void onError(Call<APIResponse<ZhuBoRoomInfoBean>> call, Throwable t) {
                    if (t instanceof ResultException) {
                        ToastUtils.showToast(((ResultException) t).getInfo());
                    } else {
                        ToastUtils.showToast("网络请求失败,请稍后重试");
                    }
                }
            });
        }

        if (isAudience) {
            LivePushActivity activity = (LivePushActivity) getActivity();
            activity.settextname(bean.getList().getLiveName(), bean.getLiveinfo().getAvater());
        }

    }

    public void setTextPeople(String s) {
        textPeople.setText(s);
    }

    @Override
    protected void initAdapter() {
        ms.setOrientation(LinearLayout.HORIZONTAL);
        mListView.setLayoutManager(ms);
        mAdapter = new GiftListAdapter(R.layout.item_gift_list, mContext);
        mAdapter.setNewData(data);
        super.initAdapter();
    }

    @OnClick({R.id.user_head, R.id.ima_guanzhu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_head:
                if (alertDialog == null) {
                    View inflate = View.inflate(getActivity(), R.layout.live_user_dialog, null);
                    XCRoundImageView ima_touxiang = (XCRoundImageView) inflate.findViewById(R.id.ima_touxiang);
                    TextView text_name = (TextView) inflate.findViewById(R.id.text_name);
                    TextView text_zhibo = (TextView) inflate.findViewById(R.id.text_zhibo);
                    TextView text_fensi = (TextView) inflate.findViewById(R.id.text_fensi);
                    TextView text_guanzhu = (TextView) inflate.findViewById(R.id.text_guanzhu);
                    Button tvOk = (Button) inflate.findViewById(R.id.btn_ok);
                    text_name.setText(bean.getLiveinfo().getNickName());
                    text_zhibo.setText(bean.getList().getRoomCount() + "");
                    text_fensi.setText(bean.getLiveinfo().getFans() + "");
                    text_guanzhu.setText(bean.getLiveinfo().getFollowed() + "");
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.mipmap.ic_launcher_moren)//图片加载出来前，显示的图片
                            .fallback(R.mipmap.ic_launcher_moren) //url为空的时候,显示的图片
                            .error(R.mipmap.ic_launcher_moren);//图片加载失败后，显示的图片
                    Glide.with(mContext).load(bean.getLiveinfo().getAvater()).apply(options).into(ima_touxiang);
                    if (type == 0) {
                        tvOk.setText("关注");
                    } else {
                        tvOk.setText("已关注");
                    }
                    tvOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (alertDialog != null) {
                                alertDialog.dismiss();
                            }
                            APP.isgame = false;
                            if (type == 0) {
                                follow("do_follow_live", type, tvOk);
                            } else {
                                follow("un_follow_live", type, tvOk);
                            }
                        }
                    });
                    alertDialog = new MaterialDialog.Builder(getActivity()).customView(inflate, false).show();
                    alertDialog.setCanceledOnTouchOutside(true);
                } else {
                    if (alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    } else {
                        alertDialog.show();
                    }
                }
                break;
            case R.id.ima_guanzhu:
                APP.isgame = false;
                call = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).do_follow_live("do_follow_live", roomid);
                call.enqueue(new MyCall<APIResponse>() {
                    @Override
                    protected void onSuccess(Call<APIResponse> call, Response<APIResponse> response) {
                        ToastUtils.showToast(response.body().getInfo());
                        imaGuanzhu.setVisibility(View.GONE);
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
                break;
        }
    }

    private void follow(String s, int b, Button tvOk) {
        APP.isgame = false;
        call = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).do_follow_live(s, roomid);
        call.enqueue(new MyCall<APIResponse>() {
            @Override
            protected void onSuccess(Call<APIResponse> call, Response<APIResponse> response) {
                ToastUtils.showToast(response.body().getInfo());
                if (b == 0) {
                    tvOk.setText("已关注");
                } else {
                    tvOk.setText("关注");
                }
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
}

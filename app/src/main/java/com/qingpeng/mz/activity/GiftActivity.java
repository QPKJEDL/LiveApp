package com.qingpeng.mz.activity;

import android.view.View;
import android.widget.TextView;

import com.qingpeng.mz.R;
import com.qingpeng.mz.adapter.GiftRecordAdapter;
import com.qingpeng.mz.api.Host;
import com.qingpeng.mz.api.RedBag;
import com.qingpeng.mz.base.BaseActivity;
import com.qingpeng.mz.bean.GiftRecordBean;
import com.qingpeng.mz.okhttp.APIResponse;
import com.qingpeng.mz.okhttp.MyCall;
import com.qingpeng.mz.okhttp.ResultException;
import com.qingpeng.mz.okhttp.RetrofitCreateHelper;
import com.qingpeng.mz.utils.APP;
import com.qingpeng.mz.utils.ToastUtils;
import com.qingpeng.mz.views.TitleBar;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

public class GiftActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleBar title;
    @BindView(R.id.text_name)
    TextView textName;
    private String type;
    private Call<APIResponse<List<GiftRecordBean>>> gift;
    private List<GiftRecordBean> gif;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gift;
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
        return R.id.recyclerView;
    }

    @Override
    protected void onResume() {
        super.onResume();
        type = getIntent().getStringExtra("type");
        title.getLlLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (type.equals("1")) {
            title.setTitle("收到的礼物");
            textName.setText("增送人");
        } else {
            title.setTitle("送出的礼物");
            textName.setText("授予人");
        }
        APP.isgame=false;
        gift = RetrofitCreateHelper.createApi(RedBag.class, Host.HOST_ZHIBO).gift_record(type);
        gift.enqueue(new MyCall<APIResponse<List<GiftRecordBean>>>() {
            @Override
            protected void onSuccess(Call<APIResponse<List<GiftRecordBean>>> call, Response<APIResponse<List<GiftRecordBean>>> response) {
                gif = (List<GiftRecordBean>) response.body().getData();
                initAdapter();
            }

            @Override
            protected void onError(Call<APIResponse<List<GiftRecordBean>>> call, Throwable t) {
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
        mAdapter = new GiftRecordAdapter(R.layout.item_gitf);
        mAdapter.setNewData(gif);
        super.initAdapter();
    }
}

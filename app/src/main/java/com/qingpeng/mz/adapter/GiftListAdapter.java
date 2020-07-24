package com.qingpeng.mz.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingpeng.mz.R;
import com.qingpeng.mz.bean.GiftListBean;
import com.qingpeng.mz.views.XCRoundImageView;

public class GiftListAdapter extends BaseQuickAdapter<GiftListBean.ListBean, BaseViewHolder> {

    private final Context context;

    public GiftListAdapter(int layoutResId, Context mContext) {
        super(layoutResId);
        this.context = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftListBean.ListBean item) {
        if (helper.getPosition()<3){
            XCRoundImageView ima = helper.getView(R.id.ima_touxiang);
            Glide.with(context).load(item.getAvater()).into(ima);
        }

    }

}
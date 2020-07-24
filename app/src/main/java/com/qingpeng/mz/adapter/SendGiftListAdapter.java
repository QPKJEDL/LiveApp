package com.qingpeng.mz.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingpeng.mz.R;
import com.qingpeng.mz.bean.GiftBean;

public class SendGiftListAdapter extends BaseQuickAdapter<GiftBean, BaseViewHolder> {

    public SendGiftListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftBean item) {
        helper.setText(R.id.text_name, item.getGiftname())
                .setText(R.id.text_num, item.getPrice() + "金币")
                .addOnClickListener(R.id.ima_xiazhu);
        ImageView ima = helper.getView(R.id.ima_xiazhu);
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher_moren)//图片加载出来前，显示的图片
                .fallback(R.mipmap.ic_launcher_moren) //url为空的时候,显示的图片
                .error(R.mipmap.ic_launcher_moren);//图片加载失败后，显示的图片
        Glide.with(mContext).load(item.getImgurl()).apply(options).into(ima);
    }
}
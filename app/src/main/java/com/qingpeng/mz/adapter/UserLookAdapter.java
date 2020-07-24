package com.qingpeng.mz.adapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingpeng.mz.R;
import com.qingpeng.mz.bean.ZhuBoRoomInfoBean;
import com.qingpeng.mz.views.XCRoundImageView;

public class UserLookAdapter extends BaseQuickAdapter<ZhuBoRoomInfoBean, BaseViewHolder> {

    public UserLookAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ZhuBoRoomInfoBean item) {
        helper.setText(R.id.text_name, item.getList().getCoverName())
                .setText(R.id.text_fensi, "粉丝: " + item.getLiveinfo().getFans())
                .addOnClickListener(R.id.ll_kanguo);
        XCRoundImageView ima = helper.getView(R.id.ima_touxiang);
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher_moren)//图片加载出来前，显示的图片
                .fallback(R.mipmap.ic_launcher_moren) //url为空的时候,显示的图片
                .error(R.mipmap.ic_launcher_moren);//图片加载失败后，显示的图片
        Glide.with(mContext).load(item.getLiveinfo().getAvater()).apply(options).into(ima);

    }
}
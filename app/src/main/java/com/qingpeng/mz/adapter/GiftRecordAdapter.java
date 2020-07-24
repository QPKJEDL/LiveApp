package com.qingpeng.mz.adapter;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingpeng.mz.R;
import com.qingpeng.mz.bean.GiftRecordBean;
import com.qingpeng.mz.utils.APP;
import com.qingpeng.mz.views.XCRoundImageView;

public class GiftRecordAdapter extends BaseQuickAdapter<GiftRecordBean, BaseViewHolder> {

    public GiftRecordAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftRecordBean item) {
        for (int i = 0; i < APP.gif.size(); i++) {
            if (APP.gif.get(i).getId() == item.getGift_id()) {
                helper.setText(R.id.text_name, item.getNickname())
                        .setText(R.id.text_money, APP.gif.get(i).getPrice())
                        .setText(R.id.text_num, item.getNum());
                XCRoundImageView ima = helper.getView(R.id.ima_touxiang);
                Glide.with(mContext)
                        .load(APP.gif.get(i).getImgurl())
                        .into(ima);
            }
        }
    }

}

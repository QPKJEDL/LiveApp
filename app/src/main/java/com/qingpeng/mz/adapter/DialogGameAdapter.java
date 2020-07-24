package com.qingpeng.mz.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingpeng.mz.R;
import com.qingpeng.mz.bean.RoomInfoBean;

public class DialogGameAdapter extends BaseQuickAdapter<RoomInfoBean, BaseViewHolder> {

    public DialogGameAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, RoomInfoBean item) {
        if (item.isIsclick()) {
            helper.setText(R.id.text_room, item.getDeskName())
                    .setBackgroundRes(R.id.text_room, R.drawable.text_34px_bg_xian_hui)
                    .addOnClickListener(R.id.text_room);

        } else {
            helper.setText(R.id.text_room, item.getDeskName())
                    .setBackgroundRes(R.id.text_room, R.drawable.text_34px_bg_xian)
                    .addOnClickListener(R.id.text_room);
        }
    }

}

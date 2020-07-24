package com.qingpeng.mz.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingpeng.mz.R;
import com.qingpeng.mz.bean.LongHuBean;

public class GameJieSuanAdapter extends BaseQuickAdapter<LongHuBean, BaseViewHolder> {

    private final int type;

    public GameJieSuanAdapter(int layoutResId, int type) {
        super(layoutResId);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, LongHuBean item) {
        if (item.isClick()) {
            helper.setText(R.id.text_name, item.getTitle())
                    .setTextColor(R.id.text_win, mContext.getResources().getColor(R.color.red))
                    .setText(R.id.text_win, "赢");
        } else {
            helper.setText(R.id.text_name, item.getTitle())
                    .setTextColor(R.id.text_win, mContext.getResources().getColor(R.color.c_ffffff))
                    .setText(R.id.text_win, "输");
        }
    }

}
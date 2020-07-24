package com.qingpeng.mz.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingpeng.mz.R;
import com.qingpeng.mz.bean.HuaTiBean;

public class HuaTiAdapter extends BaseQuickAdapter<HuaTiBean, BaseViewHolder> {

    private final int type;

    public HuaTiAdapter(int layoutResId, int type) {
        super(layoutResId);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, HuaTiBean item) {
        if (type == 1) {
            helper.setText(R.id.text_name, item.getLabel())
                    .setText(R.id.text_hot, item.getHeat()+"次播放")
                    .addOnClickListener(R.id.text_canyu);
        } else if (type == 2) {
            helper.setText(R.id.text_name, item.getChannel())
                    .addOnClickListener(R.id.text_canyu);
            TextView hot = (TextView) helper.getView(R.id.text_hot);
            hot.setVisibility(View.GONE);
        }
    }

}
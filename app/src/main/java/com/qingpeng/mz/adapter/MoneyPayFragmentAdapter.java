package com.qingpeng.mz.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingpeng.mz.R;
import com.qingpeng.mz.bean.PayMoneyBean;

public class MoneyPayFragmentAdapter extends BaseQuickAdapter<PayMoneyBean,BaseViewHolder> {
    public MoneyPayFragmentAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, PayMoneyBean item) {
        if (item.getMoney()==10){
            helper.getView(R.id.money).setBackground(mContext.getResources().getDrawable(R.mipmap.money1));
        }else if (item.getMoney()==50){
            helper.getView(R.id.money).setBackground(mContext.getResources().getDrawable(R.mipmap.money2));
        }else if (item.getMoney()==100){
            helper.getView(R.id.money).setBackground(mContext.getResources().getDrawable(R.mipmap.money3));
        }else if (item.getMoney()==200){
            helper.getView(R.id.money).setBackground(mContext.getResources().getDrawable(R.mipmap.money4));
        }else if (item.getMoney()==500){
            helper.getView(R.id.money).setBackground(mContext.getResources().getDrawable(R.mipmap.money5));
        }else if (item.getMoney()==1000){
            helper.getView(R.id.money).setBackground(mContext.getResources().getDrawable(R.mipmap.money6));
        }
        helper.addOnClickListener(R.id.ll_money);
        if (item.isIsok()){
            helper.getView(R.id.ll_money).setBackground(mContext.getResources().getDrawable(R.drawable.game_bg_bai));
        }else {
            helper.getView(R.id.ll_money).setBackground(mContext.getResources().getDrawable(R.drawable.game_bg_tou));
        }
    }
}

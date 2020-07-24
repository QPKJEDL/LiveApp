package com.qingpeng.mz.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingpeng.mz.R;
import com.qingpeng.mz.bean.LongHuBean;

public class GameAdapter extends BaseQuickAdapter<LongHuBean, BaseViewHolder> {

    public GameAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, LongHuBean item) {
        if (item.getType() == 1) {
            //百家乐
            if (item.getMoney() == 0) {
                helper.setVisible(R.id.text_money, false);
            } else {
                helper.setVisible(R.id.text_money, true);
            }
            if (item.getTitle().equals("闲对")) {
                helper.setTextColor(R.id.text_name, mContext.getResources().getColor(R.color.c_2F65FF));
            } else if (item.getTitle().equals("庄对")) {
                helper.setTextColor(R.id.text_name, mContext.getResources().getColor(R.color.c_F82FFF));
            } else if (item.getTitle().equals("和")) {
                helper.setTextColor(R.id.text_name, mContext.getResources().getColor(R.color.c_FFC72F));
            }
            helper.setText(R.id.text_name, item.getTitle())
                    .setText(R.id.text_num, "1赔" + item.getOddType())
                    .setText(R.id.text_money, item.getMoney() + "")
                    .addOnClickListener(R.id.ll_longhu);
            if (item.isClick()) {
                helper.setBackgroundRes(R.id.ll_longhu, R.drawable.game_bg_hong);
            } else {
                helper.setBackgroundRes(R.id.ll_longhu, R.drawable.game_bg_lv);
            }
        } else if (item.getType() == 2) {
            //龙虎
            if (item.getMoney() == 0) {
                helper.setVisible(R.id.text_money, false);
            } else {
                helper.setVisible(R.id.text_money, true);
            }
            if (item.getTitle().equals("龙")) {
                helper.setTextColor(R.id.text_name, mContext.getResources().getColor(R.color.c_2F65FF));
            } else if (item.getTitle().equals("虎")) {
                helper.setTextColor(R.id.text_name, mContext.getResources().getColor(R.color.c_F82FFF));
            } else if (item.getTitle().equals("和")) {
                helper.setTextColor(R.id.text_name, mContext.getResources().getColor(R.color.c_FFC72F));
            }
            helper.setText(R.id.text_name, item.getTitle())
                    .setText(R.id.text_num, "1赔" + item.getOddType())
                    .setText(R.id.text_money, item.getMoney() + "")
                    .addOnClickListener(R.id.ll_longhu);
            if (item.isClick()) {
                helper.setBackgroundRes(R.id.ll_longhu, R.drawable.game_bg_hong);
            } else {
                helper.setBackgroundRes(R.id.ll_longhu, R.drawable.game_bg_lv);
            }
        } else if (item.getType() == 3) {
            //三公
            if (item.getMoney() == 0) {
                helper.setVisible(R.id.text_money, false);
            } else {
                helper.setVisible(R.id.text_money, true);
            }
            helper.setTextColor(R.id.text_name, mContext.getResources().getColor(R.color.c_F82FFF));
            helper.setText(R.id.text_name, item.getTitle())
                    .setText(R.id.text_num, "1赔" + item.getOddType())
                    .setText(R.id.text_money, item.getMoney() + "")
                    .addOnClickListener(R.id.ll_longhu);
            if (item.isClick()) {
                helper.setBackgroundRes(R.id.ll_longhu, R.drawable.game_bg_hong);
            } else {
                helper.setBackgroundRes(R.id.ll_longhu, R.drawable.game_bg_lv);
            }
        } else if (item.getType() == 4) {
            //牛牛
            if (item.getMoney() == 0) {
                helper.setVisible(R.id.text_money, false);
            } else {
                helper.setVisible(R.id.text_money, true);
            }
            helper.setTextColor(R.id.text_name, mContext.getResources().getColor(R.color.c_F82FFF));
            helper.setText(R.id.text_name, item.getTitle())
                    .setText(R.id.text_num, "1赔" + item.getOddType())
                    .setText(R.id.text_money, item.getMoney() + "")
                    .addOnClickListener(R.id.ll_longhu);
            if (item.isClick()) {
                helper.setBackgroundRes(R.id.ll_longhu, R.drawable.game_bg_hong);
            } else {
                helper.setBackgroundRes(R.id.ll_longhu, R.drawable.game_bg_lv);
            }
        } else if (item.getType() == 5) {
            //a98
            if (item.getMoney() == 0) {
                helper.setVisible(R.id.text_money, false);
            } else {
                helper.setVisible(R.id.text_money, true);
            }
            if (item.getTitle().contains("天门")) {
                helper.setTextColor(R.id.text_name, mContext.getResources().getColor(R.color.c_2F65FF));
            } else if (item.getTitle().contains("顺门")) {
                helper.setTextColor(R.id.text_name, mContext.getResources().getColor(R.color.c_F82FFF));
            } else if (item.getTitle().equals("反门")) {
                helper.setTextColor(R.id.text_name, mContext.getResources().getColor(R.color.c_FFC72F));
            }
            helper.setText(R.id.text_name, item.getTitle())
                    .setText(R.id.text_num, "1赔" + item.getOddType())
                    .setText(R.id.text_money, item.getMoney() + "")
                    .addOnClickListener(R.id.ll_longhu);
            if (item.isClick()) {
                helper.setBackgroundRes(R.id.ll_longhu, R.drawable.game_bg_hong);
            } else {
                helper.setBackgroundRes(R.id.ll_longhu, R.drawable.game_bg_lv);
            }
        }
    }
}

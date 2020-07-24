package com.qingpeng.mz.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingpeng.mz.R;
import com.qingpeng.mz.bean.FollowListBean;
import com.qingpeng.mz.views.XCRoundImageView;

public class FollowListAdapter extends BaseQuickAdapter<FollowListBean, BaseViewHolder> {

    private final Context context;
    private AnimationDrawable anim;

    public FollowListAdapter(int layoutResId, Context mContext) {
        super(layoutResId);
        this.context = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, FollowListBean item) {
        XCRoundImageView touxiang = helper.getView(R.id.ima_touxiang);
        ImageView live = helper.getView(R.id.ima_live);
        if (item.getStatus() == 1) {
            live.setVisibility(View.VISIBLE);
            anim = (AnimationDrawable) live.getBackground();
            anim.start();
            helper.setText(R.id.text_live_status, "直播中")
                    .setText(R.id.text_name, item.getNickname())
                    .setText(R.id.text_fensi, "粉丝: " + item.getFans());
        } else {
            live.setVisibility(View.GONE);
            helper.setText(R.id.text_name, item.getNickname())
                    .setText(R.id.text_fensi, "粉丝: " + item.getFans())
                    .setText(R.id.text_live_status, "暂无直播");
        }

        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher_moren)//图片加载出来前，显示的图片
                .fallback(R.mipmap.ic_launcher_moren) //url为空的时候,显示的图片
                .error(R.mipmap.ic_launcher_moren);//图片加载失败后，显示的图片
        Glide.with(mContext)
                .load(item.getAvater())
                .apply(options)
                .into(touxiang);
    }

}
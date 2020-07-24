package com.qingpeng.mz.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingpeng.mz.R;
import com.qingpeng.mz.bean.RoomInfoListBean;

public class RoomListAdapter extends BaseQuickAdapter<RoomInfoListBean, BaseViewHolder> {

    public RoomListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, RoomInfoListBean item) {
        helper.setText(R.id.text_name, item.getCoverName())
                .setText(R.id.text_people, item.getRoomCount() + "")
                .addOnClickListener(R.id.rl_room);
        ImageView ima = helper.getView(R.id.ima_touxiang);
        RequestOptions options = new RequestOptions()
                .bitmapTransform(new RoundedCorners(5))
                .placeholder(R.mipmap.ic_launcher_moren)//图片加载出来前，显示的图片
                .fallback(R.mipmap.ic_launcher_moren) //url为空的时候,显示的图片
                .error(R.mipmap.ic_launcher_moren);//图片加载失败后，显示的图片
        Glide.with(mContext)
                .load(item.getCoverImg())
                .apply(options)
                .into(ima);
    }
}

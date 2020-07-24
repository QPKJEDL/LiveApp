package com.aliyun.alivclive.room.userlist;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.aliyun.alivclive.R;
import com.bumptech.glide.Glide;

/**
 * @author Mulberry
 *         create on 2018/4/23.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>{
    ArrayList<AlivcUserInfo> userInfos;
    private WeakReference<Context> contextWeakReference;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.iv_user_info);
        }
    }

    public UserListAdapter(Context context, ArrayList<AlivcUserInfo> userInfos) {
        contextWeakReference = new WeakReference<Context>(context);
        this.userInfos = userInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserListAdapter.ViewHolder viewHolder = new UserListAdapter.ViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.alivc_live_room_user_info_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(this.contextWeakReference.get())
            .load(R.mipmap.icon_avatar_default)
            .centerCrop()
            .crossFade()
            .into(((ViewHolder)holder).imageView);

        (((ViewHolder)holder).imageView).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onUserInfoClickListener != null){
                    onUserInfoClickListener.onUserClick(userInfos.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userInfos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    OnUserInfoClickListener onUserInfoClickListener;

    public void setOnUserInfoClickListener(
        OnUserInfoClickListener onUserInfoClickListener) {
        this.onUserInfoClickListener = onUserInfoClickListener;
    }

    public interface OnUserInfoClickListener{

        /**
         * 点击用户
         */
        void onUserClick(AlivcUserInfo alivcUserInfo);
    }
}

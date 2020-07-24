package com.aliyun.alivclive.homepage.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.alivclive.R;
import com.aliyun.alivclive.room.roominfo.AlivcLiveRoomInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akira on 2018/5/29.
 */

public class ReplayListAdapter extends RecyclerView.Adapter {

    private List<AlivcLiveRoomInfo> mInfos = new ArrayList<>();

    public void addData(List<AlivcLiveRoomInfo> data) {
        mInfos.addAll(data);
        if (mInfos.size() > data.size()) {
            int startPostion = mInfos.size() - data.size();
            notifyItemRangeInserted(startPostion, data.size());
        } else {
            notifyDataSetChanged();
        }
    }

    public List<AlivcLiveRoomInfo> getData() {
        return mInfos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_list, parent, false);
        RoomInfoHolder holder = new RoomInfoHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mInfos != null && mInfos.size() > 0) {
            AlivcLiveRoomInfo roomInfo = mInfos.get(position);
            if (holder instanceof RoomInfoHolder) {
                RoomInfoHolder roomInfoHolder = (RoomInfoHolder) holder;
                if (roomInfo != null) {
                    roomInfoHolder.tvCount.setText(roomInfo.getRoom_viewer_count() + "");
                    roomInfoHolder.tvName.setText(roomInfo.getStreamer_name());
//                    roomInfoHolder.imAvatar.setImageResource(R.drawable.bar_yellow_color);
                }
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mInfos == null ? 0 : mInfos.size();
    }

    class RoomInfoHolder extends RecyclerView.ViewHolder {
        ImageView imAvatar;
        TextView tvName;
        TextView tvCount;

        public RoomInfoHolder(View itemView) {
            super(itemView);
            imAvatar = itemView.findViewById(R.id.room_avatar);
            tvName = itemView.findViewById(R.id.room_name);
            tvCount = itemView.findViewById(R.id.room_viwer_count);
        }
    }
}

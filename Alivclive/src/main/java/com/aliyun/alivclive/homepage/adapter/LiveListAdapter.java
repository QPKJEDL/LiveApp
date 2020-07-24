package com.aliyun.alivclive.homepage.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.alivclive.BuildConfig;
import com.aliyun.alivclive.R;

import com.aliyun.alivclive.room.roominfo.AlivcLiveRoomInfo;
import com.aliyun.alivclive.utils.ImageUtils;
import com.aliyun.pusher.core.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akira on 2018/5/29.
 */

public class LiveListAdapter extends RecyclerView.Adapter {

    private List<AlivcLiveRoomInfo> mInfos = new ArrayList<>();

    private Context mContext;

    private OnItemClickListener mListener;

    public LiveListAdapter(Context mContext, OnItemClickListener listener) {
        this.mContext = mContext;
        this.mListener = listener;
    }

    public void addData(List<AlivcLiveRoomInfo> data) {
        mInfos.addAll(data);
        if (mInfos.size() > data.size()) {
            int startPostion = mInfos.size() - data.size();
            notifyItemRangeInserted(startPostion, data.size());
            notifyItemRangeChanged(startPostion, data.size());
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (mInfos != null && mInfos.size() > 0) {
            final AlivcLiveRoomInfo roomInfo = mInfos.get(position);
            if (holder instanceof RoomInfoHolder) {
                RoomInfoHolder roomInfoHolder = (RoomInfoHolder) holder;
                if (roomInfo != null) {
                    if (roomInfo.getRoom_viewer_count() < 0) {
                        roomInfoHolder.tvCount.setText("0");
                    } else {
                        roomInfoHolder.tvCount.setText(roomInfo.getRoom_viewer_count() + "");
                    }
                    roomInfoHolder.tvName.setText(roomInfo.getStreamer_name());
                    ImageUtils.loadImage(mContext, roomInfo.getRoom_screen_shot(), roomInfoHolder.imAvatar);

                    roomInfoHolder.click.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Todo go to viewer room
                            mListener.onClick(position);
                        }
                    });

                    if (BuildConfig.DEBUG) {
                        roomInfoHolder.click.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                                cm.setText(roomInfo.getRoom_id());
                                Toast.makeText(mContext, "Copy Room ID", Toast.LENGTH_SHORT).show();

                                return true;
                            }
                        });
                    }

//                    if (BuildConfig.DEBUG) {
//                        roomInfoHolder.tvStreamerId.setVisibility(View.VISIBLE);
//                        roomInfoHolder.tvRoomId.setVisibility(View.VISIBLE);
//                        roomInfoHolder.tvStreamerId.setText(String.format("uid:%s", roomInfo.getStreamer_id()));
//                        roomInfoHolder.tvRoomId.setText(String.format("room:%s", roomInfo.getRoom_id()));
//                    } else {
//                        roomInfoHolder.tvStreamerId.setVisibility(View.GONE);
//                        roomInfoHolder.tvRoomId.setVisibility(View.GONE);
//                    }
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
        View click;

        TextView tvStreamerId;
        TextView tvRoomId;


        public RoomInfoHolder(View itemView) {
            super(itemView);
            imAvatar = itemView.findViewById(R.id.room_avatar);
            tvName = itemView.findViewById(R.id.room_name);
            tvCount = itemView.findViewById(R.id.room_viwer_count);
            click = itemView.findViewById(R.id.click_view);
            tvStreamerId = itemView.findViewById(R.id.streamer_id);
            tvRoomId = itemView.findViewById(R.id.room_id);
        }
    }
}

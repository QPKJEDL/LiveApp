package com.aliyun.alivclive.room.comment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.aliyun.alivclive.R;
import com.aliyun.alivclive.room.comment.AlivcLiveMessageInfo.AlivcMsgType;
import com.aliyun.alivclive.utils.constants.AlivcConstants;

/**
 *
 * 界面评论Adapter，主要展示评论界面list的adapter类
 * @author Mulberry
 *         create on 2018/4/23.
 */
public class AlivcChatMsgListAdapter extends RecyclerView.Adapter<AlivcChatMsgListAdapter.ViewHolder>{
    ArrayList<AlivcLiveMessageInfo> chatDatas;
    private WeakReference<Context> contextWeakReference;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvComment;
        public ViewHolder(View itemView) {
            super(itemView);
            tvComment = (TextView)itemView.findViewById(R.id.tv_comment);
        }
    }

    public AlivcChatMsgListAdapter(Context context,ArrayList<AlivcLiveMessageInfo> chatDatas) {
        contextWeakReference = new WeakReference<Context>(context);
        this.chatDatas = chatDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.alivc_comment_list_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
            SpannableString spanString = null;
            if (chatDatas.get(position).getType() == AlivcMsgType.ALIVC_MESSAGE_TYPE_CHAT.getMsgType()){//设置名称颜色

                spanString = new SpannableString(chatDatas.get(position).getUserId() + ":  " + chatDatas.get(position).getDataContent().toString());
                spanString.setSpan(new ForegroundColorSpan(contextWeakReference.get().getResources().getColor(R.color.alivc_color_send_name)),
                    0, String.valueOf(chatDatas.get(position).getUserId()).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }else if (chatDatas.get(position).getType() == AlivcMsgType.ALIVC_MESSAGE_TYPE_LIKE.getMsgType()){
                // TODO: 2018/5/4

            }else if (chatDatas.get(position).getType() == AlivcMsgType.ALIVC_MESSAGE_TYPE_ALLOWALLSENDMSG.getMsgType()) {
                    //允许所有用户发言
                    spanString = new SpannableString(contextWeakReference.get().getResources().getString(R.string.alivc_message_type_allow_all_user));

            } else if (chatDatas.get(position).getType() == AlivcMsgType.ALIVC_MESSAGE_TYPE_ALLOWSENDMSG.getMsgType()) {
                    //允许用户发言
                    spanString = new SpannableString(chatDatas.get(position).getUserId() + ":  " + contextWeakReference.get().getResources().getString(R.string.alivc_message_type_allow_user));

            } else if (chatDatas.get(position).getType() == AlivcMsgType.ALIVC_MESSAGE_TYPE_FORBIDALLSENDMSG.getMsgType()) {
                    //所有用户被禁言
                    spanString = new SpannableString(contextWeakReference.get().getResources().getString(R.string.alivc_message_type_forbid_all_user));

            } else if (chatDatas.get(position).getType() == AlivcMsgType.ALIVC_MESSAGE_TYPE_FORBIDSENDMSG.getMsgType()) {
                    //禁止用户发言
                    spanString = new SpannableString(chatDatas.get(position).getUserId() + ":  " + contextWeakReference.get().getResources().getString(R.string.alivc_message_type_forbid_user));

            } else if (chatDatas.get(position).getType() == AlivcMsgType.ALIVC_MESSAGE_TYPE_LOGIN.getMsgType()) {
                    //加入房间
                    spanString = new SpannableString(chatDatas.get(position).getUserId() + ":  " + contextWeakReference.get().getResources().getString(R.string.alivc_message_type_join_room));

            } else if (chatDatas.get(position).getType() == AlivcMsgType.ALIVC_MESSAGE_TYPE_KICKOUT.getMsgType()) {
                    //踢出用户
                    spanString = new SpannableString(chatDatas.get(position).getUserId() + ":  " + contextWeakReference.get().getResources().getString(R.string.alivc_message_type_kick_out));

            } else if (chatDatas.get(position).getType() == AlivcMsgType.ALIVC_MESSAGE_TYPE_LOGOUT_ROOM.getMsgType()) {
                    //离开房间
                    spanString = new SpannableString(chatDatas.get(position).getUserId() + ":  " + contextWeakReference.get().getResources().getString(R.string.alivc_message_type_leave_room));

            } else if (chatDatas.get(position).getType() == AlivcMsgType.ALIVC_MESSAGE_UP_MIC.getMsgType()) {
                    spanString = new SpannableString(contextWeakReference.get().getResources().getString(R.string.alivc_message_type_up_mic));
            
            } else if(chatDatas.get(position).getType() == AlivcConstants.ALIVC_PUSHER_EVENT_RTMP_RECONNECT_START) {
                    spanString = new SpannableString(chatDatas.get(position).getDataContent().toString());

            } else if(chatDatas.get(position).getType() == AlivcConstants.ALIVC_PUSHER_EVENT_RTMP_RECONNECT_SUCCESS) {
                    spanString = new SpannableString(chatDatas.get(position).getDataContent().toString());
            }

            ((ViewHolder)holder).tvComment.setText(spanString);
            ((ViewHolder)holder).tvComment.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(chatDatas.get(position));
                }
            });
    }

    @Override
    public int getItemCount() {
        return chatDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    protected OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(
        OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(AlivcLiveMessageInfo item);
    }
}


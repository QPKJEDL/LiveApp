package com.aliyun.alivclive.room.comment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import com.aliyun.alivclive.R;
import com.aliyun.alivclive.room.comment.AlivcChatMsgListAdapter.OnItemClickListener;
import com.aliyun.alivclive.room.comment.AlivcLiveMessageInfo.AlivcMsgType;

/**
 * 评论列表View,负责展示评论数据
 * @author Mulberry
 *         create on 2018/4/19.
 */

public class AlivcCommentListView extends RelativeLayout {

    private RecyclerView commentListView;
    private ArrayList<AlivcLiveMessageInfo> chatDatas;
    private static AlivcChatMsgListAdapter alivcChatMsgListAdapter;
    private OnCommentClickListener onCommentClickListener;
    public AlivcCommentListView(Context context) {
        super(context);
        initView();
    }

    public AlivcCommentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AlivcCommentListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        setGravity(Gravity.BOTTOM);
        chatDatas = new ArrayList<>();
        View commentView = LayoutInflater.from(getContext()).inflate(R.layout.alivc_comment_list_view_layout,this,true);
        commentListView = (RecyclerView)commentView.findViewById(R.id.comment_list_view);
        commentListView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        alivcChatMsgListAdapter = new AlivcChatMsgListAdapter(getContext().getApplicationContext(),chatDatas);
        commentListView.setAdapter(alivcChatMsgListAdapter);
        alivcChatMsgListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AlivcLiveMessageInfo item) {
                if (onCommentClickListener != null){
                    onCommentClickListener.onClickUser(item);
                }
            }
        });
    }


    public void setOnCommentClickListener(
        OnCommentClickListener onCommentClickListener) {
        this.onCommentClickListener = onCommentClickListener;
    }

    public interface OnCommentClickListener {
        /**
         * 点击评论
         * @param alivcLiveMessageInfo 评论信息
         */
        void onClickUser(AlivcLiveMessageInfo alivcLiveMessageInfo);

    }

    private static final int UPDATE_COMMENT_LIST = 1;
    private class UiHandler extends Handler{
        WeakReference<Context> contextWeakReference;

        public UiHandler(Looper looper, Context context) {
            super(looper);
            this.contextWeakReference = new WeakReference<Context>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (contextWeakReference.get()!= null){
                commentListView.smoothScrollToPosition(chatDatas.size());
                alivcChatMsgListAdapter.notifyDataSetChanged();
            }
        }
    }

}

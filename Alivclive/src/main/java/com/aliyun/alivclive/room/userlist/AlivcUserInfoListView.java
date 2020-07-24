package com.aliyun.alivclive.room.userlist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import com.aliyun.alivclive.R;
import com.aliyun.alivclive.room.comment.AlivcChatEntity;
import com.aliyun.alivclive.room.userlist.UserListAdapter.OnUserInfoClickListener;

/**
 * 推流顶部的用户信息列表View
 * @author Mulberry
 *         create on 2018/4/19.
 */

public class AlivcUserInfoListView extends RelativeLayout{
    private static final int firstIndex = 0;
    private ArrayList<AlivcUserInfo> userInfos;
    private UserListAdapter userListAdapter;

    public AlivcUserInfoListView(Context context) {
        super(context);
        initView();
    }

    public AlivcUserInfoListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AlivcUserInfoListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        userInfos = new ArrayList<>();
        LayoutInflater.from(getContext()).inflate(R.layout.alivc_live_room_user_info_layout,null,true);
        RecyclerView userInfoListView = (RecyclerView)findViewById(R.id.userinfo_list_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        userInfoListView.setLayoutManager(linearLayoutManager);
        userListAdapter = new UserListAdapter(getContext(),userInfos);
        userListAdapter.setOnUserInfoClickListener(new OnUserInfoClickListener() {
            @Override
            public void onUserClick(AlivcUserInfo alivcUserInfo) {
                if (onUserlnfoClickListener != null){
                    onUserlnfoClickListener.onUserClick(alivcUserInfo);
                }
            }
        });

        userInfoListView.setAdapter(userListAdapter);

    }

    public void addUsers(final AlivcUserInfo user){

        android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                userInfos.add(firstIndex,user);
                userListAdapter.notifyDataSetChanged();
            }
        });

    }

    public void removeUsers(final AlivcUserInfo user){

        android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (Iterator<AlivcUserInfo> iterator = userInfos.iterator(); iterator.hasNext(); ) {
                    AlivcUserInfo alivcUserInfo = iterator.next();
                    if (user.userId.equals(alivcUserInfo.userId)) {
                        iterator.remove();
                    }
                }

                userListAdapter.notifyDataSetChanged();
            }
        });

    }

    OnUserlnfoClickListener onUserlnfoClickListener;

    public void setOnUserlnfoClickListener(
        OnUserlnfoClickListener onUserlnfoClickListener) {
        this.onUserlnfoClickListener = onUserlnfoClickListener;
    }

    public interface OnUserlnfoClickListener {
        /**
         * 点击用户图像
         * @param info
         */
        void onUserClick(AlivcUserInfo info);
    }
}

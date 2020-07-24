package com.aliyun.alivclive.setting.fragment;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.DialogFragment;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.alivclive.BuildConfig;
import com.aliyun.alivclive.R;
import com.aliyun.alivclive.listener.OnOperateUserListener;
import com.aliyun.alivclive.setting.manager.AlivcLiveUserManager;
import com.aliyun.alivclive.utils.ImageUtils;

/**
 * Created by Akira on 2018/5/30.
 */

public class UserCardDialogFragment extends DialogFragment implements View.OnClickListener {

    private String mAvatarUrl, mUsername, mUserId;
    private ImageView mImAvatar;
    private TextView mTvUsername, mTvUserId;

    private TextView mBtBanChat, mBtKickout, mBtBlacklist;
    private ImageButton mCloseImageButton;
    private boolean isArchor = false;
    private boolean isForbidUser = false;
    private boolean isKickout = false;

    private View mManagerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAvatarUrl = getArguments().getString("avatarUrl");
            mUsername = getArguments().getString("username");
            mUserId = getArguments().getString("userId");
            isArchor = getArguments().getBoolean("isArchor");
            isForbidUser = getArguments().getBoolean("isForbid");
            isKickout = getArguments().getBoolean("isKickout");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View view = inflater.inflate(R.layout.dialog_user_card, container);
        if (BuildConfig.DEBUG) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(mUserId);
                    Toast.makeText(getActivity(), "Copy User ID", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }

        mManagerView = view.findViewById(R.id.layout_bottom);
        if (isArchor && (AlivcLiveUserManager.getInstance().getUserInfo(getActivity())!= null
                && !mUserId.contains(AlivcLiveUserManager.getInstance().getUserInfo(getActivity()).getUserId()))) {
            mManagerView.setVisibility(View.VISIBLE);
        } else {
            mManagerView.setVisibility(View.GONE);
        }
        mImAvatar = (ImageView) view.findViewById(R.id.user_avatar);
        mTvUsername = (TextView) view.findViewById(R.id.user_name);
        mTvUserId = (TextView) view.findViewById(R.id.user_id);

        mBtBanChat = (TextView) view.findViewById(R.id.ban_chat);
        mBtKickout = (TextView) view.findViewById(R.id.kickout);
        mBtBlacklist = (TextView) view.findViewById(R.id.blacklist);
        mCloseImageButton = (ImageButton) view.findViewById(R.id.close);

        mCloseImageButton.setOnClickListener(this);
        mBtBanChat.setOnClickListener(this);
        mBtKickout.setOnClickListener(this);
        mBtBlacklist.setOnClickListener(this);

        if (isKickout) {
            mBtKickout.setEnabled(false);
            mBtKickout.setClickable(false);
            mBtKickout.setTextColor(Color.parseColor("#d8d8d8"));
        }

        if (!TextUtils.isEmpty(mAvatarUrl)) {
            ImageUtils.loadCircleImage(getActivity(), mAvatarUrl, mImAvatar);
        }
        if (!TextUtils.isEmpty(mUsername)) {
            mTvUsername.setText(mUsername);
        }

        if (!isForbidUser) {
            mBtBanChat.setText(R.string.alivc_ban_chat);
        } else {
            mBtBanChat.setText(R.string.alivc_on_chat);
        }


        if (!TextUtils.isEmpty(mUserId)) {
            mTvUserId.setText(String.format("ID:%s", mUserId));
        }

        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, alpha, scaleX, scaleY).setDuration(300).start();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.close) {
            dismissAllowingStateLoss();
        } else if (id == R.id.ban_chat) {
            //禁言
            mOperateListener.forbidChat();
            dismissAllowingStateLoss();
        } else if (id == R.id.kickout) {
            //踢人
            mOperateListener.kickout();
            dismissAllowingStateLoss();
        } else if (id == R.id.blacklist) {
            //拉黑
            mOperateListener.blacklist();
            dismissAllowingStateLoss();
        }
    }

    private OnOperateUserListener mOperateListener;

    public void setOperateListener(OnOperateUserListener listener) {
        mOperateListener = listener;
    }
}

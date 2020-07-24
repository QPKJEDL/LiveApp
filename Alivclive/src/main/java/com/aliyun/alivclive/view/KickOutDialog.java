package com.aliyun.alivclive.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.aliyun.alivclive.R;


/**
 * @author liuli
 *         create on 2018/6/2.
 */

public class KickOutDialog extends Dialog {

    private Button btnConfirm;//确定按钮


    private onConfirmClickListener onConfirmClickListener;//确定按钮被点击了的监听器


    public KickOutDialog(@NonNull Context context) {
        super(context);
    }

    public KickOutDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public KickOutDialog(@NonNull Context context, boolean cancelable,
                         @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setOnConfirmClickListener(KickOutDialog.onConfirmClickListener onConfirmClickListener) {
        this.onConfirmClickListener = onConfirmClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alivc_kick_dialog);
        //初始化界面控件
        initView();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onConfirmClickListener != null) {
                    onConfirmClickListener.onConfirm();
                }
            }
        });

    }


    /**
     * 初始化界面控件
     */
    private void initView() {
        btnConfirm = (Button) findViewById(R.id.yes);
    }


    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onConfirmClickListener {
        void onConfirm();
    }


}

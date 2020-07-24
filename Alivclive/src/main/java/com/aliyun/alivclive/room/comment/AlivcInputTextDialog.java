package com.aliyun.alivclive.room.comment;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.alivclive.R;


/**
 * @author Mulberry
 * @date 2018-04-24
 * 评论输入框
 */
public class AlivcInputTextDialog extends Dialog {

    private TextView btnSendMessage;
    private EditText etInputMessageme;

    private WeakReference<Context> weakReference;
    private InputMethodManager imm;
    private RelativeLayout rlDlg;
    private int mLastDiff = 0;

    public AlivcInputTextDialog(@NonNull Context context) {
        super(context);
        weakReference = new WeakReference<Context>(context);
    }

    public AlivcInputTextDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        weakReference = new WeakReference<Context>(context);
    }

    public AlivcInputTextDialog(@NonNull Context context, boolean cancelable,
                                @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        weakReference = new WeakReference<Context>(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alivc_input_text_dialog);
        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        Window window = getWindow();
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)(weakReference.get())).getWindowManager().getDefaultDisplay().getMetrics(dm);
        window.setLayout(dm.widthPixels, getWindow().getAttributes().height);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        params.dimAmount = 0.0f;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING|WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        setCancelable(true);

        etInputMessageme = (EditText) findViewById(R.id.et_input_message);
        btnSendMessage = (TextView) findViewById(R.id.btn_send_msg);
        imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = etInputMessageme.getText().toString().trim();
                if (!TextUtils.isEmpty(msg)) {

                    mOnTextSendListener.onTextSend(msg);
                    imm.showSoftInput(etInputMessageme, InputMethodManager.SHOW_FORCED);
                    imm.hideSoftInputFromWindow(etInputMessageme.getWindowToken(), 0);
                    etInputMessageme.setText("");
                    dismiss();
                } else {
                    Toast.makeText(weakReference.get(), "input can not be empty!", Toast.LENGTH_LONG).show();
                }
                etInputMessageme.setText(null);
            }
        });

        etInputMessageme.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case KeyEvent.KEYCODE_ENDCALL:
                    case KeyEvent.KEYCODE_ENTER:
                        if (etInputMessageme.getText().length() > 0) {
                            imm.hideSoftInputFromWindow(etInputMessageme.getWindowToken(), 0);
                            dismiss();
                        } else {
                            Toast.makeText(weakReference.get(), "input can not be empty!", Toast.LENGTH_LONG).show();
                        }
                        return true;
                    case KeyEvent.KEYCODE_BACK:
                        dismiss();
                        return false;
                    default:
                        return false;
                }
            }
        });

        etInputMessageme.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return false;
            }
        });

        rlDlg = findViewById(R.id.rl_input_text_layout);
        rlDlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() != R.id.rl_input_text_layout) {
                    dismiss();
                }
            }
        });

        final LinearLayout rldlgview = (LinearLayout) findViewById(R.id.ll_input_view);

        rldlgview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                Rect r = new Rect();
                //获取当前界面可视部分
                getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //获取屏幕的高度
                int screenHeight = getWindow().getDecorView().getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                int heightDifference = screenHeight - r.bottom;

                if (heightDifference <= 0 && mLastDiff > 0) {
                    dismiss();
                }
                mLastDiff = heightDifference;
            }
        });
        rldlgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(etInputMessageme.getWindowToken(), 0);
                dismiss();
            }
        });
    }

    public void setmOnTextSendListener(OnTextSendListener onTextSendListener) {
        this.mOnTextSendListener = onTextSendListener;
    }

    private OnTextSendListener mOnTextSendListener;

    public interface OnTextSendListener {
        void onTextSend(String msg);
    }


    @Override
    public void dismiss() {
        super.dismiss();
        //dismiss之前重置mLastDiff值避免下次无法打开
        mLastDiff = 0;
    }

}

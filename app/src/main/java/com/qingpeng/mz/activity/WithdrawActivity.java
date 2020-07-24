package com.qingpeng.mz.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.qingpeng.mz.R;

public class WithdrawActivity extends AppCompatActivity {
    private MaterialDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
    }


    private void showBindDialog(final int type) {
        if (alertDialog == null) {
            View inflate = View.inflate(this, R.layout.dialog_alert, null);
            TextView tvCansle = (TextView) inflate.findViewById(R.id.tv_cansle);
            TextView tvTx = (TextView) inflate.findViewById(R.id.tv_tx);
            if (type == 1) {
                tvTx.setText("未绑定银行卡，是否去绑定?");
            } else if (type == 2) {
                tvTx.setText("未绑定微信，是否去绑定?");
            } else if (type == 3) {
                tvTx.setText("未绑定支付宝，是否去绑定?");
            } else if (type == 4) {
                tvTx.setText("未设置支付密码，是否去设置?");
            }
            TextView tvOk = (TextView) inflate.findViewById(R.id.tv_ok);
            tvCansle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (alertDialog != null) {
                        alertDialog.dismiss();
                    }
                }
            });
            tvOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (alertDialog != null) {
                        alertDialog.dismiss();
                    }
                }
            });
            alertDialog = new MaterialDialog.Builder(this).customView(inflate, false).show();

        } else {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            } else {
                alertDialog.show();
            }
        }
    }
}

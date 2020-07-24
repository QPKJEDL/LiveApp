package com.qingpeng.mz.okhttp;

import com.bravin.btoast.BToast;
import com.qingpeng.mz.utils.APP;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class MyCall<T> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.code() == 200) {
            onSuccess(call, response);
        } else {
            BToast.error(APP.getContext())
                    .text("服务器超载，请稍后再试")
                    .show();
        }
    }

    protected abstract void onSuccess(Call<T> call, Response<T> response);


    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onError(call, t);
    }

    protected abstract void onError(Call<T> call, Throwable t);

}

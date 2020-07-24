package com.qingpeng.mz.okhttp;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;
    private Context context;

    public GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }


    @Override
    public T convert(ResponseBody value) throws IOException {
        //将返回的json数据储存在String类型的response中
        String response = value.string();
        //将外层的数据解析到APIResponse类型的httpResult中
        APIResponse httpResult = gson.fromJson(response, APIResponse.class);
        //服务端设定0为正确的请求，故在此为判断标准
        if (httpResult.getStatus() == 1) {
            //直接解析，正确请求不会导致json解析异常
            return gson.fromJson(response, type);
        } else if (httpResult.getStatus() == 0) {
            //定义错误响应体，并通过抛出自定义异常传递错误码及错误信息
            ErrorResponse errorResponse = gson.fromJson(response, ErrorResponse.class);
            throw new ResultException(errorResponse.getStatus(), errorResponse.getInfo());
        } else if (httpResult.getStatus() == 2) {
            ErrorResponse errorResponse = gson.fromJson(response, ErrorResponse.class);
//            SpUtils.putString(APP.getContext(), "uid", "");
//            SpUtils.putString(APP.getContext(), "toke", "");
//            SpUtils.putString(APP.getContext(), "userInfo", null);
//            SpUtils.putBoolean(APP.getContext(), "islogin", false);
//            AppManager.getAppManager().finishAllActivity();
//            context.startActivity(new Intent(context, LoginActivity.class));
            throw new ResultException(errorResponse.getStatus(), errorResponse.getInfo());
        }
        return null;
    }
}
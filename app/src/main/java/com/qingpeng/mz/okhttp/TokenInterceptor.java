package com.qingpeng.mz.okhttp;

import com.google.gson.Gson;
import com.qingpeng.mz.utils.APP;
import com.qingpeng.mz.utils.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class TokenInterceptor implements Interceptor {
    public static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        //视讯
        if (!StringUtils.isEmpty(APP.gameToken))
            builder.addHeader("token", APP.gameToken);
        if (!StringUtils.isEmpty(APP.gameuid))
            builder.addHeader("userid", APP.gameuid);
        Response proceed = chain.proceed(builder.build());
//        if (isTokenExpired(proceed)) {
//            String newToken = getNewToken();
//            Request token = chain.request().newBuilder().addHeader("token", newToken).build();
//            return chain.proceed(token);
//        }
        return proceed;
    }

    private static boolean isTokenExpired(Response response) throws IOException {
        ResponseBody body = response.body();
        BufferedSource source = body.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        long contentLength = body.contentLength();
        Charset charset = UTF8;
        MediaType contentType = body.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }
        if (contentLength != 0) {
            String s = buffer.clone().readString(charset);
            Gson gson = new Gson();
            Map<String, Object> content = gson.fromJson(s, Map.class);
            if (((int) ((double) content.get("code"))) == -2 && ((String) content.get("msg")).equals("非法访问")) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static String getNewToken() throws IOException {
//        Call<Token> dt1 = RetrofitCreateHelper.createApi(RedBag.class, RedBag.HOST).getToken("dt1");
//        Token body = dt1.execute().body();
//        if (body.getCode() == 1) {
//            LogUtils.d(body.getMsg() + "" + APP.uid);
//            String md5 = MD5Utils.getMD5(body.getMsg() + "" + APP.uid);
//            APP.Token = md5;
//            return md5;
//        } else {
//            return "";
//        }
        return "";
    }
}

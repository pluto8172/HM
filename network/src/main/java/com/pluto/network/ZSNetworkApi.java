package com.pluto.network;

import android.text.TextUtils;

import com.pluto.network.base.NetworkApi;
import com.pluto.network.beans.BaseResponse;
import com.pluto.network.config.ApiConfig;
import com.pluto.network.config.HttpHeader;
import com.pluto.network.errorhandler.ExceptionHandle;

import java.io.IOException;
import java.text.SimpleDateFormat;

import io.reactivex.functions.Function;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rxtool.RxEncodeTool;
import rxtool.RxLogTool;
import rxtool.RxTimeTool;
import rxtool.SPUtils;
import rxtool.StringEncrypt;

/**
 * 以后就用这个标准的双重检查单利模式
 */
public final class ZSNetworkApi extends NetworkApi {

    private volatile static ZSNetworkApi sInstance;

    private ZSNetworkApi() {

    }

    private static ZSNetworkApi getInstance() {
        if (sInstance == null) {
            synchronized (ZSNetworkApi.class) {
                if (sInstance == null) {
                    sInstance = new ZSNetworkApi();
                }
            }
        }
        return sInstance;
    }

    public static <T> T getService(Class<T> service) {
        return getInstance().getRetrofit(service).create(service);
    }

    @Override
    protected Interceptor getInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request.Builder builder = chain.request().newBuilder();
                String token = SPUtils.getInstance().getString("token");
                String time = RxTimeTool.getCurTimeString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
                String s = "{\"deviceType\":\"3\",\"token\":\"" + token + "\",\"time\":\"" + time + "\"}";
                String sign = RxEncodeTool.sha1(s);

                RxLogTool.i("token = " + token);
                String newSign = "{\"deviceType\":\"3\",\"token\":\"" + token + "\",\"time\":\"" + time + "\",\"sign\":\"" + sign + "\"}";
                String encrypt = null;
                try {
                    encrypt = StringEncrypt.getInstance().encrypt(newSign);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (TextUtils.isEmpty(HttpHeader.HEADER_TEAM_ID_VALUE)) {
                    HttpHeader.HEADER_TEAM_ID_VALUE = HttpHeader.TEAM_ID_DEFAULT;
                }
                builder.addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json;charset=UTF-8")
                        .addHeader("teamId", HttpHeader.HEADER_TEAM_ID_VALUE)
                        .addHeader("isSwitch", "true")
                        .addHeader("User-Sign", encrypt);
                return chain.proceed(builder.build());
            }
        };
    }

    protected <T> Function<T, T> getAppErrorHandler() {
        return new Function<T, T>() {
            @Override
            public T apply(T response) throws Exception {
                //response中code码不会0 出现错误
                if (response instanceof BaseResponse && ((BaseResponse) response).code != 200) {
                    ExceptionHandle.ServerException exception = new ExceptionHandle.ServerException();
                    exception.code = ((BaseResponse) response).code;
                    exception.message = ((BaseResponse) response).message != null ? ((BaseResponse) response).message : "";
                    throw exception;
                }
                return response;
            }
        };
    }

    @Override
    public String getFormal() {
        return ApiConfig.BASE_URL;
    }

    @Override
    public String getTest() {
        return ApiConfig.BASE_URL;
    }
}

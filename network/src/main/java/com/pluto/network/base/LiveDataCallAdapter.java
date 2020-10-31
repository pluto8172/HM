package com.pluto.network.base;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.pluto.network.beans.BaseResponse;
import com.pluto.network.errorhandler.ApiErrorCodeMap;
import com.pluto.network.errorhandler.ErrorCode;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.util.concurrent.atomic.AtomicBoolean;


import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;


public class LiveDataCallAdapter<R> implements CallAdapter<R, LiveData<R>> {

    private static final String TAG = "LiveDataCallAdapter";
    private final Type responseType;

    public LiveDataCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public LiveData<R> adapt(Call<R> call) {
        return new LiveData<R>() {
            AtomicBoolean started = new AtomicBoolean(false);

            @Override
            protected void onActive() {
                super.onActive();
                if (started.compareAndSet(false, true)) {
                    call.enqueue(new Callback<R>() {
                        @Override
                        public void onResponse(Call<R> call, Response<R> response) {
                            R body = response.body();
                            String path = call.request().url().encodedPath();

                            // 当没有信息体时通过 http code 判断业务错误
                            if (body == null && !response.isSuccessful()) {
                                BaseResponse result = new BaseResponse();
                                result.code=(ApiErrorCodeMap.getApiErrorCode(path, response.code()));
                                try {
                                    body = (R) result;
                                } catch (Exception e) {
                                    // 可能部分接口并不是由 result 包裹，此时无法获取错误码
                                }
                            } else if (body instanceof BaseResponse) {
                                BaseResponse result = (BaseResponse) body;
                                // 当请求失败时，转义API错误码到全局错误码
                                if (result.code != 200) {
                                    result.code = (ApiErrorCodeMap.getApiErrorCode(path, result.code));
                                }
                            }
                            postValue(body);
                        }

                        @Override
                        public void onFailure(Call<R> call, Throwable throwable) {
                            Log.d(TAG, "onFailure:" + call.request().url().toString() + ", error:" + throwable.getMessage());
                            if (throwable instanceof ConnectException) {
                                R body = null;
                                BaseResponse result = new BaseResponse();
                                result.code = (ErrorCode.NETWORK_ERROR.getCode());
                                try {
                                    body = (R) result;
                                } catch (Exception e) {
                                    // 可能部分接口并不是由 result 包裹，此时无法获取错误码
                                }
                                postValue(body);
                            } else {
                                postValue(null);
                            }
                        }
                    });
                }
            }
        };
    }
}

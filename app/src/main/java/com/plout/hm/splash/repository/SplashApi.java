package com.plout.hm.splash.repository;

import androidx.lifecycle.LiveData;


import com.xiangxue.network.beans.BaseResponse;

import java.util.List;

import retrofit2.http.GET;

public interface SplashApi {


    //获取地址
    @GET("region/findRegion")
    LiveData<BaseResponse<List<String>>> getAreaInfo();
}

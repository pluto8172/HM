package com.plout.hm.splash.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;


import com.xiangxue.network.ZSNetworkApi;
import com.xiangxue.network.beans.BaseResponse;
import com.xiangxue.network.common.Resource;
import com.xiangxue.network.utils.NetworkOnlyResource;

import java.util.List;

public class SplashRepository {

    private SplashApi service;

    public SplashRepository() {
        service = ZSNetworkApi.getService(SplashApi.class);
    }


    public LiveData<Resource<List<String>>> getAreaListInfo() {
        return new NetworkOnlyResource<List<String>, BaseResponse<List<String>>>() {
            @NonNull
            @Override
            protected LiveData<BaseResponse<List<String>>> createCall() {
                return service.getAreaInfo();
            }
        }.asLiveData();
    }

}

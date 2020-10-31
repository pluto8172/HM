package com.plout.hm.splash.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;


import com.pluto.network.ZSNetworkApi;
import com.pluto.network.beans.BaseResponse;
import com.pluto.network.common.Resource;
import com.pluto.network.utils.NetworkOnlyResource;

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

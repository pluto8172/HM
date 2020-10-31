package com.plout.hm.splash.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;


import com.plout.hm.splash.repository.SplashRepository;
import com.pluto.network.common.Resource;
import com.pluto.network.common.SingleSourceLiveData;


import java.util.List;


public class SplashVM extends AndroidViewModel {

    private SplashRepository repository;

    public SplashVM(@NonNull Application application) {
        super(application);
        repository = new SplashRepository();
    }


    public SingleSourceLiveData<Resource<List<String>>> addressInfoData = new SingleSourceLiveData<Resource<List<String>>>();

    public void requestAreaInfo() {
        addressInfoData.setSource(repository.getAreaListInfo());
    }


}

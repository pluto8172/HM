package com.wl.pluto.arch.mvvm2.model;

public interface MvvmNetworkObserver<F> {
    void onSuccess(F t, boolean isFromCache);
    void onFailure(Throwable e);
}

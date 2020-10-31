package com.wl.pluto.arch.base;


import android.app.Application;

import com.umeng.commonsdk.BuildConfig;
import com.pluto.network.base.INetworkRequiredInfo;

import rxtool.RxAppTool;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class NetworkRequestInfo implements INetworkRequiredInfo {
    private Application mApplication;

    public NetworkRequestInfo(Application application) {
        this.mApplication = application;
    }

    @Override
    public String getAppVersionName() {
        return RxAppTool.getAppVersionName(mApplication);
    }

    @Override
    public String getAppVersionCode() {
        return String.valueOf(RxAppTool.getAppVersionCode(mApplication));
    }

    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Override
    public Application getApplicationContext() {
        return mApplication;
    }

}

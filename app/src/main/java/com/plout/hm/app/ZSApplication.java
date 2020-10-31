package com.plout.hm.app;


import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wl.pluto.arch.BuildConfig;
import com.wl.pluto.arch.base.BaseApplication;


/**
 *
 */

public class ZSApplication extends BaseApplication {

    private static final String TAG = "ZSApplication";


    @Override
    public void onCreate() {
        super.onCreate();
        onChannel();

        initAppData();

        if (BuildConfig.DEBUG) {
            registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                    Log.d("onActivityCreated->", activity.getClass().getSimpleName());
                }

                @Override
                public void onActivityStarted(@NonNull Activity activity) {

                }

                @Override
                public void onActivityResumed(@NonNull Activity activity) {

                }

                @Override
                public void onActivityPaused(@NonNull Activity activity) {

                }

                @Override
                public void onActivityStopped(@NonNull Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(@NonNull Activity activity) {

                }
            });
        }
    }


    private void initAppData() {

    }


    /**
     * 读取渠道号/版本号等信息
     */
    private void onChannel() {
        try {
            ApplicationInfo applicationInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            // String channel = applicationInfo.metaData.getString(getPackageName());
            // SPUtils.getInstance().put("channel", channel);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
    }


}

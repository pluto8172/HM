package com.plout.hm.splash.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.launcher.ARouter;
import com.arch.demo.common.arouter.ARouterModulePath;
import com.plout.hm.R;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wl.pluto.arch.base.activity.BaseActivity;
import com.wl.pluto.arch.constant.SPKey;
import com.wl.pluto.arch.dialog.CostumerDialog;
import com.wl.pluto.arch.utils.PermissionUtils;


import rxtool.SPUtils;

public class AppSplashActivity extends BaseActivity {

    private String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};

    private CostumerDialog mPermissionDialog;

    private RxPermissions rxPermissions;


    @Override
    public int getLayoutId() {
        return R.layout.activity_splash_layout;
    }

    @Override
    protected void initViewModel() {
        super.initViewModel();
    }

    @Override
    public void initView() {
        rxPermissions = new RxPermissions(this);
        handlePermissionLogic();
    }


    @SuppressLint("CheckResult")
    private void handlePermissionLogic() {
        if (checkPermission3(this, permissions)) {
            /*if (RxFileTool.isFileExists(FileUtil.appPath)) {
                RxLogTool.i("---->", "文件路径 = " + FileUtil.appPath)
            } else {
                FileUtil.createDefaultDir()
            }*/

            handleJumpLogic();
        } else {
            requestPermissions();
        }
    }

    /**
     * 请求权限
     */

    private void requestPermissions() {
        rxPermissions.request(permissions).subscribe(
                permission -> {
                    SPUtils.getInstance().put(SPKey.SP_KEY_IS_FIRST_USE, false);
                    if (permission) {
                        //以后这个地方可以用多线程加载
                        // 用户同意所有权限
                        handleJumpLogic();

                    } else {
                        showConfirmDialog("为了您正常使用,需要使用手机存储权限和电话权限.", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                gotoPermissionManager();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        });
                    }
                }


        );
    }

    private final int REQUEST_CODE_FOR_PERMISSION = 30001;

    private void gotoPermissionManager() {
        String brand = Build.BRAND; // 手机厂商
        if (TextUtils.equals(brand.toLowerCase(), "redmi") || TextUtils.equals(brand.toLowerCase(), "xiaomi")) {
            PermissionUtils.gotoMiuiPermission(this, REQUEST_CODE_FOR_PERMISSION); // 小米
        } else if (TextUtils.equals(brand.toLowerCase(), "meizu")) {
            PermissionUtils.gotoMeizuPermission(this, REQUEST_CODE_FOR_PERMISSION);
        } else if (TextUtils.equals(brand.toLowerCase(), "huawei") || TextUtils.equals(brand.toLowerCase(), "honor")) {
            PermissionUtils.gotoHuaweiPermission(this, REQUEST_CODE_FOR_PERMISSION);
        } else {
            startActivityForResult(PermissionUtils.getAppDetailSettingIntent(this), REQUEST_CODE_FOR_PERMISSION);
        }
    }

    /**
     * 显示对话框
     */
    private void showConfirmDialog(String context, View.OnClickListener sureListener, View.OnClickListener cancelListener) {
        if (mPermissionDialog == null) {
            mPermissionDialog = new CostumerDialog.Builder(this, R.style.custom_dialog)
                    .setContentView(R.layout.dialog_confirm_cancel_layout)
                    .setCancelable(false)
                    .create();
        }

        TextView title = mPermissionDialog.findViewById(R.id.tv_title);
        title.setText("授权提示");

        TextView content = mPermissionDialog.findViewById(R.id.tv_content);
        content.setText(context);

        //确定
        TextView sure = mPermissionDialog.findViewById(R.id.tv_sure);
        sure.setOnClickListener(v -> sureListener.onClick(v));

        //取消
        TextView cancel = mPermissionDialog.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(v -> {

                    mPermissionDialog.dismiss();
                    cancelListener.onClick(v);
                }
        );
        mPermissionDialog.show();
    }


    private boolean checkPermission3(Context context, String[] permissions) {

        for (String permission : permissions) {
            int per = ContextCompat.checkSelfPermission(context, permission);
            if (PackageManager.PERMISSION_GRANTED != per) {
                return false;
            }
        }
        return true;
    }

    private void handleJumpLogic() {
        boolean isLogin = SPUtils.getInstance().getBoolean(SPKey.SP_KEY_IS_LOGIN);
        if (isLogin) {
            gotoMainActivity();
        } else {
            gotoLoginActivity();
        }
    }

    private void gotoLoginActivity() {
        ARouter.getInstance().build(ARouterModulePath.PATH_LOGIN_ACTIVITY)
                .withBoolean("intent_extra_need_refresh", true)
                .withInt("INTENT_EXTRA_PAGE_INDEX", 0)
                .navigation();
        finish();
    }

    private void gotoMainActivity() {

        ARouter.getInstance().build(ARouterModulePath.PATH_MAIN_ACTIVITY2)
                .withBoolean("intent_extra_need_refresh", true)
                .withInt("INTENT_EXTRA_PAGE_INDEX", 0)
                .navigation();
        finish();

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        super.initData(savedInstanceState);


    }

}

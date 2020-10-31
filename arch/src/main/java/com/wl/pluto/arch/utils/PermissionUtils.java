package com.wl.pluto.arch.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.wl.pluto.arch.BuildConfig;

/**
 * ================================================
 *
 * @function <p>
 * Created by szy on 2019-08-14
 * ================================================
 */
public class PermissionUtils {

    /**
     * 跳转到miui的权限管理页面
     *
     * @param context 上下文
     */
    public static void gotoMiuiPermission(Activity context, int requestCode) {
        try { // MIUI 8
            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", context.getPackageName());
            context.startActivityForResult(localIntent, requestCode);
        } catch (Exception e) {
            try { // MIUI 5/6/7
                Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                localIntent.putExtra("extra_pkgname", context.getPackageName());
                context.startActivityForResult(localIntent, requestCode);
            } catch (Exception e1) { // 否则跳转到应用详情
                context.startActivityForResult(getAppDetailSettingIntent(context), requestCode);
            }
        }
    }

    /**
     * 跳转到魅族的权限管理系统
     *
     * @param context 上下文
     */
    public static void gotoMeizuPermission(Activity context, int requestCode) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            //intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            context.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
            context.startActivityForResult(getAppDetailSettingIntent(context), requestCode);
        }
    }

    /**
     * 华为的权限管理页面
     *
     * @param context 上下文
     */
    public static void gotoHuaweiPermission(Activity context, int requestCode) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            intent.setComponent(comp);
            context.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
            context.startActivityForResult(getAppDetailSettingIntent(context), requestCode);
        }

    }

    /**
     * 获取应用详情页面intent（如果找不到要跳转的界面，也可以先把用户引导到系统设置页面）
     *
     * @param context 上下文
     * @return
     */
    public static Intent getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        return localIntent;
    }
}

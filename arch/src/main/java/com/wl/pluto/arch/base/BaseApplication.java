package com.wl.pluto.arch.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.download.DownloadListener;
import com.tencent.bugly.beta.download.DownloadTask;
import com.tencent.bugly.beta.upgrade.UpgradeStateListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.wl.pluto.arch.R;
import com.wl.pluto.arch.base.activity.AbstractBaseActivity;
import com.wl.pluto.arch.constant.AppConstant;
import com.wl.pluto.arch.constant.SPKey;
import com.pluto.network.base.NetworkApi;
import com.pluto.network.errorhandler.ErrorCode;

import imageloader.libin.com.images.loader.ImageLoader;
import rxtool.RxTool;
import rxtool.SPUtils;
import rxtool.StringEncrypt;

import static com.umeng.commonsdk.utils.UMUtils.isDebug;

public class BaseApplication extends Application {

    private static Context context;


    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull com.scwang.smart.refresh.layout.api.RefreshLayout layout) {
                //全局设置主题颜色  .setTimeFormat(new DynamicTimeFormat("更新于 %s"))
                layout.setPrimaryColorsId(R.color.white, R.color.black);
                //指定为经典Header，默认是 贝塞尔雷达Header
                return new ClassicsHeader(context);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsFooter(context).setDrawableSize(20f);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        //初始化友盟
        initUMeng();
        ARouter.init(this);
        if (isDebug(this)) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        initComponent();
        initConstantValue();

    }

    public boolean isMainProcess() {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo item : activityManager.getRunningAppProcesses()) {
            if (item.pid == pid) {
                return getApplicationInfo().packageName == item.processName;
            }
        }
        return false;
    }

    public static Context getContext() {
        return context;
    }

    private void initComponent() {


        RxTool.init(this);
        ErrorCode.init(this);

        NetworkApi.init(new NetworkRequestInfo(this));
        ImageLoader.init(this);
        //初始化本地数据库
        //GreenDao.initialize(getApplicationContext(), "cqzs.db");

        initQMUI();

        initBugly();


    }


    /**
     * 初始化友盟
     */
    private void initUMeng() {
        UMConfigure.init(this, "5f093781dbc2ec07204a7d8b", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
        UMConfigure.setLogEnabled(true);
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMConfigure.setEncryptEnabled(true);
        UMShareAPI.get(this).setShareConfig(config);
        PlatformConfig.setWeixin("wx5a9703c19b2ca731", "31f3b6554ec7f8dc0c3fba6a6da75eb1");
//        PlatformConfig.setSinaWeibo("1365332436", "458e8bf08ddba934f270c2c0f36bad8f", "http://sns.whalecloud.com");
//        PlatformConfig.setQQZone("1107422803", "3OQXCO08w5nIQcAC");
    }


    private void initBugly() {
        /**** Beta高级设置 */
        /**
         * true表示app启动自动初始化升级模块；
         * false不好自动初始化
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false
         * 在后面某个时刻手动调用
         */
        com.tencent.bugly.beta.Beta.autoInit = true;
        /**
         * true表示初始化时自动检查升级
         * false表示不会自动检查升级，需要手动调用Beta.checkUpgrade()方法
         */
        com.tencent.bugly.beta.Beta.autoCheckUpgrade = true;
        /**
         * 设置升级周期为60s（默认检查周期为0s），60s内SDK不重复向后天请求策略
         */
        com.tencent.bugly.beta.Beta.initDelay = 1 * 1000l;
        /**
         * 设置通知栏大图标，largeIconId为项目中的图片资源；
         */
        com.tencent.bugly.beta.Beta.largeIconId = R.mipmap.ic_launcher;
        /**
         * 设置状态栏小图标，smallIconId为项目中的图片资源id;
         */
        com.tencent.bugly.beta.Beta.smallIconId = R.mipmap.ic_launcher;
        /**
         * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
         * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
         */
        com.tencent.bugly.beta.Beta.defaultBannerId = R.mipmap.ic_launcher;
        /**
         * 设置sd卡的Download为更新资源保存目录;
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         */
        com.tencent.bugly.beta.Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        /**
         * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        com.tencent.bugly.beta.Beta.showInterruptedStrategy = false;
        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
         * 不设置会默认所有activity都可以显示弹窗;
         */
        com.tencent.bugly.beta.Beta.canShowUpgradeActs.add(AbstractBaseActivity.class);
        /**
         * 设置自定义升级对话框UI布局
         * 注意：因为要保持接口统一，需要用户在指定控件按照以下方式设置tag，否则会影响您的正常使用：
         * 标题：beta_title，如：android:tag="beta_title"
         * 升级信息：beta_upgrade_info  如： android:tag="beta_upgrade_info"
         * 更新属性：beta_upgrade_feature 如： android:tag="beta_upgrade_feature"
         * 取消按钮：beta_cancel_button 如：android:tag="beta_cancel_button"
         * 确定按钮：beta_confirm_button 如：android:tag="beta_confirm_button"
         * 详见layout/upgrade_dialog.xml
         */
//        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog;
        /**
         * 设置自定义tip弹窗UI布局
         * 注意：因为要保持接口统一，需要用户在指定控件按照以下方式设置tag，否则会影响您的正常使用：
         * 标题：beta_title，如：android:tag="beta_title"
         * 提示信息：beta_tip_message 如： android:tag="beta_tip_message"
         * 取消按钮：beta_cancel_button 如：android:tag="beta_cancel_button"
         * 确定按钮：beta_confirm_button 如：android:tag="beta_confirm_button"
         * 详见layout/tips_dialog.xml
         */
//        Beta.tipsDialogLayoutId = R.layout.tips_dialog;
        /**
         * 如果想监听升级对话框的生命周期事件，可以通过设置OnUILifecycleListener接口
         * 回调参数解释：
         * context - 当前弹窗上下文对象
         * view - 升级对话框的根布局视图，可通过这个对象查找指定view控件
         * upgradeInfo - 升级信息
         */
        /*  Beta.upgradeDialogLifecycleListener = new UILifecycleListener<UpgradeInfo>() {
            @Override
            public void onCreate(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onCreate");
                // 注：可通过这个回调方式获取布局的控件，如果设置了id，可通过findViewById方式获取，如果设置了tag，可以通过findViewWithTag，具体参考下面例子:

                // 通过id方式获取控件，并更改imageview图片
                ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
                imageView.setImageResource(R.mipmap.ic_launcher);

                // 通过tag方式获取控件，并更改布局内容
                TextView textView = (TextView) view.findViewWithTag("textview");
                textView.setText("my custom text");

                // 更多的操作：比如设置控件的点击事件
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), OtherActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onStart(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onStart");
            }

            @Override
            public void onResume(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onResume");
            }

            @Override
            public void onPause(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onPause");
            }

            @Override
            public void onStop(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onStop");
            }

            @Override
            public void onDestroy(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onDestory");
            }
        };*/
        /**
         * 自定义Activity参考，通过回调接口来跳转到你自定义的Actiivty中。
         */
        /* Beta.upgradeListener = new UpgradeListener() {

            @Override
            public void onUpgrade(int ret, UpgradeInfo strategy, boolean isManual, boolean isSilence) {
                if (strategy != null) {
                    Intent i = new Intent();
                    i.setClass(getApplicationContext(), UpgradeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "没有更新", Toast.LENGTH_SHORT).show();
                }
            }
        };*/

        //监听安装包下载状态
        com.tencent.bugly.beta.Beta.downloadListener = new DownloadListener() {
            @Override
            public void onReceive(DownloadTask downloadTask) {

            }

            @Override
            public void onCompleted(DownloadTask downloadTask) {

            }

            @Override
            public void onFailed(DownloadTask downloadTask, int i, String s) {

            }
        };

        //监听APP升级状态
        com.tencent.bugly.beta.Beta.upgradeStateListener = new UpgradeStateListener() {
            @Override
            public void onUpgradeFailed(boolean b) {

            }

            @Override
            public void onUpgradeSuccess(boolean b) {

            }

            @Override
            public void onUpgradeNoVersion(boolean b) {

            }

            @Override
            public void onUpgrading(boolean b) {

            }

            @Override
            public void onDownloadCompleted(boolean b) {

            }
        };
        /**
         * 已经接入Bugly用户改用上面的初始化方法,不影响原有的crash上报功能;
         * init方法会自动检测更新，不需要再手动调用Beta.checkUpdate(),如需增加自动检查时机可以使用Beta.checkUpdate(false,false);
         * 参数1： applicationContext
         * 参数2：appId
         * 参数3：是否开启debug
         */
        Bugly.init(getApplicationContext(), "43088f4d49", false);
        /**
         * 如果想自定义策略，按照如下方式设置
         */
        /***** Bugly高级设置  */
        //        BuglyStrategy strategy = new BuglyStrategy();
        /**
         * 设置app渠道号
         */
        //        strategy.setAppChannel(APP_CHANNEL);

        //        Bugly.init(getApplicationContext(), APP_ID, true, strategy);
    }

    private void initQMUI() {


        //qmui
        //QDUpgradeManager.getInstance(this).check()
        QMUISwipeBackActivityManager.init(this);


        //QDSkinManager.install(this);
        //QMUISkinMaker.init(context, new String[]{"com.qmuiteam.qmuidemo"}, new String[]{"app_skin_"}, R.attr.class);
    }

    /**
     * 初始化渠道信息
     */
    /* private void initChannelData() {
         val channelInfo = WalleChannelReader.getChannelInfo(this.applicationContext)
         if (channelInfo == null) {
             appChannel = getString(R.string.app_channel)
             appKey = getString(R.string.app_key)
         } else {
             val extraInfo = channelInfo.extraInfo
             appChannel = channelInfo.channel
             appKey = extraInfo["app_key"]
         }
         SPUtils.getInstance().put(Constant.appKey, appKey)
         SPUtils.getInstance().put(Constant.appChannel, appChannel)

     }*/
    private void initConstantValue() {
        try {
            StringEncrypt.getInstance().encrypt("123456");
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppConstant.APP_USER_ID = SPUtils.getInstance().getString(SPKey.SP_KEY_USER_ID);
        //User user = GreenDao.getSession().getUserDao().load(AppConstant.APP_USER_ID);

//        if (user != null && !TextUtils.isEmpty(user.teamId)) {
//            HttpHeader.HEADER_TEAM_ID_VALUE = user.teamId;
//        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

}

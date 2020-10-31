package com.wl.pluto.arch.base.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.gyf.immersionbar.ImmersionBar;
import com.qmuiteam.qmui.arch.QMUIFragmentActivity;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.wl.pluto.arch.R;
import com.wl.pluto.arch.base.ibase.IBaseAction;
import com.wl.pluto.arch.dialog.CostumerDialog;
import com.wl.pluto.arch.listener.OnOpenItemClick;
import com.wl.pluto.arch.model.DialogModel;
import com.wl.pluto.arch.mvvm2.viewmodel.ViewStatus;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rxtool.RxBarTool;
import rxtool.view.RxToast;


/**
 * 该Activity今后会作为项目的唯一一个BaseActivity
 * 主要功能：
 * 1 绑定布局，
 * 2 处理状态栏
 * 3 提供一些通用的方法，如加载对话框。
 */
public abstract class AbstractBaseActivity extends QMUIFragmentActivity implements IBaseAction, Observer, ViewStatus {


    public final String TAG = this.getClass().getSimpleName();
    private boolean mEnableListenKeyboardState = false;

    private TimerHandler handler;
    private long lastClickTime;
    private Unbinder unbinder;

    private static final int MSG_DIALOG_SHOW_TIME = 1001;
    private static final int DIALOG_SHOW_TIME = 10000;

    protected abstract void onRetryBtnClick();

    protected abstract void initViewModel();

    private class TimerHandler extends Handler {

        private WeakReference<AbstractBaseActivity> weakReference;

        public TimerHandler(AbstractBaseActivity activity) {
            weakReference = new WeakReference<>(activity);

        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.what == MSG_DIALOG_SHOW_TIME) {
                if (System.currentTimeMillis() - dialogCreateTime > DIALOG_SHOW_TIME) {
                    AbstractBaseActivity baseActivity = weakReference.get();
                    if (baseActivity != null) {
                        baseActivity.hindLoadingDialog();
                        baseActivity.showToast("网络超时，请稍后重试");
                    }
                } else {
                    handler.sendEmptyMessageDelayed(MSG_DIALOG_SHOW_TIME, 1000);
                }
            }
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    public boolean isNeedEventBus() {
        return false;
    }


    protected String getActivityTag() {
        return getClass().getSimpleName();
    }


    @Override
    final protected void onCreate(@Nullable Bundle savedInstanceState) {
        //修复部分 Android 8.0 手机在TargetSDK 大于 26 时，在透明主题时指定 Activity 方向时崩溃的问题
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            fixOrientation();
        }
        super.onCreate(savedInstanceState);
        // 清除已存在的 Fragment 防止因没有复用导致叠加显示
        clearAllFragmentExistBeforeCreate();

        if (!isFullScreen()) {
            ImmersionBar.with(this)
                    .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                    .statusBarColor(R.color.black)
                    .statusBarDarkFont(false)
                    .init();
        }

        //设置布局
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        handler = new TimerHandler(this);



        if (isNeedEventBus()) {
            EventBus.getDefault().register(this);
        }

        //初始化ViewModel
        initViewModel();
        //初始化布局
        initView();

        initData(savedInstanceState);
    }


    /**
     * 清除所有已存在的 Fragment 防止因重建 Activity 时，前 Fragment 没有销毁和重新复用导致界面重复显示
     * 如果有自己实现 Fragment 的复用，请复写此方法并不实现内容
     */
    public void clearAllFragmentExistBeforeCreate() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments.size() == 0) {
            return;
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment : fragments) {
            fragmentTransaction.remove(fragment);
        }
        fragmentTransaction.commitNow();
    }

    /**
     * 是否隐藏状态栏全屏
     *
     * @return
     */
    protected boolean isFullScreen() {
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mEnableListenKeyboardState) {
            addKeyboardStateListener();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeKeyBoardStateListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //移除所有
        handler.removeCallbacksAndMessages(null);

        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }

    /**
     * 隐藏键盘
     */
    public void hideInputKeyboard() {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    /**
     * 设置沉浸式状态栏
     */
    public void setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 隐藏导航键
     */
    public void hideNavigationBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**
     * 启动键盘状态监听
     *
     * @param enable
     */
    public void enableKeyboardStateListener(boolean enable) {
        mEnableListenKeyboardState = enable;
    }

    /**
     * 添加键盘显示监听
     */
    private void addKeyboardStateListener() {
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(onKeyboardStateChangedListener);
    }

    /**
     * 移除键盘显示监听
     */
    private void removeKeyBoardStateListener() {
        getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(onKeyboardStateChangedListener);
    }

    /**
     * 监听键盘显示状态
     */
    private ViewTreeObserver.OnGlobalLayoutListener onKeyboardStateChangedListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        int mScreenHeight = 0;
        boolean isCurrentActive = false;

        private int getScreenHeight() {
            if (mScreenHeight > 0) {
                return mScreenHeight;
            }
            Point point = new Point();
            ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(point);
            mScreenHeight = point.y;
            return mScreenHeight;
        }

        @Override
        public void onGlobalLayout() {
            Rect rect = new Rect();
            // 获取当前窗口显示范围
            getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            int screenHeight = getScreenHeight();
            int keyboardHeight = screenHeight - rect.bottom; // 输入法的高度
            boolean isActive = false;
            if (Math.abs(keyboardHeight) > screenHeight / 4) {
                isActive = true; // 超过屏幕1/4则表示弹出了输入法
            }

            if (isCurrentActive != isActive) {
                isCurrentActive = isActive;
                onKeyboardStateChanged(isActive, keyboardHeight);
            }
        }
    };

    /**
     * 当软键盘显示时回调
     * 此回调在调用{@link AbstractBaseActivity#enableKeyboardStateListener(boolean)}启用监听键盘显示
     *
     * @param isShown
     * @param height
     */
    public void onKeyboardStateChanged(boolean isShown, int height) {

    }

    /**
     * 判断当前主题是否是透明悬浮
     *
     * @return
     */
    private boolean isTranslucentOrFloating() {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    /**
     * 改变当前的 Activity 的显示方向
     * 解决当前Android 8.0 系统在透明主题时设定显示方向时崩溃的问题
     *
     * @return
     */
    private boolean fixOrientation() {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        /*
         * 修复 Android 8.0 手机在TargetSDK 大于 26 时，指定 Activity 方向时崩溃的问题
         */
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            return;
        }
        super.setRequestedOrientation(requestedOrientation);
    }


    public void showToast(String text) {
        RxToast.info(text);
    }

    public void showToast(int resId) {
        showToast(getString(resId));
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        recreate();
    }

    // 记录dialog 显示创建时间
    private long dialogCreateTime;

    private QMUITipDialog tipDialog;

    public void showLoadingDialog(String content) {
        if (tipDialog == null) {
            tipDialog = new QMUITipDialog.Builder(this)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord(content)
                    .create();

        }
        dialogCreateTime = System.currentTimeMillis();
        if(tipDialog != null && !tipDialog.isShowing()){
            tipDialog.show();
        }
        //handler.sendEmptyMessage(MSG_DIALOG_SHOW_TIME);
    }


    public void showLoadingDialog() {
        showLoadingDialog("正在加载");
    }

    public void hindLoadingDialog(Runnable runnable) {
        // handler.removeMessages(MSG_DIALOG_SHOW_TIME);
        if (tipDialog != null) {

            if (tipDialog.isShowing()) {
                if (System.currentTimeMillis() - dialogCreateTime < 500) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (runnable != null) {
                                runnable.run();
                            }
                            if (tipDialog != null) {
                                tipDialog.dismiss();
                                tipDialog = null;
                            }
                        }
                    }, 1000);
                } else {
                    tipDialog.dismiss();
                    tipDialog = null;
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            }
        }
    }

    /**
     * 取消加载dialog
     */
    public void hindLoadingDialog() {
        hindLoadingDialog(null);
    }


    /**
     * 为防止多次重复点击
     *
     * @return
     */
    public synchronized boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    @Override
    public void onChanged(Object o) {

    }


    /**
     * 通用dialog
     *
     * @param title         标题
     * @param content       内容
     * @param onEnsureClick 确定点击
     * @param onCancelClick 取消点击
     */
    public void showNormalDialog(String title, String content, final View.OnClickListener onEnsureClick, final View.OnClickListener onCancelClick) {

//        QMUIDialog.CustomDialogBuilder dialogBuilder = new QMUIDialog.CustomDialogBuilder(this);
//        dialogBuilder.setSkinManager(QMUISkinManager.defaultInstance(this));
//        dialogBuilder.setLayout(R.layout.dialog_praise_layout);
//        final QMUIDialog dialog = dialogBuilder.setTitle(title).create(R.style.custom_dialog);
//        dialog.show();

        CostumerDialog dialog = new CostumerDialog.Builder(this, R.style.custom_dialog)
                .setContentView(R.layout.dialog_praise_layout)
                .setCancelable(true)
                .setOutTouchCancelable(true)
                .create();

        dialog.show();
        AppCompatTextView tvTitle = dialog.findViewById(R.id.tv_title);
        AppCompatTextView tvContent = dialog.findViewById(R.id.tv_content);
        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
        tvContent.setText(content);
        dialog.findViewById(R.id.tv_ensure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEnsureClick != null) {
                    onEnsureClick.onClick(v);
                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCancelClick != null) {
                    onCancelClick.onClick(v);
                }
                dialog.dismiss();
            }
        });
    }

    /**
     * 通用dialog
     *
     * @param title   标题
     * @param content 内容
     */
    public void showNormalDialogWithOneButton(String title, String content) {
        showNormalDialogWithOneButton(title, content, null);
    }

    /**
     * 通用dialog
     *
     * @param title   标题
     * @param content 内容
     */
    public void showNormalDialogWithOneButton(String title, String content, final View.OnClickListener onEnsureClick) {

//        QMUIDialog.CustomDialogBuilder dialogBuilder = new QMUIDialog.CustomDialogBuilder(this);
//        dialogBuilder.setSkinManager(QMUISkinManager.defaultInstance(this));
//        dialogBuilder.setLayout(R.layout.dialog_with_one_button_layout);
//        final QMUIDialog dialog = dialogBuilder.setTitle(title).create(R.style.custom_dialog);
//        dialog.show();

        CostumerDialog dialog = new CostumerDialog.Builder(this, R.style.custom_dialog)
                .setContentView(R.layout.dialog_with_one_button_layout)
                .setCancelable(true)
                .setOutTouchCancelable(true)
                .create();

        dialog.show();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        AppCompatTextView tvTitle = dialog.findViewById(R.id.tv_title);
        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
        AppCompatTextView tvContent = dialog.findViewById(R.id.tv_content);
        tvContent.setText(content);
        dialog.findViewById(R.id.tv_ensure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEnsureClick != null) {
                    onEnsureClick.onClick(v);
                }
                dialog.dismiss();
            }
        });
    }

    public void showBottomSheet(DialogModel ds, final OnOpenItemClick onOpenItemClick) {
        List<String> data = new ArrayList<>();

        if (ds.getItems() == null) {
            data.addAll(ds.getList());
        } else {
            Collections.addAll(data, ds.getItems());
        }

        showSimpleBottomSheetList(
                true, true, false, null,
                data, false, false, onOpenItemClick);

    }


    private void showSimpleBottomSheetList(boolean gravityCenter,
                                           boolean addCancelBtn,
                                           boolean withIcon,
                                           CharSequence title,
                                           List<String> itemList,
                                           boolean allowDragDismiss,
                                           boolean withMark,
                                           final OnOpenItemClick onOpenItemClick) {

        QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(this);
        builder.setGravityCenter(gravityCenter)
                .setSkinManager(QMUISkinManager.defaultInstance(this))
                .setTitle(title)
                .setAddCancelBtn(addCancelBtn)
                .setAllowDrag(allowDragDismiss)
                .setNeedRightMark(withMark)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        if (onOpenItemClick != null) {
                            onOpenItemClick.onOpenItemClick(null, itemView, position, 0l);
                            dialog.dismiss();
                        }
                    }
                });
        if (withMark) {
            builder.setCheckedIndex(40);
        }
        for (String item : itemList) {
            if (withIcon) {
                //builder.addItem(ContextCompat.getDrawable(this, R.mipmap.icon_tabbar_lab), "Item " + i);
            } else {
                builder.addItem(item);
            }

        }
        builder.build().show();
    }
}

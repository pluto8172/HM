package com.wl.pluto.arch.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.wl.pluto.arch.base.activity.AbstractBaseActivity;
import com.wl.pluto.arch.base.ibase.IBaseAction;
import com.wl.pluto.arch.mvvm2.viewmodel.ViewStatus;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 主要功能：
 * 1 绑定布局，
 * 2 处理状态栏
 * 3 提供一些通用的方法，如加载对话框。
 */
public abstract class AbstractBaseFragment extends QMUIFragment implements IBaseAction, Observer, ViewStatus, View.OnClickListener {

    protected String TAG = this.getClass().getSimpleName();

    private Unbinder unbinder;
    private TimerHandler handler;
    protected AbstractBaseActivity mActivity;

    private class TimerHandler extends Handler {
        private WeakReference<AbstractBaseFragment> weakReference;

        public TimerHandler(AbstractBaseFragment fragment) {
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    }


    public abstract void initViewModel();

    protected abstract void onRetryBtnClick();


    /***
     *   初始化参数
     */
    protected void initParameters() {
    }


    protected String getFragmentTag() {
        return getClass().getSimpleName();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (AbstractBaseActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNeedEventBus()) {
            EventBus.getDefault().register(this);
        }
        handler = new TimerHandler(this);
        initParameters();
        setRetainInstance(true);
    }


    public abstract View onCustomContentView();

    @Override
    protected View onCreateView() {
        int layoutResId = getLayoutId();
        View contentView;
        if (layoutResId > 0) {
            contentView = View.inflate(getContext(), layoutResId, null);
        } else {
            contentView = onCustomContentView();
        }
        unbinder = ButterKnife.bind(this, contentView);
        return contentView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        initView();
        initData(savedInstanceState);
    }


    /**
     * 通过 id 查询当前布局的 View 控件。
     *
     * @param id  控件 id
     * @param <T> 控件类型
     * @return 返回对应的控件
     */
    public <T extends View> T findView(int id) {
        return findView(id, false);
    }

    /**
     * 通过 id 查询当前布局的 View 控件。并是否对此控件设置点击监听
     *
     * @param id      控件 id
     * @param isClick 是否设置点击点击监听
     * @param <T>     控件类型
     * @return 返回对应的控件
     */
    public <T extends View> T findView(int id, boolean isClick) {
        View viewById = getView().findViewById(id);
        if (isClick) {
            viewById.setOnClickListener(this);
        }
        return (T) viewById;
    }

    @Override
    public void onClick(View v) {
        onClick(v, v.getId());
    }


    /**
     * 通过 id 查询当前布局的 View 控件。并是否对此控件设置点击监听
     *
     * @param view    当前的布局
     * @param id      控件 id
     * @param isClick 是否设置点击点击监听
     * @param <T>     控件类型
     * @return 返回对应的控件
     */
    public <T extends View> T findView(View view, int id, boolean isClick) {
        View viewById = view.findViewById(id);
        if (isClick) {
            viewById.setOnClickListener(this);
        }
        return (T) viewById;
    }


    /**
     * 点击监听，当通过{@link #findView(int, boolean)} 方法， 当设置为true ， 设置点击监听时， 则会通过此
     * 方法进行监听回调。
     *
     * @param v  监听的 view 控件
     * @param id 控件的 ID
     */
    protected void onClick(View v, int id) {
    }


    public void showLoadingDialog() {
        showLoadingDialog("正在加载");
    }

    public void showLoadingDialog(String content) {
        if (mActivity != null) {
            mActivity.showLoadingDialog(content);
        }
    }


    /**
     * 取消加载dialog
     */
    public void hindLoadingDialog() {
        if (mActivity != null) {
            mActivity.hindLoadingDialog();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //移除所有
        handler.removeCallbacksAndMessages(null);

        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }

        if (isNeedEventBus()) {
            EventBus.getDefault().unregister(this);
        }

    }

    @Override
    public boolean isNeedEventBus() {
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
        mActivity.showNormalDialog(title, content, onEnsureClick, onCancelClick);
    }



}

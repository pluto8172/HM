package com.wl.pluto.arch.mvvm2.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;

import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.wl.pluto.arch.R;
import com.wl.pluto.arch.mvvm2.loadsir.EmptyCallback;
import com.wl.pluto.arch.mvvm2.loadsir.ErrorCallback;
import com.wl.pluto.arch.mvvm2.loadsir.LoadingCallback;
import com.wl.pluto.arch.mvvm2.viewmodel.MvvmBaseViewModel;
import com.wl.pluto.arch.mvvm2.viewmodel.ViewStatus;

import rxtool.view.RxToast;

/**
 * Created by plout on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public abstract class MvvmActivity<V extends ViewDataBinding, VM extends MvvmBaseViewModel> extends AppCompatActivity implements Observer, ViewStatus {
    protected VM viewModel;
    private LoadService mLoadService;
   // protected V viewDataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
        if (viewModel != null) {
            getLifecycle().addObserver(viewModel);
        }
    }

    private void initViewModel() {
        viewModel = getViewModel();
    }

    public void setLoadSir(View view) {
        // You can change the callback on sub thread directly.
        mLoadService = LoadSir.getDefault().register(view, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                onRetryBtnClick();
            }
        });
    }

    protected abstract void onRetryBtnClick();

    protected abstract VM getViewModel();

    public abstract int getBindingVariable();

    public abstract @LayoutRes
    int getLayoutId();

  /*  private void performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        this.viewModel = viewModel == null ? getViewModel() : viewModel;
        if (getBindingVariable() > 0) {
            viewDataBinding.setVariable(getBindingVariable(), viewModel);
        }
        viewDataBinding.executePendingBindings();
    }*/

    @Override
    public void onChanged(Object o) {
        if (o instanceof ViewStatus && mLoadService != null) {
            if (ViewStatus.LOADING.equals(o)) {
                mLoadService.showCallback(LoadingCallback.class);
            } else if (ViewStatus.EMPTY.equals(o)) {
                mLoadService.showCallback(EmptyCallback.class);
            } else if (ViewStatus.SHOW_CONTENT.equals(o)) {
                mLoadService.showSuccess();
            } else if (ViewStatus.NO_MORE_DATA.equals(o)) {
                RxToast.info(getString(R.string.no_more_data));
            } else if (ViewStatus.REFRESH_ERROR.equals(o)) {
                if (((ObservableArrayList) viewModel.dataList.getValue()).size() == 0) {
                    mLoadService.showCallback(ErrorCallback.class);
                } else {
                    RxToast.info(viewModel.errorMessage.getValue().toString());
                }
            } else if (ViewStatus.LOAD_MORE_FAILED.equals(o)) {
                RxToast.info(viewModel.errorMessage.getValue().toString());
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(getActivityTag(), "Activity:" + this + ": " + "onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(getActivityTag(), "Activity:" + this + ": " + "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(getActivityTag(), "Activity:" + this + ": " + "onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(getActivityTag(), "Activity:" + this + ": " + "onDestroy");
    }

    protected String getActivityTag() {
        return getClass().getSimpleName();
    }
}

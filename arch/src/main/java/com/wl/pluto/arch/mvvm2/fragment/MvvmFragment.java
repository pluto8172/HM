package com.wl.pluto.arch.mvvm2.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
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
public abstract class MvvmFragment<V extends ViewDataBinding, VM extends MvvmBaseViewModel, D> extends Fragment implements Observer, ViewStatus {
    protected VM viewModel;
    protected V viewDataBinding;
    protected String mFragmentTag = "";
    private LoadService mLoadService;

    public abstract int getBindingVariable();

    @LayoutRes
    public abstract int getLayoutId();

    public abstract VM getViewModel();

    public abstract void onListItemInserted(ObservableList<D> sender);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onCreate");
        initParameters();
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onCreateView");
        return viewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onViewCreated");
        viewModel = getViewModel();
        getLifecycle().addObserver(viewModel);
        viewModel.dataList.observe(getViewLifecycleOwner(), this);
        viewModel.viewStatusLiveData.observe(getViewLifecycleOwner(), this);
        if (getBindingVariable() > 0) {
            viewDataBinding.setVariable(getBindingVariable(), viewModel);
            viewDataBinding.executePendingBindings();
        }
    }

    /***
     *   初始化参数
     */
    protected void initParameters() {
    }

    protected abstract void onRetryBtnClick();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onActivityCreated");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(getContext());
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onDetach");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onResume");
    }

    @Override
    public void onDestroy() {
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onDestroyView");
        super.onDestroyView();
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

    protected abstract String getFragmentTag();

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
        } else if (o instanceof ObservableArrayList) {
            onListItemInserted((ObservableArrayList<D>) o);
        }
    }

    protected void showSuccess() {
        if (mLoadService != null) {
            mLoadService.showSuccess();
        }
    }

    protected void showLoading() {
        if (mLoadService != null) {
            mLoadService.showCallback(LoadingCallback.class);
        }
    }
}

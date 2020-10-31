package com.wl.pluto.arch.mvvm2.viewmodel;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.wl.pluto.arch.mvvm2.model.IBaseModelListener;
import com.wl.pluto.arch.mvvm2.model.MvvmBaseModel;
import com.wl.pluto.arch.mvvm2.model.PagingResult;

import java.util.List;

/**
 * Created by plout on 2017/7/20.
 */
public abstract class MvvmBaseViewModel<M extends MvvmBaseModel, D> extends ViewModel implements LifecycleObserver, IBaseModelListener<List<D>> {
    protected M model;
    public MutableLiveData<ObservableList<D>> dataList = new MutableLiveData();
    public MutableLiveData<String> viewStatusLiveData = new MutableLiveData();
    public MutableLiveData<String> errorMessage = new MutableLiveData();

    public MvvmBaseViewModel() {
        dataList.setValue(new ObservableArrayList<>());
        viewStatusLiveData.setValue(ViewStatus.LOADING);
        errorMessage.setValue("");
    }

    public void tryToRefresh() {
        if (model != null) {
            model.refresh();
        }
    }

    @Override
    protected void onCleared() {
        if (model != null) {
            model.cancel();
        }
    }

    @Override
    public void onLoadFinish(MvvmBaseModel model, List<D> data, PagingResult... pagingResult) {
        if (model == this.model) {
            if (model.isPaging()) {
                if (pagingResult[0].isFirstPage) {
                    dataList.getValue().clear();
                }
                if (pagingResult[0].isEmpty) {
                    if (pagingResult[0].isFirstPage) {
                        viewStatusLiveData.setValue(ViewStatus.EMPTY);
                    } else {
                        viewStatusLiveData.setValue(ViewStatus.NO_MORE_DATA);
                    }
                } else {
                    dataList.getValue().addAll(data);
                    dataList.postValue(dataList.getValue());
                    viewStatusLiveData.setValue(ViewStatus.SHOW_CONTENT);
                }
            } else {
                dataList.getValue().clear();
                dataList.getValue().addAll(data);
                dataList.postValue(dataList.getValue());
                viewStatusLiveData.setValue(ViewStatus.SHOW_CONTENT);
            }
            viewStatusLiveData.postValue(viewStatusLiveData.getValue());
        }
    }

    @Override
    public void onLoadFail(MvvmBaseModel model, String prompt, PagingResult... pagingResult) {
        errorMessage.setValue(prompt);
        if(model.isPaging() && !pagingResult[0].isFirstPage) {
            viewStatusLiveData.setValue(ViewStatus.LOAD_MORE_FAILED);
        } else {
            viewStatusLiveData.setValue(ViewStatus.REFRESH_ERROR);
        }
        viewStatusLiveData.postValue(viewStatusLiveData.getValue());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        dataList.postValue(dataList.getValue());
        viewStatusLiveData.postValue(viewStatusLiveData.getValue());
    }
}

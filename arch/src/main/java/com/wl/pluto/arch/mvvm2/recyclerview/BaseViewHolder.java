package com.wl.pluto.arch.mvvm2.recyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.wl.pluto.arch.mvvm2.customview.BaseCustomViewData;
import com.wl.pluto.arch.mvvm2.customview.ICustomView;


/**
 * Created by plout on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    ICustomView view;

    public BaseViewHolder(ICustomView view) {
        super((View) view);
        this.view = view;
    }

    public void bind(@NonNull BaseCustomViewData item) {
        view.setData(item);
    }
}
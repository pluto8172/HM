package com.wl.pluto.arch.mvvm2.customview;

public interface ICustomView<S extends BaseCustomViewData> {

    void setData(S data);

    void setStyle(int resId);

    void setActionListener(ICustomViewActionListener listener);

}

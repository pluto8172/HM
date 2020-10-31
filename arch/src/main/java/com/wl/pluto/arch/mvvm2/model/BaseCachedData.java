package com.wl.pluto.arch.mvvm2.model;

import java.io.Serializable;

public class BaseCachedData<T> implements Serializable {
    public long updateTimeInMills;
    public T data;
}

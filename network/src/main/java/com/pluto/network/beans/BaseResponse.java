package com.pluto.network.beans;

public class BaseResponse<T> {
    public int code;

    public String message;

    //public T data = null;

    public T value = null;


    @Override
    public String toString() {
        return "BaseResponse{" + "code=" + code + ", msg='" + message + '\'' + ", data=" + value + '}';
    }
}

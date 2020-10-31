package com.wl.pluto.arch.listener;


import com.wl.pluto.arch.base.bean.OpenMedia;

/**
 * Author: dl
 * Date: 2020/2/27 15:44
 * Description: 图片上传监听
 * Email:itdingli@163.com
 */
public interface OnImageUploadCallBackListener {
    void onImageUploadSuccess(OpenMedia media);

    void onImageUploadError(String errorMsg);
}

package com.wl.pluto.arch.listener;


import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * Created by dingli on 20/4/3.
 */
public interface TakePhotoListener {

    void onSelectImageCallback(String imagePath);
    void onSelectImageCallback(List<LocalMedia> mediaList);
}

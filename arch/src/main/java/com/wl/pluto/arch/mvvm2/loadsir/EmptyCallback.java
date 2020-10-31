package com.wl.pluto.arch.mvvm2.loadsir;


import com.kingja.loadsir.callback.Callback;
import com.wl.pluto.arch.R;


/**
 * Description:TODO
 * Create Time:2017/9/4 10:22
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */

public class EmptyCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.layout_empty;
    }

}

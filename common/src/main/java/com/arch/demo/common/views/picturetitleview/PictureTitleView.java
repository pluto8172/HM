package com.arch.demo.common.views.picturetitleview;

import android.content.Context;
import android.view.View;

import com.arch.demo.common.databinding.PictureTitleViewBinding;
import com.arch.demo.common.R;
import com.wl.pluto.arch.mvvm2.customview.BaseCustomView;
import com.pluto.webview.WebviewActivity;

/**
 * Created by plout on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class PictureTitleView extends BaseCustomView<PictureTitleViewBinding, PictureTitleViewViewModel> {
    public PictureTitleView(Context context) {
        super(context);
    }

    @Override
    public int setViewLayoutId() {
        return R.layout.picture_title_view;
    }

    @Override
    public void setDataToView(PictureTitleViewViewModel data) {
        getDataBinding().setViewModel(data);
    }

    @Override
    public void onRootClick(View view) {
        WebviewActivity.startCommonWeb(view.getContext(), getViewModel().jumpUri, "");
    }
}

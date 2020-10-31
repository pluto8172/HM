package com.arch.demo.common.views.titleview;

import android.content.Context;
import android.view.View;

import com.arch.demo.common.R;
import com.arch.demo.common.databinding.TitleViewBinding;
import com.wl.pluto.arch.mvvm2.customview.BaseCustomView;
import com.xiangxue.webview.WebviewActivity;

/**
 * Created by plout on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class TitleView extends BaseCustomView<TitleViewBinding, TitleViewData> {
    public TitleView(Context context) {
        super(context);
    }

    @Override
    public int setViewLayoutId() {
        return R.layout.title_view;
    }

    @Override
    public void setDataToView(TitleViewData data) {
        getDataBinding().setViewModel(data);
    }

    @Override
    public void onRootClick(View view) {
        WebviewActivity.startCommonWeb(view.getContext(), getViewModel().jumpUri,"");
    }
}

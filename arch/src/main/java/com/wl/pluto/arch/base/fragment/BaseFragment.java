package com.wl.pluto.arch.base.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.xiangxue.network.listener.OnResultDataListener;
import com.xiangxue.network.model.RequestWork;
import com.xiangxue.network.model.ResponseWork;

import rxtool.RxKeyboardTool;
import rxtool.view.RxToast;

public abstract class BaseFragment extends AbstractBaseFragment implements OnResultDataListener {

    @Override
    public View onCustomContentView() {
        return null;
    }

    public void onResult(RequestWork req, ResponseWork resp) throws Exception {

        if (resp.getCode() >= 201) {
            if (resp.getCode() == 401) {
                RxToast.info("帐号在其它地方登陆，或登陆信息已过期");
                ARouter.getInstance().build("/login/LoginActivity").navigation();
                getActivity().finish();
            } else {
                RxToast.info(resp.getMessage());
            }
        }
    }


    @Override
    public void initViewModel() {

    }

    @Override
    protected void onRetryBtnClick() {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }


    @Override
    public void onStop() {
        super.onStop();
        RxKeyboardTool.hideSoftInput(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}

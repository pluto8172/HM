package com.wl.pluto.arch.base.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xiangxue.network.listener.OnResultDataListener;
import com.xiangxue.network.model.RequestWork;
import com.xiangxue.network.model.ResponseWork;

import rxtool.view.RxToast;

/**
 * 这个基累是将原项目中公共方法放到这里的。
 * 主要是一些跳转和对话框的显示
 */
public abstract class BaseActivity extends AbstractBaseActivity implements UMShareListener, OnResultDataListener {

    //专门为了切换账号用的，切换账号就需要重现登录，进入首页。平时退到后台。再打开，MainActivity的onNewIntent()方法会回调
    protected static boolean isFirstLogin = false;

    @Override
    protected void onRetryBtnClick() {

    }

    @Override
    protected void initViewModel() {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    public static final String INTENT_PARAM = "intent_param";

    public void openGroup(Class cla, Bundle bundle) {
        Intent intent = new Intent(this, cla);
        if (bundle != null) {
            intent.putExtra(INTENT_PARAM, bundle);
        }
        startActivity(intent);

    }

    public void openGroup(Class cla) {
        openGroup(cla, null);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * @param type    消息类型 1文本消息  2图片消息
     * @param content 消息内容
     */
    public void sendMessage(int type, String content, int duration) {

    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {

    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {

    }


    @Override
    public void onResult(RequestWork req, ResponseWork resp) throws Exception {
        if (resp.getCode() >= 201) {
            if (resp.getCode() == 401) {
                RxToast.info("帐号在其它地方登陆，或登陆信息已过期");
                ARouter.getInstance().build("/login/LoginActivity").navigation();
                finish();
            } else {
                RxToast.info(resp.getMessage());
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


}

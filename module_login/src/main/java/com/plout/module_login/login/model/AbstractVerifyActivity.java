package com.plout.module_login.login.model;

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;

import com.arch.demo.common.widgets.ClearEditText;
import com.plout.module_login.R;
import com.plout.module_login.login.bean.ImageCodeReq;
import com.plout.module_login.login.bean.VerifyPhoneReq;
import com.plout.module_login.widget.PasswordView;
import com.wl.pluto.arch.base.activity.BaseActivity;
import com.wl.pluto.arch.dialog.CostumerDialog;
import com.xiangxue.network.config.ApiConfig;
import com.xiangxue.network.model.RequestWork;
import com.xiangxue.network.model.ResponseWork;

import imageloader.libin.com.images.loader.ImageLoader;
import rxtool.view.RxToast;

/**
 * 短信验证码
 */

public abstract class AbstractVerifyActivity extends BaseActivity {

    protected String mImageCode;
    protected String mPhoneNumber;
    protected String mLoginType = "1";
    protected String mVCode;
    protected String mPassword;
    protected PasswordView mPasswordView;
    protected ClearEditText etCode;

    /**
     * 更有from的不同，下一步显示不同的界面
     * 1--- 手机号短信验证码登录
     * 2--- 注册新账号，需要短信验证码
     * 3--- 忘记密码，需要短信验证码
     */
    protected String from;

    @Override
    public void onResult(RequestWork req, ResponseWork resp) throws Exception {
        super.onResult(req, resp);
//        if (resp.isSuccess()) {
//            if (resp instanceof LoginUserResp) {
//                LoginUserResp userResp = (LoginUserResp) resp;
//                //如果不存在，就插入数据库，如果存在，就更新数据库
//                AppConstant.APP_USER_ID = userResp.getData().id;
//                SPUtils.getInstance().put(SPKey.SP_KEY_USER_ID, AppConstant.APP_USER_ID);
//
//                User userBean = GreenDao.getSession().getUserDao().load(AppConstant.APP_USER_ID);
//                if (userBean == null) {
//                    GreenDao.getSession().getUserDao().insert(userResp.getData());
//                } else {
//                    GreenDao.getSession().getUserDao().update(userResp.getData());
//                }
//                //保存登录标记
//                SPUtils.getInstance().put(SPKey.SP_KEY_IS_LOGIN, true);
//                //保存token
//                SPUtils.getInstance().put(SPKey.SP_KEY_TOKEN, userResp.getData().getToken());
//
//                if (userResp.getData().loginPoint > 0) {
//                    RxToast.info("今日登录积分+" + userResp.getData().loginPoint);
//                }
//
//                if (!TextUtils.isEmpty(userResp.getData().teamId)) {
//                    HttpHeader.HEADER_TEAM_ID_VALUE = userResp.getData().teamId;
//                }
//
//                finish();
//
//                RxKeyboardTool.hideSoftInput(this);
//                ARouter.getInstance().build(ARouterModulePath.PATH_MAIN_ACTIVITY2)
//                        .withBoolean("intent_extra_need_refresh", true)
//                        .withInt("INTENT_EXTRA_PAGE_INDEX", 0)
//                        .navigation();
//                finish();
//
//
//            } else if (resp instanceof ImageCodeResp) {
//
//                ImageCodeResp imageCodeResp = (ImageCodeResp) resp;
//                if (imageCodeResp.data.needImage == 0) {
//                    //如果是登录
//                    if ("1".equals(from)) {
//                        doLoginAction();
//                    } else {
//                        verifyPhoneCode();
//                    }
//                } else {
//
//                    showVerifyDialog(getImageUrl());
//                }
//            }
//        }
    }

    private void verifyPhoneCode() {

        VerifyPhoneReq request = new VerifyPhoneReq();
        request.phone = mPhoneNumber;
        request.smsCode = etCode.getText().toString().trim();
        request.imageVcode = mImageCode;
        LoginClient.getSession().verifyPhoneCode(request, this);
    }

    //过去图片地址
    private String getImageUrl() {
        String result = ApiConfig.BASE_URL + "getImageCode?phone=" + mPhoneNumber + "&type=" + System.currentTimeMillis();
        Log.i("---->", result);
        return result;
    }


    public void showVerifyDialog(String imageUrl) {

        CostumerDialog dialog = new CostumerDialog.Builder(this, R.style.custom_dialog)
                .setContentView(R.layout.dialog_verify_image_layout)
                .setCancelable(true)
                .create();


        ImageView mCodeImage = dialog.findViewById(R.id.iv_code_image);
        if (mCodeImage != null) {
            ImageLoader.with(this).url(imageUrl).into(mCodeImage);
            mCodeImage.setOnClickListener(v -> {
                ImageLoader.with(this).url(getImageUrl()).into(mCodeImage);
            });
        }

        EditText editText = dialog.findViewById(R.id.et_code);
        editText.setText("");

        AppCompatTextView cancel = dialog.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(v -> {
            dialog.dismiss();

        });
        AppCompatTextView ok = dialog.findViewById(R.id.tv_ensure);
        ok.setOnClickListener(v -> {
            mImageCode = editText.getText().toString().trim();
            if (TextUtils.isEmpty(mImageCode) || mImageCode.length() < 4) {
                RxToast.info(this, "请输入图片验证码");
            } else {

                //如果是登录
                if ("1".equals(from)) {
                    doLoginAction();
                } else {//如果是注册，重置密码
                    verifyPhoneCode();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //1 验证并登录
    public void doLoginAction() {
//        LoginUserReq loginReq = new LoginUserReq();
//        loginReq.phone = mPhoneNumber;
//        loginReq.loginType = mLoginType;
//        loginReq.imageCode = mImageCode;
//        loginReq.password = mPassword;
//        loginReq.vcode = mVCode;
//        AZUser.getSession().login(loginReq, this);
    }

    public void doVerifyImageCode(String type) {
        ImageCodeReq request = new ImageCodeReq();
        request.phone = mPhoneNumber;
        request.type = type;
        LoginClient.getSession().getImageCode(request, this);
    }
}

package com.plout.module_login.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.arch.demo.common.constant.AppTextConst;
import com.arch.demo.common.widgets.StateButton;
import com.arch.demo.common.widgets.TimeTaskHelper;
import com.plout.module_login.R;
import com.plout.module_login.login.model.AbstractVerifyActivity;
import com.pluto.network.model.RequestWork;
import com.pluto.network.model.ResponseWork;

import rxtool.RxKeyboardTool;
import rxtool.RxRegTool;
import rxtool.Utils;


/**
 * 短信验证码
 */

public class VerifyActivity extends AbstractVerifyActivity implements View.OnClickListener {

    public static final String INTENT_PHONE = "intent_phone";
    public static final String INTENT_FROM = "intent_from";

    private AppCompatTextView tvAgain;
    private AppCompatTextView tvXx;
    private AppCompatImageView ivBack;
    private TimeTaskHelper timeTaskHelper;
    private StateButton sbLogin;


    public void onInitView() {
        tvAgain = findViewById(R.id.tv_code);
        tvAgain.setOnClickListener(this);
        tvAgain.setEnabled(false);

        tvXx = findViewById(R.id.tv_xx);

        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);

        sbLogin = findViewById(R.id.sb_login);
        sbLogin.setOnClickListener(this);
        sbLogin.setEnabled(false);


        etCode = findViewById(R.id.et_code);
        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6) {
                    RxKeyboardTool.hideSoftInput(VerifyActivity.this);
                    sbLogin.setEnabled(true);
                } else {
                    sbLogin.setEnabled(false);
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeTaskHelper.cancel();
    }


    /**
     * 进入设置密码界面
     */
    private void goSetPassword() {
        Intent intent = new Intent(this, SetPasswordActivity.class);
        intent.putExtra(SetPasswordActivity.INTENT_PHONE, mPhoneNumber);
        intent.putExtra(SetPasswordActivity.INTENT_CODE, etCode.getText().toString().trim());
        intent.putExtra(SetPasswordActivity.INTENT_TYPE, mType);
        startActivity(intent);
        finish();
    }

    private String mType = "1";


    @Override
    public void onClick(View view) {
        if (Utils.isFastDoubleClick()) {
            return;
        }

        int id = view.getId();
        if (id == R.id.sb_login) {//验证并登录
            switch (from) {
                case "1": // 手机号短信验证码登录
                    handleLoginLogic();
                    break;
                case "2": //注册新账号，需要短信验证码
                    mType = "1";
                    doVerifyImageCode("1");
                    break;
                case "3": //修改密码，需要短信验证码
                    mType = "2";

                    //先判断是否需要显示图片验证
                    doVerifyImageCode("1");
                    break;

            }
        } else if (id == R.id.tv_code) {//重新获取
//            SendSmsReq sendSmsReq = new SendSmsReq();
//            sendSmsReq.setPhone(mPhoneNumber);
//            sendSmsReq.setType("1");
//            AZUser.getSession().sendSms(sendSmsReq, this);
//            tvAgain.setEnabled(false);
            //hindLoadingDialog();
        } else if (id == R.id.iv_back) {
            finish();
        } else if (id == R.id.tv_policy) {// //专业用户合作协议

            Bundle bundle1 = new Bundle();
            bundle1.putString("title", "");
            bundle1.putString("url", AppTextConst.Doctor_DoctorAgreement);
            //openGroup(WebViewFragment.class, bundle1);
        } else if (id == R.id.tv_protect) {//隐私协议
            Bundle bundle2 = new Bundle();
            bundle2.putString("title", "");
            bundle2.putString("url", AppTextConst.Public_PrivacyAgreement);
            //openGroup(WebViewFragment.class, bundle2);
        }
    }

    private void handleLoginLogic() {
        mLoginType = "1";
        mVCode = etCode.getText().toString().trim();
        mPassword = "";
        mImageCode = "";
        from = "1";
        doVerifyImageCode("1");
    }

    @Override
    public void onResult(RequestWork req, ResponseWork resp) throws Exception {
        super.onResult(req, resp);
//        if (resp.isSuccess()) {
//            if (resp instanceof SendSmsResp) {
//                timeTaskHelper.Waiting();
//                //发送成功
//                etCode.setText("");
//                sbLogin.setEnabled(false);
//            } else if (resp instanceof VerifyPhoneResp) {
//                goSetPassword();
//            }
//        } else {
//            if (resp instanceof SendSmsResp) {
//                tvAgain.setEnabled(true);
//            }
//        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_verify;
    }

    @Override
    public void initView() {

        onInitView();

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mPhoneNumber = getIntent().getStringExtra(INTENT_PHONE);
        from = getIntent().getStringExtra(INTENT_FROM);

        timeTaskHelper = new TimeTaskHelper(this, tvAgain, 60);
        timeTaskHelper.Waiting();
        //initForLogin(true, TextUtils.isEmpty(from) ? false : true);
        tvXx.setText("验证码已发送至 +86 " + RxRegTool.hidePhoneNumber(mPhoneNumber));

        switch (from) {
            case "1": // 手机号短信验证码登录
                sbLogin.setText("登录");
                break;
            case "2": //注册新账号，需要短信验证码
            case "3": //忘记密码，需要短信验证码
            default:
                sbLogin.setText("下一步");
                break;
        }
    }
}

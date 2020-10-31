package com.plout.module_login.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.arch.demo.common.widgets.ClearEditText;
import com.arch.demo.common.widgets.StateButton;
import com.plout.module_login.R;
import com.plout.module_login.login.model.AbstractVerifyActivity;
import com.xiangxue.network.model.RequestWork;
import com.xiangxue.network.model.ResponseWork;

import rxtool.RxRegTool;
import rxtool.Utils;
import rxtool.view.RxToast;


/**
 * 手机号密码登录
 */

public class LoginActivity2 extends AbstractVerifyActivity {

    private AppCompatImageView ivBack;

    private ClearEditText mPhoneEditText;
    private StateButton mLoginButton;

    private TextView mLogin2Text;
    private TextView mRegisterText;
    private AppCompatTextView mForgetPassword;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login2;
    }

    @Override
    public void initView() {


        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(listener);

        mPhoneEditText = findViewById(R.id.et_telephone);
        mPasswordView = findViewById(R.id.input_password);

        mForgetPassword = findViewById(R.id.tv_forget_password);
        mForgetPassword.setOnClickListener(listener);

        mLoginButton = findViewById(R.id.sb_login_button);
        mLoginButton.setOnClickListener(listener);
        mLoginButton.setEnabled(false);

        mLogin2Text = findViewById(R.id.tv_login_1);
        mLogin2Text.setOnClickListener(listener);

        mRegisterText = findViewById(R.id.tv_register);
        mRegisterText.setOnClickListener(listener);

        mPhoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() >= 11 && RxRegTool.isMobileSimple(mPhoneEditText.getText().toString().trim()) && mPasswordView.getText().length() >= 6) {
                    mLoginButton.setEnabled(true);
                } else {
                    mLoginButton.setEnabled(false);
                }
            }
        });

        mPasswordView.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() >= 6 && RxRegTool.isMobileSimple(mPhoneEditText.getText().toString().trim())) {
                    mLoginButton.setEnabled(true);
                } else {
                    mLoginButton.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }


    @Override
    public void onResult(RequestWork req, ResponseWork resp) throws Exception {
        super.onResult(req, resp);
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (Utils.isFastDoubleClick()) {
                return;
            }
            int id = v.getId();
            if (id == R.id.iv_back) {
                finish();
            } else if (id == R.id.tv_login_1) { //手机号密码登录
                Intent intent = new Intent(LoginActivity2.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else if (id == R.id.tv_register) {
                Intent intent1 = new Intent(LoginActivity2.this, RegisterActivity.class);
                startActivity(intent1);
                finish();
            } else if (id == R.id.tv_forget_password) {
                startActivity(new Intent(LoginActivity2.this, ForgetPasswordActivity.class));
            } else if (id == R.id.sb_login_button) {//登录
                handleLoginLogic();
            }
        }
    };


    private void handleLoginLogic() {
        mPhoneNumber = mPhoneEditText.getText().toString().trim();
        mLoginType = "2";
        mPassword = mPasswordView.getText();
        if (!RxRegTool.isMobileSimple(mPhoneNumber)) {
            RxToast.info(this, "请输入正确的手机号");
            return;
        }

        if (mPassword.length() < 6) {
            RxToast.info(this, "请输入6位以上的密码");
            return;
        }
        mVCode = "";
        mImageCode = "";
        from = "1";
        doVerifyImageCode("2");
    }

}

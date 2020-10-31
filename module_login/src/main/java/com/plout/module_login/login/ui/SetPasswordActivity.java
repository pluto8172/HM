package com.plout.module_login.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.arch.demo.common.widgets.StateButton;
import com.plout.module_login.widget.PasswordView;

import com.plout.module_login.R;
import com.plout.module_login.login.bean.RegisterReq;
import com.plout.module_login.login.bean.UpdatePasswordReq;
import com.plout.module_login.login.model.LoginClient;
import com.xiangxue.network.model.RequestWork;
import com.xiangxue.network.model.ResponseWork;

import com.wl.pluto.arch.base.activity.BaseActivity;

import rxtool.Utils;


/**
 * 设置密码
 */

public class SetPasswordActivity extends BaseActivity implements View.OnClickListener {

    public static final String INTENT_PHONE = "intent_phone";
    public static final String INTENT_CODE = "intent_code";

    //
    public static final String INTENT_TYPE = "intent_type";

    private AppCompatImageView ivBack;
    private PasswordView mPassword;
    private StateButton sbLogin;
    private String mPhoneNumber;
    private String mMsgCode;

    //1=注册新用户，2=忘记密码
    private String mType;

    @Override
    public int getLayoutId() {
        return R.layout.activity_set_password;
    }


    @Override
    public void initView() {
        mPhoneNumber = getIntent().getStringExtra(INTENT_PHONE);
        mMsgCode = getIntent().getStringExtra(INTENT_CODE);
        mType = getIntent().getStringExtra(INTENT_TYPE);

        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);

        sbLogin = findViewById(R.id.sb_login);
        sbLogin.setOnClickListener(this);
        //将sbLogin设为不可用，在布局中设置会导致代码设置无效
        sbLogin.setEnabled(false);

        mPassword = findViewById(R.id.input_password);
        mPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 6) {
                    //RxKeyboardTool.hideSoftInput(SetPasswordActivity.this);
                    sbLogin.setEnabled(true);
                } else {
                    sbLogin.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }


    @Override
    public void onClick(View view) {
        if (Utils.isFastDoubleClick()) {
            return;
        }

        int id = view.getId();
        if (id == R.id.sb_login) {//
            doSubmitAction();
        } else if (id == R.id.iv_back) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onResult(RequestWork req, ResponseWork resp) throws Exception {
        super.onResult(req, resp);
        if (resp.isSuccess()) {
            showVerifyDialog();
        }
    }

    private void doSubmitAction() {

        switch (mType) {
            case "1":
                doRegisterAction();
                break;
            case "2":
                doModifyPassword();
                break;
        }
    }

    private void doRegisterAction() {
        RegisterReq request = new RegisterReq();
        request.phone = mPhoneNumber;
        request.password = mPassword.getText();
        request.vCode = mMsgCode;
        LoginClient.getSession().registerNewUser(request, this);
    }

    private void doModifyPassword() {
        UpdatePasswordReq request = new UpdatePasswordReq();
        request.phone = mPhoneNumber;
        request.vCode = mMsgCode;
        request.password = mPassword.getText();
        LoginClient.getSession().resetPassword(request, this);
    }

    public void showVerifyDialog() {
        String content;
        if ("1".equals(mType)) {
            content = "注册成功，马上返回登录";
        } else {
            content = "密码设置成功";
        }

        showNormalDialogWithOneButton("", content, v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}

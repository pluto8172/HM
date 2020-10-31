package com.plout.module_login.login.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.plout.module_login.widget.PasswordView;
import com.plout.module_login.R;
import com.plout.module_login.login.bean.UpdatePasswordReq;
import com.plout.module_login.login.model.LoginClient;
import com.xiangxue.network.model.RequestWork;
import com.xiangxue.network.model.ResponseWork;

import rxtool.view.RxToast;
import com.wl.pluto.arch.base.activity.BaseActivity;


/**
 * 修改密码
 */

public class ModifyPasswordActivity2 extends BaseActivity {

    public static final String INTENT_PHONE = "intent_phone";
    public static final String INTENT_CODE = "intent_code";

    private AppCompatImageView ivBack;


    private PasswordView mPassword;
    private TextView mCancellationButton;

    private String mPhoneNumber;
    private String mMsgCode;

    @Override
    public int getLayoutId() {
        return R.layout.activity_set_password_layout2;
    }

    @Override
    public void initView() {

        mPhoneNumber = getIntent().getStringExtra(INTENT_PHONE);
        mMsgCode = getIntent().getStringExtra(INTENT_CODE);


        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> finish());

        mCancellationButton = findViewById(R.id.tv_confirm_cancellation);
        mCancellationButton.setEnabled(false);
        mCancellationButton.setOnClickListener(v -> {
            doSubmitAction();
        });

        //将mCancellationButton设为不可用，在布局中设置会导致代码设置无效
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
                    mCancellationButton.setEnabled(true);
                } else {
                    mCancellationButton.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    private void doSubmitAction() {
        UpdatePasswordReq request = new UpdatePasswordReq();
        request.phone = mPhoneNumber;
        request.vCode = mMsgCode;
        request.password = mPassword.getText();
        LoginClient.getSession().updatePassword(request, this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




    @Override
    public void onResult(RequestWork req, ResponseWork resp) throws Exception {
        super.onResult(req, resp);
        if (resp.isSuccess()) {
            RxToast.info(this, "密码修改成功");
            setResult(Activity.RESULT_OK);
            finish();
        }
    }
}

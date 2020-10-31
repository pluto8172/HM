package com.plout.module_login.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.arch.demo.common.widgets.ClearEditText;
import com.arch.demo.common.widgets.StateButton;
import com.plout.module_login.R;
import com.plout.module_login.login.bean.VerifyPhoneReq;
import com.plout.module_login.login.model.LoginClient;
import com.wl.pluto.arch.base.activity.BaseActivity;

import com.pluto.network.model.RequestWork;
import com.pluto.network.model.ResponseWork;

import rxtool.RxKeyboardTool;
import rxtool.RxRegTool;
import rxtool.Utils;


/**
 * 修改密码
 */

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private ClearEditText etTelePhone;
    private StateButton sbGetCode;
    private AppCompatImageView ivBack;


    public void onInitView() {

        etTelePhone = findViewById(R.id.et_telephone);
        ivBack = findViewById(R.id.iv_back);
        sbGetCode = findViewById(R.id.sb_get_code);


        sbGetCode.setEnabled(false);

        sbGetCode.setOnClickListener(this);
        ivBack.setOnClickListener(this);


        etTelePhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (RxRegTool.isMobileSimple(s.toString())) {
                    RxKeyboardTool.hideSoftInput(ForgetPasswordActivity.this);
                    sbGetCode.setEnabled(true);
                } else {
                    sbGetCode.setEnabled(false);
                }
            }
        });
    }


    private void verifyLoginPhoneNumber() {
        VerifyPhoneReq request = new VerifyPhoneReq();
        request.phone = etTelePhone.getText().toString();
        LoginClient.getSession().verifyLoginPhoneNumber(request, this);
    }

    private void sendMsgCode() {
//        SendSmsReq sendSmsReq = new SendSmsReq();
//        sendSmsReq.setPhone(etTelePhone.getText().toString());
//        sendSmsReq.setType("2");
//        AZUser.getSession().sendSms(sendSmsReq, this);
    }

    @Override
    public void onResult(RequestWork req, ResponseWork resp) throws Exception {
        super.onResult(req, resp);
//        if (resp.isSuccess()) {
//            if (resp instanceof SendSmsResp) {
//                onNextClick();
//            } else if (resp instanceof VerifyPhoneResp) {
//                sendMsgCode();
//            }
//        }
    }

    private void onNextClick() {
        Intent intent = new Intent();
        intent.putExtra(VerifyActivity.INTENT_PHONE, etTelePhone.getText().toString());
        intent.putExtra(VerifyActivity.INTENT_FROM, "3");
        intent.setClass(ForgetPasswordActivity.this, VerifyActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        if (Utils.isFastDoubleClick()) {
            return;
        }

        int id = view.getId();
        if (id == R.id.sb_get_code) {//忘记和修改密码一样，这里传2
            //先验证手机号是否登录
            verifyLoginPhoneNumber();
        } else if (id == R.id.iv_back) {
            finish();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_froget_password;
    }

    @Override
    public void initView() {
        onInitView();
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }
}

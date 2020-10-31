package com.plout.module_login.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.arch.demo.common.arouter.ARouterModulePath;
import com.arch.demo.common.event.ExitAppEvent;
import com.arch.demo.common.widgets.ClearEditText;
import com.arch.demo.common.widgets.StateButton;
import com.plout.module_login.R;
import com.plout.module_login.login.bean.VerifyPhoneReq;
import com.plout.module_login.login.model.LoginClient;
import com.wl.pluto.arch.base.activity.BaseActivity;
import com.xiangxue.network.model.RequestWork;
import com.xiangxue.network.model.ResponseWork;

import org.greenrobot.eventbus.EventBus;

import rxtool.RxKeyboardTool;
import rxtool.RxRegTool;
import rxtool.Utils;


/**
 * 手机短信验证码登录
 */
@Route(path = ARouterModulePath.PATH_LOGIN_ACTIVITY)
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private ClearEditText etTelePhone;
    private StateButton sbGetCode;
    private AppCompatImageView ivBack;

    private TextView mLogin2Text;
    private TextView mRegisterText;


    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }


    @Override
    public void initView() {


        etTelePhone = findViewById(R.id.et_telephone);
        ivBack = findViewById(R.id.iv_back);
        sbGetCode = findViewById(R.id.sb_get_code);

        mLogin2Text = findViewById(R.id.tv_login_2);
        mLogin2Text.setOnClickListener(this);

        mRegisterText = findViewById(R.id.tv_register);
        mRegisterText.setOnClickListener(this);

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
                    RxKeyboardTool.hideSoftInput(LoginActivity.this);
                    sbGetCode.setEnabled(true);
                } else {
                    sbGetCode.setEnabled(false);
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
//        if (resp.isSuccess()) {
//            if (resp instanceof SendSmsResp) {
//                Intent intent = new Intent();
//                intent.putExtra(VerifyActivity.INTENT_PHONE, etTelePhone.getText().toString());
//                intent.putExtra(VerifyActivity.INTENT_FROM, "1");
//                intent.setClass(LoginActivity.this, VerifyActivity.class);
//                startActivity(intent);
//            }
//            if (resp instanceof VerifyPhoneResp) {
//                getVerifyCode();
//            }
//        }
    }

    private void verifyLoginPhoneNumber() {
        VerifyPhoneReq request = new VerifyPhoneReq();
        request.phone = etTelePhone.getText().toString();
        LoginClient.getSession().verifyLoginPhoneNumber(request, this);
    }

    private void getVerifyCode() {
//        SendSmsReq sendSmsReq = new SendSmsReq();
//        sendSmsReq.setPhone(etTelePhone.getText().toString());
//        sendSmsReq.setType("1");
//        AZUser.getSession().sendSms(sendSmsReq, this);
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
        if (id == R.id.sb_get_code) {//获取验证码
            verifyLoginPhoneNumber();
        } else if (id == R.id.iv_back) {
            EventBus.getDefault().post(new ExitAppEvent());
            finish();
        } else if (id == R.id.tv_login_2) { //手机号密码登录

            Intent intent = new Intent(this, LoginActivity2.class);
            startActivity(intent);
        } else if (id == R.id.tv_register) {
            Intent intent1 = new Intent(this, RegisterActivity.class);
            startActivity(intent1);
        }
    }
}

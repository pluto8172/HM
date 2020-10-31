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
import com.arch.demo.common.widgets.ClearEditText;
import com.arch.demo.common.widgets.StateButton;
import com.plout.module_login.R;
import com.plout.module_login.login.bean.VerifyPhoneReq;
import com.plout.module_login.login.model.LoginClient;
import com.wl.pluto.arch.base.activity.BaseActivity;
import com.pluto.network.model.RequestWork;
import com.pluto.network.model.ResponseWork;
import com.pluto.webview.WebviewActivity;

import rxtool.RxKeyboardTool;
import rxtool.RxRegTool;
import rxtool.Utils;


/**
 * 注册界面
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private ClearEditText etTelePhone;
    private StateButton sbGetCode;
    private AppCompatImageView ivBack;
    private boolean enable = true;

    private AppCompatTextView tvPolicy;
    private AppCompatTextView tvProtect;


    @Override
    public int getLayoutId() {
        return R.layout.activity_register_layout;
    }


    @Override
    public void initView() {


        etTelePhone = findViewById(R.id.et_telephone);
        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);

        sbGetCode = findViewById(R.id.sb_get_code);
        sbGetCode.setOnClickListener(this);
        sbGetCode.setEnabled(false);

        tvPolicy = findViewById(R.id.tv_policy);
        tvPolicy.setOnClickListener(this);
        tvProtect = findViewById(R.id.tv_protect);
        tvProtect.setOnClickListener(this);


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
                    RxKeyboardTool.hideSoftInput(RegisterActivity.this);
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
        enable = true;
//        if (resp.isSuccess()) {
//            if (resp instanceof SendSmsResp) {
//                Intent intent = new Intent();
//                intent.putExtra(VerifyActivity.INTENT_PHONE, etTelePhone.getText().toString());
//                intent.putExtra(VerifyActivity.INTENT_FROM, "2");
//                intent.setClass(RegisterActivity.this, VerifyActivity.class);
//                startActivity(intent);
//            } else if (resp instanceof VerifyPhoneResp) {
//                getMessageCode();
//            }
//        }
    }

    private void verifyRegisterPhoneNumber() {
        VerifyPhoneReq request = new VerifyPhoneReq();
        request.phone = etTelePhone.getText().toString();
        LoginClient.getSession().verifyRegisterPhoneNumber(request, this);
    }

    private void getMessageCode() {
//        SendSmsReq sendSmsReq = new SendSmsReq();
//        sendSmsReq.setPhone(etTelePhone.getText().toString());
//        sendSmsReq.setType("5");
//        AZUser.getSession().sendSms(sendSmsReq, this);
//        enable = false;
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
            if (!enable) {
                return;
            }
            verifyRegisterPhoneNumber();
        } else if (id == R.id.iv_back) {
            finish();
        } else if (id == R.id.tv_policy) {// //专业用户合作协议

            WebviewActivity.startCommonWeb(this, AppTextConst.Doctor_DoctorAgreement, "专业用户合作协议");
//            Bundle bundle1 = new Bundle();
//            bundle1.putString("title", "");
//            bundle1.putString("url", AppTextConst.Doctor_DoctorAgreement);
            //openGroup(WebViewFragment.class, bundle1);
        } else if (id == R.id.tv_protect) {//隐私协议
            WebviewActivity.startCommonWeb(this, AppTextConst.Public_PrivacyAgreement, "隐私协议");

//            Bundle bundle2 = new Bundle();
//            bundle2.putString("title", "");
//            bundle2.putString("url", AppTextConst.Public_PrivacyAgreement);
            //openGroup(WebViewFragment.class, bundle2);
        } else if (id == R.id.tv_login_2) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}

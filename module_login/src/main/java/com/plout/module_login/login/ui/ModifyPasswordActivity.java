package com.plout.module_login.login.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.arch.demo.common.arouter.ARouterModulePath;
import com.arch.demo.common.widgets.TimeTaskHelper;
import com.plout.module_login.R;
import com.plout.module_login.login.model.AbstractVerifyActivity;
import com.xiangxue.network.model.RequestWork;
import com.xiangxue.network.model.ResponseWork;

import rxtool.RxKeyboardTool;
import rxtool.RxRegTool;
import rxtool.Utils;


/**
 * 修改密码
 */
@Route(path = ARouterModulePath.PATH_MODIFY_PASSWORD_ACTIVITY)
public class ModifyPasswordActivity extends AbstractVerifyActivity implements View.OnClickListener {

    private AppCompatTextView tvAgain;
    private AppCompatImageView ivBack;

    private TextView mCancellationButton;
    private TimeTaskHelper timeTaskHelper;
    private TextView mPhoneText;


    @Override
    public int getLayoutId() {
        return R.layout.activity_modify_password_layout;
    }

    @Override
    public void initView() {


        // mPhoneNumber = getUser().getPhone();

        mPhoneText = findViewById(R.id.tv_user_phone_number);
        mPhoneText.setText(RxRegTool.hidePhoneNumber(mPhoneNumber));

        tvAgain = findViewById(R.id.tv_code);
        tvAgain.setOnClickListener(this);

        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);

        etCode = findViewById(R.id.et_code);

        mCancellationButton = findViewById(R.id.tv_confirm_cancellation);
        mCancellationButton.setOnClickListener(this);
        mCancellationButton.setEnabled(false);

        //发送验证码
        //sendMsgCode();

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
                    RxKeyboardTool.hideSoftInput(ModifyPasswordActivity.this);
                    mCancellationButton.setEnabled(true);
                } else {
                    mCancellationButton.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        timeTaskHelper = new TimeTaskHelper(this, tvAgain, 60);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeTaskHelper.cancel();

    }

    private void onNextClick() {
        Intent intent = new Intent(this, ModifyPasswordActivity2.class);
        intent.putExtra(ModifyPasswordActivity2.INTENT_PHONE, mPhoneNumber);
        intent.putExtra(ModifyPasswordActivity2.INTENT_CODE, etCode.getText().toString().trim());
        startActivityForResult(intent, 10001);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            finish();
        }
    }

    /**
     * 修改和重置密码，都是传2
     */
    private void sendMsgCode() {
//        SendSmsReq sendSmsReq = new SendSmsReq();
//        sendSmsReq.setPhone(getUser().phone);
//        sendSmsReq.setType("2");
//        AZUser.getSession().sendSms(sendSmsReq, this);
//        tvAgain.setEnabled(false);
    }


    @Override
    public void onClick(View view) {
        if (Utils.isFastDoubleClick()) {
            return;
        }


        int id = view.getId();
        if (id == R.id.tv_code) {//重新获取
            sendMsgCode();
        } else if (id == R.id.iv_back) {
            finish();
        } else if (id == R.id.tv_confirm_cancellation) {//先判断是否需要图片验证码
            from = "5";
            doVerifyImageCode("1");
        }
    }


    @Override
    public void onResult(RequestWork req, ResponseWork resp) throws Exception {
        super.onResult(req, resp);
//        if (resp.isSuccess()) {
//            if (resp instanceof SendSmsResp) {
//                timeTaskHelper.Waiting();
//                //发送成功
//                etCode.setText("");
//                mCancellationButton.setEnabled(false);
//
//            } else if (resp instanceof VerifyPhoneResp) {
//                onNextClick();
//            }
//        } else {
//            if (resp instanceof SendSmsResp) {
//                tvAgain.setEnabled(true);
//            }
//        }
    }
}

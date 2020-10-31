package com.plout.module_login.login.model;


import com.plout.module_login.login.bean.ImageCodeReq;
import com.plout.module_login.login.bean.RegisterReq;
import com.plout.module_login.login.bean.UpdatePasswordReq;
import com.plout.module_login.login.bean.VerifyPhoneReq;
import com.pluto.network.listener.OnResultDataListener;


public class LoginClient {

    private static LoginClient Instance = new LoginClient();

    public static LoginClient getSession() {
        return Instance;
    }

    /**
     * 获取图片验证码
     */
    public void getImageCode(ImageCodeReq req, OnResultDataListener onResultDataListener, Object... obj) {
        //requestGet(ModeApi.getUrl(ModeApi.getImageCode), req, new ImageCodeResp(), onResultDataListener, obj);
    }


    /**
     * 注册验证
     */
    public void verifyRegisterPhoneNumber(VerifyPhoneReq req, OnResultDataListener onResultDataListener, Object... obj) {
        // requestPost(ModeApi.getUrl(ModeApi.verifyRegisterPhoneNumber), req, new VerifyPhoneResp(), onResultDataListener, obj);
    }

    /**
     * 登录验证
     */
    public void verifyLoginPhoneNumber(VerifyPhoneReq req, OnResultDataListener onResultDataListener, Object... obj) {
        //requestPost(ModeApi.getUrl(ModeApi.verifyLoginPhoneNumber), req, new VerifyPhoneResp(), onResultDataListener, obj);
    }

    /**
     * 验证手机号和短信
     */
    public void verifyPhoneCode(VerifyPhoneReq req, OnResultDataListener onResultDataListener, Object... obj) {
        // requestPost(ModeApi.getUrl(ModeApi.verifyPhoneCode), req, new VerifyPhoneResp(), onResultDataListener, obj);
    }

    /**
     * 注册新用户
     */
    public void registerNewUser(RegisterReq req, OnResultDataListener onResultDataListener, Object... obj) {
        //requestPost(ModeApi.getUrl(ModeApi.registerNewUser), req, new RegisterResp(), onResultDataListener, obj);
    }


    /**
     * 修改密码
     */
    public void updatePassword(UpdatePasswordReq req, OnResultDataListener onResultDataListener, Object... obj) {
        //requestPost(ModeApi.getUrl(ModeApi.updatePassword), req, new RegisterResp(), onResultDataListener, obj);
    }


    /**
     * 重置密码
     */
    public void resetPassword(UpdatePasswordReq req, OnResultDataListener onResultDataListener, Object... obj) {
        // requestPost(ModeApi.getUrl(ModeApi.resetPassword), req, new RegisterResp(), onResultDataListener, obj);
    }
}

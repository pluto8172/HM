package com.pluto.network.model;

import android.text.TextUtils;

import com.pluto.network.work.RequestParams;
import com.pluto.network.utils.SLog;

import rxtool.StringEncrypt;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by tangyx on 2016/12/26.
 */

public class ResponseWork extends ClientModel {
    /**
     * 网络请求状态
     */
    private int Code;
    private List<Object> positionParams;
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getRCode() {
        return Code;
    }

    public void setRCode(int Code) {
        this.Code = Code;
    }

    public void setPositionParams(List<Object> positionParams) {
        this.positionParams = positionParams;
    }

    public <T extends Object> T getPositionParams(int position) {
        return (T) positionParams.get(position);
    }

    /**
     * 自己处理参数赋值
     */
    public void onResultData(RequestParams params, Object result) {
        if (params.resp != null && result instanceof String) {
            String value = (String) result;
            if (!TextUtils.isEmpty(value)) {
                //过滤字段不存在的情况
                try {
                    BaseResponse baseResponse = JsonFactory.getInstance().getGson().fromJson(value, BaseResponse.class);
                    if (baseResponse.getData() != null && baseResponse.getCode() == 200) {
                        String decrypt = StringEncrypt.getInstance().decrypt(baseResponse.getData());
                        value = "{\"code\":200,\"message\":\"" + baseResponse.getMessage() + "\",\"data\":" + decrypt + "}";

                        if (value.startsWith("[") && value.endsWith("]")) {
                            JSONObject tempJson = new JSONObject();
                            tempJson.put("list", new JSONArray(value));
                            value = tempJson.toString();
                        }
                    }

                    if (SLog.debug){
                        SLog.v("Result data:" + value);
                    }
                    params.resp = JsonFactory.getInstance().getGson().fromJson(value, params.resp.getClass());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 判断结果
     */
    public boolean isSuccess() {
        return false;
    }

    /**
     * 内容
     */
    public String getMessage() {
        return null;
    }
}

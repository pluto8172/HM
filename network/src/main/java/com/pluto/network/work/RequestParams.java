package com.pluto.network.work;

import android.text.TextUtils;

import com.pluto.network.listener.OnResultDataListener;
import com.pluto.network.model.RequestWork;
import com.pluto.network.model.ResponseWork;
import com.pluto.network.utils.SLog;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLSocketFactory;

/**
 * Created by tangyx on 15/9/1.
 * 封装网络上传信息
 */
public class RequestParams implements Serializable{
    /**
     * 请求地址
     */
    public String url="";
    /**
     * 请求头信息
     */
    public Map<String,Object> headMap;
    /**
     * post内容
     */
    public byte[] postData;
    /**
     * 请求方式
     */
    public HttpType method;
    /**
     * 开始发起请求时间
     */
    public long start=0;
    /**
     * 结束时间
     */
    public long end=0;
    /**
     * 当前网络请求的唯一标示
     */
    public int eventCode=-1;

    public OnResultDataListener onResultDataListener;

    private SSLSocketFactory sslSocketFactory;

    public RequestWork req;
    public ResponseWork resp;

    private List<Object> paramsList;
    /**
     * 图文混合上传
     */
    private Map<String,File> fileParams;
    private Map<String,String> textParams;

    /**
     * 是否自动取消加载提示框
     */
    public RequestParams(){

    }
    public RequestParams(String url){
        this();
        this.url = url;
    }

    /**
     *
     * @param url 请求网络地址
     * @param headMap header内容
     */
    public RequestParams(String url,Map<String,Object> headMap){
        this(url);
        this.headMap = headMap;
        if(eventCode==-1){
            eventCode = new Random().nextInt();
        }
    }

    /**
     *
     * @param url
     * @param headMap
     * @param postData post上传内容
     */
    public RequestParams(String url,Map<String,Object> headMap,byte[] postData){
        this(url,headMap);
        this.postData = postData;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    public void putParams(Object... obj){
        if(obj.length==0){
            return;
        }
        paramsList = new ArrayList<>();
        for (int i=0;i<obj.length;i++){
            paramsList.add(obj[i]);
        }
    }

    public List<Object> getParamsList() {
        return paramsList;
    }

    public Object getPositionParams(int position){
        if(paramsList!=null){
            return paramsList.get(position);
        }
        return null;
    }

    public void addHeader(String key,String val){
        if(this.headMap==null){
            this.headMap = new HashMap<>();
        }
        this.headMap.put(key,val);
    }

    public void addFileParam(String name,File file){
        if(!file.exists()){
            if(SLog.debug) SLog.e("上传文件不存在:" + file);
            return;
        }
        if(fileParams==null){
            fileParams = new HashMap<>();
        }
        fileParams.put(name, file);
    }
    /**
     * 添加文字
     */
    public void addTextParam(String name,String text){
        if(TextUtils.isEmpty(text)){
            return;
        }
        if(textParams==null){
            textParams = new HashMap<>();
        }
        textParams.put(name,text);
    }

    public Map<String, File> getFileParams() {
        return fileParams;
    }

    public Map<String, String> getTextParams() {
        return textParams;
    }

}

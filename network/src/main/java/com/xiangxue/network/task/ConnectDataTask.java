package com.xiangxue.network.task;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;

import com.xiangxue.network.work.HttpConnectWork;
import com.xiangxue.network.work.HttpType;
import com.xiangxue.network.work.RequestParams;
import com.xiangxue.network.utils.SLog;

public class ConnectDataTask extends AsyncTask<Object, Integer, Object> {

    protected HttpConnectWork hc;
    protected RequestParams params;

    public ConnectDataTask(RequestParams params) {
        hc = new HttpConnectWork();
        this.params = params;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        params.start = System.currentTimeMillis();
    }

    @Override
    protected Object doInBackground(Object... param) {
        HttpType method = params.method;
        hc.setSSLSocket(params.getSslSocketFactory());
        Object result = null;
        if (method == HttpType.GET) {
            result = hc.getRequestManager(params.url, params.headMap);
        } else if (method == HttpType.POST) {
            result = hc.postRequestManager(params.url, params.headMap, params.postData);
        } else if (method == HttpType.PUT) {
            result = hc.putRequestManager(params.url, params.headMap, params.postData);
        } else if (method == HttpType.DELETE) {
            result = hc.deleteRequestManager(params.url, params.headMap, params.postData);
        } else if (method == HttpType.DOWN_IMAGE) {
            result = hc.downFile(params.url, params.headMap);
        } else if (method == HttpType.DOWN_FILE) {
            result = hc.downFile(params.url, params.headMap, params.req.getSavePathFile());
        } else if (method == HttpType.UPLOAD) {
            result = hc.uploadFileManager(params.url, params.getFileParams(), params.getTextParams(), params.headMap);
        }
//		if(SLog.debug)SLog.v("Result data:"+result);
        params.resp.setRCode(hc.getCode());
        params.resp.onResultData(params, result);
        params.resp.setPositionParams(params.getParamsList());
        //对象重新变化后赋值网络状态
        params.resp.setRCode(hc.getCode());
        return result;
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        params.end = System.currentTimeMillis();
        try {
            if (params.onResultDataListener != null) {
                params.onResultDataListener.onResult(params.req, params.resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    /**
     * Get请求
     */
    @SuppressLint("ObsoleteSdkInt")
    public void doGet() {
        if (params == null) {
            throw new NullPointerException("params can not null!");
        }
        if (SLog.debug) SLog.d("Get：" + params.url);
        params.method = HttpType.GET;
        //根据不同的api采用并行模式进行开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.executeOnExecutor(THREAD_POOL_EXECUTOR);
        } else {
            this.execute();
        }
    }

    /**
     * Post请求
     */
    public void doPost() {
        if (params == null) {
            throw new NullPointerException("params can not null!");
        }
        if (SLog.debug) SLog.d("Post：" + params.url);
        params.method = HttpType.POST;
        //根据不同的api采用并行模式进行开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.executeOnExecutor(THREAD_POOL_EXECUTOR);
        } else {
            this.execute();
        }
    }

    /**
     * Put请求
     */
    public void doPut() {
        if (params == null) {
            throw new NullPointerException("params can not null!");
        }
        if (SLog.debug) SLog.d("Put：" + params.url);
        params.method = HttpType.PUT;
        //根据不同的api采用并行模式进行开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.executeOnExecutor(THREAD_POOL_EXECUTOR);
        } else {
            this.execute();
        }
    }

    /**
     * DELETE请求
     */
    public void doDelete() {
        if (params == null) {
            throw new NullPointerException("params can not null!");
        }
        if (SLog.debug) SLog.d("Delete：" + params.url);
        params.method = HttpType.DELETE;
        //根据不同的api采用并行模式进行开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.executeOnExecutor(THREAD_POOL_EXECUTOR);
        } else {
            this.execute();
        }
    }

    /**
     * 下载图片
     */
    public void downImg() {
        if (params == null) {
            throw new NullPointerException("params can not null!");
        }
        if (SLog.debug) SLog.d("Down Img：" + params.url);
        params.method = HttpType.DOWN_IMAGE;
        //根据不同的api采用并行模式进行开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.executeOnExecutor(THREAD_POOL_EXECUTOR);
        } else {
            this.execute();
        }
    }

    /**
     * 下载文件
     */
    public void downFile() {
        if (params == null) {
            throw new NullPointerException("params can not null!");
        }
        if (SLog.debug) SLog.d("Down File：" + params.url);
        params.method = HttpType.DOWN_FILE;
        //根据不同的api采用并行模式进行开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.executeOnExecutor(THREAD_POOL_EXECUTOR);
        } else {
            this.execute();
        }
    }

    /**
     * 上传
     */
    public void uploadFile() {
        if (params == null) {
            throw new NullPointerException("params can not null!");
        }
        if (SLog.debug) SLog.d("Upload File：" + params.url);
        params.method = HttpType.UPLOAD;
        //根据不同的api采用并行模式进行开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.executeOnExecutor(THREAD_POOL_EXECUTOR);
        } else {
            this.execute();
        }
    }

    /**
     * 关闭网络链接
     */
    public void discount() {
        if (hc != null) {
            hc.disconnect();
        }
    }
}

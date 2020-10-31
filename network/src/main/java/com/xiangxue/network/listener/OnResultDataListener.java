package com.xiangxue.network.listener;


import com.xiangxue.network.model.RequestWork;
import com.xiangxue.network.model.ResponseWork;

/**
 * Created by tangyx on 16/7/18.
 *
 */
public interface OnResultDataListener {

    void onResult(RequestWork req, ResponseWork resp) throws Exception;
}

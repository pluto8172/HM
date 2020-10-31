package com.pluto.network.listener;


import com.pluto.network.model.RequestWork;
import com.pluto.network.model.ResponseWork;

/**
 * Created by tangyx on 16/7/18.
 *
 */
public interface OnResultDataListener {

    void onResult(RequestWork req, ResponseWork resp) throws Exception;
}

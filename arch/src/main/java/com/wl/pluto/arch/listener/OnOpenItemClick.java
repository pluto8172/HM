package com.wl.pluto.arch.listener;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by tangyx on 15/11/25.
 */
public interface OnOpenItemClick {
    void onOpenItemClick(AdapterView<?> parent, View view, int position, long id);
}

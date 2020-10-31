package com.wl.pluto.component.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * @author szy
 * Created on 2019-08-22
 * @function 过滤点击事件，LinearLayout上层的子view可以正常收到相关的点击事件，但是放在LinearLayout下层的所有控件都不能收到事件，事件会被该LinearLayout消化掉
 */
public class NoTouchLinearLayout extends LinearLayout {
    public NoTouchLinearLayout(Context context) {
        super(context);
    }

    public NoTouchLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoTouchLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}

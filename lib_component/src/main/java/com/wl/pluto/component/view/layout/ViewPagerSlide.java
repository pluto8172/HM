package com.wl.pluto.component.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by szy on 2019-06-18
 * @function 可以禁止手动滑动的viewPager
 */
public class ViewPagerSlide extends ViewPager {

    //是否可以进行滑动
    private boolean isSlide = false;

    public void setSlide(boolean slide) {
        isSlide = slide;
    }


    public ViewPagerSlide(@NonNull Context context) {
        super(context);
    }

    public ViewPagerSlide(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isSlide) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return isSlide;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isSlide) {
            return super.onTouchEvent(ev);
        } else {
            return isSlide;
        }
    }
}

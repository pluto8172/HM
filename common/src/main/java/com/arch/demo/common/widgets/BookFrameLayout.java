package com.arch.demo.common.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author szy
 * Created on 2019-10-11
 * @function
 */
public class BookFrameLayout extends FrameLayout {

    public BookFrameLayout(@NonNull Context context) {
        super(context);
    }

    public BookFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BookFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (width * 293) / 260;
        setMeasuredDimension(width, height);
    }
}

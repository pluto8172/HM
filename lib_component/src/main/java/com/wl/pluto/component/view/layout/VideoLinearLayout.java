package com.wl.pluto.component.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * @author szy
 * Created on 2019-08-22
 * @function
 *
 * 16：9 的LinearLayout
 *
 */
public class VideoLinearLayout extends LinearLayout {
    public VideoLinearLayout(Context context) {
        super(context);
    }

    public VideoLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (width * 9) / 16;
        setMeasuredDimension(width, height);
    }
}

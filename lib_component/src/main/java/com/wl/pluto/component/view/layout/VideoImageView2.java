package com.wl.pluto.component.view.layout;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * 垂直方向的16：9图片控件
 */
public class VideoImageView2 extends AppCompatImageView {

    public VideoImageView2(Context context) {
        super(context);
    }

    public VideoImageView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoImageView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (width * 16) / 9;
        setMeasuredDimension(width, height);
    }
}

package com.wl.pluto.component.view.layout;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * 长宽比为16：9  的图片控件
 */
public class VideoImageView extends AppCompatImageView {

    public VideoImageView(Context context) {
        super(context);
    }

    public VideoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
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

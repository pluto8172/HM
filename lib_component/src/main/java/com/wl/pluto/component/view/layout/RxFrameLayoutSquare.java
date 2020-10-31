package com.wl.pluto.component.view.layout;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 方形的Fragment布局
 */
public class RxFrameLayoutSquare extends FrameLayout {

    public RxFrameLayoutSquare(Context context) {
        super(context);
    }

    public RxFrameLayoutSquare(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RxFrameLayoutSquare(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RxFrameLayoutSquare(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //noinspection SuspiciousNameCombination
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}

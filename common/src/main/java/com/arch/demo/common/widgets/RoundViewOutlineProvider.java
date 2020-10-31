package com.arch.demo.common.widgets;

import android.graphics.Outline;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewOutlineProvider;


/**
 * ================================================
 *
 * @function 视图圆形裁剪
 * Created by plout on 2019/05/05.
 * ================================================
 */
public class RoundViewOutlineProvider extends ViewOutlineProvider {
    private float mRadius;

    public RoundViewOutlineProvider(float radius) {
        this.mRadius = radius;
    }

    @Override
    public void getOutline(View view, Outline outline) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        int leftMargin = 0;
        int topMargin = 0;
        Rect selfRect = new Rect(leftMargin, topMargin,
                rect.right - rect.left - leftMargin,
                rect.bottom - rect.top - topMargin
        );
        outline.setRoundRect(selfRect, mRadius);
    }
}

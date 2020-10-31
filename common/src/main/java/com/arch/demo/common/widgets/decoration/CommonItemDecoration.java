package com.arch.demo.common.widgets.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ================================================
 *
 * @function 分割线
 * Created by plout on 2019/02/15.
 * ================================================
 */
public class CommonItemDecoration extends RecyclerView.ItemDecoration {
    private int pxTop = 0;
    private int pxBottom = 0;
    private int pxLeft = 0;
    private int pxRight = 0;

    public CommonItemDecoration(int pxBottom) {
        this.pxBottom = pxBottom;
    }

    public CommonItemDecoration(int pxTop, int pxBottom, int pxLeft, int pxRight) {
        this.pxTop = pxTop;
        this.pxBottom = pxBottom;
        this.pxLeft = pxLeft;
        this.pxRight = pxRight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(pxLeft, pxTop, pxRight, pxBottom);
    }
}

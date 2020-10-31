package com.arch.demo.common.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;

import rxtool.RxDeviceTool;


/**
 * ================================================
 *
 * @function <p>
 * Created by szy on 2019-07-04
 * ================================================
 */
public class FreeView extends AppCompatImageView {

    private OnPuzzleSuccessListener puzzleSuccessListener;

    public interface OnPuzzleSuccessListener {
        void onClickListener();

    }

    /**
     * 距离目标地点的最大距离，小于这个值就自动吸附过去
     */
    private static final int MAX_DISTANCE = 50;

    private int lastX = 0;
    private int lastY = 0;

    private int dx;
    private int dy;
    private float movex = 0;
    private float movey = 0;

    private int screenWidth;
    private int screenHeight;

    private int originLeft;
    private int originTop;

    /**
     * 拼图图片的类型，共有4个图片，每天图片判断边界的条件不一样，
     * <p>
     * 第一张图片判断左上角
     * 第二张图片判断右上角
     * 第三张图片判断左下角
     * 第四张图片判断右下角
     */

    public void setPuzzleSuccessListener(OnPuzzleSuccessListener listener) {
        puzzleSuccessListener = listener;
    }




    //是否拖动
    private boolean isCanDrag = true;

    //是否可以松手后恢复到原来到位置
    private boolean isCanReset = true;

    public FreeView(Context context) {
        super(context);
        init();
    }

    public FreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public boolean isCanDrag() {
        return isCanDrag;
    }

    private void init() {
        screenWidth = RxDeviceTool.getScreenWidth();
        screenHeight = RxDeviceTool.getScreenHeight();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }


    @Override
    public boolean callOnClick() {
        return super.callOnClick();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isCanDrag) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    originLeft = getLeft();
                    originTop = getTop();
                    Log.i("----ACTION_DOWN->", "originLeft = " + originLeft + " originTop = " + originTop);

                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    movex = lastX;
                    movey = lastY;

                    break;
                case MotionEvent.ACTION_MOVE:

                    dx = (int) event.getRawX() - lastX;
                    dy = (int) event.getRawY() - lastY;

                    //Log.i("----->", "dx = " + dx + " dy = " + dy);


                    int left = getLeft() + dx;
                    int top = getTop() + dy;

                    int right = getRight() + dx;
                    int bottom = getBottom() + dy;
                    // Log.i("----ACTION_MOVE->", "left = " + left + " top = " + top + " right =" + right + " bottom = " + bottom);

                    //判断边界值
                    if (left < 0) {
                        left = 0;
                        right = left + getWidth();
                    }
                    if (right > screenWidth) {
                        right = screenWidth;
                        left = right - getWidth();
                    }
                    if (top < 0) {
                        top = 0;
                        bottom = top + getHeight();
                    }
                    if (bottom > screenHeight) {
                        bottom = screenHeight;
                        top = bottom - getHeight();
                    }


                    layout(left, top, right, bottom);
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                default:
                     Log.i("----ACTION_UP->", "");
                    if (isCanReset) {
                        Log.i("----ACTION_UP->", "originLeft = " + originLeft + " lastX = " + lastX);
                        if (Math.abs(getLeft() - originLeft) < MAX_DISTANCE || Math.abs(getTop() - originTop) < MAX_DISTANCE) {
                            Log.i("----ACTION_UP->", "响应点击");
                            return callOnClick();
                        }
                    }
                    break;
            }
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }


}

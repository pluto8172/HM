package com.arch.demo.common.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by szy on 2019-07-10
 *
 * @function 禁止手动滑动的RecyclerView
 */
class XRecyclerView : RecyclerView {

    //是否可以进行滑动
    private val isSlide = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return isSlide
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return isSlide
    }
}

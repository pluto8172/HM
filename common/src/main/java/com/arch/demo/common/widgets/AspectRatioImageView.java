package com.arch.demo.common.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.arch.demo.common.R;


/**
 * @author Ivan
 * created on :2018/8/17 13:39
 * description:宽高比imageview
 */
public class AspectRatioImageView extends AppCompatImageView {

    /**
     * 宽高比
     */
    private float ratio;

    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView);
        ratio = typedArray.getFloat(R.styleable.AspectRatioImageView_imageRatio, 0.0f);
        //使用完TypedArray，记得回收
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取宽度的模式和尺寸
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //获取高度的模式和尺寸
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY && ratio != 0) {
            //宽确定，高不确定
            heightSize = (int) (widthSize / ratio + 0.5f);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        } else if (widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY && ratio != 0) {
            //高确定，宽不确定
            widthSize = (int) (heightSize * ratio + 0.5f);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        } else {

        }
        //保存宽高数据
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }
}

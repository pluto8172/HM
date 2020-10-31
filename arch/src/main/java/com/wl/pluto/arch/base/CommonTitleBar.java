package com.wl.pluto.arch.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.wl.pluto.arch.R;


/**
 * 通用的topbar
 */
public class CommonTitleBar extends FrameLayout {

    public final static int DEFAULT_TEXT_COLOR = Color.BLACK;
    private AppCompatImageView mBackImage;
    private AppCompatTextView mTitleView;
    private AppCompatTextView mRightText;
   // private AppCompatImageView mRightImage;

    public CommonTitleBar(@NonNull Context context) {
        this(context, null);
    }

    public CommonTitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull Context context,  @Nullable AttributeSet attrs){
        View view =  LayoutInflater.from(context).inflate(R.layout.common_top_bar_layout, this, true);
        mBackImage = view.findViewById(R.id.iv_back_image);
        mTitleView = view.findViewById(R.id.tv_title_text);
        mRightText = view.findViewById(R.id.tv_right_text);

        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.CommonTitleBar);
        String title = arr.getString(R.styleable.CommonTitleBar_ctb_title);
        String rightTitle = arr.getString(R.styleable.CommonTitleBar_ctb_right_text);

        int rightTextColor = arr.getColor(R.styleable.CommonTitleBar_ctb_right_text_color,DEFAULT_TEXT_COLOR);
        arr.recycle();

        if(!TextUtils.isEmpty(title)){
            mTitleView.setText(title);
        }

        if(!TextUtils.isEmpty(rightTitle)){
            mRightText.setText(rightTitle);
            mRightText.setTextColor(rightTextColor);
        }
    }

    public void setBackImage(int resId){
        if(mBackImage != null){
            mBackImage.setImageResource(resId);
        }
    }

    public void setRightImage(int resId){
        if(mRightText!= null && resId>0){
            mRightText.setCompoundDrawablesRelativeWithIntrinsicBounds(resId, 0, 0, 0);
        }
    }

    public void setRightTextVisible(boolean visible){
        if(mRightText != null){
            if(visible){
                mRightText.setVisibility(VISIBLE);
            }else {
                mRightText.setVisibility(GONE);
            }
        }
    }



    public void setTitle(String title){
        if(mTitleView != null){
            mTitleView.setText(title);
        }
    }

    public void setRightTextColor(int color){
        if(mRightText != null){
            mRightText.setTextColor(color);
        }
    }
    public void setRightText(String text){
        if(mRightText != null){
            mRightText.setText(text);
        }
    }

    public View getRightTextView(){
        return mRightText;
    }



    public void setOnBackListener(View.OnClickListener listener){
        if(mBackImage != null){
            mBackImage.setOnClickListener(listener);
        }
    }

    public void setOnRightTextListener(View.OnClickListener listener){
        if(mRightText != null){
            mRightText.setOnClickListener(listener);
        }
    }


}

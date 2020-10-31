package com.plout.module_login.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

import com.plout.module_login.R;


public class PasswordView extends FrameLayout {

    private AppCompatEditText mEditText;
    private AppCompatImageView mImage;
    private View mUnderLine;

    private boolean isShowPwd = false;
    private boolean isShowLine = true;
    private boolean isShowImage = true;

    public PasswordView(Context context) {
        this(context, null);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PasswordView, defStyleAttr, 0);
            try {
                isShowLine = styledAttributes.getBoolean(R.styleable.PasswordView_pwd_showLine, true);
                isShowImage = styledAttributes.getBoolean(R.styleable.PasswordView_pwd_showImage, true);

            } finally {
                styledAttributes.recycle();
            }
        }

        View layout = View.inflate(context, R.layout.widget_password_layout, null);
        mEditText = layout.findViewById(R.id.et_code);
        mImage = layout.findViewById(R.id.iv_image);
        if(isShowImage){
            mImage.setVisibility(VISIBLE);
        }else {
            mImage.setVisibility(GONE);
        }

        mUnderLine = layout.findViewById(R.id.view_nuder_line);
        if (isShowLine) {
            mUnderLine.setVisibility(VISIBLE);
        } else {
            mUnderLine.setVisibility(GONE);
        }

        mImage.setOnClickListener(v -> {
            //如果
            togglePasswordIconVisibility();

        });
        addView(layout);
    }

    private void togglePasswordIconVisibility() {
        isShowPwd = !isShowPwd;
        handlePasswordInputVisibility();
        if (isShowPwd) {
            mImage.setImageResource(R.mipmap.ic_pwd_y);
        } else {
            mImage.setImageResource(R.mipmap.ic_pwd_n);
        }
    }


    /**
     * This method is called when restoring the state (e.g. on orientation change)
     */
    private void handlePasswordInputVisibility() {
        int selectionStart = mEditText.getSelectionStart();
        int selectionEnd = mEditText.getSelectionEnd();
        if (isShowPwd) {
            mEditText.setTransformationMethod(null);
        } else {
            mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }
        mEditText.setSelection(selectionStart, selectionEnd);

    }

    public AppCompatEditText getEditText(){
        return mEditText;
    }
    public String getText(){
        return mEditText.getText().toString().trim();
    }

}

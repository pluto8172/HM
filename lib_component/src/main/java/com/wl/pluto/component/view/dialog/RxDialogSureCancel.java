package com.wl.pluto.component.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wl.pluto.component.R;


/**
 * @author vondear
 * @date 2016/7/19
 * 确认 取消 Dialog
 */
public class RxDialogSureCancel extends RxDialog {

    private ImageView mIvLogo;
    private TextView mTvContent;
    private TextView mTvSure;
    private TextView mTvCancel;
    private TextView mTvTitle;

    public RxDialogSureCancel(Context context, int themeResId) {
        super(context, themeResId);
        initView(1);
    }

    public RxDialogSureCancel(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(1);
    }

    public RxDialogSureCancel(Context context) {
        super(context);
        initView(1);
    }

    public RxDialogSureCancel(Activity context, int type) {
        super(context);
        initView(type);
    }

    public RxDialogSureCancel(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView(1);
    }

    public ImageView getLogoView() {
        return mIvLogo;
    }

    public void setTitle(String title) {
        if(mTvTitle != null){
            mTvTitle.setText(title);
        }
    }

    public TextView getTitleView() {
        return mTvTitle;
    }

    public void setContent(String content) {
        this.mTvContent.setText(content);
    }

    public TextView getContentView() {
        return mTvContent;
    }

    public void setSure(String strSure) {
        this.mTvSure.setText(strSure);
    }

    public TextView getSureView() {
        return mTvSure;
    }

    public void setCancel(String strCancel) {
        this.mTvCancel.setText(strCancel);
    }

    public TextView getCancelView() {
        return mTvCancel;
    }

    public void setSureListener(View.OnClickListener sureListener) {
        mTvSure.setOnClickListener(sureListener);
    }

    public void setCancelListener(View.OnClickListener cancelListener) {
        mTvCancel.setOnClickListener(cancelListener);
    }

    private void initView(int type) {
        View dialogView;
        if(type ==1){
            dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_two_button_layout, null);

        }else {
            dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_two_button_layout2, null);
        }

        mTvTitle = dialogView.findViewById(R.id.tv_title);
        mTvSure = dialogView.findViewById(R.id.tv_sure);
        mTvCancel = dialogView.findViewById(R.id.tv_cancel);
        mTvContent = dialogView.findViewById(R.id.tv_content);
        mTvContent.setTextIsSelectable(true);

        setContentView(dialogView);
    }
}

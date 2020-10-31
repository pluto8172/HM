package com.wl.pluto.arch.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

/**
 * Created by Ivan on 18-5-28.
 */

public class AlertController {


    private CostumerDialog mDialog;
    private Window mWindow;
    private DialogViewHelper mViewHelper;

    public AlertController(CostumerDialog costumerDialog, Window window) {
        this.mDialog = costumerDialog;
        this.mWindow = window;
    }


    public void setText(int viewId, CharSequence text) {

        mViewHelper.setText(viewId, text);

    }

    public void setOnClickListener(int viewId, WeakReference<View.OnClickListener> onClickListenerWeakReference) {
        mViewHelper.setOnClickListener(viewId, onClickListenerWeakReference);
    }

    public void setViewHelper(DialogViewHelper viewHelper) {
        mViewHelper = viewHelper;
    }

    public <T extends View> T getView(int viewId) {
        return mViewHelper.getView(viewId);
    }

    public void setOnDismissListener(View.OnClickListener onDismissListener) {
        mViewHelper.setOnDismissListener(onDismissListener);
    }

    public void setTextColor(int viewId, int color) {
        mViewHelper.setTextColor(viewId, color);
    }

    public static class AlertParams {

        public Context mContext;
        public int mThemeResId;
        public boolean mCancelable = true;
        public boolean mOutTouchCancelable = false;

        public DialogInterface.OnCancelListener mOnCancelListener;
        public DialogInterface.OnDismissListener mOnDismissListener;
        public DialogInterface.OnKeyListener mOnKeyListener;
        public View mView;
        public int mViewLayoutResId;

        public SparseArray<CharSequence> mTextArray = new SparseArray<>();
        public SparseArray<Integer> mTextColorArray = new SparseArray<>();
        public SparseArray<Integer> mVisibilityArray = new SparseArray<>();

        // to avoid leak canery
        public SparseArray<WeakReference<View.OnClickListener>> mListenerArray = new SparseArray();
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        public int mGravity = Gravity.CENTER;
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        public int mAnimations = 0;
        public int color = Color.parseColor("#333333");

        public AlertParams(Context context, int themeResId) {
            this.mContext = context;
            this.mThemeResId = themeResId;
        }


        /**
         * @param mAlert
         */
        public void apply(AlertController mAlert) {
            //1. set layout

            DialogViewHelper viewHelper = null;
            if (mViewLayoutResId != 0) {
                viewHelper = new DialogViewHelper(mContext, mViewLayoutResId);
            }


            if (mView != null) {
                viewHelper = new DialogViewHelper();
                viewHelper.setContentView(mView);
            }

            if (viewHelper == null) {
                throw new IllegalArgumentException("please call setContentView() ");
            }

            // 设置Controller的辅助类
            mAlert.setViewHelper(viewHelper);


            mAlert.getDialog().setContentView(viewHelper.getContentView());


            //2. set text
            int textArraysize = mTextArray.size();
            int textColorArraysize = mTextColorArray.size();
            for (int i = 0; i < textArraysize; i++) {
                mAlert.setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }
            for (int i = 0; i < textColorArraysize; i++) {
                mAlert.setTextColor(mTextColorArray.keyAt(i), mTextColorArray.valueAt(i));
            }
            //3. set onclicklistener


            int clickArraySize = mListenerArray.size();
            for (int i = 0; i < clickArraySize; i++) {
                mAlert.setOnClickListener(mListenerArray.keyAt(i), mListenerArray.valueAt(i));
            }
            //4. set text
            int visibilityArraySize = mVisibilityArray.size();
            for (int i = 0; i < visibilityArraySize; i++) {
                mAlert.setViewVisibility(mVisibilityArray.keyAt(i), mVisibilityArray.valueAt(i));
            }
            //5. set appConfig  fullWidth
            Window window = mAlert.getWindow();
            window.setGravity(mGravity);

            if (mAnimations != 0) {
                window.setWindowAnimations(mAnimations);
            }

            WindowManager.LayoutParams params = window.getAttributes();

            params.width = mWidth;
            params.height = mHeight;
            window.setAttributes(params);


        }
    }

    private void setViewVisibility(int viewId, int viewVisibility) {
        mViewHelper.setViewVisibility(viewId, viewVisibility);
    }

    public CostumerDialog getDialog() {
        return mDialog;
    }


    public Window getWindow() {
        return mWindow;
    }


}

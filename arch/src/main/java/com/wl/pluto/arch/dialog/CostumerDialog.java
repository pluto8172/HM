package com.wl.pluto.arch.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.wl.pluto.arch.R;

import java.lang.ref.WeakReference;

/**
 * Created by Ivan on 18-5-28.
 */

public class CostumerDialog extends Dialog {


    private Context mContext;
    private AlertController mAlert;


    public CostumerDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
        mAlert = new AlertController(this, getWindow());
    }

    public CostumerDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mAlert = new AlertController(this, getWindow());
    }

    public CostumerDialog setText(int viewId, CharSequence text) {

        mAlert.setText(viewId, text);
        return this;

    }

    public CostumerDialog setOnClickListener(int viewId, View.OnClickListener onClickListener) {
        mAlert.setOnClickListener(viewId, new WeakReference<>(onClickListener));
        return this;
    }

    /**
     * 为TextView设置字体颜色
     *
     * @param viewId
     * @param color  not resource color
     * @return
     */
    public CostumerDialog setTextColor(int viewId, int color) {
        mAlert.setTextColor(viewId, color);
        return this;
    }


    public CostumerDialog setMyOnDismissListener(OnDismissListener onDismissListener) {
        super.setOnDismissListener(onDismissListener);
        return this;
    }

    public <T extends View> T getView(int viewId) {
        return mAlert.getView(viewId);
    }


    public static class Builder {

        private final AlertController.AlertParams P;

        public Builder(Context context) {
            this(context, R.style.custom_dialog);
        }

        public Builder(Context context, int themeResId) {
            P = new AlertController.AlertParams(context, themeResId);
        }


        public Builder setContentView(View view) {
            P.mView = view;
            P.mViewLayoutResId = 0;
            return this;
        }

        public Builder setContentView(int layoutId) {
            P.mView = null;
            P.mViewLayoutResId = layoutId;
            return this;
        }

        public Builder setText(int viewId, CharSequence text) {
            if (viewId > 0 && !TextUtils.isEmpty(text)) {
                P.mTextArray.put(viewId, text);
            }
            return this;
        }

        public Builder setTextColor(int viewId, int color) {
            P.mTextColorArray.put(viewId, color);
            return this;
        }

        public Builder setViewVisibility(int viewId, int viewVisibility) {
            P.mVisibilityArray.put(viewId, viewVisibility);
            return this;
        }

        public Builder setOnClickListener(int viewId, View.OnClickListener listener) {
            P.mListenerArray.put(viewId, new WeakReference<View.OnClickListener>(listener));
            return this;
        }


        //
        public Builder fullWidth() {
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        public Builder fullHeight() {
            P.mHeight = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        public Builder setOutTouchCancelable(boolean outTouchCancelable) {
            P.mOutTouchCancelable = outTouchCancelable;
            return this;
        }

        //
        public Builder fromBottom(boolean isAnimation) {
            if (isAnimation) {
//               P.mAnimations =
            }
            P.mGravity = Gravity.BOTTOM;
            return this;
        }

        //
        public Builder setWidthAndHeight(int width, int height) {
            P.mWidth = width;
            P.mHeight = height;
            return this;
        }

        public Builder addDefaultAnimation() {
//            P.mAnimations = xxx;
            return this;
        }

        public Builder addAnimations(int animation) {
            P.mAnimations = animation;
            return this;
        }

        public CostumerDialog create() {
            CostumerDialog dialog = new CostumerDialog(P.mContext, P.mThemeResId);
            P.apply(dialog.mAlert);
            dialog.setCanceledOnTouchOutside(P.mOutTouchCancelable);
            dialog.setCancelable(P.mCancelable);


            dialog.setOnCancelListener(P.mOnCancelListener);
            dialog.setOnDismissListener(P.mOnDismissListener);

            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return dialog;
        }

        public CostumerDialog show() {
            CostumerDialog costumerDialog = create();
            costumerDialog.show();
            return costumerDialog;
        }

        public Builder setWidthAndHeightPercent(float widthPercent, float heightPercent) {
            if (widthPercent != 0) {
                //P.mWidth = (int) (ScreenUtils.getScreenWidth() * widthPercent);
            }
            if (heightPercent != 0) {
                // P.mHeight = (int) (ScreenUtils.getScreenHeight() * heightPercent);
            }
            return this;
        }
    }

}

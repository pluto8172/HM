package com.wl.pluto.arch.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by Ivan on 18-5-28.
 */

public class DialogViewHelper {
    private View mContentView;
    private SparseArray<WeakReference<View>> mViews;

    public DialogViewHelper(Context mContext, int mViewLayoutResId) {
        this();
        mContentView = LayoutInflater.from(mContext).inflate(mViewLayoutResId, null);
    }

    public DialogViewHelper() {
        mViews = new SparseArray<>();
    }

    public void setContentView(View mView) {
        this.mContentView = mView;
    }

    public void setText(int viewId, CharSequence text) {
        TextView tv = mContentView.findViewById(viewId);
        if (tv != null) {
            tv.setText(text);
        }

    }

    public void setOnClickListener(int viewId, WeakReference<View.OnClickListener> onClickListenerWeakReference) {
        View view = mContentView.findViewById(viewId);
        if (view != null) {
            view.setOnClickListener(onClickListenerWeakReference.get());
        }

    }

    public View getContentView() {
        return mContentView;
    }

    public <T extends View> T getView(int viewId) {
        WeakReference<View> viewWeakReference = mViews.get(viewId);
        View view = null;
        if (viewWeakReference != null) {
            view = viewWeakReference.get();
        }

        if (view == null) {
            view = mContentView.findViewById(viewId);
            if (view != null) {
                mViews.put(viewId, new WeakReference<>(view));
            }
        }

        return (T) view;
    }

    public void setOnDismissListener(View.OnClickListener onDismissListener) {

    }

    public void setTextColor(int viewId, int color) {
        TextView tv = mContentView.findViewById(viewId);
        if (tv != null) {
            tv.setTextColor(color);
        }
    }

    public void setViewVisibility(int viewId, int viewVisibility) {
        mContentView.findViewById(viewId).setVisibility(viewVisibility);
    }
}

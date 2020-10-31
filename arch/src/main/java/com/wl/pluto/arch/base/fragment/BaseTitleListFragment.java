package com.wl.pluto.arch.base.fragment;

import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.wl.pluto.arch.R;

/**
 * 代标题的列表
 *
 * @param <T>
 * @param <VH>
 */
public abstract class BaseTitleListFragment<T, VH extends BaseViewHolder> extends BaseListFragment2<T, VH> {


    TextView mTitle;

    @Override
    public int getLayoutId() {
        return R.layout.base_fragment_title_list_layout;
    }

    @Override
    public void initView() {
        super.initView();
        mTitle = findView(R.id.tv_title);
    }

    public void setTitle(String title) {
        if (mTitle != null) {
            mTitle.setText(title);
        }
    }

}

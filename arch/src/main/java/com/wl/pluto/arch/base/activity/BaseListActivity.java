package com.wl.pluto.arch.base.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.wl.pluto.arch.R;
import com.wl.pluto.arch.base.CommonTitleBar;

import java.util.List;

public abstract class BaseListActivity<T, VH extends BaseViewHolder> extends BaseActivity {


    protected RecyclerView mRecyclerView;
    protected SmartRefreshLayout mRefreshLayout;
    protected CommonTitleBar mTopBar;
    protected TextView mEmptyView;
    protected BaseQuickAdapter<T, VH> mRecycleViewAdapter;

    /**
     * 当前的页码
     */
    protected int mCurrentPage = 1;

    @Override
    public int getLayoutId() {
        return R.layout.base_activity_list_layout;
    }

    @Override
    public void initView() {
        mRecyclerView = findViewById(R.id.rcv_recycler_view);
        mRefreshLayout = findViewById(R.id.srl_refreshLayout);
        mTopBar = findViewById(R.id.tb_top_bar);
        mEmptyView = findViewById(R.id.tv_empty_view);
        initLayout();
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (isNeedAutoRefresh()) { //startLoadingAnimation();
            onLoadData(mCurrentPage);
        }
    }

    public abstract BaseQuickAdapter<T, VH> getRecycleViewAdapter();

    public abstract void onLoadData(int pageIndex);

    public boolean isFixedItem() {
        return true;
    }

    public void setTitle(String title) {
        mTopBar.setTitle(title);
    }

    public void setRightClickListener(View.OnClickListener listener) {
        mTopBar.setOnRightTextListener(listener);
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
    }


    public RecyclerView.ItemDecoration getRecyclerItemDecoration() {
        return null;
    }

    /**
     * 提供重写 设置是否 下拉刷新
     */
    public boolean canRefresh() {
        return true;
    }

    /**
     * 提供重写 设置是否 上拉加载
     */
    public boolean canLoadMore() {
        return true;
    }

    /**
     * 是否需要自动加载数据
     *
     * @return
     */
    public boolean isNeedAutoRefresh() {
        return false;
    }


    //TODO 这里应该是放页面中间的加载动画
    public void onShowLoading() {
    }

    public void onHideLoading() {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
        //hideLoadingDialog()
    }

    public void setRecyclerViewBackgroundColor(int color) {
        if (mRecyclerView != null) {
            mRecyclerView.setBackgroundColor(color);
        }
    }

    public void onShowNetError() {
        if (mRecycleViewAdapter.getData().size() <= 0) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    public void onRefreshView() {
        onShowLoading();
        mCurrentPage = 1;
        onLoadData(mCurrentPage);
    }

    public void onShowNoMore() {
        mRefreshLayout.finishLoadMoreWithNoMoreData();
    }

    private void initLayout() {

        mTopBar.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecyclerView.setHasFixedSize(isFixedItem());
        if (canRefresh()) {
            mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull com.scwang.smart.refresh.layout.api.RefreshLayout refreshLayout) {
                    mCurrentPage = 1;
                    onLoadData(mCurrentPage);
                }
            });
        } else {
            mRefreshLayout.setEnableRefresh(false);
        }
        if (canLoadMore()) {
            mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    mCurrentPage++;
                    onLoadData(mCurrentPage);
                }
            });
        } else {
            mRefreshLayout.setEnableLoadMore(false);
        }
        if (mRecycleViewAdapter == null) {
            mRecycleViewAdapter = getRecycleViewAdapter();
        }

        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.setAdapter(mRecycleViewAdapter);
        RecyclerView.ItemDecoration itemDecoration = getRecyclerItemDecoration();
        if (itemDecoration != null) {
            mRecyclerView.addItemDecoration(itemDecoration);
        }


        if (mEmptyView != null) {
            mEmptyView.setOnClickListener(v -> loadContentData());
        }

    }


    /**
     * 需要在子类中主动去调用该方法来获取数据
     */
    public void loadContentData() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.GONE);
        }
        //加载数据
        onRefreshView();
    }

    /**
     * 在处理List 中 第一页就没有数据，会自动调用
     */
    public void showEmptyLayout(String emptyText, int emptyImageRes) {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(emptyText)) {
                mEmptyView.setText(emptyText);
            }
            if (emptyImageRes > 0) {
                mEmptyView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, emptyImageRes, 0, 0);
            }
        }
    }

    /**
     * 隐藏空页面
     */
    public void hideEmptyLayout() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    public void handleListData(List<T> listData) {
        handleListData(listData, "");
    }

    public void handleListData(List<T> listData, String emptyText) {
        handleListData(listData, emptyText, -1);
    }

    /**
     * 统一处理 List 数据
     *
     * @param listData 返回的数据
     */
    public void handleListData(List<T> listData, String emptyText, int emptyImageRes) {
        try {
            if (mCurrentPage > 1) {
                mRefreshLayout.finishLoadMore();
                if (listData != null && listData.size() != 0) {
                    mRecycleViewAdapter.addData(listData);
                } else {
                    mRefreshLayout.finishLoadMoreWithNoMoreData();
                }
            } else {
                if (listData != null && listData.size() != 0) {
                    mRecycleViewAdapter.setNewData(listData);
                    mRecycleViewAdapter.notifyDataSetChanged();
                    hideEmptyLayout();
                } else {
                    mRecycleViewAdapter.setNewData(null);
                    mRefreshLayout.setEnableLoadMore(false);
                    showEmptyLayout(emptyText, emptyImageRes);
                }
                mRefreshLayout.finishRefresh();
            }
            mRecycleViewAdapter.notifyDataSetChanged();
            onHideLoading();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

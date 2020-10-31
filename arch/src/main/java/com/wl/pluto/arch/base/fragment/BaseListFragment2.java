package com.wl.pluto.arch.base.fragment;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.wl.pluto.arch.R;

import java.util.List;

public abstract class BaseListFragment2<T, VH extends BaseViewHolder> extends BaseFragment {


    protected RelativeLayout mContentLayout;
    protected RecyclerView mRecyclerView;
    protected SmartRefreshLayout mRefreshLayout;
    protected TextView mEmptyView;
    protected BaseQuickAdapter<T, VH> mRecycleViewAdapter;

    /**
     * 当前的页码
     */
    int mCurrentPage = 1;

    @Override
    public int getLayoutId() {
        return R.layout.base_fragment_list_layout;
    }

    @Override
    public void initView() {
        mContentLayout = findView(R.id.ll_content_layout);
        mRecyclerView = findView(R.id.rcv_recycler_view);
        mRefreshLayout = findView(R.id.srl_refreshLayout);
        mEmptyView = findView(R.id.tv_empty_view);
        initLayout();
    }

    abstract BaseQuickAdapter<T, VH> getRecycleViewAdapter();

    abstract void onLoadData(int pageIndex);

    public boolean isFixedItem() {
        return true;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false);
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

    public boolean isNeedAutoRefresh() {
        return false;
    }


    //TODO 这里应该是放页面中间的加载动画
    public void onShowLoading() {
    }

    public void onHideLoading() {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
        // hideLoadingDialog()
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
        if (isNeedAutoRefresh()) { //startLoadingAnimation();
            onLoadData(mCurrentPage);
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
    public void showEmptyLayout() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
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

    /**
     * 统一处理 List 数据
     *
     * @param listData 返回的数据
     */
    public void handleListData(List<T> listData) {
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
                    showEmptyLayout();
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

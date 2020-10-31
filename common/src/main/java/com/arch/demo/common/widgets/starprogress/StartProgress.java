package com.arch.demo.common.widgets.starprogress;import android.content.Context;import android.content.res.TypedArray;import android.util.AttributeSet;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.ImageView;import android.widget.LinearLayout;import androidx.annotation.NonNull;import androidx.annotation.Nullable;import androidx.recyclerview.widget.RecyclerView;import com.arch.demo.common.R;import java.util.ArrayList;import java.util.List;public class StartProgress extends LinearLayout {    private static final int DEFAULT_MAX = 5;    /**     * 进度最大值     **/    private int mMax = DEFAULT_MAX;    /**     * 进度值     **/    private int mProgress = 3;    private RecyclerView mRecyclerView;    private StarProgressListAdapter mAdapter;    public StartProgress(Context context) {        this(context, null);    }    public StartProgress(Context context, @Nullable AttributeSet attrs) {        this(context, attrs, 0);    }    public StartProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {        super(context, attrs, defStyleAttr);        if (attrs != null) {            initAttributes(context, attrs, defStyleAttr);        }        init(context);    }    public void setProgress(int progress){        mProgress = progress;        if(mAdapter != null){            mAdapter.uploadLightCount(mProgress);        }        invalidate();    }    /**     * 初始化自定义属性     */    private void initAttributes(Context context, AttributeSet attrs, int defStyleAttr) {        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StartProgressBar, defStyleAttr, 0);        try {            mMax = attributes.getInteger(R.styleable.StartProgressBar_spb_total_start, DEFAULT_MAX);            mProgress = attributes.getInteger(R.styleable.StartProgressBar_spb_light_start, 0);            if (mMax <= 0) {                mMax = DEFAULT_MAX;            }            if (mProgress > mMax) {                mProgress = mMax;            } else if (mProgress < 0) {                mProgress = 0;            }        } finally {            attributes.recycle();        }    }    private void init(Context context) {        View rootView = LayoutInflater.from(context).inflate(R.layout.widget_start_progress_bar_layout, this, false);        mRecyclerView = rootView.findViewById(R.id.rcv_recycler_view);        mAdapter = new StarProgressListAdapter(mProgress);        mRecyclerView.setAdapter(mAdapter);        addView(rootView);    }    public class StarProgressListAdapter extends RecyclerView.Adapter<StarProgressListAdapter.ItemViewHolder> {        private List<StarItem> mStarItemList = new ArrayList<>(5);        public StarProgressListAdapter(int lightCount) {            for (int i = 0; i < mMax; i++) {                if (i < lightCount) {                    mStarItemList.add(new StarItem(true));                } else {                    mStarItemList.add(new StarItem(false));                }            }        }        public void uploadLightCount(int count){            for (int i = 0; i < mMax; i++) {                mStarItemList.get(i).isSelect = i < count;            }            notifyDataSetChanged();        }        @NonNull        @Override        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {            View itemLayout = View.inflate(parent.getContext(), R.layout.layout_star_progress_bar_list_item, null);            return new ItemViewHolder(itemLayout);        }        @Override        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {            StarItem item = mStarItemList.get(position);            if (item.isSelect) {                holder.starImage.setImageResource(R.mipmap.ic_progress_start_y);            } else {                holder.starImage.setImageResource(R.mipmap.ic_progress_start_n);            }        }        @Override        public int getItemCount() {            return mMax;        }        public class ItemViewHolder extends RecyclerView.ViewHolder {            ImageView starImage;            public ItemViewHolder(@NonNull View itemView) {                super(itemView);                starImage = itemView.findViewById(R.id.iv_star);            }        }    }}
package com.wl.pluto.component.main.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wl.pluto.component.R;

import java.util.List;

import rxtool.RxActivityTool;
import rxtool.Utils;
import rxtool.view.RxToast;

/**
 * @author vondear
 * @date 2016/11/13
 */

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<ModelMainItem> mValues;

    public MainRecyclerViewAdapter(List<ModelMainItem> items) {
        mValues = items;
    }

    @Override
    public MainRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_main, parent, false);
        context = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mItem = mValues.get(position);

        holder.tvName.setText(holder.mItem.getName());

        Glide.with(context).
                load(holder.mItem.getImage()).
                thumbnail(0.5f).
                into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                RxActivityTool.skipActivity(context, holder.mItem.getActivity());
                RxToast.info("fjladjfldasjfldkajfladsjfl");

            }
        });
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public ModelMainItem mItem;

        ImageView imageView;
        TextView tvName;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            imageView = view.findViewById(R.id.imageView);
            tvName = view.findViewById(R.id.tv_name);
        }
    }
}

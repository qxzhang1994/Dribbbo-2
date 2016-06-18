package com.jiuzhang.guojing.dribbbo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

public class InfiniteAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_LOADING = 1;

    private List<Shot> data;
    private final Context context;
    private final LoadMoreListener loadMoreListener;

    private boolean showLoading;

    public InfiniteAdapter(@NonNull Context context,
                           @NonNull List<Shot> data,
                           @NonNull LoadMoreListener loadMoreListener) {
        this.context = context;
        this.data = data;
        this.loadMoreListener = loadMoreListener;
        this.showLoading = true;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOADING) {
            View view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.list_item_loading, parent, false);
            return new BaseViewHolder(view);
        } else {
            View view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.list_item_shot, parent, false);
            return new ShotViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        final int viewType = getItemViewType(position);
        if (viewType == TYPE_ITEM) {
            ShotViewHolder shotViewHolder = (ShotViewHolder) holder;

            final Shot shot = data.get(position);
            shotViewHolder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShotActivity.class);
                    context.startActivity(intent);
                }
            });
            shotViewHolder.actionBucket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "bucket clicked", Toast.LENGTH_SHORT).show();
                }
            });
            shotViewHolder.image.setImageDrawable(context.getResources().getDrawable(R.mipmap.artboard_5));
            shotViewHolder.likeCount.setText(String.valueOf(shot.likeCount));
            shotViewHolder.bucketCount.setText(String.valueOf(shot.bucketCount));
            shotViewHolder.viewCount.setText(String.valueOf(shot.viewCount));
        } else {
            loadMoreListener.onLoadMore();
        }
    }

    @Override
    public int getItemCount() {
        return showLoading ? data.size() + 1 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (showLoading) {
            return position == data.size() ? TYPE_LOADING : TYPE_ITEM;
        } else {
            return TYPE_ITEM;
        }
    }

    public void append(@NonNull List<Shot> moreData) {
        data.addAll(moreData);
        notifyDataSetChanged();
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }

}

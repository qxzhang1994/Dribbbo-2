package com.jiuzhang.guojing.dribbbo.view.bucket_list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiuzhang.guojing.dribbbo.R;
import com.jiuzhang.guojing.dribbbo.model.Bucket;
import com.jiuzhang.guojing.dribbbo.view.base.BaseViewHolder;
import com.jiuzhang.guojing.dribbbo.view.base.InfiniteAdapter;

import java.util.List;

public class BucketListAdapter extends InfiniteAdapter<Bucket> {

    public BucketListAdapter(@NonNull Context context,
                             @NonNull List<Bucket> data,
                             @NonNull LoadMoreListener loadMoreListener) {
        super(context, data, loadMoreListener);
    }

    @Override
    protected BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext())
                                  .inflate(R.layout.list_item_bucket, parent, false);
        return new BucketViewHolder(view);
    }

    @Override
    protected void onBindItemViewHolder(BaseViewHolder holder, int position) {
        final Bucket bucket = getData().get(position);
        final BucketViewHolder bucketViewHolder = (BucketViewHolder) holder;

        bucketViewHolder.bucketName.setText(bucket.name);
        bucketViewHolder.bucketCount.setText(bucket.shots_count);
    }
}

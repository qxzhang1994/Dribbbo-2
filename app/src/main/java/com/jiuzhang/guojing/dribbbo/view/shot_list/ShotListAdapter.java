package com.jiuzhang.guojing.dribbbo.view.shot_list;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.jiuzhang.guojing.dribbbo.R;
import com.jiuzhang.guojing.dribbbo.model.Shot;
import com.jiuzhang.guojing.dribbbo.utils.ModelUtils;
import com.jiuzhang.guojing.dribbbo.view.shot_detail.ShotActivity;
import com.jiuzhang.guojing.dribbbo.view.base.BaseViewHolder;
import com.jiuzhang.guojing.dribbbo.view.base.InfiniteAdapter;
import com.jiuzhang.guojing.dribbbo.view.shot_detail.ShotFragment;

import java.util.List;

class ShotListAdapter extends InfiniteAdapter<Shot> {

    private final ShotListFragment shotListFragment;

    public ShotListAdapter(@NonNull ShotListFragment shotListFragment,
                           @NonNull List<Shot> data,
                           @NonNull LoadMoreListener loadMoreListener) {
        super(shotListFragment.getContext(), data, loadMoreListener);
        this.shotListFragment = shotListFragment;
    }

    @Override
    protected BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext())
                                  .inflate(R.layout.list_item_shot, parent, false);
        return new ShotViewHolder(view);
    }

    @Override
    protected void onBindItemViewHolder(BaseViewHolder holder, int position) {
        ShotViewHolder shotViewHolder = (ShotViewHolder) holder;

        final Shot shot = getData().get(position);
        shotViewHolder.cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShotActivity.class);
                intent.putExtra(ShotFragment.KEY_SHOT,
                                ModelUtils.toString(shot, new TypeToken<Shot>(){}));
                shotListFragment.startActivityForResult(intent, ShotListFragment.REQ_CODE_SHOT);
            }
        });

        shotViewHolder.likeCount.setText(String.valueOf(shot.likes_count));
        shotViewHolder.bucketCount.setText(String.valueOf(shot.buckets_count));
        shotViewHolder.viewCount.setText(String.valueOf(shot.views_count));

        Glide.with(getContext())
             .load(shot.getImageUrl())
             .placeholder(ContextCompat.getDrawable(shotListFragment.getContext(), R.drawable.shot_placeholder))
             .into(shotViewHolder.image);
    }
}

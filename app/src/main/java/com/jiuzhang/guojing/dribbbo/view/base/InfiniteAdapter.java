package com.jiuzhang.guojing.dribbbo.view.base;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.jiuzhang.guojing.dribbbo.R;
import com.jiuzhang.guojing.dribbbo.model.Shot;
import com.jiuzhang.guojing.dribbbo.utils.ModelUtils;
import com.jiuzhang.guojing.dribbbo.view.ShotActivity;
import com.jiuzhang.guojing.dribbbo.view.shot_detail.ShotFragment;
import com.jiuzhang.guojing.dribbbo.view.shot_list.ShotListFragment;
import com.jiuzhang.guojing.dribbbo.view.shot_list.ShotViewHolder;

import java.util.List;

public class InfiniteAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_LOADING = 1;

    private List<Shot> data;
    private final Context context;
    private final ShotListFragment shotListFragment;
    private final LoadMoreListener loadMoreListener;

    private boolean showLoading;

    public InfiniteAdapter(@NonNull Context context,
                           @NonNull ShotListFragment shotListFragment,
                           @NonNull List<Shot> data,
                           @NonNull LoadMoreListener loadMoreListener) {
        this.context = context;
        this.shotListFragment = shotListFragment;
        this.data = data;
        this.loadMoreListener = loadMoreListener;
        this.showLoading = true;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOADING) {
            View view = LayoutInflater.from(context)
                                      .inflate(R.layout.list_item_loading, parent, false);
            return new BaseViewHolder(view);
        } else {
            View view = LayoutInflater.from(context)
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
                    intent.putExtra(ShotFragment.KEY_SHOT,
                                    ModelUtils.toString(shot, new TypeToken<Shot>(){}));
                    shotListFragment.startActivityForResult(intent, ShotListFragment.REQ_CODE_SHOT);
                }
            });

            shotViewHolder.actionLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (shot.liked) {
                        shotListFragment.unlikeShot(shot.id);
                    } else {
                        shotListFragment.likeShot(shot.id);
                    }
                }
            });
            shotViewHolder.actionBucket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "bucket clicked", Toast.LENGTH_SHORT).show();
                }
            });

            shotViewHolder.likeCount.setText(String.valueOf(shot.likes_count));
            shotViewHolder.bucketCount.setText(String.valueOf(shot.buckets_count));
            shotViewHolder.viewCount.setText(String.valueOf(shot.views_count));

            Glide.with(context)
                 .load(shot.images.get(Shot.IMAGE_NORMAL))
                 .into(shotViewHolder.image);

            shotViewHolder.likeCount.setCompoundDrawablesWithIntrinsicBounds(
                    shot.liked
                            ? context.getResources().getDrawable(R.mipmap.ic_favorite_black_18dp)
                            : context.getResources().getDrawable(R.mipmap.ic_favorite_border_black_18dp)
                    ,
                    null,
                    null,
                    null);
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

    public void setData(@NonNull List<Shot> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public List<Shot> getData() {
        return data;
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }

}

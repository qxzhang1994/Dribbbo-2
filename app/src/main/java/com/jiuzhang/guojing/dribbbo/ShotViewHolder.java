package com.jiuzhang.guojing.dribbbo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShotViewHolder extends BaseViewHolder {

    @BindView(R.id.shot_root) View root;
    @BindView(R.id.shot_action_like) View actionLike;
    @BindView(R.id.shot_action_bucket) View actionBucket;
    @BindView(R.id.shot_like_count) TextView likeCount;
    @BindView(R.id.shot_bucket_count) TextView bucketCount;
    @BindView(R.id.shot_view_count) TextView viewCount;
    @BindView(R.id.shot_image) ImageView image;

    public ShotViewHolder(View itemView) {
        super(itemView);
    }
}

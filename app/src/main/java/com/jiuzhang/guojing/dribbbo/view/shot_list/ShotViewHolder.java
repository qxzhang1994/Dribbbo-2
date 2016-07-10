package com.jiuzhang.guojing.dribbbo.view.shot_list;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiuzhang.guojing.dribbbo.R;
import com.jiuzhang.guojing.dribbbo.view.base.BaseViewHolder;

import butterknife.BindView;

public class ShotViewHolder extends BaseViewHolder {

    @BindView(R.id.shot_root) public View root;
    @BindView(R.id.shot_action_like) public View actionLike;
    @BindView(R.id.shot_action_bucket) public View actionBucket;
    @BindView(R.id.shot_like_count) public TextView likeCount;
    @BindView(R.id.shot_bucket_count) public TextView bucketCount;
    @BindView(R.id.shot_view_count) public TextView viewCount;
    @BindView(R.id.shot_image) public ImageView image;

    public ShotViewHolder(View itemView) {
        super(itemView);
    }
}

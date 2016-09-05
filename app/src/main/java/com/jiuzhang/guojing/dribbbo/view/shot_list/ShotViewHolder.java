package com.jiuzhang.guojing.dribbbo.view.shot_list;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jiuzhang.guojing.dribbbo.R;
import com.jiuzhang.guojing.dribbbo.view.base.BaseViewHolder;

import butterknife.BindView;

class ShotViewHolder extends BaseViewHolder {

    @BindView(R.id.shot_clickable_cover) public View cover;
    @BindView(R.id.shot_like_count) public TextView likeCount;
    @BindView(R.id.shot_bucket_count) public TextView bucketCount;
    @BindView(R.id.shot_view_count) public TextView viewCount;
    @BindView(R.id.shot_image) public SimpleDraweeView image;
//    @BindView(R.id.shot_image) public ImageView image;

    public ShotViewHolder(View itemView) {
        super(itemView);
    }
}

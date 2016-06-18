package com.jiuzhang.guojing.dribbbo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShotCommentViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.comment_author_name) TextView authorName;
    @BindView(R.id.comment_author_picture) ImageView authorPicture;
    @BindView(R.id.comment_content) TextView content;
    @BindView(R.id.comment_created_at) TextView createdAt;
    @BindView(R.id.comment_like_count) TextView likeCount;

    public ShotCommentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}

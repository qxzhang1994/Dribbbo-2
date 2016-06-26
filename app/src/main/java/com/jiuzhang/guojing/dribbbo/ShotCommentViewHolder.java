package com.jiuzhang.guojing.dribbbo;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;

public class ShotCommentViewHolder extends BaseViewHolder {

    @BindView(R.id.comment_author_name) TextView authorName;
    @BindView(R.id.comment_author_picture) ImageView authorPicture;
    @BindView(R.id.comment_content) TextView content;
    @BindView(R.id.comment_created_at) TextView createdAt;
    @BindView(R.id.comment_like_count) TextView likeCount;

    public ShotCommentViewHolder(View itemView) {
        super(itemView);
    }
}

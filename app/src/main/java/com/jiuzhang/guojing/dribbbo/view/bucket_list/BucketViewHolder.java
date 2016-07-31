package com.jiuzhang.guojing.dribbbo.view.bucket_list;

import android.view.View;
import android.widget.TextView;

import com.jiuzhang.guojing.dribbbo.R;
import com.jiuzhang.guojing.dribbbo.view.base.BaseViewHolder;

import butterknife.BindView;

public class BucketViewHolder extends BaseViewHolder {

    @BindView(R.id.bucket_name) TextView bucketName;
    @BindView(R.id.bucket_shot_count) TextView bucketCount;

    public BucketViewHolder(View itemView) {
        super(itemView);
    }
}

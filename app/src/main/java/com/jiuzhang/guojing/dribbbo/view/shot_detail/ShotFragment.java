package com.jiuzhang.guojing.dribbbo.view.shot_detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.jiuzhang.guojing.dribbbo.R;
import com.jiuzhang.guojing.dribbbo.model.Shot;
import com.jiuzhang.guojing.dribbbo.utils.ModelUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShotFragment extends Fragment {

    public static final String KEY_SHOT = "shot";

    private static final int VIEW_TYPE_SHOT_IMAGE = 0;
    private static final int VIEW_TYPE_SHOT_DETAIL = 1;
    private static final int VIEW_TYPE_SHOT_COMMENT = 2;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private Shot shot;

    public static ShotFragment newInstance(@NonNull Shot shot) {
        Bundle args = new Bundle();
        args.putString(KEY_SHOT, ModelUtils.toString(shot, new TypeToken<Shot>(){}));

        ShotFragment fragment = new ShotFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shot, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        shot = ModelUtils.toObject(getArguments().getString(KEY_SHOT),
                                   new TypeToken<Shot>(){});

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view;
                switch (viewType) {
                    case VIEW_TYPE_SHOT_IMAGE:
                        view = LayoutInflater.from(getContext())
                                .inflate(R.layout.list_item_shot_image, parent, false);
                        return new ShotImageViewHolder(view);
                    case VIEW_TYPE_SHOT_DETAIL:
                        view = LayoutInflater.from(getContext())
                                .inflate(R.layout.list_item_shot_detail, parent, false);
                        return new ShotDetailViewHolder(view);
                    case VIEW_TYPE_SHOT_COMMENT:
                        view = LayoutInflater.from(getContext())
                                .inflate(R.layout.list_item_shot_comment, parent, false);
                        return new ShotCommentViewHolder(view);
                    default:
                        return null;
                }
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                final int viewType = getItemViewType(position);
                switch (viewType) {
                    case VIEW_TYPE_SHOT_IMAGE:
                        ShotImageViewHolder shotImageViewHolder = (ShotImageViewHolder) holder;
                        Glide.with(getContext())
                             .load(shot.images.get(Shot.IMAGE_HIDPI))
                             .into(shotImageViewHolder.image);
                        break;
                    case VIEW_TYPE_SHOT_DETAIL:
                        ShotDetailViewHolder shotDetailViewHolder = (ShotDetailViewHolder) holder;
                        shotDetailViewHolder.title.setText(shot.title);
                        shotDetailViewHolder.authorName.setText(shot.user.name);

                        shotDetailViewHolder.description.setText(Html.fromHtml(shot.description));
                        shotDetailViewHolder.description.setMovementMethod(LinkMovementMethod.getInstance());

                        shotDetailViewHolder.likeCount.setText(String.valueOf(shot.likes_count));
                        shotDetailViewHolder.bucketCount.setText(String.valueOf(shot.buckets_count));
                        shotDetailViewHolder.viewCount.setText(String.valueOf(shot.views_count));

                        Glide.with(getContext())
                            .load(shot.user.avatar_url)
                            .into(shotDetailViewHolder.authorPicture);

                        shotDetailViewHolder.likeCount.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), "Like count clicked", Toast.LENGTH_SHORT).show();
                            }
                        });
                        shotDetailViewHolder.bucketCount.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), "Bucket count clicked", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                }
            }

            @Override
            public int getItemCount() {
                return 2;
            }

            @Override
            public int getItemViewType(int position) {
                if (position == 0) {
                    return VIEW_TYPE_SHOT_IMAGE;
                } else if (position == 1) {
                    return VIEW_TYPE_SHOT_DETAIL;
                } else {
                    return VIEW_TYPE_SHOT_COMMENT;
                }
            }
        });
    }
}

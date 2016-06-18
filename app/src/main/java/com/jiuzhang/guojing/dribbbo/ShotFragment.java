package com.jiuzhang.guojing.dribbbo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShotFragment extends Fragment {

    private static final int VIEW_TYPE_SHOT_IMAGE = 0;
    private static final int VIEW_TYPE_SHOT_DETAIL = 1;
    private static final int VIEW_TYPE_SHOT_COMMENT = 2;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private Shot shot;

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
        shot = mockData();

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
                        shotImageViewHolder.image.setImageDrawable(getResources().getDrawable(R.mipmap.artboard_5));
                        break;
                    case VIEW_TYPE_SHOT_DETAIL:
                        ShotDetailViewHolder shotDetailViewHolder = (ShotDetailViewHolder) holder;
                        shotDetailViewHolder.title.setText(shot.title);
                        shotDetailViewHolder.description.setText(shot.description);
                        shotDetailViewHolder.authorName.setText(shot.author.name);
                        shotDetailViewHolder.likeCount.setText(String.valueOf(shot.likeCount));
                        shotDetailViewHolder.bucketCount.setText(String.valueOf(shot.bucketCount));
                        shotDetailViewHolder.viewCount.setText(String.valueOf(shot.viewCount));

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
                    case VIEW_TYPE_SHOT_COMMENT:
                        final Comment comment = shot.comments.get(position - 2);
                        ShotCommentViewHolder shotCommentViewHolder = (ShotCommentViewHolder) holder;
                        shotCommentViewHolder.authorName.setText(comment.author.name);
                        shotCommentViewHolder.content.setText(comment.content);
                        shotCommentViewHolder.likeCount.setText(String.valueOf(comment.likeCount));
                        break;
                }
            }

            @Override
            public int getItemCount() {
                return 5;
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

    private Shot mockData() {
        User author = new User();
        author.name = "Jing Guo";
        author.profilePictureUrl = "https://d13yacurqjgara.cloudfront.net/users/536/avatars/normal/2f7475306c01c1bbaf344ba9644725ad.png?1458737015";

        List<Comment> comments = new ArrayList<>();
        Comment comment1 = new Comment();
        comment1.author = author;
        comment1.content = "comment content";
        comment1.createdAt = new Date();
        comment1.likeCount = 3;
        comments.add(comment1);

        Comment comment2 = new Comment();
        comment2.author = author;
        comment2.content = "comment2 content comment2 content comment2 content comment2 content comment2 content";
        comment2.createdAt = new Date();
        comment2.likeCount = 30;
        comments.add(comment2);

        Comment comment3 = new Comment();
        comment3.author = author;
        comment3.content = "comment3 content";
        comment3.createdAt = new Date();
        comment3.likeCount = 300;
        comments.add(comment3);

        Shot shot = new Shot();
        shot.title = "Amazing shot";
        shot.imageUrl = "https://d13yacurqjgara.cloudfront.net/users/536/screenshots/2766763/artboard_5.png";
        shot.description = "shot desc\nshot desc\nshot desc shot desc shot desc shot desc shot desc shot desc ";
        shot.likeCount = 100;
        shot.viewCount = 1000;
        shot.bucketCount = 10;
        shot.author = author;
        shot.comments = comments;

        return shot;
    }
}

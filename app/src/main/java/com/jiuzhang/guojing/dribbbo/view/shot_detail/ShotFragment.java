package com.jiuzhang.guojing.dribbbo.view.shot_detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.reflect.TypeToken;
import com.jiuzhang.guojing.dribbbo.R;
import com.jiuzhang.guojing.dribbbo.dribbble.Dribbble;
import com.jiuzhang.guojing.dribbbo.dribbble.DribbbleException;
import com.jiuzhang.guojing.dribbbo.model.Shot;
import com.jiuzhang.guojing.dribbbo.utils.ModelUtils;
import com.jiuzhang.guojing.dribbbo.utils.PermissionUtils;
import com.jiuzhang.guojing.dribbbo.view.bucket_list.BucketListActivity;
import com.jiuzhang.guojing.dribbbo.view.bucket_list.BucketListFragment;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShotFragment extends Fragment {

    public static final String KEY_SHOT = "shot";

    private static final int VIEW_TYPE_SHOT_IMAGE = 0;
    private static final int VIEW_TYPE_SHOT_DETAIL = 1;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private Shot shot;
    private boolean isLiking;

    private ImageView shotImageView;
    private Bitmap bitmap;

    public static ShotFragment newInstance(@NonNull Bundle args) {
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
                    default:
                        return null;
                }
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                final int viewType = getItemViewType(position);
                switch (viewType) {
                    case VIEW_TYPE_SHOT_IMAGE:
                        shotImageView = ((ShotImageViewHolder) holder).image;

                        Drawable placeholderDrawable = getResources().getDrawable(R.drawable.shot_placeholder);
                        String imageUrl = shot.images.get(Shot.IMAGE_HIDPI);
                        if (TextUtils.isEmpty(imageUrl)) {
                            // in case there's no hidpi image
                            imageUrl = shot.images.get(Shot.IMAGE_NORMAL);
                        }

                        if (imageUrl.endsWith("gif")) {
                            Glide.with(getContext())
                                 .load(imageUrl)
                                 .asGif()
                                 .placeholder(placeholderDrawable)
                                 .into(new GifTarget());
                        } else {
                            Glide.with(getContext())
                                 .load(imageUrl)
                                 .asBitmap()
                                 .placeholder(placeholderDrawable)
                                 .into(new BitmapTarget());
                        }
                        break;
                    case VIEW_TYPE_SHOT_DETAIL:
                        final ShotDetailViewHolder shotDetailViewHolder = (ShotDetailViewHolder) holder;
                        shotDetailViewHolder.title.setText(shot.title);
                        shotDetailViewHolder.authorName.setText(shot.user.name);

                        shotDetailViewHolder.description.setText(Html.fromHtml(
                                shot.description == null ? "" : shot.description));
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

                        shotDetailViewHolder.likeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!isLiking) {
                                    isLiking = true;
                                    AsyncTaskCompat.executeParallel(new LikeTask(shot.id, !shot.liked));
                                }
                            }
                        });

                        shotDetailViewHolder.bucketButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), BucketListActivity.class);
                                intent.putExtra(BucketListFragment.KEY_CHOOSING_MODE, true);
                                startActivity(intent);
                            }
                        });

                        shotDetailViewHolder.shareButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shareShot();
                            }
                        });

                        Drawable likeDrawable = shot.liked
                                ? getResources().getDrawable(R.mipmap.ic_favorite_black_18dp)
                                : getResources().getDrawable(R.mipmap.ic_favorite_border_black_18dp);
                        shotDetailViewHolder.likeButton.setImageDrawable(likeDrawable);
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
                } else {
                    return VIEW_TYPE_SHOT_DETAIL;
                }
            }
        });
    }

    private class LikeTask extends AsyncTask<Void, Void, String> {

        private String id;
        private boolean like;

        public LikeTask(String id, boolean like) {
            this.id = id;
            this.like = like;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                if (like) {
                    Dribbble.likeShot(id);
                } else {
                    Dribbble.unlikeShot(id);
                }
                return null;
            } catch (DribbbleException e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String errorMsg) {
            isLiking = false;

            if (errorMsg == null) {
                shot.liked = like;
                shot.likes_count += like ? 1 : -1;
                recyclerView.getAdapter().notifyDataSetChanged();

                Intent resultIntent = new Intent();
                resultIntent.putExtra(KEY_SHOT, ModelUtils.toString(shot, new TypeToken<Shot>(){}));
                getActivity().setResult(Activity.RESULT_OK, resultIntent);
            }
        }
    }

    private void shareShot() {
        if (!PermissionUtils.checkWriteExternalStoragePermission(getContext())) {
            PermissionUtils.requestWriteExternalStoragePermission(getActivity());
            return;
        }

        if (bitmap == null) {
            return;
        }

        Uri imageUri = imageToUri(getContext(), bitmap, shot.title);

        if (imageUri != null) {
            // imageUri equals null means image loading not finished
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                                 shot.title + " " + shot.html_url);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, "Share shot"));
        }
    }

    private static Uri imageToUri(@NonNull Context context,
                                  @NonNull Bitmap bitmap,
                                  @NonNull String title) {
        return Uri.parse(MediaStore.Images.Media.insertImage(
                context.getContentResolver(),
                bitmap,
                title,
                null));
    }

    private class BitmapTarget extends SimpleTarget<Bitmap> {

        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            bitmap = resource;
            shotImageView.setImageBitmap(resource);
        }
    }

    private class GifTarget extends SimpleTarget<GifDrawable> {

        @Override
        public void onResourceReady(GifDrawable resource, GlideAnimation<? super GifDrawable> glideAnimation) {
            bitmap = resource.getFirstFrame();
            shotImageView.setImageDrawable(resource);
        }
    }
}

package com.jiuzhang.guojing.dribbbo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jiuzhang.guojing.dribbbo.R;

public class ImageUtils {

    public static void loadUserPicture(@NonNull final Context context,
                                       @NonNull ImageView imageView,
                                       @NonNull String url) {
        Glide.with(context)
             .load(url)
             .asBitmap()
             .placeholder(ContextCompat.getDrawable(context, R.drawable.user_picture_placeholder))
             .into(new BitmapImageViewTarget(imageView) {
                 @Override
                 protected void setResource(Bitmap resource) {
                     RoundedBitmapDrawable circularBitmapDrawable =
                             RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                     circularBitmapDrawable.setCircular(true);
                     view.setImageDrawable(circularBitmapDrawable);
                 }
             });
    }
}

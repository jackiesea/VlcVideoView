package com.mr.csh.mrcsh_vlcvideoview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by Mr.CSH on 2016/6/27.
 */
public class GlideOptions {
    public static void showImage(Context context, String url, ImageView imageView, int defaultImage) {
        if (context == null || ((Activity) context).isFinishing()) {
            return;
        }

        RequestBuilder<Drawable> requestBuilder = Glide.with(context).load(url);
        RequestOptions requestOptions = new RequestOptions();

        if (defaultImage >= 0) {
            requestOptions.placeholder(defaultImage)   //加载时图片
                    .error(defaultImage);     //加载失败时图片
        }

        requestBuilder.apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())  //动画渐变加载
                .into(imageView);
    }
}

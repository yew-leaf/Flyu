package us.xingkong.flyu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.zhihu.matisse.engine.ImageEngine;

/**
 * @作者: Xuer
 * @包名: us.xingkong.flyu
 * @类名: MatisseEngine
 * @创建时间: 2018/5/9 21:03
 * @最后修改于:
 * @版本: 1.0
 * @描述:
 * @更新日志:
 */
public class MatisseEngine implements ImageEngine {

    @Override
    public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(placeholder)
                //.error(R.drawable.error)
                .override(resize, resize);
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void loadAnimatedGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(placeholder)
                //.error(R.drawable.error)
                .override(resize, resize);
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .override(resizeX, resizeY)
                .priority(Priority.HIGH);
        Glide.with(context)
                .load(uri)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void loadAnimatedGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .override(resizeX, resizeY)
                .priority(Priority.HIGH);
        Glide.with(context)
                .load(uri)
                .apply(options)
                .into(imageView);
    }

    @Override
    public boolean supportAnimatedGif() {
        return true;
    }
}

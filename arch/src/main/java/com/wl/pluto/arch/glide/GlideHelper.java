package com.wl.pluto.arch.glide;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;

/**
 * ================================================
 *
 * @function 图片加载助手
 * Created by plout on 2019/03/07.
 * ================================================
 */
public class GlideHelper {
    /**
     * 借助内部类 实现线程安全的单例模式
     * 属于懒汉式单例，因为Java机制规定，内部类SingletonHolder只有在getInstance()
     * 方法第一次调用的时候才会被加载（实现了lazy），而且其加载过程是线程安全的。
     * 内部类加载的时候实例化一次instance。
     */
    public GlideHelper() {
    }

    private static class LoadImageHelperHolder {
        private final static GlideHelper INSTANCE = new GlideHelper();
    }

    public static GlideHelper getInstance() {
        return LoadImageHelperHolder.INSTANCE;
    }


    /**
     * 加载图片公用方法
     *
     * @param context   上下文
     * @param url       图片链接
     * @param imageView 要显示图片的控件
     */
    public static void load(Context context, String url, @NonNull ImageView imageView) {

        if (!TextUtils.isEmpty(url)) {
            RequestOptions options = new RequestOptions()
                    .dontAnimate();

            Glide.with(context)
                    .load(getImageUrl(url))
                    .apply(options)
                    .into(imageView);
        }
    }

    /**
     * 加载图片公用方法
     *
     * @param context   上下文
     * @param url       图片链接
     * @param imageView 要显示图片的控件
     */
    public static void loadWarp(Context context, String url, @NonNull ImageView imageView) {

        RequestOptions options = new RequestOptions()
                // .override(RxDeviceTool.getScreenWidth(), -1)
                .dontAnimate();

        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);

    }

    public static void loadAutoAdapt(Context context, String url, @NonNull ImageView imageView) {

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .dontAnimate();

        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);

    }

    /**
     * 加载图片公用方法
     *
     * @param context    上下文
     * @param url        图片链接
     * @param resourceId 默认图片
     * @param imageView  要显示图片的控件
     */
    public static void setLoadImage(Context context, String url, @DrawableRes int resourceId, @NonNull ImageView imageView) {
        if (isEmpty(url)) {
            imageView.setImageResource(resourceId);
        } else {
            Glide.with(context).load(getImageUrl(url))
                    .apply(new RequestOptions().error(resourceId).dontAnimate())
                    .into(imageView);
        }
    }

    /**
     * 加载图片公用方法
     *
     * @param activity   上下文 避免长时间拥有Context
     * @param url        图片链接
     * @param resourceId 默认图片
     * @param imageView  要显示图片的控件
     */
    public static void setLoadImage(Activity activity, @NonNull String url, @DrawableRes int resourceId, @NonNull ImageView imageView) {
        if (!isEmpty(url)) {
            if (activity != null && !activity.isDestroyed()) {
                Glide.with(activity)
                        .load(getImageUrl(url))
                        .apply(new RequestOptions().error(resourceId).dontAnimate())
                        .into(imageView);
            }
        } else {
            imageView.setImageResource(resourceId);
        }
    }

    /**
     * 加载图片公用方法
     *
     * @param activity   上下文 避免长时间拥有Context
     * @param url        图片链接
     * @param resourceId 默认图片
     * @param imageView  要显示图片的控件
     */
    public static void setLoadImageListener(Activity activity, @NonNull String url, @DrawableRes int resourceId, @NonNull ImageView imageView, CommonCallback<Object> callback) {
        if (!isEmpty(url)) {
            if (activity != null && !activity.isDestroyed()) {
                Glide.with(activity)
                        .load(getImageUrl(url))
                        .apply(new RequestOptions().error(resourceId).dontAnimate())
                        .addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                callback.onError("网络出错");
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                imageView.setImageDrawable(resource);
                                callback.onSuccess("加载成功");
                                return false;
                            }
                        }).into(imageView);
            }
        } else {
            imageView.setImageResource(resourceId);
        }
    }

    /**
     * 加载图片公用方法
     *
     * @param activity   上下文 避免长时间拥有Context
     * @param url        图片链接
     * @param resourceId 默认图片
     * @param imageView  要显示图片的控件
     */
    public static void setLoadImageNoCache(Activity activity, @NonNull String url, @DrawableRes int resourceId, @NonNull ImageView imageView) {
        if (!isEmpty(url)) {
            if (activity != null && !activity.isDestroyed()) {
                RequestOptions options = new RequestOptions().placeholder(resourceId).error(resourceId).dontAnimate();
                options.signature(new ObjectKey(System.currentTimeMillis()));

                Glide.with(activity).load(getImageUrl(url))
                        .apply(options)
                        .into(imageView);
            }
        } else {
            imageView.setImageResource(resourceId);
        }
    }

    /**
     * 加载圆形图片
     *
     * @param activity
     * @param url
     * @param resourceId
     * @param imageView
     */
    public static void setLoadCirlcleImage(Activity activity, @NonNull String url, @DrawableRes int resourceId, @NonNull ImageView imageView) {
        if (activity != null && !activity.isDestroyed()) {
            Glide.with(activity).load(getImageUrl(url))
                    .apply(new RequestOptions().placeholder(resourceId).error(resourceId).circleCrop().dontAnimate())
                    .into(imageView);
        }
    }


    /**
     * 获取图片路径的方法
     *
     * @param url 图片链接
     */
    public static String getImageUrl(String url) {
        if (url.startsWith("http")) {
            return url;
        } else {
            return "https://api.beikekid.com" + url;
        }
    }

    /**
     * 判断字符串是否为null或长度为0
     *
     * @param input 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(CharSequence input) {
        return input == null || input.length() == 0;
    }


    /**
     * 请求回调
     */
    public interface CommonCallback<T> {
        void onSuccess(T t);

        void onError(String errMsg);
    }
}

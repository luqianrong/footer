package com.example.footer.application;

import android.app.Application;
import android.graphics.Bitmap;

import com.example.footer.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by android on 11/12/15.
 */
public class BaseApplication extends Application {

    private DisplayImageOptions defaultOptions;

    @Override
    public void onCreate() {
        super.onCreate();
        initJpush();
        initImageLoader();

    }

    private void initJpush() {
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);
    }



    public DisplayImageOptions getDefaultOptions() {
        return defaultOptions;
    }

    /**
     * imageloader inital
     */
    private void initImageLoader() {

        defaultOptions = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisc()
                .cacheInMemory()
                //.showImageForEmptyUri(R.drawable.banner_default)
                //.showStubImage(R.drawable.banner_default)
                //.showImageOnLoading(R.drawable.banner_default)
//                .showImageOnFail(R.drawable.banner_default)
                .build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
                this).threadPriority(Thread.NORM_PRIORITY - 2)
                .defaultDisplayImageOptions(defaultOptions)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .imageDownloader(new BaseImageDownloader(this))
                .imageDecoder(new BaseImageDecoder(false))
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.FIFO).build();


        ImageLoader.getInstance().init(
                configuration);
    }
}

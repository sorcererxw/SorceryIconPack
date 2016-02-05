package com.sorcerer.sorcery.iconpack;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import im.fir.sdk.FIR;

/**
 * Created by Sorcerer on 2016/1/26 0026.
 */
public class SIP extends Application {

    public static DisplayImageOptions mOptions;

    @Override
    public void onCreate() {
        super.onCreate();
        FIR.init(this);
        initImageLoader(this);
    }

    /**
     * 初始化ImageLoader
     */
    private void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(200 * 1024 * 1024) // 200 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                //.writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);


        mOptions = new DisplayImageOptions.Builder()
//                .showImageOnLoading(
//                        new ColorDrawable(getResources().getColor(R.color.transparent))
//                )   //加载过程中
//                .showImageForEmptyUri(R.mipmap.ic_launcher) //uri为空时
//                .showImageOnFail(R.mipmap.ic_launcher)      //加载失败时
                .cacheOnDisk(true)
                .cacheInMemory(true)                             //允许cache在内存和磁盘中
                .bitmapConfig(Bitmap.Config.RGB_565)             //图片压缩质量参数
                .build();
    }
}

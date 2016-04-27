package com.sorcerer.sorcery.iconpack;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import cn.bmob.v3.Bmob;
import im.fir.sdk.FIR;

/**
 * Created by Sorcerer on 2016/1/26 0026.
 */
public class SIP extends Application {

    public static final String PACKAGE_NAME = "com.sorcerer.sorcery.iconpack";
    public static DisplayImageOptions mOptions;
    public static String LOGTAG = "[SIP]";
    private Application mApplication = this;

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                initImageLoader(mApplication);
            }
        }).start();
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
                .showImageOnLoading(
                        new ColorDrawable(ContextCompat.getColor(this, R.color.transparent))
                )   //加载过程中
//                .showImageForEmptyUri(R.mipmap.ic_launcher) //uri为空时
//                .showImageOnFail(R.mipmap.ic_launcher)      //加载失败时
                .cacheOnDisk(true)
                .cacheInMemory(true)                             //允许cache在内存和磁盘中
                .bitmapConfig(Bitmap.Config.RGB_565)             //图片压缩质量参数
                .build();
    }
}

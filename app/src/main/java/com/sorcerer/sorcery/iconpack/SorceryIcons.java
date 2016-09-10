package com.sorcerer.sorcery.iconpack;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.StrictMode;

import com.sorcerer.sorcery.iconpack.models.IconBean;
import com.sorcerer.sorcery.iconpack.net.leancloud.AVService;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/1/26 0026
 */
public class SorceryIcons extends Application {

    public static final boolean ENABLE_LEAKCARRY = false;

    private RefWatcher mRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        AVService.init(this);
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(
                    new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(
                    new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
        if (BuildConfig.DEBUG && ENABLE_LEAKCARRY) {
            mRefWatcher = LeakCanary.install(this);
        }
    }

    public static RefWatcher getRefWatcher(Context context) {
        SorceryIcons app = (SorceryIcons) context.getApplicationContext();
        return app.mRefWatcher;
    }
}

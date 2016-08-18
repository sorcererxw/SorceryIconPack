package com.sorcerer.sorcery.iconpack;

import android.app.Application;
import android.content.Context;

import com.sorcerer.sorcery.iconpack.net.leancloud.AVService;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Sorcerer on 2016/1/26 0026.
 */
public class SorceryIcons extends Application {

    private RefWatcher mRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        AVService.init(this);
        if (BuildConfig.DEBUG) {
            mRefWatcher = LeakCanary.install(this);
        }
    }

    public static RefWatcher getRefWatcher(Context context) {
        SorceryIcons app = (SorceryIcons) context.getApplicationContext();
        return app.mRefWatcher;
    }
}

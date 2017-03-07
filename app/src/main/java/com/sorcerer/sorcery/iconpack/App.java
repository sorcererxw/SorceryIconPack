package com.sorcerer.sorcery.iconpack;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.sorcerer.sorcery.iconpack.network.leancloud.AVService;
import com.sorcerer.sorcery.iconpack.utils.NetUtil;
import com.tencent.bugly.crashreport.CrashReport;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import rx_activity_result2.RxActivityResult;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/10
 */

public class App extends Application {

    private AppComponent mAppComponent;

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        super.onCreate();
        AVService.init(this);
        if (!BuildConfig.DEBUG) {
            CrashReport.initCrashReport(this, "900053240", false);
        }
        RxActivityResult.register(this);
        Timber.plant(new Timber.DebugTree());

        sContext = getApplicationContext();

        try {
            NetUtil.enableSSLSocket();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            Timber.e(e);
        }

        if (BuildConfig.DEBUG) {
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            android.content.res.Configuration conf = res.getConfiguration();
            conf.locale = new Locale("zh");
            res.updateConfiguration(conf, dm);
        }
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}

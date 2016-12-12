package com.sorcerer.sorcery.iconpack;

import android.app.Application;

import com.sorcerer.sorcery.iconpack.net.leancloud.AVService;
import com.tencent.bugly.crashreport.CrashReport;

import rx_activity_result2.RxActivityResult;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/10
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AVService.init(this);
        if (!BuildConfig.DEBUG) {
            CrashReport.initCrashReport(this, "900053240", false);
        }
        RxActivityResult.register(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}

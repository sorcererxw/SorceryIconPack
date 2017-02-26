package com.sorcerer.sorcery.iconpack;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.sorcerer.sorcery.iconpack.net.leancloud.AVService;
import com.sorcerer.sorcery.iconpack.utils.NetUtil;
import com.tencent.bugly.crashreport.CrashReport;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import rx_activity_result2.RxActivityResult;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/10
 */

public class App extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
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
    }
}

package com.sorcerer.sorcery.iconpack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.oasisfeng.condom.CondomContext;
import com.sorcerer.sorcery.iconpack.network.leancloud.AVService;
import com.sorcerer.sorcery.iconpack.utils.NetUtil;
import com.tencent.bugly.crashreport.CrashReport;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
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
    private static App sApp;

    public static App getInstance() {
        return sApp;
    }

    private List<Activity> mActivityList;

    @Override
    public void onCreate() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        super.onCreate();
        AVService.init(CondomContext.wrap(this, "av service"));
        if (!BuildConfig.DEBUG) {
            CrashReport.initCrashReport(
                    CondomContext.wrap(this, "crash report"), "900053240", false);
        }
        RxActivityResult.register(this);
        Timber.plant(new Timber.DebugTree() {
            @Override
            public void e(String message, Object... args) {
                super.e(message, args);
            }
        });

        sApp = this;

        mActivityList = new ArrayList<>();
        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);

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

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        mActivityList = null;
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public List<Activity> getActivityList() {
        return mActivityList;
    }

    private ActivityLifecycleCallbacks mActivityLifecycleCallbacks =
            new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    mActivityList.add(activity);
                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    mActivityList.remove(activity);
                }
            };
}

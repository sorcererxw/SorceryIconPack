package com.sorcerer.sorcery.iconpack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.sorcerer.sorcery.iconpack.utils.NetUtil;
import com.tencent.bugly.crashreport.CrashReport;

import java.lang.reflect.Method;
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
        if (!BuildConfig.DEBUG) {
            CrashReport.initCrashReport(this, "900053240", false);
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

//        if (BuildConfig.DEBUG) {
//            Resources res = getResources();
//            DisplayMetrics dm = res.getDisplayMetrics();
//            android.content.res.Configuration conf = res.getConfiguration();
//            conf.locale = new Locale("zh");
//            res.updateConfiguration(conf, dm);
//        }

        showDebugDBAddressLogToast(this);
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

    public static void showDebugDBAddressLogToast(Context context) {
        if (BuildConfig.DEBUG) {
            try {
                Class<?> debugDB = Class.forName("com.amitshekhar.DebugDB");
                Method getAddressLog = debugDB.getMethod("getAddressLog");
                Object value = getAddressLog.invoke(null);
                Timber.d("showDebugDBAddressLogToast: " + value.toString());
//                Toast.makeText(context, (String) value, Toast.LENGTH_LONG).show();
            } catch (Exception ignore) {

            }
        }
    }
}
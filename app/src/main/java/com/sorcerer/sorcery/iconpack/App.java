package com.sorcerer.sorcery.iconpack;

import android.app.Application;

import com.sorcerer.sorcery.iconpack.utils.NetUtil;
import com.tencent.bugly.crashreport.CrashReport;

import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;
import rx_activity_result2.RxActivityResult;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/10
 */

public class App extends Application {

    private static App sApp;
    private AppComponent mAppComponent;

    public static App getInstance() {
        return sApp;
    }

    public static void showDebugDBAddressLogToast() {
        if (BuildConfig.DEBUG) {
            try {
                Class<?> debugDB = Class.forName("com.amitshekhar.DebugDB");
                Method getAddressLog = debugDB.getMethod("getAddressLog");
                Object value = getAddressLog.invoke(null);
                Timber.d("showDebugDBAddressLogToast: " + value.toString());
            } catch (Exception ignore) {

            }
        }
    }

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

        try {
            NetUtil.enableSSLSocket();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            Timber.e(e);
        }

//        if (BuildConfig.DEBUG) {
//            LocaleUtil.INSTANCE.forceChinese(this);
//        }

        BGASwipeBackManager.getInstance().init(this);

        showDebugDBAddressLogToast();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}

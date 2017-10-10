package com.sorcerer.sorcery.iconpack;

import android.app.Application;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.sorcerer.sorcery.iconpack.data.db.Db;
import com.sorcerer.sorcery.iconpack.utils.NetUtil;

import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

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

    private SorceryPrefs mPrefs;
    private Db mDb;

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
        super.onCreate();
        RxActivityResult.register(this);
        Timber.plant(new Timber.DebugTree() {
            @Override
            public void e(String message, Object... args) {
                super.e(message, args);
            }
        });

        sApp = this;

        mPrefs = new SorceryPrefs(this);
        mDb = new Db(this);

        try {
            NetUtil.enableSSLSocket();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            Timber.e(e);
        }

        BGASwipeBackManager.getInstance().init(this);

        showDebugDBAddressLogToast();

        Locale systemLocale = getResources().getConfiguration().locale;
        mPrefs.language().asObservable().subscribe(language -> {
            Locale locale;
            if (TextUtils.isEmpty(language)) {
                locale = systemLocale;
            } else {
                locale = new Locale(language);
            }
            Resources res = getResources();
            // Change locale settings in the app.
            DisplayMetrics dm = res.getDisplayMetrics();
            android.content.res.Configuration conf = res.getConfiguration();
            conf.setLocale(locale); // API 17+ only.
            // Use conf.locale = new Locale(...) if targeting lower versions
            res.updateConfiguration(conf, dm);
        }, Timber::e);
    }

    public SorceryPrefs prefs() {
        if (mPrefs == null) {
            mPrefs = new SorceryPrefs(this);
        }
        return mPrefs;
    }

    public Db db() {
        if (mDb == null) {
            mDb = new Db(this);
        }
        return mDb;
    }

}

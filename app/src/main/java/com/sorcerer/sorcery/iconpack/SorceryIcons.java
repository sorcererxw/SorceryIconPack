package com.sorcerer.sorcery.iconpack;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.sorcerer.sorcery.iconpack.net.leancloud.AVService;
import com.sorcerer.sorcery.iconpack.util.Prefs.SorceryPrefs;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import rx_activity_result.RxActivityResult;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/1/26 0026
 */
public class SorceryIcons extends Application {

//    public SorceryIcons() {
//        super(ShareConstants.TINKER_ENABLE_ALL,
//                "tinker.sample.android.app.SampleApplicationLike");
//        AVService.init(this);
//        if (!BuildConfig.DEBUG) {
//            CrashReport.initCrashReport(getApplicationContext(), "900053240", false);
//        }
//        RxActivityResult.register(this);
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        AVService.init(this);
        if (!BuildConfig.DEBUG) {
            CrashReport.initCrashReport(getApplicationContext(), "900053240", false);
        }
        RxActivityResult.register(this);
    }
}

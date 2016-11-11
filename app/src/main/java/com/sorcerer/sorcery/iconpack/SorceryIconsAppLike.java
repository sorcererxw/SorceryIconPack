package com.sorcerer.sorcery.iconpack;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.sorcerer.sorcery.iconpack.net.leancloud.AVService;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import com.sorcerer.sorcery.iconpack.tinker.log.MyLogImp;
import com.sorcerer.sorcery.iconpack.tinker.util.SampleApplicationContext;
import com.sorcerer.sorcery.iconpack.tinker.util.TinkerManager;

import rx_activity_result.RxActivityResult;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/10
 */
@SuppressWarnings("unused")
@DefaultLifeCycle(application = "com.sorcerer.sorcery.iconpack.SorceryIconsApp",
                  flags = ShareConstants.TINKER_ENABLE_ALL,
                  loadVerifyFlag = false)
public class SorceryIconsAppLike extends DefaultApplicationLike {
    public SorceryIconsAppLike(Application application, int tinkerFlags,
                               boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                               long applicationStartMillisTime,
                               Intent tinkerResultIntent,
                               Resources[] resources,
                               ClassLoader[] classLoader,
                               AssetManager[] assetManager) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime,
                applicationStartMillisTime, tinkerResultIntent, resources, classLoader,
                assetManager);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        //you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        SampleApplicationContext.application = getApplication();
        SampleApplicationContext.context = getApplication();
        TinkerManager.setTinkerApplicationLike(this);
        TinkerManager.initFastCrashProtect();
        //should set before tinker is installed
        TinkerManager.setUpgradeRetryEnable(true);

        //optional set logIml, or you can use default debug log
        TinkerInstaller.setLogIml(new MyLogImp());

        //installTinker after load multiDex
        //or you can put com.tencent.tinker.** to main dex
        TinkerManager.installTinker(this);

        AVService.init(getApplication());
        if (!BuildConfig.DEBUG) {
            CrashReport.initCrashReport(getApplication(), "900053240", false);
        }
        RxActivityResult.register(getApplication());
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(
            Application.ActivityLifecycleCallbacks callback) {
        getApplication().registerActivityLifecycleCallbacks(callback);
    }
}

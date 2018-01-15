package com.sorcerer.sorcery.iconpack.apply.xposed;

import android.content.res.Resources;
import android.content.res.XResources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Keep;
import android.util.TypedValue;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/8
 */

@Keep
public class Xposed
        implements IXposedHookZygoteInit, IXposedHookLoadPackage, IXposedHookInitPackageResources {

    private IconReplacement[] mIconReplacements;
    private boolean mActive;

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        log("new sorcery started");
        XSharedPreferences sharedPreferences =
                new XSharedPreferences(Constants.SORCERY_PACKAGE, Constants.XPOSED_PREFERENCE);
        sharedPreferences.makeWorldReadable();
        mActive = sharedPreferences.getBoolean(Constants.ACTIVE_PREFERENCE_KEY, false);
        Gson gson = new Gson();
        mIconReplacements = gson.fromJson(
                sharedPreferences.getString(Constants.REPLACEMENT_DISPOSE_PREFERENCE_KEY, ""),
                IconReplacement[].class
        );
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals(Constants.SORCERY_PACKAGE)) {
            XposedHelpers.findAndHookMethod(
                    "com.sorcerer.sorcery.iconpack.apply.xposed.XposedInstaller",
                    lpparam.classLoader,
                    "isModuleActive",
                    new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(Boolean.TRUE);
                        }
                    });
        }
        if (!mActive) {
            return;
        }
        if (lpparam.packageName.equals("com.teslacoilsw.launcher")) {
            handleNova(lpparam);
        }
    }

    private void handleNova(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("android.content.res.Resources", lpparam.classLoader,
                "getValueForDensity",
                Integer.TYPE, Integer.TYPE, TypedValue.class, Boolean.TYPE,
                new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        log("Overriding Nova Asset Manager call");
                        Resources res = (Resources) param.thisObject;
                        int resId = (Integer) param.args[0];
                        TypedValue value = (TypedValue) param.args[2];
                        File file = new File(Constants.ICON_PATH,
                                res.getResourcePackageName(resId) + "_" + resId);
                        if (file.exists()) {
                            value.string = "replaceWithSIP";
                        } else {
                            log("nova: " + file.getAbsolutePath() + " not exist");
                        }
                    }
                });
        XposedHelpers.findAndHookMethod("android.content.res.AssetManager", lpparam.classLoader,
                "openNonAssetFd",
                Integer.TYPE, String.class,
                new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if (param.args[1].equals("replaceWithSIP")) {
                            param.setResult(null);
                        }
                    }
                });
        XposedHelpers.findAndHookMethod("android.content.res.AssetManager", lpparam.classLoader,
                "openNonAssetFd",
                String.class,
                new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if (param.args[0].equals("replaceWithSIP")) {
                            param.setResult(null);
                        }
                    }
                });
    }

    @Override
    public void handleInitPackageResources(InitPackageResourcesParam param)
            throws Throwable {
        if (!mActive) {
            log("not active");
            return;
        }
        List<IconReplacement> replacementList = Stream.of(mIconReplacements)
                .filter(value -> param.packageName.equals(value.getPackageName()))
                .collect(Collectors.toList());
        if (replacementList == null || replacementList.size() == 0) {
            return;
        }
        Stream.of(replacementList).forEach(iconReplacement -> {
            File file = new File(Constants.ICON_PATH,
                    iconReplacement.getPackageName() + "_" + iconReplacement.getOriginRes());
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                param.res.setReplacement(iconReplacement.getOriginRes(),
                        new XResources.DrawableLoader() {
                            @Override
                            public Drawable newDrawable(XResources res, int id) throws Throwable {
                                return new BitmapDrawable(res, bitmap);
                            }
                        });
            } else {
                log(file.getAbsolutePath() + " not exist");
            }
        });
    }

    private void log(String message) {
        XposedBridge.log("Sorcery Xposed: " + message);
    }
}

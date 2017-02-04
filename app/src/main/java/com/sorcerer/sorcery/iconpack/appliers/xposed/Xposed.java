package com.sorcerer.sorcery.iconpack.appliers.xposed;

import android.content.res.Resources;
import android.content.res.XModuleResources;
import android.util.TypedValue;

import com.sorcerer.sorcery.iconpack.xposed.XposedUtils;
import com.sorcerer.sorcery.iconpack.xposed.theme.IconReplacementItem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/8
 */

public class Xposed
        implements IXposedHookZygoteInit, IXposedHookLoadPackage, IXposedHookInitPackageResources {
    private final static String PACKAGE_NAME = "com.sorcerer.sorcery.iconpack";
    private final static String PREF_NAME = "SIP_XPOSED";
    private XSharedPreferences mXSharedPref;
    private int mDisplayDpi;
    private List<String> mIconPackages = new ArrayList<>();
    private Map<String, List<IconReplacementItem>> mIconReplacementsMap = new HashMap<>();
    private String mThemePackage;
    private String mThemePackagePath;
    private boolean mActive;

    @Override
    public void handleInitPackageResources(
            InitPackageResourcesParam initPackageResourcesParam)
            throws Throwable {

    }

    @Override
    public void handleLoadPackage(LoadPackageParam loadPackageParam)
            throws Throwable {
        if (loadPackageParam.packageName.equals("com.teslacoilsw.launcher")) {
            handleNova(loadPackageParam);
        }
    }

    private void handleNova(LoadPackageParam loadPackageParam) {
        final String replaceTag = "replaceWithSIP";

        XposedHelpers.findAndHookMethod("android.content.res.Resources",
                loadPackageParam.classLoader,
                "getValueForDensity",
                Integer.TYPE, Integer.TYPE, TypedValue.class, Boolean.TYPE,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Resources resources = (Resources) param.thisObject;
                        int resId = (Integer) param.args[0];
                        String resName = resources.getResourceName(resId);
                        TypedValue typedValue = (TypedValue) param.args[2];
                        if (new File(XposedUtils.getCacheFilePath(resName, resId)).exists()) {
                            typedValue.string = replaceTag;
                        }
                    }
                });

        XposedHelpers.findAndHookMethod("android.content.res.AssetManager",
                loadPackageParam.classLoader,
                "openNonAssetFd",
                Integer.TYPE, String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if (param.args[1].equals(replaceTag)) {
                            param.setResult(null);
                        }
                    }
                });

        XposedHelpers.findAndHookMethod("android.content.res.AssetManager",
                loadPackageParam.classLoader,
                "openNonAssetFd",
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if (param.args[0].equals(replaceTag)) {
                            param.setResult(null);
                        }
                    }
                });
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        mXSharedPref = new XSharedPreferences(PACKAGE_NAME, PREF_NAME);
        mDisplayDpi = mXSharedPref.getInt("display_dpi", 320);
        mThemePackage = "com.sorcerer.sorcery.iconpakc";


        XModuleResources themeRes = XModuleResources.createInstance(mThemePackagePath, null);
    }
}

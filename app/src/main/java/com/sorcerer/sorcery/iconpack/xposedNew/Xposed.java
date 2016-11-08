package com.sorcerer.sorcery.iconpack.xposedNew;

import android.util.TypedValue;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/8
 */

public class Xposed
        implements IXposedHookZygoteInit, IXposedHookLoadPackage, IXposedHookInitPackageResources {
    @Override
    public void handleInitPackageResources(
            InitPackageResourcesParam initPackageResourcesParam)
            throws Throwable {

    }

    @Override
    public void handleLoadPackage(LoadPackageParam loadPackageParam)
            throws Throwable {
        if (loadPackageParam.packageName.equals("com.teslacoilsw.launcher")) {

        }
    }

    private void handleNova(LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod("android.content.res.Resources",
                loadPackageParam.classLoader,
                "getValueForDensity",
                Integer.TYPE, Integer.TYPE, TypedValue.class, Boolean.TYPE,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    }
                });
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {

    }
}

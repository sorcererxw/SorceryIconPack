package com.sorcerer.sorcery.iconpack.xposed;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Sorcerer on 2016/2/10 0010.
 */
public class Xposed implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam)
            throws Throwable {
        XposedBridge.log("Sorcery Icon Pack:" + loadPackageParam.packageName);
    }

}

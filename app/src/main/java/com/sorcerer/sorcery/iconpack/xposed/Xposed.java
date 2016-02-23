package com.sorcerer.sorcery.iconpack.xposed;

import android.content.res.Resources;
import android.content.res.XModuleResources;
import android.content.res.XResources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import com.google.gson.Gson;
import com.sorcerer.sorcery.iconpack.xposed.theme.IconReplacementItem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Sorcerer on 2016/2/10 0010.
 */
public class Xposed
        implements IXposedHookZygoteInit, IXposedHookLoadPackage, IXposedHookInitPackageResources {
    private static final String SHARED_PREFERENCE_NAME = "SIP_XPOSED";
    private static String MODULE_PATH;
    public static String TAG = "SIP/Xposed";
    public static XSharedPreferences XSharedPrefs;
    private static int mDisplayDpi;
    private static ArrayList<String> mIconPackages = new ArrayList();
    private static HashMap<String, ArrayList<IconReplacementItem>> mIconReplacementsHashMap =
            new HashMap();
    private static String mThemePackage;
    private static String mThemePackagePath;
    private boolean mActive;


    private void log(Object o) {
        XposedBridge.log("[" + TAG + "]" + o.toString());
    }

    public void initZygote(StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;
        XSharedPrefs = new XSharedPreferences("com.sorcerer.sorcery.iconpack",
                SHARED_PREFERENCE_NAME);
        mDisplayDpi = XSharedPrefs.getInt("display_dpi", 320);
        mThemePackage = XSharedPrefs.getString("theme_package_name", null);
        mThemePackagePath = XSharedPrefs.getString("theme_package_path", null);
        mActive = XSharedPrefs.getBoolean("pref_global_load", false);
        if (mActive) {
            log(mThemePackage == null ? "theme package is null" : "theme package is contain");
            if (mThemePackage != null) {
                XposedBridge.log("[" + TAG + "] [" + mThemePackage + "] Loading...");
                XModuleResources themeRes =
                        XModuleResources.createInstance(mThemePackagePath, null);
//            try {
//                if (mThemeIconShader == null) {
//                    int shaderRes = themeRes.getIdentifier("shader", "xml", mThemePackage);
//                    if (shaderRes != 0) {
//                        mThemeIconShader = IconShader.parseXml(themeRes.getXml(shaderRes));
//                        XposedBridge.log("[" + TAG + "] [" + mThemePackage + "] Loaded shader.xml");
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
                try {
                    Gson gson = new Gson();
//                if (XSharedPrefs.getString("theme_icon_mask", null) != null) {
//                    mThemeIconMask = (IconMaskItem) gson.fromJson(XSharedPrefs.getString("theme_icon_mask", null), IconMaskItem.class);
//                }
                    for (String pkg : (String[]) gson
                            .fromJson(XSharedPrefs.getString("theme_icon_packages", null),
                                    String[].class)) {
                        mIconPackages.add(pkg);
                        IconReplacementItem[] iconReplacementItems = (IconReplacementItem[]) gson
                                .fromJson(XSharedPrefs.getString("theme_icon_for_" + pkg, null),
                                        IconReplacementItem[].class);
                        ArrayList<IconReplacementItem> items = new ArrayList();
                        for (IconReplacementItem item : iconReplacementItems) {
                            items.add(item);
                        }
                        mIconReplacementsHashMap.put(pkg, items);
                    }
                    if (mIconPackages.contains("android")) {
                        Iterator i$ =
                                ((ArrayList) mIconReplacementsHashMap.get("android")).iterator();
                        while (i$.hasNext()) {
                            IconReplacementItem it = (IconReplacementItem) i$.next();
                            if (new File(XposedUtils
                                    .getCacheFilePath(it.getPackageName(), it.getOrigRes()))
                                    .exists()) {
                                XResources.setSystemWideReplacement(it.getOrigRes(),
                                        new XResources.DrawableLoader() {
                                            public Drawable newDrawable(XResources res, int id)
                                                    throws Throwable {
                                                return XposedUtils.getCachedIcon(res,
                                                        res.getResourcePackageName(id),
                                                        id);
                                            }
                                        });
                            } else if (!it.hasNoCustomIcon()) {
                                try {
                                    final Drawable icon = new BitmapDrawable(XResources.getSystem(),
                                            XposedUtils.getBitmapForDensity(themeRes,
                                                    mDisplayDpi,
                                                    it.getReplacementRes()));
                                    XResources.setSystemWideReplacement(it.getOrigRes(),
                                            new XResources.DrawableLoader() {
                                                public Drawable newDrawable(XResources res, int id)
                                                        throws Throwable {
                                                    return icon;
                                                }
                                            });
                                } catch (Exception e2) {
                                    XposedBridge
                                            .log("[" + TAG + "] \tFAILED (Orig Res Not Found): " +
                                                    it.getPackageName());
                                }
                            } else {
//                            try {
//                                XResources.setSystemWideReplacement(it.getOrigRes(),
//                                        new DrawableLoader() {
//                                            public Drawable newDrawable(XResources res, int id)
//                                                    throws Throwable {
//                                                Drawable icon = Utils.themeIconWithShader(res,
//                                                        XModuleResources
//                                                                .createInstance(mThemePackagePath,
//                                                                        res),
//                                                        mDisplayDpi,
//                                                        id,
//                                                        mThemeIconMask,
//                                                        mThemeIconShader);
//                                                Utils.cacheDrawable(res.getResourcePackageName(id),
//                                                        id,
//                                                        (BitmapDrawable) icon);
//                                                return icon;
//                                            }
//                                        });
//                            } catch (Exception e3) {
//                                XposedBridge.log("[" + TAG + "] \tFAILED (Orig Res Not Found): " +
//                                        it.getPackageName());
//                            }
                            }
                        }
                    }
                } catch (Exception e4) {
                    Writer sw = new StringWriter();
                    e4.printStackTrace(new PrintWriter(sw));
                    XposedBridge.log(sw.toString());
                }
            }
        }
    }

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (mActive) {
            if (mThemePackage != null && new File(mThemePackagePath).exists()) {
                if (lpparam.packageName.equals("com.teslacoilsw.launcher")) {
                    appWorkaroundOne(lpparam);
                }
                if (lpparam.packageName.equals("com.abcOrganizer.lite")) {
                    appWorkaroundThree(lpparam, "com.abcOrganizer.lite", "icon");
                }
                if (lpparam.packageName.equals("com.abcOrganizer")) {
                    appWorkaroundThree(lpparam, "com.abcOrganizer", "icon");
                }
            }
            if (lpparam.packageName.equals("com.tsf.shell") && mThemePackage != null &&
                    new File(mThemePackagePath).exists()) {
                appWorkaroundTwo(lpparam, "com.tsf.shell", "tsf_ico");
            }
        }
    }

//    private void appWorkaroundOne(XC_LoadPackage.LoadPackageParam lpparam) {
//        XposedHelpers.findAndHookMethod("android.content.res.Resources",
//                lpparam.classLoader,
//                "getValueForDensity",
//                new Object[]{Integer.TYPE, Integer.TYPE, TypedValue.class, Boolean.TYPE,
//                        new XC_MethodHook() {
//                            protected void afterHookedMethod(MethodHookParam param)
//                                    throws Throwable {
//                                Resources res = (Resources) param.thisObject;
//                                int resId = ((Integer) param.args[0]).intValue();
//                                String packageName = res.getResourcePackageName(resId);
//                                TypedValue value = (TypedValue) param.args[2];
//                                if (mIconPackages.contains(packageName)) {
//                                    Iterator i$ = ((ArrayList) mIconReplacementsHashMap
//                                            .get(packageName)).iterator();
//                                    while (i$.hasNext()) {
//                                        if (((IconReplacementItem) i$.next()).getOrigRes() ==
//                                                resId) {
//                                            value.string = "replaceWithIconThemer";
//                                            return;
//                                        }
//                                    }
//                                }
//                            }
//                        }});
//        XC_MethodHook methodHook = new XC_MethodHook() {
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                if (param.args[0].equals("replaceWithIconThemer")) {
//                    param.setResult(null);
//                }
//            }
//        };
//        XposedHelpers.findAndHookMethod("android.content.res.AssetManager",
//                lpparam.classLoader,
//                "openNonAssetFd",
//                new Object[]{Integer.TYPE, String.class, methodHook});
//        XposedHelpers.findAndHookMethod("android.content.res.AssetManager",
//                lpparam.classLoader,
//                "openNonAssetFd",
//                new Object[]{String.class, methodHook});
//    }

    private void appWorkaroundOne(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("android.content.res.Resources",
                lpparam.classLoader,
                "getValueForDensity",
                new Object[]{Integer.TYPE, Integer.TYPE, TypedValue.class, Boolean.TYPE,
                        new XC_MethodHook() {
                            protected void afterHookedMethod(MethodHookParam param)
                                    throws Throwable {
                                XposedBridge.log("[" + TAG + "] [" + mThemePackage +
                                        "] Overriding Nova Asset Manager call");
                                Resources res = (Resources) param.thisObject;
                                int resId = ((Integer) param.args[0]).intValue();
                                String resName = res.getResourceName(resId);
                                TypedValue value = (TypedValue) param.args[2];
                                if (new File(XposedUtils
                                        .getCacheFilePath(res.getResourcePackageName(resId),
                                                resId)).exists()) {
                                    value.string = "replaceWithIconThemer";
                                }
                            }
                        }});
        XposedHelpers.findAndHookMethod("android.content.res.AssetManager",
                lpparam.classLoader,
                "openNonAssetFd",
                new Object[]{Integer.TYPE, String.class, new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if (param.args[1].equals("replaceWithIconThemer")) {
                            param.setResult(null);
                        }
                    }
                }});
        XposedHelpers.findAndHookMethod("android.content.res.AssetManager",
                lpparam.classLoader,
                "openNonAssetFd",
                new Object[]{String.class, new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if (param.args[0].equals("replaceWithIconThemer")) {
                            param.setResult(null);
                        }
                    }
                }});
    }


    private void appWorkaroundTwo(XC_LoadPackage.LoadPackageParam lpparam,
                                  final String packageNameToIgnore, final String resNameToIgnore) {
        XposedHelpers.findAndHookMethod("android.content.res.Resources",
                lpparam.classLoader,
                "openRawResource",
                new Object[]{Integer.TYPE, new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Resources res = (Resources) param.thisObject;
                        int resId = ((Integer) param.args[0]).intValue();
                        String resName = res.getResourceName(resId);
                        String packageName = res.getResourcePackageName(resId);
                        if ((!packageName.equals(packageNameToIgnore) ||
                                resName.equals(resNameToIgnore)) &&
                                mIconReplacementsHashMap.get(packageName) != null) {
                            Resources themeRes =
                                    XModuleResources.createInstance(mThemePackagePath, null);
                            Iterator i$ = ((ArrayList) mIconReplacementsHashMap.get(packageName))
                                    .iterator();
                            while (i$.hasNext()) {
                                IconReplacementItem it = (IconReplacementItem) i$.next();
                                if (it.getOrigResName().equals(resName)) {
                                    File file = new File(XposedUtils
                                            .getCacheFilePath(packageName, it.getOrigRes()));
                                    if (!(file.exists() || mThemePackagePath == null ||
                                            !new File(mThemePackagePath).exists())) {
//                                IconMaskItem im = getIconMask();
                                        if (it.hasNoCustomIcon() && false) {
//                                    Utils.cacheDrawable(packageName, it.getOrigResName(), Utils.themeIconWithShader(res, themeRes, mDisplayDpi, resId, im, getIconShaders()));
                                        } else if (it.getReplacementResName() != null) {
                                            XposedUtils.cacheDrawable(it.getPackageName(),
                                                    it.getOrigRes(),
                                                    (BitmapDrawable) new BitmapDrawable(res,
                                                            XposedUtils
                                                                    .getBitmapForDensity(themeRes,
                                                                            mDisplayDpi,
                                                                            it.getReplacementRes())));
                                        }
                                    }
                                    if (file.exists()) {
                                        if (param.getResult() != null) {
                                            ((InputStream) param.getResult()).close();
                                        }
                                        param.setResult(new BufferedInputStream(new FileInputStream(
                                                file)));
                                        return;
                                    }
                                    return;
                                }
                            }
                        }
                    }
                }});
    }


    private void appWorkaroundThree(XC_LoadPackage.LoadPackageParam lpparam,
                                    final String packageNameToIgnore,
                                    final String resNameToIgnore) {
        XC_MethodHook methodHook = new XC_MethodHook() {
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Resources res = (Resources) param.args[0];
                int resId = ((Integer) param.args[1]).intValue();
                String resName = res.getResourceName(resId);
                String packageName = res.getResourcePackageName(resId);
                BitmapFactory.Options bmOpts = null;
                if (param.args[2] != null) {
                    bmOpts = (BitmapFactory.Options) param.args[2];
                }
                if ((!packageName.equals(packageNameToIgnore) || resName.equals(resNameToIgnore)) &&
                        mIconReplacementsHashMap.get(packageName) != null) {
                    Resources themeRes =
                            XModuleResources.createInstance(mThemePackagePath, null);
                    Iterator i$ = ((ArrayList) mIconReplacementsHashMap.get(packageName))
                            .iterator();
                    while (i$.hasNext()) {
                        IconReplacementItem it = (IconReplacementItem) i$.next();
                        if (it.getOrigResName().equals(resName)) {
                            File file = new File(XposedUtils
                                    .getCacheFilePath(packageName, it.getOrigRes()));
                            if (!(file.exists() || mThemePackage == null ||
                                    !new File(mThemePackagePath).exists())) {
//                                IconMaskItem im = getIconMask();
                                if (it.hasNoCustomIcon() && false) {
//                                    Utils.cacheDrawable(packageName,
//                                            it.getOrigResName(),
//                                            Utils.themeIconWithShader(res,
//                                                    themeRes,
//                                                    mDisplayDpi,
//                                                    resId,
//                                                    im,
//                                                    getIconShaders()));
                                } else if (it.getReplacementResName() != null) {
                                    XposedUtils.cacheDrawable(it.getPackageName(),
                                            it.getOrigRes(),
                                            (BitmapDrawable) new BitmapDrawable(res,
                                                    XposedUtils.getBitmapForDensity(themeRes,
                                                            mDisplayDpi,
                                                            it.getReplacementRes())));
                                }
                            }
                            if (file.exists()) {
                                param.setResult(XposedUtils
                                        .getCachedIcon(res, packageName, resId, bmOpts)
                                        .getBitmap());
                                return;
                            }
                            return;
                        }
                    }
                }
            }
        };
        XposedHelpers.findAndHookMethod("android.graphics.BitmapFactory",
                lpparam.classLoader,
                "decodeResource",
                new Object[]{Resources.class, Integer.TYPE, BitmapFactory.Options.class,
                        methodHook});
        XposedHelpers.findAndHookMethod("android.graphics.BitmapFactory",
                lpparam.classLoader,
                "decodeResource",
                new Object[]{Resources.class, Integer.TYPE, methodHook});
    }

    public void handleInitPackageResources(
            XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
        if (mActive) {
            log("---------------------------------------------------");
            log(resparam.packageName);
            if (mIconPackages.contains(resparam.packageName)) {
                log("contain");
                Iterator i$ =
                        ((ArrayList) mIconReplacementsHashMap.get(resparam.packageName)).iterator();
                while (i$.hasNext()) {
                    IconReplacementItem it = (IconReplacementItem) i$.next();
                    if (new File(XposedUtils.getCacheFilePath(it.getPackageName(), it.getOrigRes()))
                            .exists()) {
                        log("exist");
                        resparam.res
                                .setReplacement(it.getOrigRes(), new XResources.DrawableLoader() {
                                    public Drawable newDrawable(XResources res, int id)
                                            throws Throwable {
                                        return XposedUtils
                                                .getCachedIcon(res,
                                                        res.getResourcePackageName(id),
                                                        id);
                                    }
                                });
                    } else if (it.hasNoCustomIcon() && false) {
//                    try {
//                        resparam.res.setReplacement(it.getOrigRes(), new DrawableLoader() {
//                            public Drawable newDrawable(XResources res, int id) throws Throwable {
//                                Drawable icon = Utils.themeIconWithShader(res,
//                                        XModuleResources.createInstance(mThemePackagePath, res),
//                                        mDisplayDpi,
//                                        id,
//                                        mThemeIconMask,
//                                        mThemeIconShader);
//                                Utils.cacheDrawable(res.getResourcePackageName(id),
//                                        id,
//                                        (BitmapDrawable) icon);
//                                return icon;
//                            }
//                        });
//                    } catch (Exception e) {
//                        XposedBridge.log("[" + TAG + "] \tFAILED (Orig Res Not Found): " +
//                                it.getPackageName());
//                    }
                    } else if (!it.hasNoCustomIcon()) {
                        try {
                            XposedUtils.cacheDrawable(it.getPackageName(),
                                    it.getOrigRes(),
                                    (BitmapDrawable) new BitmapDrawable(resparam.res,
                                            XposedUtils.getBitmapForDensity(XModuleResources
                                                            .createInstance(mThemePackagePath,
                                                                    resparam.res),
                                                    mDisplayDpi,
                                                    it.getReplacementRes())));
                            resparam.res
                                    .setReplacement(it.getOrigRes(),
                                            new XResources.DrawableLoader() {
                                                public Drawable newDrawable(XResources res, int id)
                                                        throws Throwable {
                                                    return XposedUtils.getCachedIcon(res,
                                                            res.getResourcePackageName(id),
                                                            id);
                                                }
                                            });
                        } catch (Exception e2) {
                            XposedBridge.log("[" + TAG + "] \tFAILED (Orig Res Not Found): " +
                                    it.getPackageName());
                        }
                    }
                }
            } else if (!new File(mThemePackagePath).exists()) {
            }
        }
    }
}

package com.sorcerer.sorcery.iconpack.xposed;

import android.content.res.Resources;
import android.content.res.XModuleResources;
import android.content.res.XResources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.TypedValue;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.xposed.theme.IconReplacementItem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
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
 * Created by Sorcerer on 2016/2/26 0026.
 */
public class Mod implements IXposedHookZygoteInit, IXposedHookLoadPackage,
        IXposedHookInitPackageResources {
    private static final String PACKAGE_NAME = "com.sorcerer.sorcery.iconpack";
    private static final String SHARED_PREFERENCE_NAME = "SIP_XPOSED";
    public static String TAG = "Mod";
    public static long mFileSize;
    public static long mLastModified;
    private String MODULE_PATH;
    private XSharedPreferences
            XSharedPrefs = new XSharedPreferences(PACKAGE_NAME,
            SHARED_PREFERENCE_NAME);
    private Gson gson = new Gson();
    private XResources.DrawableLoader loadIconFromCache = new XResources.DrawableLoader() {
        public Drawable newDrawable(XResources res, int id) throws Throwable {
            return XposedUtils.getCachedIcon(res, res.getResourcePackageName(id), id);
        }
    };
    private int mDisplayDpi;
    private HashMap<String, ArrayList<IconReplacementItem>> mIconReplacementsHashMap;
    //    private IconMaskItem mThemeIconMask;
//    private CompiledIconShader mThemeIconShader;
    private String mThemePackage;
    private String mThemePackagePath;

    public void initZygote(StartupParam startupParam) throws Throwable {
        this.MODULE_PATH = startupParam.modulePath;
        XposedBridge.log("[" + TAG + "] Version " + XModuleResources
                .createInstance(this.MODULE_PATH, null)
                .getString(R.string.version_name));
        loadThemeDetails();
        markPrefFileChanged();
        try {
            processIconForSystem();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            XposedBridge.log(sw.toString());
        }
    }

    private void processIconForSystem() throws Exception {
        if (this.mThemePackage != null) {
            loadReplacementsForPackage("android");
            if (this.mIconReplacementsHashMap.get("android") != null) {
                Resources themeRes = XModuleResources.createInstance(this.mThemePackagePath, null);
                Iterator i$ = ((ArrayList) this.mIconReplacementsHashMap.get("android")).iterator();
                while (i$.hasNext()) {
                    IconReplacementItem it = (IconReplacementItem) i$.next();
                    File file = new File(XposedUtils
                            .getCacheFilePath(it.getPackageName(), it.getOrigRes()));
                    if (!file.exists()) {
//                        if (it.hasNoCustomIcon() && new File(this.mThemePackagePath).exists() &&
//                                this.mThemeIconMask != null) {
//                            try {
//                                XposedUtils.cacheDrawable(XResources.getSystem()
//                                                .getResourcePackageName(it.getOrigRes()),
//                                        it.getOrigResName(),
//                                        XposedUtils.themeIconWithShader(XResources.getSystem(),
//                                                themeRes,
//                                                this.mDisplayDpi,
//                                                it.getOrigRes(),
//                                                this.mThemeIconMask,
//                                                this.mThemeIconShader));
//                            } catch (Exception e) {
//                                XposedBridge.log("[" + TAG + "] \tFAILED (Orig Res Not Found): " +
//                                        it.getPackageName());
//                            }
//                        }
                        if (!it.hasNoCustomIcon()
                                && new File(this.mThemePackagePath).exists()) {
                            try {
                                XposedUtils.cacheDrawable(it.getPackageName(),
                                        it.getOrigRes(),
                                        new BitmapDrawable(XResources.getSystem(),
                                                XposedUtils.getBitmapForDensity(themeRes,
                                                        this.mDisplayDpi,
                                                        it.getReplacementRes())));
                            } catch (Exception e2) {
                                XposedBridge.log("[" + TAG + "] \tFAILED (Orig Res Not Found): "
                                        + it.getPackageName());
                            }
                        }
                    }
                    if (file.exists()) {
                        XResources
                                .setSystemWideReplacement(it.getOrigRes(), this.loadIconFromCache);
                    }
                }
            }
        }
    }

    private void loadThemeDetails() {
        if (hasPrefFileChanged()) {
            this.XSharedPrefs.reload();
            this.mDisplayDpi = this.XSharedPrefs.getInt("display_dpi", 320);
            if (this.mThemePackage != this.XSharedPrefs.getString("theme_package_name", null)) {
                this.mThemePackage = this.XSharedPrefs.getString("theme_package_name", null);
            }
            this.mThemePackagePath = this.XSharedPrefs.getString("theme_package_path", null);
            if (this.mIconReplacementsHashMap == null) {
                this.mIconReplacementsHashMap = new HashMap();
            }
        }
    }

//    private IconMaskItem getIconMask() {
//        if (hasPrefFileChanged()) {
//            this.XSharedPrefs.reload();
//            markPrefFileChanged();
//            if (this.XSharedPrefs.getString("theme_icon_mask", null) == null) {
//                return null;
//            }
//            this.mThemeIconMask = (IconMaskItem) this.gson
//                    .fromJson(this.XSharedPrefs.getString("theme_icon_mask", null),
//                            IconMaskItem.class);
//        }
//        return this.mThemeIconMask;
//    }

//    private CompiledIconShader getIconShaders() {
//        if (hasPrefFileChanged()) {
//            this.XSharedPrefs.reload();
//            markPrefFileChanged();
//            XModuleResources themeRes =
//                    XModuleResources.createInstance(this.mThemePackagePath, null);
//            try {
//                int shaderRes = themeRes.getIdentifier("shader", "xml", this.mThemePackage);
//                if (shaderRes != 0) {
//                    this.mThemeIconShader = IconShader.parseXml(themeRes.getXml(shaderRes));
//                    XposedBridge
//                            .log("[" + TAG + "] [" + this.mThemePackage + "] Loaded shader.xml");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//        return this.mThemeIconShader;
//    }

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        loadThemeDetails();
        if (lpparam.packageName.equals(PACKAGE_NAME)) {
            XposedHelpers.findAndHookMethod("com.sorcerer.sorcery.xposed.XposedUtils",
                    lpparam.classLoader,
                    "isModuleActive",
                    new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(true);
                        }
                    });
        }
        if (this.mThemePackage != null) {
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
        if (lpparam.packageName.equals("com.tsf.shell")
                && this.mThemePackage != null
                && new File(this.mThemePackagePath).exists()) {
            appWorkaroundTwo(lpparam, "com.tsf.shell", "tsf_ico");
        }
    }

    private void appWorkaroundOne(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("android.content.res.Resources",
                lpparam.classLoader,
                "getValueForDensity",
                Integer.TYPE, Integer.TYPE, TypedValue.class, Boolean.TYPE,
                new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam param)
                            throws Throwable {
                        XposedBridge.log("[" + Mod.TAG + "] [" + Mod.this.mThemePackage
                                + "] Overriding Nova Asset Manager call");
                        Resources res = (Resources) param.thisObject;
                        int resId = (Integer) param.args[0];
                        String resName = res.getResourceName(resId);
                        TypedValue value = (TypedValue) param.args[2];
                        if (new File(XposedUtils
                                .getCacheFilePath(res.getResourcePackageName(resId),
                                        resId)).exists()) {
                            value.string = "replaceWithIconThemer";
                        }
                    }
                });
        XposedHelpers.findAndHookMethod("android.content.res.AssetManager",
                lpparam.classLoader,
                "openNonAssetFd",
                new Object[]{Integer.TYPE, String.class, new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if (param.args[1].equals("replaceWithIconThemer")) {
                            param.setResult(null);
                        }
                    }
                }
                });
        XposedHelpers.findAndHookMethod("android.content.res.AssetManager",
                lpparam.classLoader,
                "openNonAssetFd",
                new Object[]{String.class, new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if (param.args[0].equals("replaceWithIconThemer")) {
                            param.setResult(null);
                        }
                    }
                }
                });
    }

    private void appWorkaroundTwo(XC_LoadPackage.LoadPackageParam lpparam,
            final String packageNameToIgnore,
            final String resNameToIgnore) {
        XposedHelpers.findAndHookMethod("android.content.res.Resources",
                lpparam.classLoader,
                "openRawResource",
                Integer.TYPE, new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Resources res = (Resources) param.thisObject;
                        int resId = (Integer) param.args[0];
                        String resName = res.getResourceName(resId);
                        String packageName = res.getResourcePackageName(resId);
                        if ((!packageName.equals(packageNameToIgnore) || resName
                                .equals(resNameToIgnore))
                                && Mod.this.mIconReplacementsHashMap.get(packageName) != null) {
                            Resources themeRes = XModuleResources
                                    .createInstance(Mod.this.mThemePackagePath, null);
                            Iterator i$ =
                                    ((ArrayList) Mod.this.mIconReplacementsHashMap.get(packageName))
                                            .iterator();
                            while (i$.hasNext()) {
                                IconReplacementItem it = (IconReplacementItem) i$.next();
                                if (it.getOrigResName().equals(resName)) {
                                    File file = new File(XposedUtils
                                            .getCacheFilePath(packageName, it.getOrigRes()));
                                    if (!(file.exists() || Mod.this.mThemePackagePath == null
                                            || !new File(Mod.this.mThemePackagePath).exists())) {
//                                        IconMaskItem im = Mod.this.getIconMask();
//                                        if (it.hasNoCustomIcon() && im != null) {
//                                            XposedUtils.cacheDrawable(packageName,
//                                                    it.getOrigResName(),
//                                                    XposedUtils.themeIconWithShader(res,
//                                                            themeRes,
//                                                            Mod.this.mDisplayDpi,
//                                                            resId,
//                                                            im,
//                                                            Mod.this.getIconShaders()));
//                                        } else
                                        if (it.getReplacementResName() != null) {
                                            XposedUtils.cacheDrawable(it.getPackageName(),
                                                    it.getOrigRes(),
                                                    (BitmapDrawable) new BitmapDrawable(res,
                                                            XposedUtils
                                                                    .getBitmapForDensity(themeRes,
                                                                            Mod.this.mDisplayDpi,
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
                });
    }

    private void appWorkaroundThree(XC_LoadPackage.LoadPackageParam lpparam,
            final String packageNameToIgnore,
            final String resNameToIgnore) {
        XC_MethodHook methodHook = new XC_MethodHook() {
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Resources res = (Resources) param.args[0];
                int resId = (Integer) param.args[1];
                String resName = res.getResourceName(resId);
                String packageName = res.getResourcePackageName(resId);
                BitmapFactory.Options bmOpts = null;
                if (param.args[2] != null) {
                    bmOpts = (BitmapFactory.Options) param.args[2];
                }
                if ((!packageName.equals(packageNameToIgnore) || resName.equals(resNameToIgnore))
                        && Mod.this.mIconReplacementsHashMap.get(packageName) != null) {
                    Resources themeRes =
                            XModuleResources.createInstance(Mod.this.mThemePackagePath, null);
                    Iterator i$ = ((ArrayList) Mod.this.mIconReplacementsHashMap.get(packageName))
                            .iterator();
                    while (i$.hasNext()) {
                        IconReplacementItem it = (IconReplacementItem) i$.next();
                        if (it.getOrigResName().equals(resName)) {
                            File file = new File(XposedUtils
                                    .getCacheFilePath(packageName, it.getOrigRes()));
                            if (!(file.exists() || Mod.this.mThemePackage == null
                                    || !new File(Mod.this.mThemePackagePath).exists())) {
//                                IconMaskItem im = Mod.this.getIconMask();
//                                if (it.hasNoCustomIcon() && im != null) {
//                                    XposedUtils.cacheDrawable(packageName,
//                                            it.getOrigResName(),
//                                            XposedUtils.themeIconWithShader(res,
//                                                    themeRes,
//                                                    Mod.this.mDisplayDpi,
//                                                    resId,
//                                                    im,
//                                                    Mod.this.getIconShaders()));
//                                } else
                                if (it.getReplacementResName() != null) {
                                    XposedUtils.cacheDrawable(it.getPackageName(),
                                            it.getOrigRes(),
                                            (BitmapDrawable) new BitmapDrawable(res,
                                                    XposedUtils.getBitmapForDensity(themeRes,
                                                            Mod.this.mDisplayDpi,
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
        processIconForApps(resparam);
    }

    private void loadReplacementsForPackage(String packageName) {
        String app_themed_icon_packed =
                this.XSharedPrefs.getString("theme_icon_for_" + packageName, null);
        if (app_themed_icon_packed != null) {
            this.mIconReplacementsHashMap.put(packageName,
                    (ArrayList) this.gson.fromJson(app_themed_icon_packed,
                            new TypeToken<ArrayList<IconReplacementItem>>() {
                            }.getType()));
            XposedBridge.log("[" + TAG + "] [" + this.mThemePackage
                    + "] Loading replacements for package " + packageName);
            return;
        }
        this.mIconReplacementsHashMap.remove(packageName);
    }

    private void processIconForApps(XC_InitPackageResources.InitPackageResourcesParam resparam)
            throws Throwable {
        if (hasPrefFileChanged()) {
            loadThemeDetails();
            markPrefFileChanged();
            loadReplacementsForPackage(resparam.packageName);
        }
        if (this.mThemePackage != null && this.mThemePackagePath != null
                && this.mIconReplacementsHashMap.get(resparam.packageName) != null) {
            processIconReplacements(resparam.res,
                    (ArrayList) this.mIconReplacementsHashMap.get(resparam.packageName));
        }
    }

    private void processIconReplacements(XResources res, ArrayList<IconReplacementItem> icons)
            throws Throwable {
        Iterator i$ = icons.iterator();
        while (i$.hasNext()) {
            IconReplacementItem it = (IconReplacementItem) i$.next();
            File file = new File(XposedUtils
                    .getCacheFilePath(it.getPackageName(), it.getOrigRes()));
            if (!file.exists()) {
                if (this.mThemePackagePath != null) {
//                    IconMaskItem im = getIconMask();
                    BitmapDrawable icon;
//                    if (it.hasNoCustomIcon() && new File(this.mThemePackagePath).exists() &&
//                            im != null) {
//                        try {
//                            icon = XposedUtils.themeIconWithShader(res,
//                                    XModuleResources.createInstance(this.mThemePackagePath, res),
//                                    this.mDisplayDpi,
//                                    it.getOrigRes(),
//                                    im,
//                                    getIconShaders());
//                            XposedUtils.cacheDrawable(res.getResourcePackageName(it.getOrigRes()),
//                                    it.getOrigRes(),
//                                    icon);
//                            icon.getBitmap().recycle();
//                            XposedBridge.log("[" + TAG + "] \tSUCCESS Generated cache for " +
//                                    it.getOrigResName());
//                        } catch (Exception e) {
//                            XposedBridge.log("[" + TAG + "] \tFAILED (Orig Res Not Found): " +
//                                    it.getOrigResName());
//                        }
//                    } else
                    if (!it.hasNoCustomIcon() && new File(this.mThemePackagePath).exists()) {
                        try {
                            icon = new BitmapDrawable(res,
                                    XposedUtils.getBitmapForDensity(XModuleResources
                                                    .createInstance(this.mThemePackagePath, res),
                                            this.mDisplayDpi,
                                            it.getReplacementRes()));
                            XposedUtils.cacheDrawable(it.getPackageName(), it.getOrigRes(), icon);
                            icon.getBitmap().recycle();
                            XposedBridge.log("[" + TAG + "] \tSUCCESS Generated cache for "
                                    + it.getOrigResName());
                        } catch (Exception e2) {
                            XposedBridge.log("[" + TAG + "] \tFAILED (Orig Res Not Found): "
                                    + it.getOrigResName());
                        }
                    }
                } else {
                    return;
                }
            }
            if (file.exists()) {
                res.setReplacement(it.getOrigResName(), this.loadIconFromCache);
            }
        }
    }

    public boolean hasPrefFileChanged() {
        File mFile = new File(Environment.getDataDirectory(),
                "data/" + PACKAGE_NAME + "/shared_prefs/" + SHARED_PREFERENCE_NAME + ".xml");
        if (!mFile.canRead()) {
            return true;
        }
        long lastModified = mFile.lastModified();
        long fileSize = mFile.length();
        if (mLastModified == lastModified && mFileSize == fileSize) {
            return false;
        }
        return true;
    }

    public void markPrefFileChanged() {
        if (!new File(Environment.getDataDirectory(),
                "data/" + PACKAGE_NAME + "/shared_prefs/" + SHARED_PREFERENCE_NAME + ".xml")
                .canRead()) {
        }
    }
}
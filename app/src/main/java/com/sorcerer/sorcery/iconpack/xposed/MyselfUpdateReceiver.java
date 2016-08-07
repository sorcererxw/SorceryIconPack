package com.sorcerer.sorcery.iconpack.xposed;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.google.gson.Gson;
import com.sorcerer.sorcery.iconpack.xposed.theme.IconReplacementItem;
import com.sorcerer.sorcery.iconpack.xposed.theme.Util;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Sorcerer on 2016/2/23 0023.
 */
public class MyselfUpdateReceiver extends BroadcastReceiver {
    private static String TAG = "MyselfUpdateReceiver";
    private static String SHARED_PREFERENCE_NAME = "SIP_XPOSED";
    private boolean mActive;
    private SharedPreferences mPrefs;
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        mContext = context;
        mPrefs = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_WORLD_READABLE);
        mActive = mPrefs.getBoolean("pref_global_load", false);
        if (mActive) {
            if (action.equals(Intent.ACTION_MY_PACKAGE_REPLACED)) {
                reapply(context);
                String installedPkgName = "com.sorcerer.sorcery.iconpack";

                PackageManager pm = context.getPackageManager();
                SharedPreferences prefs =
                        context.getSharedPreferences(SHARED_PREFERENCE_NAME,
                                Context.MODE_WORLD_READABLE);
                SharedPreferences.Editor editor = prefs.edit();
                int displayDpi = prefs.getInt("display_dpi", 320);
                String themePackageName = prefs.getString("theme_package_name", null);
                String themePackagePath = prefs.getString("theme_package_path", null);
                Log.d(TAG, themePackageName);
                Log.d(TAG, themePackagePath);
                if (themePackageName != null && themePackagePath != null
                        && new File(themePackagePath).exists()) {
                    Log.d(TAG, "start");
                    try {
                        XmlPullParser xrp;
                        ArrayList<IconReplacementItem> items;
                        Resources origPkgRes;
                        Gson gson = new Gson();
                        ApplicationInfo themePackage =
                                pm.getApplicationInfo(themePackageName, 128);
                        Resources r = pm.getResourcesForApplication(themePackage.packageName);

                        if (r.getIdentifier("appfilter", "xml", themePackage.packageName) == 0) {
                            InputStream istr = r.getAssets().open("values/appfilter.xml");
                            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                            factory.setNamespaceAware(true);
                            xrp = factory.newPullParser();
                            xrp.setInput(istr, "UTF-8");
                        } else {
                            xrp = r.getXml(r
                                    .getIdentifier("appfilter",
                                            "xml",
                                            themePackage.packageName));
                        }
                        ArrayList<IconReplacementItem> themeIconsForApp = new ArrayList();
                        Iterator i$ =
                                Util.ParseIconReplacements(themePackage.packageName, r, xrp)
                                        .iterator();
                        while (i$.hasNext()) {
                            IconReplacementItem item = (IconReplacementItem) i$.next();
                            if (item.getPackageName().equals(installedPkgName)) {
                                Log.d(TAG, "found");
                                try {
                                    ActivityInfo activityInfo =
                                            pm.getActivityInfo(new ComponentName(item
                                                            .getPackageName(), item.getActivityName()),
                                                    128);
                                    if (activityInfo != null) {
                                        items = themeIconsForApp;
                                        origPkgRes = pm.getResourcesForApplication(item
                                                .getPackageName());
                                        item.setOrigRes(activityInfo.getIconResource());
                                        item.setOrigResName(origPkgRes
                                                .getResourceName(activityInfo
                                                        .getIconResource()));
                                        if (items.contains(item)) {
                                            items.remove(item);
                                        }
                                        items.add(item);
                                        if (item.getOrigRes() != 0) {
                                            try {
                                                String newPackageName = origPkgRes
                                                        .getResourcePackageName(item
                                                                .getOrigRes());
                                                if (!newPackageName
                                                        .equals(item.getPackageName())) {
                                                    item.setPackageName(newPackageName);
                                                }
                                            } catch (Exception e2) {
                                            }
                                        }
                                        XposedUtils.cacheDrawable(item.getPackageName(),
                                                item.getOrigRes(),
                                                (BitmapDrawable) new BitmapDrawable(origPkgRes,
                                                        XposedUtils.getBitmapForDensity(r,
                                                                displayDpi,
                                                                item.getReplacementRes())));
                                    }
                                } catch (Exception e3) {
                                }
                            }
                        }
                        editor.putString("theme_icon_for_" + installedPkgName,
                                gson.toJson((Object) themeIconsForApp));
                        editor.putLong("theme_last_applied", System.currentTimeMillis());
                        editor.commit();
                        Log.d(TAG, "Updated " + installedPkgName);

                    } catch (Exception e7) {
                        e7.printStackTrace();
                    }
                }
            }

        }
    }


    protected void reapply(Context context) {
        mContext = context;

        tryAndApplyIcon(mContext.getApplicationInfo());

    }

    private boolean appIsInstalledInMountASEC() {
        return mContext.getApplicationInfo().sourceDir.contains("asec/");
    }

    private void tryAndApplyIcon(final ApplicationInfo themePackage) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    XmlPullParser xrp;
                    XmlPullParser xrp2;
                    ArrayList<IconReplacementItem> items;
                    Resources origPkgRes;
                    SharedPreferences.Editor editor = mPrefs.edit();
                    Gson gson = new Gson();
                    ArrayList<String> mIconPackages = new ArrayList();
                    HashMap<String, ArrayList<IconReplacementItem>> mIconReplacementsHashMap =
                            new HashMap();
                    String themePackagePath = themePackage.sourceDir;

                    PackageManager pm = mContext.getPackageManager();
                    Resources r = mContext.getPackageManager()
                            .getResourcesForApplication(themePackage.packageName);
                    if (r.getIdentifier("appfilter", "xml", themePackage.packageName) == 0) {
                        InputStream istr = r.getAssets().open("values/appfilter.xml");
                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        factory.setNamespaceAware(true);
                        xrp = factory.newPullParser();
                        xrp.setInput(istr, "UTF-8");
                        InputStream istr2 = r.getAssets().open("values/appfilter.xml");
                        XmlPullParserFactory factory2 = XmlPullParserFactory.newInstance();
                        factory2.setNamespaceAware(true);
                        xrp2 = factory2.newPullParser();
                        xrp2.setInput(istr2, "UTF-8");
                    } else {
                        xrp = r.getXml(r
                                .getIdentifier("appfilter", "xml", themePackage.packageName));
                        xrp2 = r.getXml(r
                                .getIdentifier("appfilter", "xml", themePackage.packageName));
                    }
                    for (Map.Entry<String, ?> entry : mPrefs.getAll()
                            .entrySet()) {
                        if (((String) entry.getKey()).contains("theme_icon_for_")) {
                            editor.remove((String) entry.getKey());
                        }
                    }
                    editor.commit();

                    Iterator i$ = Util.ParseIconReplacements(themePackage.packageName, r, xrp)
                            .iterator();
                    while (i$.hasNext()) {
                        IconReplacementItem item = (IconReplacementItem) i$.next();
                        try {
                            ActivityInfo activityInfo = pm.getActivityInfo(new ComponentName(
                                    item.getPackageName(),
                                    item.getActivityName()), 128);
                            Log.d(TAG, "activity: " + item.getActivityName());
                            Log.d(TAG, "orig res name: " + item.getOrigResName());
                            Log.d(TAG, "component: " + item.getComponent());
                            Log.d(TAG, "orig res: " + item.getOrigRes());
                            Log.d(TAG, "replacement res: " + item.getReplacementRes());
                            Log.d(TAG, "replacement res name: " + item.getReplacementResName());
                            Log.d(TAG, "package: " + item.getPackageName());
                            if (activityInfo != null) {
                                if (mIconReplacementsHashMap.get(item.getPackageName()) == null) {
                                    mIconReplacementsHashMap
                                            .put(item.getPackageName(), new ArrayList());
                                }
                                items = (ArrayList) mIconReplacementsHashMap
                                        .get(item.getPackageName());
                                origPkgRes =
                                        pm.getResourcesForApplication(item.getPackageName());
                                if (activityInfo.getIconResource() != 0) {
                                    try {
                                        item.setPackageName(origPkgRes
                                                .getResourcePackageName(activityInfo
                                                        .getIconResource()));
                                    } catch (Exception e) {
                                    }
                                }
                                item.setOrigRes(activityInfo.getIconResource());

                                if (!items.contains(item)) {
                                    items.add(item);

                                    XposedUtils.cacheDrawable(item.getPackageName(),
                                            item.getOrigRes(),
                                            (BitmapDrawable) new BitmapDrawable(origPkgRes,
                                                    XposedUtils.getBitmapForDensity(r,
                                                            mPrefs.getInt("display_dpi", 320),
                                                            item.getReplacementRes())));
                                }
                                XposedUtils.cacheDrawable(item.getPackageName(),
                                        item.getOrigRes(),
                                        (BitmapDrawable) new BitmapDrawable(origPkgRes,
                                                XposedUtils.getBitmapForDensity(r,
                                                        mPrefs.getInt("display_dpi", 320),
                                                        item.getReplacementRes())));
                            }
                        } catch (Exception e2) {
                        }
                    }
                    editor.putString("theme_package_name", themePackage.packageName);
                    editor.putString("theme_package_path", themePackagePath);
                    for (Map.Entry<String, ArrayList<IconReplacementItem>> entry2 : mIconReplacementsHashMap
                            .entrySet()) {
                        mIconPackages.add(entry2.getKey());
                        editor.putString("theme_icon_for_" + ((String) entry2.getKey()),
                                gson.toJson(((ArrayList) entry2.getValue()).toArray()));
                    }
                    editor.putString("theme_icon_packages",
                            gson.toJson(mIconPackages.toArray()));
                    editor.commit();
                } catch (Exception e6) {
                    e6.printStackTrace();
                }
            }
        }).start();
    }
}

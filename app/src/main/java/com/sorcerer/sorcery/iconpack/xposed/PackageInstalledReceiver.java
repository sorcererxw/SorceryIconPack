package com.sorcerer.sorcery.iconpack.xposed;

import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.google.gson.Gson;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.xposed.theme.IconMaskItem;
import com.sorcerer.sorcery.iconpack.xposed.theme.IconReplacementItem;
import com.sorcerer.sorcery.iconpack.xposed.theme.IconShader;
import com.sorcerer.sorcery.iconpack.xposed.theme.Util;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class PackageInstalledReceiver extends BroadcastReceiver {
    private static String TAG = "SIP/PackageInstalledReceiver";
    private static String SHARED_PREFERENCE_NAME = "SIP_XPOSED";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "PackageInstalledReceiver onReceive: " + action);
        if (action.equals("android.intent.action.PACKAGE_ADDED")) {
            if (!intent.getBooleanExtra("InvokedByIconThemer", false)) {
                String installedPkgName = intent.getData().getEncodedSchemeSpecificPart();
                Log.d(TAG, "\tApp Name: " + installedPkgName);
                PackageManager pm = context.getPackageManager();
                SharedPreferences prefs =
                        context.getSharedPreferences(SHARED_PREFERENCE_NAME,
                                Context.MODE_WORLD_READABLE);
                Editor editor = prefs.edit();
                int displayDpi = prefs.getInt("display_dpi", 320);
                String themePackageName = prefs.getString("theme_package_name", null);
                String themePackagePath = prefs.getString("theme_package_path", null);
                if (themePackageName != null && themePackagePath != null &&
                        new File(themePackagePath).exists()) {
//                    Intent intent2;
//                    if (themePackageName.equals(installedPkgName)) {
//                        if (prefs.getBoolean("restartNotification", true)) {
//                            intent2 = new Intent(context, ThemeChooserActivity.class);
//                            intent2.putExtra("reapplyTheme", true);
//                            ((NotificationManager) context.getSystemService("notification")).notify(
//                                    1,
//                                    new Builder(context).setSmallIcon(R.drawable.ic_stat_restart)
//                                            .setContentTitle("Unicon").setContentText(
//                                            "Tap here to reapply theme for changes to take effect.")
//                                            .setTicker(
//                                                    "Unicon needs to reapply your theme for changes to take effect")
//                                            .setPriority(-2).setContentIntent(PendingIntent
//                                            .getActivity(context, 1, intent2, 268435456)).build());
//                            return;
//                        }
//                    }
                    try {
                        XmlPullParser xrp;
                        ArrayList<IconReplacementItem> items;
                        Resources origPkgRes;
                        Gson gson = new Gson();
                        ApplicationInfo themePackage = pm.getApplicationInfo(themePackageName, 128);
                        Resources r = pm.getResourcesForApplication(themePackage.packageName);
                        IconMaskItem mThemeIconMask = null;
//                        try {
//                            int shaderRes = r.getIdentifier("shader", "xml", themePackageName);
//                            if (shaderRes != 0) {
//                                mThemeIconShader = IconShader.parseXml(r.getXml(shaderRes));
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        if (prefs.getString("theme_icon_mask", null) != null) {
//                            mThemeIconMask = (IconMaskItem) gson
//                                    .fromJson(prefs.getString("theme_icon_mask", null),
//                                            IconMaskItem.class);
//                        }
                        if (r.getIdentifier("appfilter", "xml", themePackage.packageName) == 0) {
                            InputStream istr = r.getAssets().open("appfilter.xml");
                            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                            factory.setNamespaceAware(true);
                            xrp = factory.newPullParser();
                            xrp.setInput(istr, "UTF-8");
                        } else {
                            xrp = r.getXml(r
                                    .getIdentifier("appfilter", "xml", themePackage.packageName));
                        }
                        ArrayList<IconReplacementItem> themeIconsForApp = new ArrayList();
                        Iterator i$ = Util.ParseIconReplacements(themePackage.packageName, r, xrp)
                                .iterator();
                        while (i$.hasNext()) {
                            IconReplacementItem item = (IconReplacementItem) i$.next();
                            if (item.getPackageName().equals(installedPkgName)) {
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
                                                .getResourceName(activityInfo.getIconResource()));
                                        if (items.contains(item)) {
                                            items.remove(item);
                                        }
                                        items.add(item);
                                        if (item.getOrigRes() != 0) {
                                            try {
                                                String newPackageName = origPkgRes
                                                        .getResourcePackageName(item.getOrigRes());
                                                if (!newPackageName.equals(item.getPackageName())) {
                                                    item.setPackageName(newPackageName);
                                                }
                                            } catch (Exception e2) {
                                            }
                                        }
                                        Utils.cacheDrawable(item.getPackageName(),
                                                item.getOrigRes(),
                                                (BitmapDrawable) new BitmapDrawable(origPkgRes,
                                                        Utils.getBitmapForDensity(r,
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
//                        if (prefs.getBoolean("restartNotification", true)) {
//                            intent2 = new Intent(context, ThemeChooserActivity.class);
//                            intent2.putExtra("promptRestartLauncher", true);
//                            ((NotificationManager) context.getSystemService("notification")).notify(
//                                    0,
//                                    new Builder(context).setSmallIcon(R.drawable.ic_stat_restart)
//                                            .setContentTitle("Unicon").setContentText(
//                                            "Tap here to restart launcher for changes to take effect.")
//                                            .setTicker("Unicon needs to restart your launcher")
//                                            .setPriority(-2).setContentIntent(PendingIntent
//                                            .getActivity(context, 0, intent2, 268435456)).build());
//                        }
                    } catch (Exception e7) {
                        e7.printStackTrace();
                    }
                }
            }
        }
    }
}

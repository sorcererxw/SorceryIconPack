package com.sorcerer.sorcery.iconpack.xposed;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.media.TransportMediator;
import android.util.Log;
import android.support.v4.app.NotificationCompat.Builder;

import com.google.gson.Gson;
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.activities.LabActivity;
import com.sorcerer.sorcery.iconpack.xposed.theme.IconMaskItem;
import com.sorcerer.sorcery.iconpack.xposed.theme.IconReplacementItem;
import com.sorcerer.sorcery.iconpack.xposed.theme.IconShader;
import com.sorcerer.sorcery.iconpack.xposed.theme.Util;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Sorcerer on 2016/2/26 0026.
 */
public class PackageAddedReceiver extends BroadcastReceiver {
    private static final String TAG = "SIP/PAReceiver";

    private static String SHARED_PREFERENCE_NAME = "SIP_XPOSED";
    private boolean mActive;
    private SharedPreferences mPrefs;

    public void onReceive(Context context, Intent intent) {

        mPrefs = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_WORLD_READABLE);
        mActive = mPrefs.getBoolean("pref_global_load", false);
        if (mActive == false) {
            return;
        }
        String action = intent.getAction();
        Log.d(TAG, "PackageInstalledReceiver onReceive: " + action);
        if (action.equals("android.intent.action.PACKAGE_ADDED")) {
            if (!intent.getBooleanExtra("InvokedBySorcery", false)) {
                String installedPkgName = intent.getData().getEncodedSchemeSpecificPart();
                Log.d(TAG, "\tApp Name: " + installedPkgName);
                PackageManager pm = context.getPackageManager();
                SharedPreferences prefs =
                        context.getSharedPreferences(LabActivity.SHARED_PREFERENCE_NAME,
                                Context.MODE_WORLD_READABLE);
                SharedPreferences.Editor editor = prefs.edit();
                int displayDpi = prefs.getInt("display_dpi", 320);
                String themePackageName = prefs.getString("theme_package_name", null);
                String themePackagePath = prefs.getString("theme_package_path", null);
                if (themePackageName != null && themePackagePath != null &&
                        new File(themePackagePath).exists()) {
                    Intent intent2;
//                    if (themePackageName.equals(installedPkgName)) {
//                        if (prefs.getBoolean("restartNotification", true)) {
//                            intent2 = new Intent(context, LabActivity.class);
//                            intent2.putExtra("reapplyTheme", true);
//                            ((NotificationManager) context
//                                    .getSystemService(Context.NOTIFICATION_SERVICE)).notify(
//                                    1,
//                                    new Builder(context)
//                                            .setLargeIcon(BitmapFactory.decodeResource(context
//                                                    .getResources(), R.mipmap.ic_launcher))
//                                            .setSmallIcon(R.mipmap.ic_launcher)
//                                            .setContentTitle("Sorcery")
//                                            .setContentText(
//                                                    context.getString(R.string.global_load_package_add_notify))
//                                            .setTicker(
//                                                    "Sorcery needs to reapply your theme for " +
//                                                            "changes to take effect")
//                                            .setPriority(Notification.PRIORITY_DEFAULT)
//                                            .setContentIntent(PendingIntent.getActivity(context,
//                                                    1,
//                                                    intent2,
//                                                    PendingIntent.FLAG_CANCEL_CURRENT))
//                                            .setAutoCancel(true)
//                                            .build());
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
//                        CompiledIconShader mThemeIconShader = null;
//                        IconMaskItem mThemeIconMask = null;
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
                                                    PackageManager.GET_META_DATA);
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

//                        if (mThemeIconMask != null) {
//                            PackageInfo packageInfo = pm.getPackageInfo(installedPkgName, 129);
//                            String packageName = installedPkgName;
//                            items = themeIconsForApp;
//                            origPkgRes = pm.getResourcesForApplication(packageInfo.packageName);
//                            IconReplacementItem iconReplacementItem =
//                                    new IconReplacementItem(packageInfo.packageName, null);
//                            iconReplacementItem.setOrigRes(packageInfo.applicationInfo.icon);
//                            iconReplacementItem.setOrigResName(origPkgRes
//                                    .getResourceName(packageInfo.applicationInfo.icon));
//                            if (packageInfo.applicationInfo.icon != 0) {
//                                try {
//                                    packageName = origPkgRes
//                                            .getResourcePackageName(packageInfo.applicationInfo.icon);
//                                    iconReplacementItem.setPackageName(packageName);
//                                } catch (Exception e4) {
//                                }
//                            }
//                            if (!(packageInfo.applicationInfo.icon == 0 ||
//                                    items.contains(iconReplacementItem))) {
//                                try {
//                                    XposedUtils.cacheDrawable(iconReplacementItem.getPackageName(),
//                                            iconReplacementItem.getOrigResName(),
//                                            XposedUtils.themeIconWithShader(origPkgRes,
//                                                    r,
//                                                    displayDpi,
//                                                    iconReplacementItem.getOrigRes(),
//                                                    mThemeIconMask,
//                                                    mThemeIconShader));
//                                } catch (Exception e5) {
//                                }
//                                items.add(iconReplacementItem);
//                            }
//                            if (packageInfo.activities != null) {
//                                for (ActivityInfo ai : packageInfo.activities) {
//                                    iconReplacementItem =
//                                            new IconReplacementItem(packageName, ai.name);
//                                    iconReplacementItem.setOrigRes(ai.getIconResource());
//                                    iconReplacementItem.setOrigResName(origPkgRes
//                                            .getResourceName(ai.getIconResource()));
//                                    if (!(ai.getIconResource() == 0 ||
//                                            items.contains(iconReplacementItem))) {
//                                        try {
//                                            XposedUtils.cacheDrawable(iconReplacementItem
//                                                            .getPackageName(),
//                                                    iconReplacementItem.getOrigResName(),
//                                                    XposedUtils.themeIconWithShader(pm
//                                                                    .getResourcesForApplication(packageInfo.packageName),
//                                                            r,
//                                                            displayDpi,
//                                                            iconReplacementItem.getOrigRes(),
//                                                            mThemeIconMask,
//                                                            mThemeIconShader));
//                                            items.add(iconReplacementItem);
//                                        } catch (Exception e6) {
//                                        }
//                                    }
//                                }
//                            }
//                        }
                        editor.putString("theme_icon_for_" + installedPkgName,
                                gson.toJson((Object) themeIconsForApp));
                        editor.putLong("theme_last_applied", System.currentTimeMillis());
                        editor.commit();
                        Log.d(TAG, "Updated " + installedPkgName);
                        if (prefs.getBoolean("restartNotification", true)) {
                            intent2 = new Intent(context, LabActivity.class);
                            intent2.putExtra("promptRestartLauncher", true);
                            ((NotificationManager) context
                                    .getSystemService(Context.NOTIFICATION_SERVICE)).notify(
                                    0,
                                    new Builder(context)
//                                            .setLargeIcon(BitmapFactory.decodeResource(context
//                                                    .getResources(), R.mipmap.ic_launcher))
                                            .setSmallIcon(R.drawable.ic_notification)
                                            .setContentTitle("Sorcery")
                                            .setContentText(
                                                    context.getString(R.string.global_load_package_add_notify))
//                                            .setTicker("Sorcery needs to restart your launcher")
//                                            .setPriority(Notification.PRIORITY_MIN)
                                            .setContentIntent
                                                    (PendingIntent.getActivity(context,
                                                            0,
                                                            intent2,
                                                            PendingIntent.FLAG_CANCEL_CURRENT))
                                            .setAutoCancel(true)
                                            .build());
                        }
                    } catch (Exception e7) {
                        e7.printStackTrace();
                    }
                }
            }
        }
    }
}

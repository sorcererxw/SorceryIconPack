package com.sorcerer.sorcery.iconpack.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Build;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.sorcerer.sorcery.iconpack.models.AppInfo;
import com.sorcerer.sorcery.iconpack.models.AppfilterItem;
import com.sorcerer.sorcery.iconpack.models.LauncherInfo;

import org.xmlpull.v1.XmlPullParser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/4/26
 */
public class PackageUtil {
    public static List<AppInfo> getComponentInfo(Context context, boolean withHasCustomIcon)
            throws Exception {
        PackageManager pm = context.getApplicationContext().getPackageManager();
        List<ResolveInfo> list = getInstallApps(context);

        final List<String> xmlStringList = getAppfilterComponentList(context);

        return Stream.of(list)
                .map(resolveInfo -> {
                    AppInfo tempAppInfo = new AppInfo(
                            resolveInfo.activityInfo.packageName + "/"
                                    + resolveInfo.activityInfo.name,
                            resolveInfo.loadLabel(pm).toString(),
                            resolveInfo.loadIcon(pm));
                    if (withHasCustomIcon && xmlStringList.contains(tempAppInfo.getCode())) {
                        tempAppInfo.setHasCustomIcon(true);
                    }
                    return tempAppInfo;
                })
                .sorted((o1, o2) -> {
                    try {
                        String s1 = new String(o1.getName().getBytes("GB2312"), "ISO-8859-1");
                        String s2 = new String(o2.getName().getBytes("GB2312"), "ISO-8859-1");
                        return s1.compareTo(s2);
                    } catch (UnsupportedEncodingException e) {
                        Timber.e(e);
                    }
                    return o1.getName().compareTo(o2.getName());
                })
                .collect(Collectors.toList());
    }

    public static String getCurrentLauncher(Context context) {
        try {
            PackageManager localPackageManager = context.getPackageManager();
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            return localPackageManager.resolveActivity(intent,
                    PackageManager.MATCH_DEFAULT_ONLY).activityInfo.packageName;
        } catch (Exception e) {
            Timber.e(e);
        }
        return "";
    }

    public static List<ResolveInfo> getInstallApps(Context context) {
        try {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            Intent intent = new Intent("android.intent.action.MAIN", null);
            intent.addCategory("android.intent.category.LAUNCHER");
            return pm.queryIntentActivities(intent, 0);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static List<AppfilterItem> getAppfilterList(Context context) {
        List<AppfilterItem> list = new ArrayList<>();
        int i = context.getResources().getIdentifier("appfilter", "xml", context.getPackageName());
        XmlResourceParser parser = context.getResources().getXml(i);
        int eventType;
        try {
            eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("item")) {
                            String component = parser.getAttributeValue(null, "component");
                            String drawable = parser.getAttributeValue(null, "drawable");
                            if (component.startsWith(":")) {
                                break;
                            }
                            try {
                                list.add(new AppfilterItem(
                                        component.substring(14, component.length() - 1), drawable));
                            } catch (Exception e) {
                                Timber.e(e);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            Timber.e(e);
        }
        return list;
    }

    public static List<String> getAppfilterComponentList(Context context) {
        List<String> list = new ArrayList<>();
        int i = context.getResources().getIdentifier("appfilter", "xml", context.getPackageName());
        XmlResourceParser parser = context.getResources().getXml(i);
        int eventType;
        try {
            eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("item")) {
                            String component = parser.getAttributeValue(null, "component");
                            try {
                                if (component.charAt(0) != ':') {
                                    list.add(component.substring(14, component.length() - 1));
                                }
                            } catch (Exception e) {
                                Timber.e(e);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            Timber.e(e);
        }
        return list;
    }

    public static Map<String, List<String>> getIconWithPackageList(Context context) {
        Map<String, List<String>> map = new HashMap<>();
        int i = context.getResources().getIdentifier("appfilter", "xml", context.getPackageName());
        XmlResourceParser parser = context.getResources().getXml(i);
        int eventType;
        try {
            eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("item")) {
                            String drawable = parser.getAttributeValue(null, "drawable");
                            String component =
                                    parser.getAttributeValue(null, "component").substring(14);
                            try {
                                if (component.charAt(0) != ':') {
                                    String packageName = component.split("/")[0];
                                    if (!map.containsKey(drawable)) {
                                        map.put(drawable, new ArrayList<>());
                                    }
                                    List<String> packageList = map.get(drawable);
                                    if (!packageList.contains(packageName)) {
                                        packageList.add(packageName);
                                        map.put(drawable, packageList);
                                    }
                                }
                            } catch (Exception e) {
                                Timber.e(e);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            Timber.e(e);
        }
        return map;
    }

    public static Map<String, List<String>> getPackageWithDrawableList(Context context) {
        Map<String, List<String>> map = new HashMap<>();
        int i = context.getResources().getIdentifier("appfilter", "xml", context.getPackageName());
        XmlResourceParser parser = context.getResources().getXml(i);
        int eventType;
        try {
            eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("item")) {
                            String drawable = parser.getAttributeValue(null, "drawable");
                            String component = parser.getAttributeValue(null, "component");
                            if (component.startsWith(":")) {
                                break;
                            }
                            Timber.d(component);
                            component = component.substring(14);
                            try {
                                if (component.charAt(0) != ':') {
                                    String packageName = component.split("/")[0];
                                    if (!map.containsKey(packageName)) {
                                        map.put(packageName, new ArrayList<>());
                                    }
                                    List<String> drawableList = map.get(packageName);
                                    if (!drawableList.contains(drawable)) {
                                        drawableList.add(drawable);
                                    }
                                    map.put(packageName, drawableList);
                                }
                            } catch (Exception e) {
                                Timber.e(e);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            Timber.e(e);
        }
        return map;
    }

    public static boolean isLauncherInstalled(Context context, String packageName) {
        return isPackageInstalled(context, packageName);
    }

    public static boolean isXposedInstalled(Context context) {
        return isPackageInstalled(context, "de.robv.android.xposed.installer");
    }

    public static boolean isAlipayInstalled(Context context) {
        return isPackageInstalled(context, "com.eg.android.AlipayGphone");
    }

    public static boolean isPackageInstalled(Context context, String packageName) {
        try {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception e) {
//            Timber.e(e);
            Timber.d("not installed: %s", packageName);
        }
        return false;
    }

    /**
     * @param context context
     * @param name    drawable name in appfilter
     * @return "ComponentInfo{packageName/activity}"
     */
    public static String getComponentByName(Context context, String name) {
        int i = context.getResources().getIdentifier("appfilter", "xml", context.getPackageName());
        XmlResourceParser parser = context.getResources().getXml(i);
        int eventType;
        try {
            eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("item")) {
                            if (parser.getAttributeValue(1).matches("^" + name + "$") &&
                                    !parser.getAttributeValue(0).startsWith(":")) {
                                return parser.getAttributeValue(0);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            Timber.e(e);
        }
        return null;
    }

    public static List<LauncherInfo> generateLauncherInfo(Context context, int id) {
        String[] launchers = context.getResources().getStringArray(id);
        return Stream.of(launchers)
                .map(launcher -> {
                    String[] tmp = launcher.split("\\|");
                    return new LauncherInfo(context, tmp[1], tmp[0]);
                })
                .collect(Collectors.toList());
    }

    public static String getAppEnglishName(Activity context, String packageName) {
        return getAppLocaleName(context, packageName, Locale.ENGLISH);
    }

    public static String getAppChineseName(Activity context, String packageName) {
        return getAppLocaleName(context, packageName, Locale.CHINA);
    }

    public static String getAppLocaleName(Activity context, String packageName, Locale locale) {
        try {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            ApplicationInfo appInfo
                    = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            if (appInfo != null) {
                Configuration config = new Configuration();

                if (Build.VERSION.SDK_INT >= 17) {
                    config.setLocale(locale);
                } else {
                    config.locale = locale;
                }

                final Resources appRes = pm.getResourcesForApplication(packageName);

                appRes.updateConfiguration(config, context.getBaseContext().getResources()
                        .getDisplayMetrics());

                return appRes.getString(appInfo.labelRes);
            }
        } catch (Exception e) {
            Timber.e(e);
        }
        return getAppSystemName(context, packageName);
    }

    public static String getAppSystemName(Context context, String packageName) {
        String res = "";
        try {
            PackageManager packageManager = context.getApplicationContext().getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            res = packageManager.getApplicationLabel(applicationInfo).toString();
        } catch (Exception e) {
            Timber.e(e);
        }
        return res;
    }

}

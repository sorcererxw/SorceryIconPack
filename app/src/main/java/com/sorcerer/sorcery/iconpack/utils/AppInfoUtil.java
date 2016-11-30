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
import android.util.Log;

import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.AppInfo;
import com.sorcerer.sorcery.iconpack.models.AppfilterItem;
import com.sorcerer.sorcery.iconpack.models.LauncherInfo;

import org.xmlpull.v1.XmlPullParser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/4/26
 */
public class AppInfoUtil {
    private static final String TAG = "AppInfoUtil";

    public static List<AppInfo> getComponentInfo(Context context, boolean withHasCustomIcon) {
        List<AppInfo> appInfoList = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> list = getInstallApps(pm);
        Iterator<ResolveInfo> iterator = list.iterator();

        List<String> xmlStringList = new ArrayList<>();

        if (withHasCustomIcon) {
            xmlStringList = getAppfilterComponentList(context);
        }

        for (int i = 0; i < list.size(); i++) {
            ResolveInfo resolveInfo = iterator.next();
            AppInfo tempAppInfo = new AppInfo(
                    resolveInfo.activityInfo.packageName + "/" + resolveInfo.activityInfo.name,
                    resolveInfo.loadLabel(pm).toString(),
                    resolveInfo.loadIcon(pm));
            if (withHasCustomIcon) {
                if (xmlStringList.contains(tempAppInfo.getCode())) {
                    tempAppInfo.setHasCustomIcon(true);
                }
            }
            appInfoList.add(tempAppInfo);
        }
        Collections.sort(appInfoList, new Comparator<AppInfo>() {
            @Override
            public int compare(AppInfo lhs, AppInfo rhs) {
                try {
                    String s1 = new String(lhs.getName().getBytes("GB2312"), "ISO-8859-1");
                    String s2 = new String(rhs.getName().getBytes("GB2312"), "ISO-8859-1");
                    return s1.compareTo(s2);
                } catch (UnsupportedEncodingException e) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace();
                    }
                    return lhs.getName().compareTo(rhs.getName());
                }
            }
        });
        return appInfoList;
    }

    public static List<ResolveInfo> getInstallApps(PackageManager pm) {
        Intent intent = new Intent("android.intent.action.MAIN", null);
        intent.addCategory("android.intent.category.LAUNCHER");
        return pm.queryIntentActivities(intent, 0);
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return list;
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
        final PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
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
            e.printStackTrace();
        }
        return null;
    }

    public static List<LauncherInfo> generateLauncherInfo(Context context) {
        List<LauncherInfo> list = new ArrayList<>();
        String[] launchers = context.getResources().getStringArray(R.array.launchers);
        for (String launcher : launchers) {
            String[] tmp = launcher.split("\\|");
            if (BuildConfig.DEBUG) {
                Log.d(TAG, launcher + "\n" + tmp[1] + "\n" + tmp[0] + "\n---------");
            }
            list.add(new LauncherInfo(context, tmp[1], tmp[0]));
        }
        return list;
    }

    public static String getAppEnglishName(Activity context, String packageName) {
        return getAppLocaleName(context, packageName, Locale.ENGLISH);
    }

    public static String getAppChineseName(Activity context, String packageName) {
        return getAppLocaleName(context, packageName, Locale.CHINA);
    }

    public static String getAppLocaleName(Activity context, String packageName, Locale locale) {
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo appInfo
                    = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);

            if (appInfo != null) {
                final String label = String.valueOf(pm.getApplicationLabel(appInfo));

                if (BuildConfig.DEBUG) {
                    Log.w(TAG, "Current app label is " + label);
                }

                Configuration config = new Configuration();

                if (Build.VERSION.SDK_INT >= 17) {
                    config.setLocale(locale);
                } else {
                    config.locale = locale;
                }

                final Resources appRes = pm.getResourcesForApplication(packageName);

                appRes.updateConfiguration(config, context.getBaseContext().getResources()
                        .getDisplayMetrics());

                final String localizedLabel = appRes.getString(appInfo.labelRes);

                if (BuildConfig.DEBUG) {
                    Log.w(TAG, "Localized app label is " + localizedLabel);
                }

                return localizedLabel;
            }
        } catch (PackageManager.NameNotFoundException e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "Failed to obtain app info!");
                e.printStackTrace();
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "Other exception");
                e.printStackTrace();
            }
        }
        return getAppSystemName(context, packageName);
    }

    public static String getAppSystemName(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        return applicationInfo == null
                ? ""
                : packageManager.getApplicationLabel(applicationInfo).toString();
    }

}

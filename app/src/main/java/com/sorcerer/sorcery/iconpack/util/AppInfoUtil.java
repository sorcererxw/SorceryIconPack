package com.sorcerer.sorcery.iconpack.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.sorcerer.sorcery.iconpack.models.AppInfo;

import org.xmlpull.v1.XmlPullParser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Sorcerer on 2016/4/26.
 */
public class AppInfoUtil {
    private static final String TAG = "SIP/AppInfoUtil";

    public static List<AppInfo> getComponentInfo(Context context, boolean withHasCustomIcon) {
        List<AppInfo> appInfoList = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN", null);
        intent.addCategory("android.intent.category.LAUNCHER");
        List list = pm.queryIntentActivities(intent, 0);
        Iterator iterator = list.iterator();

        String xmlString = "";

        if (withHasCustomIcon) {
            xmlString = getAppfilterToString(context);
        }
        Log.d(TAG, xmlString);

        for (int i = 0; i < list.size(); i++) {
            ResolveInfo resolveInfo = (ResolveInfo) iterator.next();
            AppInfo tempAppInfo = new AppInfo(
                    resolveInfo.activityInfo.packageName + "/" + resolveInfo.activityInfo.name,
                    resolveInfo.loadLabel(pm).toString(),
                    resolveInfo.loadIcon(pm));
            if (withHasCustomIcon) {
                Log.d(TAG, tempAppInfo.getCode());
                if (xmlString.contains(tempAppInfo.getCode())) {
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
                    e.printStackTrace();
                    return lhs.getName().compareTo(rhs.getName());
                }
            }
        });
        return appInfoList;
    }

    public static String getAppfilterToString(Context context) {
        String res = "";
        int i = context.getResources().getIdentifier("appfilter", "xml", context.getPackageName());
        XmlResourceParser parser = context.getResources().getXml(i);
        int eventType = -1;
        try {
            eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:

                        if (parser.getName().equals("item")) {
                            res += parser.getAttributeValue(0);
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
        return res;
    }

    public static boolean isLauncherInstalled(Context context, String packageName) {
        return isPackageInstalled(context, packageName);
    }

    public static boolean isXposedInstalled(Context context) {
        return isPackageInstalled(context, "de.robv.android.xposed.installed");
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
                            if (parser.getAttributeValue(1).matches("^"+name+"$")) {
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
}

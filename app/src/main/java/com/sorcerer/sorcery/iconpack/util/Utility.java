package com.sorcerer.sorcery.iconpack.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.AppInfo;
import com.sorcerer.sorcery.iconpack.models.LauncherInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Sorcerer on 2016/1/21 0021.
 */
public class Utility {

    public static List<AppInfo> getComponentInfo(Context context) {
        List<AppInfo> appInfoList = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN", null);
        intent.addCategory("android.intent.category.LAUNCHER");
        List list = pm.queryIntentActivities(intent, 0);
        Iterator iterator = list.iterator();

        for (int i = 0; i < list.size(); i++) {
            ResolveInfo resolveInfo = (ResolveInfo) iterator.next();
            AppInfo tempAppInfo = new AppInfo(
                    resolveInfo.activityInfo.packageName + "/" + resolveInfo.activityInfo.name,
                    resolveInfo.loadLabel(pm).toString());
            appInfoList.add(tempAppInfo);
        }
        return appInfoList;
    }

    public static List<LauncherInfo> generateLauncherInfo(Context context) {
        List<LauncherInfo> list = new ArrayList<>();
        String[] launchers = context.getResources().getStringArray(R.array.launchers_list);
        for (String launcher : launchers) {
            String[] tmp = launcher.split("\\|");
            list.add(new LauncherInfo(context, tmp[1], tmp[0]));
        }
        return list;
    }

    public static boolean isLauncherInstalled(Context context, String packageName) {
        final PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}

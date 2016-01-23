package com.sorcerer.sorcery.iconpack.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Debug;
import android.util.Log;

import com.sorcerer.sorcery.iconpack.launchers.NovaLauncher;
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

    public static List<LauncherInfo> generateLauncherInfo(Context context){
        return null;
    }

    public static void applyLauncher(String label, Context context){
        switch (label){
            case "Nova":
                new NovaLauncher(context);
                break;
        }
    }
}

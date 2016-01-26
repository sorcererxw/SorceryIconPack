package com.sorcerer.sorcery.iconpack.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.AppInfo;
import com.sorcerer.sorcery.iconpack.models.LauncherInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    public static void downloadFile(Context context, String urlString, String filepath) {
        try {
            URL url = new URL(urlString);

            //打开到url的连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //以下为java IO部分，大体来说就是先检查文件夹是否存在，不存在则创建,然后的文件名重复问题，没有考虑
            InputStream istream = connection.getInputStream();
            String filename = urlString.substring(urlString.lastIndexOf("/") + 1);

            File dir = new File(filepath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(filepath + filename);
            file.createNewFile();

            OutputStream output = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 4];
            while (istream.read(buffer) != -1) {
                output.write(buffer);

            }
            output.flush();
            output.close();
            istream.close();
            //最后toast出文件名，因为这个程序是单线程的，所以要下载完文件以后才会执行这一句，中间的时间类似于死机，不过多线程还没有学到
            Toast.makeText(context, filename, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

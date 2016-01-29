package com.sorcerer.sorcery.iconpack.util;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.AppInfo;
import com.sorcerer.sorcery.iconpack.models.LauncherInfo;
import com.sorcerer.sorcery.iconpack.models.MailSenderInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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

    public static void downloadFile(Context context, String urlString) {
//        String serviceString = Context.DOWNLOAD_SERVICE;
//        DownloadManager downloadManager;
//        downloadManager = (DownloadManager) context.getSystemService(serviceString);
//
//        Uri uri = Uri.parse(urlString);
//        DownloadManager.Request request = new DownloadManager.Request(uri);
//        request.setNotificationVisibility(DownloadManager.Request
//                .VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        long id = downloadManager.enqueue(request);


//        URL url = new URL(urlString.toString());
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setConnectTimeout(20000);
//        conn.setRequestMethod("GET");
//
//        if (conn.getResponseCode() == 200) {
//            success= true;
//        }
//        if(conn!=null)
//            conn.disconnect();
//        return success;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent .setData(Uri.parse(urlString));
        context.startActivity(intent);
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

package com.sorcerer.sorcery.iconpack.util;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.AppInfo;
import com.sorcerer.sorcery.iconpack.models.ComponentBean;
import com.sorcerer.sorcery.iconpack.models.LauncherInfo;
import com.sorcerer.sorcery.iconpack.models.MailSenderInfo;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Sorcerer on 2016/1/21 0021.
 */
public class Utility {

    private static final String TAG = "SIP/Utility";

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
//        String s = "";
//        for (int j = 0; j < list.size(); j++) {
//            if (list.get(j).isCalendar()) {
//                s += "calendar\n";
//                s += list.get(j).getPrefix() + "\n";
//            } else {
//                s += "item\n";
//                s += list.get(j).getDrawable() + "\n";
//            }
//            s += list.get(j).getComponent() + "\n\n";
//        }
        return res;
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
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

    public static void downloadFile(Context context, String urlString, String filename) {
        String serviceString = Context.DOWNLOAD_SERVICE;
        DownloadManager downloadManager;
        downloadManager = (DownloadManager) context.getSystemService(serviceString);

        Uri uri = Uri.parse(urlString);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setMimeType("application/vnd.android.package-archive");
        request.setNotificationVisibility(DownloadManager.Request
                .VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
        long id = downloadManager.enqueue(request);
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

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static View getContentView(Activity ac) {
        ViewGroup view = (ViewGroup) ac.getWindow().getDecorView();
        FrameLayout content = (FrameLayout) view.findViewById(android.R.id.content);
        return content.getChildAt(0);
    }
}

package com.sorcerer.sorcery.iconpack.util;

import android.app.DownloadManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;

/**
 * Created by Sorcerer on 2016/6/2 0002.
 */
public class NetUtil {
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
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

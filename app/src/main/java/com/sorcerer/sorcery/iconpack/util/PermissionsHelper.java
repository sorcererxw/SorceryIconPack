package com.sorcerer.sorcery.iconpack.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/2/9 0009
 */
public class PermissionsHelper {
    public static final int WRITE_EXTERNAL_STORAGE_CODE = 10;
    public static final int READ_PHONE_STATE_CODE = 16;
    public static final String WRITE_EXTERNAL_STORAGE_MANIFEST =
            "android.permission" + ".WRITE_EXTERNAL_STORAGE";
    public static final String READ_PHONE_STATE_MANIFEST = "android.permission.READ_PHONE_STATE";


    public static void requestWriteExternalStorage(Activity activity) {
        if (!hasPermission(activity, WRITE_EXTERNAL_STORAGE_MANIFEST)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission
                    .WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
        }
    }

    public static void requestReadPhoneState(Activity activity) {
        if (!hasPermission(activity, READ_PHONE_STATE_MANIFEST)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission
                    .READ_PHONE_STATE}, READ_PHONE_STATE_CODE);
        }
    }


    public static void requestPermissions(Activity activity, String[] permissions) {
        List<String> list = new ArrayList<>();
        for (String permission : permissions) {
            if (!hasPermission(activity, permission)) {
                list.add(permission);
            }
        }
        String[] newPermissions = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            newPermissions[i]=list.get(i);
        }
        ActivityCompat.requestPermissions(activity, newPermissions, 1);
    }

    public static boolean hasPermission(Activity activity, String manifestPermission) {
        return ContextCompat.checkSelfPermission(activity, manifestPermission)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasPermissions(Activity activity, String[] manifestPermissions) {
        boolean granted = true;
        for (String manifestPermission : manifestPermissions) {
            granted = granted && (ContextCompat.checkSelfPermission(activity, manifestPermission)
                    == PackageManager.PERMISSION_GRANTED);
        }
        return granted;
    }
}

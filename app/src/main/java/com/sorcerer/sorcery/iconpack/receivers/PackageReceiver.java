package com.sorcerer.sorcery.iconpack.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sorcerer.sorcery.iconpack.apply.appliers.xposed.XposedInstaller;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/24
 */

public class PackageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
            XposedInstaller.onPackageInstall(context, intent.getDataString());
        } else if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
            XposedInstaller.onPackageUninstall(context, intent.getDataString());
        }
    }
}

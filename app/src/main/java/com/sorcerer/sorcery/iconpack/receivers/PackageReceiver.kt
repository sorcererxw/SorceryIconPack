package com.sorcerer.sorcery.iconpack.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import com.sorcerer.sorcery.iconpack.apply.xposed.XposedInstaller

/**
 * @description:
 * *
 * @author: Sorcerer
 * *
 * @date: 2017/2/24
 */

class PackageReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_PACKAGE_ADDED == intent.action) {
            XposedInstaller.onPackageInstall(context, intent.dataString)
        } else if (Intent.ACTION_PACKAGE_REMOVED == intent.action) {
            XposedInstaller.onPackageUninstall(context, intent.dataString)
        }
    }
}

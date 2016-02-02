package com.sorcerer.sorcery.iconpack;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import im.fir.sdk.FIR;

/**
 * Created by Sorcerer on 2016/1/26 0026.
 */
public class SIP extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        FIR.init(this);
    }
}

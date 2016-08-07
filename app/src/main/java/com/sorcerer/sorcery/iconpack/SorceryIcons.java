package com.sorcerer.sorcery.iconpack;

import android.app.Application;

import com.sorcerer.sorcery.iconpack.net.leancloud.AVService;

/**
 * Created by Sorcerer on 2016/1/26 0026.
 */
public class SorceryIcons extends Application {

    public static boolean DEBUG = true;


    @Override
    public void onCreate() {
        super.onCreate();
        AVService.init(this);

    }
}

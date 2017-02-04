package com.sorcerer.sorcery.iconpack.net.leancloud;

import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.sorcerer.sorcery.iconpack.BuildConfig;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/7/22
 */
public class AVService {
    public static void init(Context context) {
        AVOSCloud.initialize(context, BuildConfig.LEANCLOUD_ID, BuildConfig.LEANCLOUD_KEY);
        AVOSCloud.useAVCloudCN();
    }
}

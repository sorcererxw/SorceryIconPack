package com.sorcerer.sorcery.iconpack.net.leancloud;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.widget.TextSwitcher;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.sorcerer.sorcery.iconpack.BuildConfig;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/7/22
 */
public class AVService {
    public static void init(Context context) {
        AVObject.registerSubclass(RequestBean.class);
        AVObject.registerSubclass(LikeBean.class);

        AVOSCloud.initialize(context, BuildConfig.LEANCLOUD_ID, BuildConfig.LEANCLOUD_KEY);
        AVAnalytics.enableCrashReport(context, true);
        AVOSCloud.useAVCloudCN();
    }
}

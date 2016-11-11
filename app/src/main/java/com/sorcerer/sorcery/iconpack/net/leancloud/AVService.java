package com.sorcerer.sorcery.iconpack.net.leancloud;

import android.content.Context;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/7/22
 */
public class AVService {
    public static void init(Context context) {
        AVObject.registerSubclass(RequestBean.class);
        AVObject.registerSubclass(LikeBean.class);

        AVOSCloud.initialize(context,
                "740H3GFA2IOWSuXPg5bJ8aLJ-gzGzoHsz",
                "AHqwrdQPTp9HCXjDi65CID60"
        );
        AVAnalytics.enableCrashReport(context, true);
        AVOSCloud.useAVCloudCN();
    }
}

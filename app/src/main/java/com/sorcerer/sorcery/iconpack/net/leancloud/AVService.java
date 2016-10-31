package com.sorcerer.sorcery.iconpack.net.leancloud;

import android.content.Context;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.util.ResourceUtil;

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
                ResourceUtil.getString(context, R.string.leancloud_app_id),
                ResourceUtil.getString(context, R.string.leancloud_app_key)
        );
        AVAnalytics.enableCrashReport(context, true);
        AVOSCloud.useAVCloudCN();
    }
}

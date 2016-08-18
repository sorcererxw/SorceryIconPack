package com.sorcerer.sorcery.iconpack.net.leancloud;

import android.content.Context;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.SorceryIcons;
import com.sorcerer.sorcery.iconpack.util.ResourceUtil;

/**
 * Created by Sorcerer on 2016/7/22.
 */
public class AVService {
    public static void init(Context context) {
        // 注册子类
        AVObject.registerSubclass(RequestBean.class);
        AVObject.registerSubclass(LikeBean.class);
//        AVOSCloud.setDebugLogEnabled(true);

        AVOSCloud.initialize(context,
                ResourceUtil.getString(context, R.string.leancloud_app_id),
                ResourceUtil.getString(context, R.string.leancloud_app_key)
        );
        AVAnalytics.enableCrashReport(context, true);
        AVOSCloud.useAVCloudCN();


        AVAnalytics.enableCrashReport(context, !BuildConfig.DEBUG);
        // 启用崩溃错误报告
//        AVAnalytics.enableCrashReport(ctx, true);
//        AVOSCloud.setLastModifyEnabled(true);
    }
}

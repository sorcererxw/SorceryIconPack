package com.sorcerer.sorcery.iconpack.showcase.detail;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.sorcerer.sorcery.iconpack.network.coolapk.CoolapkClient;
import com.sorcerer.sorcery.iconpack.network.spiders.AppDisplayInfoSpider;
import com.sorcerer.sorcery.iconpack.network.spiders.models.AppDisplayInfo;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/8
 */

class AppDisplayInfoGetter {
    static Observable<AppDisplayInfo> getAppDisplayInfo(final String packageName,
                                                        Context context,
                                                        boolean chinese) {
        return Observable.just(new AppDisplayInfo())
                .flatMap(info -> {
                    if (PackageUtil.isPackageInstalled(context, packageName)) {
                        Timber.d("installed " + packageName);
                        try {
                            PackageManager pm =
                                    context.getApplicationContext().getPackageManager();
                            try {
                                ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
                                info.setAppName((String) pm.getApplicationLabel(ai));
                                info.setIcon(pm.getApplicationIcon(ai));
                            } catch (final PackageManager.NameNotFoundException e) {
                                Timber.e(e);
                            }
                        } catch (Exception e) {
                            Timber.e(e);
                        }
                    } else {
                        Timber.d("not installed " + packageName);
                    }
                    return Observable.just(info);
                })
                .flatMap(info -> {
                    if (chinese && info.needAddition()) {
                        Timber.d("get from coolapk");
                        return CoolapkClient.getInstance().getAppDisplayInfo(packageName)
                                .map(newInfo -> {
                                    info.add(newInfo);
                                    return info;
                                });
                    } else {
                        return Observable.just(info);
                    }
                })
//                .flatMap(new Function<AppDisplayInfo, ObservableSource<AppDisplayInfo>>() {
//                    @Override
//                    public ObservableSource<AppDisplayInfo> apply(AppDisplayInfo info)
//                            throws Exception {
//                        if (chinese && info.needAddition()) {
//                            Timber.d("get from qq");
//                            return AppDisplayInfoSpider.getNameFromQQ(packageName)
//                                    .map(newInfo -> {
//                                        info.add(newInfo);
//                                        return info;
//                                    });
//                        } else {
//                            return Observable.just(info);
//                        }
//                    }
//                })
                .flatMap(info -> {
                    if (info.needAddition()) {
                        Timber.d("get from play");
                        return AppDisplayInfoSpider.getNameFromPlay(packageName)
                                .map(newInfo -> {
                                    info.add(newInfo);
                                    return info;
                                });
                    } else {
                        return Observable.just(info);
                    }
                })
                .map(info -> {
                    if (info.getAppName() != null) {
                        info.setAppName(formatTitle(info.getAppName()));
                    }
                    return info;
                });
    }

    private static String formatTitle(String title) {
        String[] reg = new String[]{
                "-", ":", "：",
        };
        for (int i = 0; i < reg.length && !isLengthOk(title); i++) {
            String[] split = title.split(reg[i]);
            if (split.length == 2) {
                title = split[0];
            }
        }
        if (!isLengthOk(title)) {
            if (title.matches("[^\\(^\\)]*\\([^\\(^\\)]*\\)")) {
                title = title.split("\\(")[0];
            }
        }
        if (!isLengthOk(title)) {
            if (title.matches("[\\u4e00-\\u9fa5]+[A-Za-z\\s]+")) {
                Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]+");
                Matcher matcher = pattern.matcher(title);
                if (matcher.find()) {
                    title = matcher.group();
                }
            } else if (title.matches("[A-Za-z\\s]+[\\u4e00-\\u9fa5]+")) {
                Pattern pattern = Pattern.compile("[A-Za-z\\s]+");
                Matcher matcher = pattern.matcher(title);
                if (matcher.find()) {
                    title = matcher.group();
                }
            }
        }

        return title.trim();
    }

    private static boolean isLengthOk(String s) {
        return s.replaceAll("[^\\x00-\\xff]", "***")
                .replaceAll("[\\x00-\\xff]", "**").getBytes().length < 30;
    }
}

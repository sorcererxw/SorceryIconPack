package com.sorcerer.sorcery.iconpack.apply.appliers.xposed;

import android.os.Environment;
import android.support.annotation.Keep;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/22
 */

@Keep
final class Constants {
    static final String SORCERY_PACKAGE = "com.sorcerer.sorcery.iconpack";
    static final String XPOSED_PREFERENCE = "xposed";
    static final String REPLACEMENT_DISPOSE_PREFERENCE_KEY = "REPLACEMENT_DISPOSE";
    static final String ACTIVE_PREFERENCE_KEY = "ACTIVE";
    static final String ICON_PATH =
            Environment.getDataDirectory() + "/data/" + SORCERY_PACKAGE + "/files/";

    public static boolean isModuleActive() {
        return false;
    }
}

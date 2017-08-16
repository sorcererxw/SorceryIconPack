package com.sorcerer.sorcery.iconpack.utils;

import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/4/26
 */
public class StringUtil {

    public static String handleLongXmlString(String s) {
        String res = s.replace("|", "\n");
        res = res.replace("#", "    ");
        return res;
    }

    public static String componentInfoToPackageName(String componentInfo) {
        try {
            return componentInfo.split("/")[0].split("\\{")[1];
        } catch (Exception e) {
            Timber.e(e);
        }
        return null;
    }

}

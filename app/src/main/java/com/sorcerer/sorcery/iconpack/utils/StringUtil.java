package com.sorcerer.sorcery.iconpack.utils;

/**
 * Created by Sorcerer on 2016/4/26.
 */
public class StringUtil {
    public static boolean isNullOrEmpty(String s){
        return s==null || s.isEmpty();
    }

    public static boolean isMail(String mail) {
        return mail.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0_9_-]{2,3}){1,2})$");
    }

    public static String handleLongXmlString(String s) {
        String res = s.replace("|", "\n");
        res = res.replace("#", "    ");
        return res;
    }

    public static String componentInfoToPackageName(String componentInfo) {
        try {
            return componentInfo.split("/")[0].split("\\{")[1];
        } catch (Exception e) {
            return null;
        }
    }

}

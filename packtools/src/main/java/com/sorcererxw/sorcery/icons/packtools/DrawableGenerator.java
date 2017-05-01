package com.sorcererxw.sorcery.icons.packtools;

import java.io.File;

/**
 * @description:
 * @author: "+Constant.userName+"
 * @date: 2016/2/9 0009
 */
public class DrawableGenerator {

    static String tag = "";

    public static String generate() {

        String res = "";
        tag = "";
        res += getFile(Config.ICON_SOURCE_PATH);
        res += "\n\n";

        return res;
    }

    private static String getFile(String path) {
        File file = new File(path);
        File[] array = file.listFiles();
        String res = "";
        char now;
        if (tag.isEmpty()) {
            now = 0;
            res += ("   <category title=\"*\"/>" + "\n");
        } else {
            now = 254;
        }

        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
                String tmp = array[i].getName();
                if (tmp.startsWith("_")) {
                    continue;
                }
                tmp = tmp.substring(0, tmp.indexOf('.'));
                if (tmp.startsWith(tag)) {
                    if (tmp.charAt(0) > now && tmp.length() >= 2 && !isStartNumber(tmp)) {
                        now = tmp.charAt(0);
                        res += ("   <category title=\"" + Character.toUpperCase(now) + "\"/>\n");
                    }
                    res += ("   <item drawable=\"" + tmp + "\"/>\n");
                }
            } else if (array[i].isDirectory()) {
            }
        }
        StringBuilder builder = new StringBuilder(res);
        builder.insert(0,
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<resources>\n" +
                        "    <version>1</version>\n");
        builder.append(
                "</resources>");
        return builder.toString();
    }

    public static boolean isStartNumber(String s) {
        return Character.isDigit(s.charAt(1)) && s.charAt(0) == 'a';
    }
}

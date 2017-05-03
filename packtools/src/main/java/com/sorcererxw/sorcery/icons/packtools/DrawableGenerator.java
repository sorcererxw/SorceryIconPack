package com.sorcererxw.sorcery.icons.packtools;

import java.io.File;

/**
 * @description:
 * @author: "+Constant.userName+"
 * @date: 2016/2/9 0009
 */
class DrawableGenerator {

    static String generate() {

        String res = "";
        res += getFile(Config.ICON_SOURCE_PATH);
        res += "\n\n";
        return res;
    }

    private static String getFile(String path) {
        File dir = new File(path);
        File[] array = dir.listFiles();
        String res = "";
        char now = 0;
        res += ("   <category title=\"*\"/>" + "\n");

        assert array != null;
        for (File icon : array) {
            if (!icon.isFile()) {
                continue;
            }
            String tmp = icon.getName();
            if (tmp.startsWith("_")) {
                continue;
            }
            tmp = tmp.substring(0, tmp.indexOf('.'));
            if (tmp.charAt(0) > now && tmp.length() >= 2 && !isStartNumber(tmp)) {
                now = tmp.charAt(0);
                res += ("   <category title=\"" + Character.toUpperCase(now) + "\"/>\n");
            }
            res += ("   <item drawable=\"" + tmp + "\"/>\n");
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

    private static boolean isStartNumber(String s) {
        return Character.isDigit(s.charAt(1)) && s.charAt(0) == 'a';
    }
}

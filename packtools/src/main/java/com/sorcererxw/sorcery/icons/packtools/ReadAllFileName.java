package com.sorcererxw.sorcery.icons.packtools;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by "+Constant.userName+" on 2016/2/9 0009.
 */
public class ReadAllFileName {

    static String tag = "";

    public static void main(String[] args) {

        // This is the path where the file's name you want to take.
        String path = "C:\\Users\\" + Constant.userName +
                "\\AndroidStudioProjects\\SorceryIconPack\\app\\src\\main\\res\\drawable-nodpi";
        writeFile("C:\\Users\\" + Constant.userName +
                "\\AndroidStudioProjects\\SorceryIconPack\\testProgram\\iconpack.txt", getFile());

        try {
            java.awt.Desktop.getDesktop().open(new File("C:\\Users\\" + Constant.userName +
                    "\\AndroidStudioProjects\\SorceryIconPack\\testProgram\\iconpack.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String pre = "**";

    private static String getFile() {
        String[] a = {
//                "ali",
//                "baidu",
//                "netease",
//                "google",
//                "htc",
//                "lenovo",
//                "lg",
//                "moto",
//                "microsoft",
//                "samsung",
//                "sony",
//                "tencent",
//                "miui",
//                "xiaomi",
//                "flyme",
//                "meizu",
//                "oneplus",
                ""
        };

        String res = "";
        for (String s : a) {
            tag = s;
            res += getFile("C:\\Users\\" + Constant.userName +
                    "\\AndroidStudioProjects\\SorceryIconPack\\app\\src\\main\\res\\drawable-nodpi");
            res += "\n\n";
        }
        return res;
    }


    private static String getFile(String path) {
        File file = new File(path);
        File[] array = file.listFiles();
        String res = "";
        char now;
        if (tag.isEmpty()) {
            now = 0;
            res += ("<item>" + pre + "*" + pre + "</item>" + "\n");
        } else {
            now = 254;
        }

        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
                String tmp = array[i].getName();
                if(tmp.startsWith("_")){
                    continue;
                }
                tmp = tmp.substring(0, tmp.indexOf('.'));
                if (tmp.startsWith(tag)) {
                    if (tmp.charAt(0) > now && tmp.length() >= 2 && !isStartNumber(tmp)) {
                        now = tmp.charAt(0);
                        res += ("   <item>" + pre + Character.toUpperCase(now) + pre + "</item>" + "\n");
                    }
                    res += ("   <item>" + tmp + "</item>" + "\n");
                }
            } else if (array[i].isDirectory()) {
//                getFile(array[i].getPath());
            }
        }
        StringBuilder builder = new StringBuilder(res);
        builder.insert(0,
                "<string-array name=\"icon_pack" + (tag.equals("") ? "" : "_") + tag + "\" translatable=\"false\">\n");
        builder.append("</string-array>");
        return builder.toString();
    }

    public static boolean isStartNumber(String s) {
        return Character.isDigit(s.charAt(1)) && s.charAt(0) == 'a';
    }

    public static void writeFile(String filePathAndName, String fileContent) {
        try {
            File f = new File(filePathAndName);
            if (!f.exists()) {
                f.createNewFile();
            }
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
            BufferedWriter writer = new BufferedWriter(write);
            writer.write(fileContent);
            writer.close();
        } catch (Exception e) {
            System.out.println("写文件内容操作出错");
            e.printStackTrace();
        }
    }
}

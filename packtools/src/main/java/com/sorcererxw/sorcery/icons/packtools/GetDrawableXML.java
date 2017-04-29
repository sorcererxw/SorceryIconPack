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
public class GetDrawableXML {

    static String tag = "";

    public static void main(String[] args) {

        // This is the path where the file's name you want to take.
        String path = "D:\\OneDrive\\drawable-nodpi";
        writeFile("C:\\Users\\" + Constant.userName +
                "\\AndroidStudioProjects\\SorceryIconPack\\testProgram\\iconpack.txt", getFile());

        try {
            java.awt.Desktop.getDesktop().open(new File("C:\\Users\\" + Constant.userName +
                    "\\AndroidStudioProjects\\SorceryIconPack\\testProgram\\iconpack.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFile() {
        String[] a = {
//                "ali",
//                "baidu",
//                "cyanogenmod",
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
//                "flyme",
                ""
        };
        String res = "";
        for (String s : a) {
            tag = s;
            res += getFile("C:\\Users\\Sorcerer\\Google 云端硬盘\\drawable-nodpi");
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
            res += ("   <category title=\"*\"/>" + "\n");
        } else {
            now = 254;
        }

        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
                String tmp = array[i].getName();
                if (tmp.startsWith("_")) continue;
                tmp = tmp.substring(0, tmp.indexOf('.'));
                if (tmp.startsWith(tag)) {
                    if (tmp.charAt(0) > now && tmp.length() >= 2 && !isStartNumber(tmp)) {
                        now = tmp.charAt(0);
                        res += ("   <category title=\"" + Character.toUpperCase(now) + "\"/>\n");
                    }
                    res += ("   <item drawable=\"" + tmp + "\"/>\n");
                }
            } else if (array[i].isDirectory()) {
//                getFile(array[i].getPath());
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

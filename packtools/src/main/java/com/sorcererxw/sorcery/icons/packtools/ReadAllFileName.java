package com.sorcererxw.sorcery.icons.packtools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * @description:
 * @author: "+Constant.userName+"
 * @date: 2016/2/9 0009
 */
public class ReadAllFileName {

    static String tag = "";

    public static void main(String[] args) {

        writeFile("C:\\Users\\" + Config.userName +
                "\\AndroidStudioProjects\\SorceryIconPack\\testProgram\\iconpack.txt", getFile());


        try {
            java.awt.Desktop.getDesktop().open(new File("C:\\Users\\" + Config.userName +
                    "\\AndroidStudioProjects\\SorceryIconPack\\testProgram\\iconpack.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFile() {

        String res = "";
        tag = "";
        res += getFile("C:\\Users\\" + Config.userName +
                "\\AndroidStudioProjects\\SorceryIconPack\\app\\src\\main\\res\\drawable-nodpi");
        res += "\n\n";
        return res;
    }


    private static String getFile(String path) {
        File file = new File(path);
        File[] array = file.listFiles();
        String res = "";
        char nowCate = 0;
        String pre = "**";
        if (tag.isEmpty()) {
            res += ("<item>" + pre + "*" + pre + "</item>" + "\n");
        }

        assert array != null;
        for (File icon : array) {
            if (!icon.isFile()) {
                continue;
            }
            if (icon.getName().startsWith("_")) {
                continue;
            }
            String tmp = icon.getName();
            tmp = tmp.substring(0, tmp.indexOf('.'));
            if (tmp.charAt(0) > nowCate && tmp.length() >= 2 && !isStartNumber(tmp)) {
                nowCate = tmp.charAt(0);
                res += ("   <item>" + pre + Character.toUpperCase(nowCate) + pre + "</item>"
                        + "\n");
            }
            res += ("   <item>" + tmp + "</item>" + "\n");
        }
        StringBuilder builder = new StringBuilder(res);
        builder.insert(0,
                "<string-array name=\"icon_pack" + (tag.equals("") ? "" : "_") + tag
                        + "\" translatable=\"false\">\n");
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

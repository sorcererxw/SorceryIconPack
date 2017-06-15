package com.sorcererxw.sorcery.icons.packtools;

import com.luhuiguo.chinese.ChineseUtils;
import com.sorcererxw.sorcery.icons.packtools.leancloud.LeancloudClient;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PackTools {
    public static void main(String... args) {
        pack();
    }

    /*
     * 1. copy icons from source path to project no-dpi folder
     * 2. generate all icon array
     * 3. generate drawable.xml
     * 4. generate appfilter.xml
     * 5. zhCN -> zhTW
     */

    private static void pack() {
        try {
            copyIcons();
            System.out.println("copying icons successfully");
        } catch (IOException e) {
            System.out.println("copying icons failed");
            e.printStackTrace();
        }

        try {
            handleIconsArray();
            System.out.println("icon_pack successfully written");
        } catch (IOException e) {
            System.out.println("icon_pack write failed");
            e.printStackTrace();
        }

        try {
            Utils.writeFile(new File(Config.PROJECT_MAIN_PATH + "\\assets\\drawable.xml"),
                    DrawableGenerator.generate());
            System.out.println("drawable.xml successfully written");
        } catch (IOException e) {
            System.out.println("drawable.xml write failed");
            e.printStackTrace();
        }

        try {
            handleAppfilter();
            System.out.println("appfilter successfully written");
        } catch (IOException e) {
            System.out.println("appfilter write failed");
            e.printStackTrace();
        }

        try {
            String s = ChineseUtils.toTraditional(
                    Utils.readFile(new File(
                            Config.PROJECT_MAIN_PATH + "\\res\\values-zh-rCN\\strings.xml")
                    )
            );
            Utils.writeFile(
                    new File(Config.PROJECT_MAIN_PATH + "\\res\\values-zh-rHK\\strings.xml"),
                    s);
            Utils.writeFile(
                    new File(Config.PROJECT_MAIN_PATH + "\\res\\values-zh-rTW\\strings.xml"),
                    s);
            System.out.println("strings-cn successfully written");
        } catch (IOException e) {
            System.out.println("strings-cn write failed");
            e.printStackTrace();
        }
    }

    private static void copyIcons() throws IOException {
        File fileDestDir = new File(Config.PROJECT_MAIN_PATH + "\\res\\drawable-nodpi");
        FileUtils.cleanDirectory(fileDestDir);
        File iconSourceDir = new File(Config.ICON_SOURCE_PATH);
        Arrays.stream(iconSourceDir.listFiles())
                .forEach(file -> {
                    try {
                        FileUtils.copyFileToDirectory(file, fileDestDir);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private static void handleIconsArray() throws IOException {
        File iconPack = new File(Config.PROJECT_MAIN_PATH + "\\res\\values\\icon_pack.xml");
        Document document = Jsoup.parse(Utils.readFile(iconPack));
        String res = document.getElementsByTag("string-array").stream()
                .map(element -> {
                    if (element.attr("name").equals("icon_pack")) {
                        final char[] now = {0};
                        return "<string-array name=\"icon_pack\" translatable=\"false\">\n"
                                + Arrays.stream(new File(Config.ICON_SOURCE_PATH).listFiles())
                                .filter(file -> file.isFile()
                                        && file.getName().matches("[a-z][a-z0-9_]+.png"))
                                .map(file -> file.getName().split("\\.")[0])
                                .sorted()
                                .map(s -> {
                                    if (s.charAt(0) > now[0]) {
                                        if (s.matches("a[0-9].*")) {
                                            if (now[0] == 0) {
                                                now[0] = 1;
                                                return "<item>*****</item>\n"
                                                        + "<item>" + s + "</item>";
                                            }
                                        } else {
                                            now[0] = s.charAt(0);
                                            return "<item>**" + Character.toUpperCase(now[0])
                                                    + "**</item>\n"
                                                    + "<item>" + s + "</item>";
                                        }
                                    }
                                    return "<item>" + s + "</item>";
                                })
                                .collect(Collectors.joining("\n"))
                                + "</string-array>";
                    } else {
                        return element.toString();
                    }
                })
                .collect(Collectors.joining("\n"));
        Utils.writeFile(iconPack,
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                        + "<resources>\n"
                        + res.replaceAll(">\\s+", ">").replaceAll("\\s+<", "<")
                        + "</resources>");
    }

    private static void handleAppfilter() throws IOException {
        File xmlAppfilter = new File(Config.PROJECT_MAIN_PATH + "\\res\\xml\\appfilter.xml");
        File assetsAppfilter = new File(Config.PROJECT_MAIN_PATH + "\\assets\\appfilter.xml");
        if (!xmlAppfilter.exists()) {
            xmlAppfilter.createNewFile();
        }
        if (!assetsAppfilter.exists()) {
            assetsAppfilter.createNewFile();
        }
        String s = AppfilterGenerator.generate(LeancloudClient.getInstance().scanIconTable());
        Utils.writeFile(xmlAppfilter, s);
        Utils.writeFile(assetsAppfilter, s);
    }
}

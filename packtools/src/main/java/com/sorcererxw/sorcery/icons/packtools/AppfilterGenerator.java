package com.sorcererxw.sorcery.icons.packtools;

import com.sorcererxw.sorcery.icons.packtools.leancloud.IconTableItem;
import com.sorcererxw.sorcery.icons.packtools.leancloud.LeancloudClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/5/1
 */

public class AppfilterGenerator {
    public static String generate(List<IconTableItem.IconBean> list) throws FileNotFoundException {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<resources>\n"
                + provideTemplate() + "\n"
                + provideCalendar() + "\n"
                + provideSimple(list) + "\n"
                + "</resources>";
    }

    public static String provideTemplate() throws FileNotFoundException {
        return Jsoup.parse(Utils.readFile(new File(
                "C:\\Users\\Sorcerer\\AndroidStudioProjects\\SorceryIconPack\\app\\src\\main\\res\\xml\\appfilter.xml")))
                .body()
                .getElementsByTag("item")
                .stream()
                .filter(element -> element.attr("component").startsWith(":"))
                .map(Element::toString)
                .collect(Collectors.joining("\n"));
    }

    public static String provideCalendar() throws FileNotFoundException {
        return Jsoup.parse(Utils.readFile(new File(
                "C:\\Users\\Sorcerer\\AndroidStudioProjects\\SorceryIconPack\\app\\src\\main\\res\\xml\\appfilter.xml")))
                .body()
                .getElementsByTag("calendar")
                .stream()
                .map(Element::toString)
                .collect(Collectors.joining("\n"));
    }

    public static String provideSimple(List<IconTableItem.IconBean> list) {
        return list.stream().map(iconBean -> iconBean.getComponents().stream().map(component ->
                String.format("<item"
                        + " component=\"ComponentInfo{%s}\""
                        + " drawable=\"%s\"/>", component, iconBean.getDrawable()))
                .collect(Collectors.joining("\n")))
                .collect(Collectors.joining("\n"));
    }

    public static void main(String... args) throws FileNotFoundException {
//        Document document = Jsoup.parse(Utils.readFile(new File(
//                "C:\\Users\\Sorcerer\\AndroidStudioProjects\\SorceryIconPack\\app\\src\\main\\res\\xml\\appfilter.xml")));
//        System.out.println(document.body());
//        System.out.println(document.getElementsByTag("item").size());
//        System.out.println(provideCalendar());
//        System.out.println();
        System.out.println(generate(LeancloudClient.getInstance().scanIconTable()));
    }
}

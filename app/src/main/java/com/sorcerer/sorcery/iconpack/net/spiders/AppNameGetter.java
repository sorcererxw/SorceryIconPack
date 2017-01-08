package com.sorcerer.sorcery.iconpack.net.spiders;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/1/3
 */

public class AppNameGetter {

    public static Observable<String> getName(final String packageName) {
        return Observable.just("")
                .flatMap(name -> name.isEmpty() ?
                        getNameFromCoolapk(packageName) : Observable.just(name))
                .flatMap(name -> name.isEmpty() ?
                        getNameFromQQ(packageName) : Observable.just(name))
                .flatMap(name -> name.isEmpty() ?
                        getNameFromPlay(packageName) : Observable.just(name));
    }

    private static Observable<String> getNameFromQQ(String packageName) {
        return Observable.just(packageName)
                .map(name -> {
                    try {
                        Document document = Jsoup
                                .connect("http://sj.qq.com/myapp/detail.htm?apkName="
                                        + name)
                                .header("User-Agent",
                                        "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
                                .timeout(3000)
                                .get();
                        return document
                                .getElementsByClass("com-container").get(0)
                                .getElementsByClass("det-main-container").get(0)
                                .getElementsByClass("det-ins-container J_Mod ").get(0)
                                .getElementsByClass("det-ins-data").get(0)
                                .getElementsByClass("det-name").get(0)
                                .getElementsByClass("det-name-int").get(0)
                                .html();
                    } catch (Exception e) {
                        Timber.e("QQ: failed");
                        Timber.e(e);
                    }
                    return "";
                });
    }

    private static Observable<String> getNameFromCoolapk(String packageName) {
        return Observable.just(packageName)
                .map(name -> {
                    try {
                        Document document = Jsoup
                                .connect("http://www.coolapk.com/apk/" + name)
                                .header("User-Agent",
                                        "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
                                .timeout(3000)
                                .get();
                        return document
                                .getElementsByClass("container ex-container").get(0)
                                .getElementsByClass("row").get(0)
                                .getElementsByClass("col-md-5").get(0)
                                .getElementsByClass("panel panel-default ex-card").get(0)
                                .getElementsByClass("tab-content ex-card-wrapper").get(0)
                                .getElementById("ex-apk-detail-pane")
                                .getElementsByTag("dd").get(0)
                                .html();
                    } catch (Exception e) {
                        Timber.e("coolapk: failed");
                        Timber.e(e);
                    }
                    return "";
                })
                .map(s -> {
                    String[] strings = s.split(":");
                    if (strings.length == 2) {
                        return strings[0];
                    }
                    return s;
                });
    }

    private static Observable<String> getNameFromWandoujia(String packageName) {
        return Observable.just(packageName)
                .map(name -> {
                    try {
                        Document document = Jsoup
                                .connect("http://www.wandoujia.com/apps/" + name)
                                .header("User-Agent",
                                        "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
                                .timeout(3000)
                                .get();
                        return document
                                .getElementsByClass("container").get(0)
                                .getElementsByClass("detail-wrap ").get(0)
                                .getElementsByClass("detail-top clearfix").get(0)
                                .getElementsByClass("app-info").get(0)
                                .getElementsByClass("app-name").get(0)
                                .getElementsByTag("span").get(0)
                                .html();
                    } catch (Exception e) {
                        Timber.e("wandoujia: failed");
                        Timber.e(e);
                    }
                    return "";
                });
    }

    private static Observable<String> getNameFromPlay(String packageName) {
        return Observable.just(packageName)
                .map(name -> {
                    try {
                        Document document = Jsoup
                                .connect("https://play.google.com/store/apps/details?id="
                                        + name)
                                .header("User-Agent",
                                        "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
                                .timeout(3000)
                                .get();
                        return document
                                .getElementsByClass("wrapper wrapper-with-footer").get(0)
                                .getElementsByClass("body-content").get(0)
                                .getElementsByClass("outer-container").get(0)
                                .getElementsByClass("inner-container").get(0)
                                .getElementsByClass("main-content").get(0)
                                .getElementsByTag("div").get(0)
                                .getElementsByClass(
                                        "details-wrapper apps square-cover id-track-partial-impression id-deep-link-item")
                                .get(0)
                                .getElementsByClass("details-info").get(0)
                                .getElementsByClass("info-container").get(0)
                                .getElementsByClass("info-box-top").get(0)
                                .getElementsByClass("document-title").get(0)
                                .getElementsByClass("id-app-title").html();
                    } catch (Exception e) {
                        Timber.e("Google play: failed");
                        Timber.e(e);
                    }
                    return "";
                });
    }
}

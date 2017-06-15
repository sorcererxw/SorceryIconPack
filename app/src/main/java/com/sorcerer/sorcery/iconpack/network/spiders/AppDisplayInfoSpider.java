package com.sorcerer.sorcery.iconpack.network.spiders;

import android.text.TextUtils;

import com.sorcerer.sorcery.iconpack.network.spiders.models.AppDisplayInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.reactivex.Observable;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/8
 */

public class AppDisplayInfoSpider {

    private static final String USER_AGENT =
            "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";

    public static Observable<AppDisplayInfo> getNameFromQQ(String packageName) {
        return Observable.just(packageName)
                .map(name -> {
                    try {
                        Document document = Jsoup
                                .connect("http://sj.qq.com/myapp/detail.htm?apkName=" + name)
                                .header("User-Agent", USER_AGENT)
                                .timeout(3000)
                                .get();
                        Elements elementsByClass =
                                document.getElementsByClass("det-ins-container J_Mod ");
                        if (elementsByClass.size() > 0) {
                            return elementsByClass.get(0);
                        }
                    } catch (Exception e) {
                        Timber.e("QQ: failed");
                        Timber.e(e);
                    }
                    return new Element("<p></p>");
                }).map(element -> {
                    AppDisplayInfo info = new AppDisplayInfo();
                    try {
                        String url = element.getElementsByClass("det-icon").get(0)
                                .getElementsByTag("img").get(0)
                                .attr("src");
                        if (!TextUtils.isEmpty(url)) {
                            info.setIconUrl(url);
                        }
                    } catch (IndexOutOfBoundsException e) {
//                      Timber.e(e);
                    } catch (Exception e) {
                        Timber.e(e);
                    }

                    try {
                        String name = element.getElementsByClass("det-name-int").get(0)
                                .html().trim();
                        if (!TextUtils.isEmpty(name)) {
                            info.setAppName(name);
                        }
                    } catch (IndexOutOfBoundsException e) {
//                      Timber.e(e);
                    } catch (Exception e) {
                        Timber.e(e);
                    }
                    return info;
                });
    }

    public static Observable<AppDisplayInfo> getNameFromPlay(String packageName) {
        return Observable.just(packageName)
                .map(name -> {
                    try {
                        Document document = Jsoup
                                .connect("https://play.google.com/store/apps/details?id=" + name)
                                .header("User-Agent", USER_AGENT)
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
                                .getElementsByClass("info-container").get(0);
                    } catch (Exception e) {
                        Timber.e("Google play: failed");
//                        Timber.e(e);
                    }
                    return new Element("<p></p>");
                })
                .map(element -> {
                    AppDisplayInfo info = new AppDisplayInfo();
                    try {
                        Element imgElement = element.getElementsByClass("cover-image").get(0);
                        String url = imgElement.attr("src");
                        if (!TextUtils.isEmpty(url)) {
                            if (url.startsWith("//")) {
//                                url = url.substring(2);
                                url = "https:" + url;
                            }
                            info.setIconUrl(url);
                        }
                    } catch (Exception e) {
//                        Timber.e(e);
                    }

                    try {
                        String name =
                                element.getElementsByClass("id-app-title").get(0).html().trim();
                        if (!TextUtils.isEmpty(name)) {
                            info.setAppName(name);
                        }
                    } catch (Exception e) {
//                        Timber.e(e);
                    }
                    return info;
                });
    }
}

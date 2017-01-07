package com.sorcerer.sorcery.iconpack.net.spiders;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/1/5
 */

public class AppSearchResultGetter {
    public static Observable<List<String>> search(String text) {
        return searchOnCoolapk(text);
    }

    private static Observable<List<String>> searchOnCoolapk(String searchText) {
        return Observable.just(searchText)
                .flatMap(new Function<String, Observable<Element>>() {
                    public Observable<Element> apply(String text) {
                        try {
                            Document document = Jsoup
                                    .connect("http://www.coolapk.com/apk/search?q=" + text)
                                    .header("User-Agent",
                                            "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
                                    .timeout(3000)
                                    .get();
                            return Observable.fromIterable(document
                                    .getElementsByClass("container ex-flex-container").get(0)
                                    .getElementsByClass("row").get(0)
                                    .getElementsByClass("col-md-8 ex-flex-col ").get(0)
                                    .getElementsByClass("panel panel-default ex-card").get(0)
                                    .getElementsByClass("ex-card-body").get(0)
                                    .getElementsByClass("media-list ex-card-app-list").get(0)
                                    .getElementsByTag("li"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return Observable.fromIterable(new ArrayList<Element>());
                    }
                })
                .map(element -> element
                        .getElementsByClass("media-body").get(0)
                        .getElementsByClass("media-heading").get(0)
                        .getElementsByTag("a").get(0))
                .map(element -> element.attr("href").substring(5))
                .toList()
                .flatMapObservable(Observable::just);
    }
}

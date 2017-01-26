package com.sorcerer.sorcery.iconpack.net.spiders;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.sorcerer.sorcery.iconpack.net.coolapk.CoolapkClient;
import com.sorcerer.sorcery.iconpack.net.coolapk.CoolapkSearchResult;
import com.sorcerer.sorcery.iconpack.net.coolapk.CoolapkSearchResult.CoolapkSearchBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/1/5
 */

public class AppSearchResultGetter {

    public static Observable<List<String>> searchViaApi(String text) {
        return searchViaApi(text, 1)
                .zipWith(searchViaApi(text, 2),
                        (list, list2) -> Stream.concat(Stream.of(list), Stream.of(list2))
                                .collect(Collectors.toList()))
                .zipWith(searchViaApi(text, 3),
                        (list, list2) -> Stream.concat(Stream.of(list), Stream.of(list2))
                                .collect(Collectors.toList()));
    }

    public static Observable<List<String>> searchViaApi(String text, int page) {
        return CoolapkClient.getInstance().search(text, page)
                .flatMap(new Function<CoolapkSearchResult, ObservableSource<CoolapkSearchBean>>() {
                    @Override
                    public ObservableSource<CoolapkSearchBean> apply(
                            CoolapkSearchResult coolapkSearchResult) throws Exception {
                        return Observable.fromIterable(coolapkSearchResult.getData());
                    }
                })
                .map(CoolapkSearchBean::getPackageName)
                .toList()
                .toObservable();
    }

    @SuppressWarnings("unchecked")
    public static Observable<List<String>> search(String text) {
        return Observable.zip(
                searchApkOnCoolapk(text),
                searchGameOnCoolapk(text),
                (list, list2) -> Stream.concat(Stream.of(list), Stream.of(list2))
                        .collect(Collectors.toList()));
    }

    private static Observable<List<String>> searchApkOnCoolapk(String searchText) {
        return searchOnCoolapk(searchText, "apk", 1);
    }

    private static Observable<List<String>> searchGameOnCoolapk(String searchText) {
        return searchOnCoolapk(searchText, "game", 1);
    }

    private static Observable<List<String>> searchOnCoolapk(String searchText,
                                                            String type,
                                                            int page) {
        return Observable.just(searchText)
                .map(text -> "http://www.coolapk.com/" + type + "/search?q=" + text + "&p=" + page)
                .flatMap(new Function<String, Observable<Element>>() {
                    public Observable<Element> apply(String url) {
                        try {
                            Document document = Jsoup
                                    .connect(url)
                                    .header("User-Agent",
                                            "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
                                    .timeout(3000)
                                    .get();
                            return Observable.fromIterable(document
                                    .getElementsByClass("media-list ex-card-app-list").get(0)
                                    .getElementsByTag("li"));
                        } catch (Exception e) {
                            Timber.e(e);
                        }
                        return Observable.fromIterable(new ArrayList<>());
                    }
                })
                .map(element -> element
                        .getElementsByClass("media-body").get(0)
                        .getElementsByClass("media-heading").get(0)
                        .getElementsByTag("a").get(0)
                        .attr("href")
                )
                .map(href -> href.substring(("/" + type + "/").length()))
                .toList()
                .flatMapObservable(Observable::just);
    }
}

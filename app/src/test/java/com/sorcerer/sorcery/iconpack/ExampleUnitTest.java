package com.sorcerer.sorcery.iconpack;

import com.annimon.stream.Stream;
import com.google.gson.Gson;
import com.sorcerer.sorcery.iconpack.net.avos.AvosBatchRequest;
import com.sorcerer.sorcery.iconpack.net.avos.AvosBatchResult;
import com.sorcerer.sorcery.iconpack.net.avos.AvosClient;
import com.sorcerer.sorcery.iconpack.net.avos.AvosIconRequestBean;
import com.sorcerer.sorcery.iconpack.net.avos.AvosRequest;
import com.sorcerer.sorcery.iconpack.net.coolapk.CoolapkClient;
import com.sorcerer.sorcery.iconpack.net.coolapk.CoolapkSearchResult;
import com.sorcerer.sorcery.iconpack.net.spiders.AppNameGetter;
import com.sorcerer.sorcery.iconpack.net.spiders.AppSearchResultGetter;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        AppSearchResultGetter.search("settings")
                .subscribe(strings -> {
                    System.out.println(Arrays.toString(strings.toArray()).replace(",", "\n"));
                });
    }

    @Test
    public void appNameGetterTest() throws Exception {
        AppNameGetter.getName("com.tiantonglaw.readlaw")
                .subscribe(System.out::println, Throwable::printStackTrace);
    }

    @Test
    public void searchTest() throws Exception {
        CoolapkClient.getInstance().search("qq")
                .subscribe(coolapkSearchResult -> {
                    System.out.println(coolapkSearchResult.getData().size());
                });
    }

    @Test
    public void requestBodyTest() throws Exception {
        AvosClient.getInstance().postBatch(
                new AvosBatchRequest(Arrays.asList(
                        new AvosRequest("POST", "/1.1/classes/RequestTable",
                                new AvosIconRequestBean("test", "test", "test", "test",
                                        "test")),
                        new AvosRequest("POST", "path",
                                new AvosIconRequestBean("test", "test", "test", "test",
                                        "test"))
                ))
        ).subscribe(new Observer<List<AvosBatchResult>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<AvosBatchResult> avosBatchResults) {
                Stream.of(avosBatchResults)
                        .forEach(result -> System.out.println(new Gson().toJson(result)));
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });

    }
}
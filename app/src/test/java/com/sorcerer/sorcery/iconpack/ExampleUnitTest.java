package com.sorcerer.sorcery.iconpack;

import com.google.gson.Gson;
import com.sorcerer.sorcery.iconpack.net.avos.models.AvosBatchRequest;
import com.sorcerer.sorcery.iconpack.net.avos.AvosClient;
import com.sorcerer.sorcery.iconpack.net.avos.models.AvosIconRequestBean;
import com.sorcerer.sorcery.iconpack.net.avos.models.AvosQuerySelection;
import com.sorcerer.sorcery.iconpack.net.avos.models.AvosRequest;
import com.sorcerer.sorcery.iconpack.net.coolapk.CoolapkClient;
import com.sorcerer.sorcery.iconpack.net.spiders.AppNameGetter;
import com.sorcerer.sorcery.iconpack.net.spiders.AppSearchResultGetter;
import com.sorcerer.sorcery.iconpack.utils.NetUtil;

import org.junit.Test;

import java.util.Arrays;
import java.util.Locale;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void requestTimeTest() throws Exception {
//        com.twitter.android
//        com.sorcerer.sorcery.iconpack
        AvosClient.getInstance().getAppRequestedTime("com.twitter.android")
                .subscribe(System.out::println);
    }

    @Test
    public void mapToJsonTest() throws Exception {
        AvosQuerySelection selection = new AvosQuerySelection();
        selection.setSelection("package", "com.sorcerer.sorcery.iconpack");
        System.out.println(selection.toString());
    }

    @Test
    public void addition_isCorrect() throws Exception {
        AppSearchResultGetter.search("settings")
                .subscribe(strings -> {
                    System.out.println(Arrays.toString(strings.toArray()).replace(",", "\n"));
                });
    }

    @Test
    public void appNameGetterTest() throws Exception {
        AppNameGetter.getName("com.tiantonglaw.readlaw", Locale.getDefault())
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
        System.out.println(new Gson().toJson(new AvosBatchRequest(Arrays.asList(
                new AvosRequest("POST", "/1.1/classes/RequestTable",
                        new AvosIconRequestBean("test", "test", "test", "test",
                                "test")),
                new AvosRequest("POST", "path",
                        new AvosIconRequestBean("test", "test", "test", "test",
                                "test"))
        ))));

    }
}
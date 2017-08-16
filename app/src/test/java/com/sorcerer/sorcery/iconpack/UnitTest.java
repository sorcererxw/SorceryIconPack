package com.sorcerer.sorcery.iconpack;

import com.google.gson.Gson;
import com.sorcerer.sorcery.iconpack.network.avos.models.AvosBatchRequest;
import com.sorcerer.sorcery.iconpack.network.avos.models.AvosIconRequestBean;
import com.sorcerer.sorcery.iconpack.network.avos.models.AvosQuerySelection;
import com.sorcerer.sorcery.iconpack.network.avos.models.AvosRequest;
import com.sorcerer.sorcery.iconpack.network.sorcery.SorceryClient;
import com.sorcerer.sorcery.iconpack.network.spiders.AppSearchResultGetter;
import com.sorcererxw.sorcery.icons.packtools.PackTools;

import org.junit.Test;

import java.util.Arrays;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class UnitTest {
    @Test
    public void pack() throws Exception {
        PackTools.pack();
    }

    @Test
    public void requestTimeTest() throws Exception {
//        AvosClient.getInstance().getAppRequestedTime("com.twitter.android")
//                .subscribe(System.out::println);
        SorceryClient.getInstance().getRequestStatistic("com.tencent.mma")
                .subscribe(sorceryRequestStatistic ->
                                System.out.println(sorceryRequestStatistic.getCount()),
                        Throwable::printStackTrace);
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
package com.sorcerer.sorcery.iconpack;

import com.annimon.stream.Stream;
import com.sorcerer.sorcery.iconpack.net.coolapk.CoolapkClient;
import com.sorcerer.sorcery.iconpack.net.coolapk.CoolapkSearchResult;
import com.sorcerer.sorcery.iconpack.net.spiders.AppNameGetter;
import com.sorcerer.sorcery.iconpack.net.spiders.AppSearchResultGetter;
import com.sorcerer.sorcery.iconpack.utils.NetUtil;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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
//        NetUtil.enableSSLSocket();
//        AppSearchResultGetter.searchViaApi("qq")
//                .subscribe(list -> {
//                    Stream.of(list).forEach(System.out::println);
//                });
        CoolapkClient.getInstance().search("qq")
                .subscribe(new Consumer<CoolapkSearchResult>() {
                    @Override
                    public void accept(CoolapkSearchResult coolapkSearchResult) throws Exception {
                        System.out.println(coolapkSearchResult.getData().size());
                    }
                });
    }
}
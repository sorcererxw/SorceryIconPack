package com.sorcerer.sorcery.iconpack;

import com.sorcerer.sorcery.iconpack.net.spiders.AppSearchResultGetter;

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
        AppSearchResultGetter.search("计算器")
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        System.out.println(Arrays.toString(strings.toArray()).replace(",", "\n"));
                    }
                });
    }
}
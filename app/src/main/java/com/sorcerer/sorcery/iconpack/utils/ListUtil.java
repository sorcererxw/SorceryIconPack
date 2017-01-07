package com.sorcerer.sorcery.iconpack.utils;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/1/6
 */

public class ListUtil {

    public static List<String> containsItems(List<String> list, String fragment) {
        return Stream.of(list).filter(s -> s.contains(fragment)).collect(Collectors.toList());
    }
}

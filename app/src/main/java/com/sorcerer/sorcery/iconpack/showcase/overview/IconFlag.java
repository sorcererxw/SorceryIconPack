package com.sorcerer.sorcery.iconpack.showcase.overview;

import java.util.Comparator;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/3/5
 */

public enum IconFlag {
    NEW(0),
    INSTALLED(1),
    ALL(2),
    ALI(3),
    BAIDU(4),
    NETEASE(5),
    GOOGLE(6),
    MICROSOFT(7),
    SAMSUNG(8),
    SONY(9),
    TENCENT(10),
    XIAOMI(11),
    FLYME(12),
    ONEPLUS(13),
    HUAWEI(14),
    GAME(15);

    private int mOrder;

    IconFlag(int order) {
        mOrder = order;
    }

    public int getOrder() {
        return mOrder;
    }


    @Override
    public String toString() {
        if (this == GAME) {
            return "game_";
        }
        return super.toString();
    }
}

class IconFlagComparator implements Comparator<IconFlag> {

    @Override
    public int compare(IconFlag o1, IconFlag o2) {
        return o1.getOrder() < o2.getOrder() ? -1 : 1;
    }
}

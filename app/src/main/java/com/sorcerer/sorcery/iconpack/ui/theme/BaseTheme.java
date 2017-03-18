package com.sorcerer.sorcery.iconpack.ui.theme;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/3/12
 */

public abstract class BaseTheme {

    public abstract boolean isDark();

    abstract String name();

    abstract int style();

    abstract int primaryColor();

    abstract int primaryDarkColor();

    abstract int accentColor();

    abstract int backgroundColor();

    abstract int cardColor();

    abstract int primaryTextColor();

    abstract int secondaryTextColor();
}

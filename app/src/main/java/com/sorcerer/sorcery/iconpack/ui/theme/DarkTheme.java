package com.sorcerer.sorcery.iconpack.ui.theme;

import com.sorcerer.sorcery.iconpack.R;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/3/12
 */

public class DarkTheme extends BaseTheme {

    public static final String NAME = "DARK";

    @Override
    public boolean isDark() {
        return true;
    }

    @Override
    String name() {
        return NAME;
    }

    @Override
    public int style() {
        return R.style.DarkTheme;
    }

    @Override
    int primaryColor() {
        return R.color.theme_dark_colorPrimary;
    }

    @Override
    int primaryDarkColor() {
        return R.color.theme_dark_colorPrimaryDark;
    }

    @Override
    int accentColor() {
        return R.color.theme_dark_colorAccent;
    }

    @Override
    int backgroundColor() {
        return R.color.theme_dark_colorBackground;
    }

    @Override
    int cardColor() {
        return R.color.theme_dark_colorCard;
    }

    @Override
    int primaryTextColor() {
        return R.color.palette_white;
    }

    @Override
    int secondaryTextColor() {
        return R.color.palette_white;
    }
}

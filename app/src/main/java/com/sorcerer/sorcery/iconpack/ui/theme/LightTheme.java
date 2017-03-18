package com.sorcerer.sorcery.iconpack.ui.theme;

import com.sorcerer.sorcery.iconpack.R;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/3/12
 */

public class LightTheme extends BaseTheme {

    public static final String NAME = "LIGHT";

    @Override
    public boolean isDark() {
        return false;
    }

    @Override
    String name() {
        return NAME;
    }

    @Override
    int style() {
        return R.style.LightTheme;
    }

    @Override
    int primaryColor() {
        return R.color.palette_green_500;
    }

    @Override
    int primaryDarkColor() {
        return R.color.palette_green_700;
    }

    @Override
    int accentColor() {
        return R.color.palette_brown_500;
    }

    @Override
    int backgroundColor() {
        return R.color.palette_grey_100;
    }

    @Override
    int cardColor() {
        return R.color.palette_white;
    }

    @Override
    int primaryTextColor() {
        return R.color.palette_grey_900;
    }

    @Override
    int secondaryTextColor() {
        return R.color.palette_grey_800;
    }
}

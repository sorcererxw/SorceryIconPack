package com.sorcerer.sorcery.iconpack.ui.theme

import com.sorcerer.sorcery.iconpack.R

/**
 * @description:
 * *
 * @author: Sorcerer
 * *
 * @date: 2017/3/12
 */

class LightTheme : BaseTheme() {

    override val isDark: Boolean
        get() = false

    override fun name(): String {
        return NAME
    }

    override fun style(): Int {
        return R.style.LightTheme
    }

    override fun primaryColor(): Int {
        return R.color.palette_green_500
    }

    override fun primaryDarkColor(): Int {
        return R.color.palette_green_700
    }

    override fun accentColor(): Int {
        return R.color.palette_brown_500
    }

    override fun backgroundColor(): Int {
        return R.color.palette_grey_100
    }

    override fun cardColor(): Int {
        return R.color.palette_white
    }

    override fun primaryTextColor(): Int {
        return R.color.palette_grey_900
    }

    override fun secondaryTextColor(): Int {
        return R.color.palette_grey_800
    }

    companion object {

        val NAME = "LIGHT"
    }
}

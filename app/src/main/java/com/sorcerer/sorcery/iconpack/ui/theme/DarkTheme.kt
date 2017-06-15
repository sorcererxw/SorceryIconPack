package com.sorcerer.sorcery.iconpack.ui.theme

import com.sorcerer.sorcery.iconpack.R

/**
 * @description:
 * *
 * @author: Sorcerer
 * *
 * @date: 2017/3/12
 */

class DarkTheme : BaseTheme() {

    override val isDark: Boolean
        get() = true

    override fun name(): String {
        return NAME
    }

    override fun style(): Int {
        return R.style.DarkTheme
    }

    override fun primaryColor(): Int {
        return R.color.theme_dark_colorPrimary
    }

    override fun primaryDarkColor(): Int {
        return R.color.theme_dark_colorPrimaryDark
    }

    override fun accentColor(): Int {
        return R.color.theme_dark_colorAccent
    }

    override fun backgroundColor(): Int {
        return R.color.theme_dark_colorBackground
    }

    override fun cardColor(): Int {
        return R.color.theme_dark_colorCard
    }

    override fun primaryTextColor(): Int {
        return R.color.palette_white
    }

    override fun secondaryTextColor(): Int {
        return R.color.palette_white
    }

    companion object {
        val NAME = "DARK"
    }
}

package com.sorcerer.sorcery.iconpack.ui.theme

/**
 * @description:
 * *
 * @author: Sorcerer
 * *
 * @date: 2017/3/12
 */

abstract class BaseTheme {

    abstract val isDark: Boolean

    abstract fun name(): String

    abstract fun style(): Int

    abstract fun primaryColor(): Int

    abstract fun primaryDarkColor(): Int

    abstract fun accentColor(): Int

    abstract fun backgroundColor(): Int

    abstract fun cardColor(): Int

    abstract fun primaryTextColor(): Int

    abstract fun secondaryTextColor(): Int
}

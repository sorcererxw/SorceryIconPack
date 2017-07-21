package com.sorcerer.sorcery.iconpack.utils

import android.content.Context
import com.sorcerer.sorcery.iconpack.R
import java.util.*

/**
 * @description:
 * *
 * @author: Sorcerer
 * *
 * @date: 2017/2/5
 */

object LocaleUtil {
    fun isChinese(context: Context): Boolean {
        return context.resources.getString(R.string.language) == "zh"
    }

    @Suppress("DEPRECATION")
    fun forceChinese(context: Context) {
        val res = context.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = Locale("zh")
        res.updateConfiguration(conf, dm)
    }
}

package com.sorcerer.sorcery.iconpack.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

/**
 * @description:
 * *
 * @author: Sorcerer
 * *
 * @date: 2016/4/28
 */
object DisplayUtil {

    fun dip2px(context: Context, dipValue: Float): Int {
        return dip2px(context.resources, dipValue)
    }

    fun dip2px(resources: Resources, dipValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }


    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun sp2px(context: Context, sp: Float): Int {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                context.resources.displayMetrics
        ).toInt()
    }

}

package com.sorcerer.sorcery.iconpack.utils

import android.graphics.Typeface
import android.widget.TextView

/**
 * @description:
 * *
 * @author: Sorcerer
 * *
 * @date: 2017/4/12
 */

object TextWeightUtil {
    fun medium(textView: TextView) {
        try {
            val font = Typeface.create("sans-serif-medium", Typeface.NORMAL)
            if (font != null) {
                textView.typeface = font
            }
        } catch (ignore: Exception) {
        }

    }

    fun regular(textView: TextView) {
        try {
            val font = Typeface.create("sans-serif-regular", Typeface.NORMAL)
            if (font != null) {
                textView.typeface = font
            }
        } catch (ignore: Exception) {
        }

    }

    fun light(textView: TextView) {
        try {
            val font = Typeface.create("sans-serif-light", Typeface.NORMAL)
            if (font != null) {
                textView.typeface = font
            }
        } catch (ignore: Exception) {
        }

    }
}

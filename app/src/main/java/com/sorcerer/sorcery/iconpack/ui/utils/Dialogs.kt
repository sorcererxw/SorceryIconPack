package com.sorcerer.sorcery.iconpack.ui.utils

import android.content.Context

import com.afollestad.materialdialogs.MaterialDialog
import com.sorcerer.sorcery.iconpack.R
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil

/**
 * @description:
 * *
 * @author: Sorcerer
 * *
 * @date: 2017/2/17
 */

object Dialogs {
    fun builder(context: Context): MaterialDialog.Builder {
        return MaterialDialog.Builder(context)
                .backgroundColor(ResourceUtil.getAttrColor(context, R.attr.colorCard))
    }

    fun indeterminateProgressDialog(context: Context,
                                    message: String): MaterialDialog {
        return MaterialDialog.Builder(context)
                .progress(true, 0)
                .content(message)
                .canceledOnTouchOutside(false)
                .build()
    }

    fun horizontalIndeterminateProgressDialog(context: Context,
                                              message: String): MaterialDialog {
        return MaterialDialog.Builder(context)
                .progress(true, 0)
                .content(message)
                .canceledOnTouchOutside(false)
                .progressIndeterminateStyle(true)
                .build()
    }
}

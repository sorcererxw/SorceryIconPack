package com.sorcerer.sorcery.iconpack.ui.utils;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/17
 */

public class Dialogs {
    public static MaterialDialog indeterminateProgressDialog(Context context,
                                                             String message) {
        return new MaterialDialog.Builder(context)
                .progress(true, 0)
                .content(message)
                .canceledOnTouchOutside(false)
                .build();
    }

    public static MaterialDialog horizontalIndeterminateProgressDialog(Context context,
                                                                       String message) {
        return new MaterialDialog.Builder(context)
                .progress(true, 0)
                .content(message)
                .canceledOnTouchOutside(false)
                .progressIndeterminateStyle(true)
                .build();
    }
}

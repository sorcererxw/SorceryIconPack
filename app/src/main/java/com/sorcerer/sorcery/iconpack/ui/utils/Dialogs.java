package com.sorcerer.sorcery.iconpack.ui.utils;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/17
 */

public class Dialogs {
    public static MaterialDialog.Builder builder(Context context) {
        return new MaterialDialog.Builder(context)
                .backgroundColor(ResourceUtil.getAttrColor(context, R.attr.colorCard));
    }

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

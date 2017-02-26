package com.sorcerer.sorcery.iconpack.utils;

import android.content.Context;

import com.sorcerer.sorcery.iconpack.R;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/5
 */

public class LocaleUtil {
    public static boolean isChinese(Context context) {
        return context.getResources().getString(R.string.language).equals("zh");
    }
}

package com.sorcerer.sorcery.iconpack.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.sorcerer.sorcery.iconpack.R;

import java.util.Locale;

/**
 * @description: *
 * @author: Sorcerer
 * *
 * @date: 2017/2/5
 */

public class LocaleUtil {
    public static boolean isChinese(Context context) {
        return context.getResources().getString(R.string.language).equals("zh");
    }

    public void forceChinese(Context context) {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = new Locale("zh");
        res.updateConfiguration(conf, dm);
    }
}

package com.sorcerer.sorcery.iconpack.ui.theme;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.f2prateek.rx.preferences2.Preference;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.settings.prefs.Prefs;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/3/12
 */

class ThemePrefs extends Prefs {
    ThemePrefs(Context context) {
        super(context);
    }

    @Override
    protected String providePreferenceName() {
        return "theme";
    }

    Preference<Integer> accentColor() {
        return getRxSharedPreferences().getInteger("accentColor",
                ContextCompat.getColor(mContext, R.color.colorAccent));
    }

    Preference<Integer> cardColor() {
        return getRxSharedPreferences().getInteger("cardColor",
                ContextCompat.getColor(mContext, R.color.palette_white));
    }

    Preference<Integer> primaryColor() {
        return getRxSharedPreferences().getInteger("primaryColor",
                ContextCompat.getColor(mContext, R.color.colorPrimary));
    }

    Preference<Integer> primaryDarkColor() {
        return getRxSharedPreferences().getInteger("primaryDarkColor",
                ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
    }

    Preference<Integer> backgroundColor() {
        return getRxSharedPreferences().getInteger("backgroundColor",
                ContextCompat.getColor(mContext, R.color.colorBackground));
    }

    Preference<Integer> primaryTextColor() {
        return getRxSharedPreferences().getInteger("primaryTextColor",
                ContextCompat.getColor(mContext, R.color.primary_text));
    }

    Preference<Integer> secondaryTextColor() {
        return getRxSharedPreferences().getInteger("secondaryTextColor",
                ContextCompat.getColor(mContext, R.color.secondary_text));
    }

    Preference<String> theme() {
        return getRxSharedPreferences().getString("CURRENT_THEME", "");
    }
}

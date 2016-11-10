package com.sorcerer.sorcery.iconpack.util.Prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/3
 */

public abstract class Prefs {

    private final SharedPreferences mPreferences;

    protected abstract String providePreferenceName();

    public Prefs(Context context) {
        mPreferences = context.getSharedPreferences(providePreferenceName(), Context.MODE_PRIVATE);
    }

    SharedPreferences getPreferences() {
        return mPreferences;
    }

}

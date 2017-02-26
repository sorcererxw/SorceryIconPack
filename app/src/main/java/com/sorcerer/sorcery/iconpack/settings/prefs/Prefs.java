package com.sorcerer.sorcery.iconpack.settings.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.f2prateek.rx.preferences2.RxSharedPreferences;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/3
 */

abstract class Prefs {

    private final SharedPreferences mPreferences;
    private final RxSharedPreferences mRxSharedPreferences;

    protected abstract String providePreferenceName();

    Prefs(Context context) {
        mPreferences = context.getSharedPreferences(providePreferenceName(), Context.MODE_PRIVATE);
        mRxSharedPreferences = RxSharedPreferences.create(mPreferences);
    }

    SharedPreferences getPreferences() {
        return mPreferences;
    }

    RxSharedPreferences getRxSharedPreferences() {
        return mRxSharedPreferences;
    }

}

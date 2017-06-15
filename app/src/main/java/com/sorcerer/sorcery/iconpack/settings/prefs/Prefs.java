package com.sorcerer.sorcery.iconpack.settings.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.f2prateek.rx.preferences2.RxSharedPreferences;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/3
 */

public abstract class Prefs {

    private final SharedPreferences mPreferences;
    private final RxSharedPreferences mRxSharedPreferences;
    protected Context mContext;

    public Prefs(Context context) {
        mContext = context;
        mPreferences = context.getSharedPreferences(providePreferenceName(), Context.MODE_PRIVATE);
        mRxSharedPreferences = RxSharedPreferences.create(mPreferences);
    }

    protected abstract String providePreferenceName();

    public SharedPreferences getPreferences() {
        return mPreferences;
    }

    public RxSharedPreferences getRxSharedPreferences() {
        return mRxSharedPreferences;
    }

}

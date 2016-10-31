package com.sorcerer.sorcery.iconpack.util.Prefs;

import android.content.Context;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/4
 */

public class SorceryPrefs extends Prefs {

    @Override
    protected String providePreferenceName() {
        return "SorceryIcons";
    }

    public SorceryPrefs(Context context) {
        super(context);
    }

    private SorceryPreference<Boolean> mDonated;

    public SorceryPreference<Boolean> donated() {
        if (mDonated == null) {
            mDonated = new SorceryPreference<>(getPreferences(), "donated", false);
        }
        return mDonated;
    }
}

package com.sorcerer.sorcery.iconpack.utils.Prefs;

import android.content.Context;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/4
 */

public class SorceryPrefs extends Prefs {

    private static SorceryPrefs mSorceryPrefs;

    public static SorceryPrefs getInstance(Context context) {
        if (mSorceryPrefs == null) {
            mSorceryPrefs = new SorceryPrefs(context);
        }
        return mSorceryPrefs;
    }

    private SorceryPrefs(Context context) {
        super(context);
    }

    @Override
    protected String providePreferenceName() {
        return "SorceryIcons";
    }

    private SorceryPreference<Boolean> mDonated;

    public SorceryPreference<Boolean> donated() {
        if (mDonated == null) {
            mDonated = new SorceryPreference<>(getPreferences(), "donated", false);
        }
        return mDonated;
    }

    private SorceryPreference<Boolean> mDevOptionsOpened;

    public SorceryPreference<Boolean> devOptionsOpened() {
        if (mDevOptionsOpened == null) {
            mDevOptionsOpened =
                    new SorceryPreference<>(getPreferences(), "dev options opened", false);
        }
        return mDevOptionsOpened;
    }
}

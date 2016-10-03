package com.sorcerer.sorcery.iconpack.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/3
 */

public class Prefs {
    private final SharedPreferences mPreferences;

    public Prefs(Context context) {
        mPreferences = context.getSharedPreferences("FeedMan", Context.MODE_PRIVATE);
    }

    private SorceryPreference<Boolean> mDonated;

    public SorceryPreference<Boolean> donated() {
        if (mDonated == null) {
            mDonated = new SorceryPreference<>(mPreferences, "donated", false);
        }
        return mDonated;
    }

}

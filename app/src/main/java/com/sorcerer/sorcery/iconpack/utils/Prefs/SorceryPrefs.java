package com.sorcerer.sorcery.iconpack.utils.Prefs;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

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

    private Map<String, SorceryPreference<Boolean>> tabShowMap = new HashMap<>();

    public SorceryPreference<Boolean> isTabShow(String tabName) {
        if (tabShowMap.containsKey(tabName)) {
            return tabShowMap.get(tabName);
        }
        SorceryPreference<Boolean> sp =
                new SorceryPreference<>(getPreferences(), "tab_show_" + tabName, true);
        tabShowMap.put(tabName, sp);
        return sp;
    }

    private SorceryPreference<Boolean> mUserGuideShowed;

    public SorceryPreference<Boolean> userGuideShowed() {
        if (mUserGuideShowed == null) {
            mUserGuideShowed =
                    new SorceryPreference<>(getPreferences(), "user_guide_showed", false);
        }
        return mUserGuideShowed;
    }
}

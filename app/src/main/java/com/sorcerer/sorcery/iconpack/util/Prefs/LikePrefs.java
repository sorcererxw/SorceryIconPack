package com.sorcerer.sorcery.iconpack.util.Prefs;

import android.content.Context;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/4
 */

public class LikePrefs extends Prefs{

    public LikePrefs(Context context) {
        super(context);
    }

    @Override
    protected String providePreferenceName() {
        return "ICON_LIKE";
    }

    public SorceryPreference<Integer> like(String name){
        return new SorceryPreference<>(getPreferences(), name, 0);
    }
}

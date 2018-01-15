package com.sorcerer.sorcery.iconpack.apply.database.base;

import com.sorcerer.sorcery.iconpack.App;
import com.sorcerer.sorcery.iconpack.SorceryPrefs;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/3/7
 */

public abstract class BaseLauncherApplier implements ILauncherApplier {
    protected SorceryPrefs mPrefs;

    protected BaseLauncherApplier() {
        mPrefs = App.getInstance().prefs();
    }
}

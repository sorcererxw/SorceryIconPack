package com.sorcerer.sorcery.iconpack.ui.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.sorcerer.sorcery.iconpack.App;
import com.sorcerer.sorcery.iconpack.SorceryPrefs;

import javax.inject.Inject;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/3/7
 */

public abstract class BasePreferenceFragmentCompat extends PreferenceFragmentCompat {
    @Inject
    protected SorceryPrefs mPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        App.getInstance().getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }
}

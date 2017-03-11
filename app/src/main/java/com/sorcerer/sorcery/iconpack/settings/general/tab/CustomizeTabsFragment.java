package com.sorcerer.sorcery.iconpack.settings.general.tab;

import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.PreferenceScreen;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.iconShowCase.overview.IconFlag;
import com.sorcerer.sorcery.iconpack.ui.fragments.BasePreferenceFragmentCompat;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/3/5
 */

public class CustomizeTabsFragment extends BasePreferenceFragmentCompat {
    private PreferenceScreen mPreferenceScreen;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_settings_customize_tabs, rootKey);
        mPreferenceScreen = (PreferenceScreen) findPreference("preference_screen");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IconFlag[] flags = IconFlag.values();
        for (int i = 0; i < flags.length; i++) {
            SwitchPreference switchPreference =
                    new SwitchPreference(mPreferenceScreen.getContext());

            switchPreference.setKey("switchPreference_" + flags[i].name());

            mPrefs.isTabShow(flags[i].name().toLowerCase())
                    .asObservable().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(switchPreference::setChecked);

            switchPreference.setTitle(ResourceUtil
                    .getStringFromResString(getContext(), "tab_" + flags[i].name().toLowerCase()));
            if (i == flags.length - 1) {
                switchPreference.setLayoutResource(R.layout.layout_preference_card_last);
            } else {
                switchPreference.setLayoutResource(R.layout.layout_preference_card);
            }
            int index = i;
            switchPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                mPrefs.isTabShow(flags[index].name().toLowerCase()).set((Boolean) newValue);
                return true;
            });
            mPreferenceScreen.addPreference(switchPreference);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(R.string.preference_general_customize_tabs);
    }
}

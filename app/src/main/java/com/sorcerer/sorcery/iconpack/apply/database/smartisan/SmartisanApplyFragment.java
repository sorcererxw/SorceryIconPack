package com.sorcerer.sorcery.iconpack.apply.database.smartisan;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.utils.Dialogs;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/28
 */

public class SmartisanApplyFragment extends PreferenceFragmentCompat {

    private Preference mApplyPreference;
    private Preference mRestorePreference;

    public static SmartisanApplyFragment newInstance() {

        Bundle args = new Bundle();

        SmartisanApplyFragment fragment = new SmartisanApplyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_apply_smartisan, rootKey);

        mApplyPreference = findPreference("preference_apply");
        mRestorePreference = findPreference("preference_restore");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplyPreference.setOnPreferenceClickListener(preference -> {
            Dialogs.builder(getContext())
                    .title(R.string.apply_by_database_declaration_title)
                    .content(ResourceUtil
                            .getString(getContext(),
                                    R.string.apply_by_database_declaration_content)
                            .replaceAll("\\{\\}", "Smartisan")
                            .replaceAll("\\|", "\n"))
                    .positiveText(R.string.ok)
                    .negativeText(R.string.cancel)
                    .onPositive((dialog, which) -> SmartisanApplyProgressActivity
                            .apply(getContext(), true))
                    .onNegative((dialog, which) -> dialog.dismiss())
                    .build().show();
            return true;
        });

        mRestorePreference.setOnPreferenceClickListener(preference -> {
            SmartisanApplyProgressActivity.apply(getContext(), false);
            return true;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(R.string.apply_smartisan_title);
    }
}

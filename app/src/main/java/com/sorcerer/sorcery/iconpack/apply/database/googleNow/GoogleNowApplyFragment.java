package com.sorcerer.sorcery.iconpack.apply.database.googleNow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.settings.prefs.SorceryPreference;
import com.sorcerer.sorcery.iconpack.ui.fragments.BasePreferenceFragmentCompat;
import com.sorcerer.sorcery.iconpack.ui.utils.Dialogs;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/28
 */

public class GoogleNowApplyFragment extends BasePreferenceFragmentCompat {

    private Preference mApplyPreference;
    private Preference mRestorePreference;
    private SwitchPreference mDisableWorkPreference;
    private SorceryPreference<Boolean> mDisableWork;
    private Snackbar mReapplySnackbar;

    public static GoogleNowApplyFragment newInstance() {

        Bundle args = new Bundle();

        GoogleNowApplyFragment fragment = new GoogleNowApplyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_apply_google_now, rootKey);

        mApplyPreference = findPreference("preference_apply");
        mRestorePreference = findPreference("preference_restore");
        mDisableWorkPreference = (SwitchPreference) findPreference("preference_disable_work");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplyPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Dialogs.builder(getContext())
                        .title(R.string.apply_by_database_declaration_title)
                        .content(ResourceUtil
                                .getString(getContext(),
                                        R.string.apply_by_database_declaration_content)
                                .replaceAll("\\{\\}", "Google Now")
                                .replaceAll("\\|", "\n"))
                        .positiveText(R.string.ok)
                        .negativeText(R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog,
                                                @NonNull DialogAction which) {
                                GoogleNowApplyProgressActivity
                                        .apply(getContext(), true);
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog,
                                                @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .build().show();
                return true;
            }
        });

        mRestorePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                GoogleNowApplyProgressActivity.apply(getContext(), false);
                return true;
            }
        });

        mDisableWork = mPrefs.isGoogleNowDisableWork();

        mDisableWorkPreference.setChecked(mDisableWork.getValue());
        mDisableWorkPreference.setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        mDisableWork.setValue((Boolean) newValue);
                        showReapplySnackbar();
                        return true;
                    }
                });
    }

    private void showReapplySnackbar() {
        if (mReapplySnackbar == null) {
            if (getView() == null) {
                return;
            }
            mReapplySnackbar = Snackbar.make(getView(), R.string.apply_option_reapply_hint,
                    Snackbar.LENGTH_SHORT);
        }
        if (!mReapplySnackbar.isShown()) {
            mReapplySnackbar.show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(R.string.apply_google_now_title);
    }
}

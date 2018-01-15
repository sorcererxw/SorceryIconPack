package com.sorcerer.sorcery.iconpack.apply.database.pixel;

import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.settings.prefs.SorceryPreference;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseFragmentSubActivity;
import com.sorcerer.sorcery.iconpack.ui.fragments.BasePreferenceFragmentCompat;
import com.sorcerer.sorcery.iconpack.ui.utils.Dialogs;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/27
 */

public class PixelApplyFragment extends BasePreferenceFragmentCompat {

    private Preference mApplyPreference;
    private Preference mRestorePreference;
    private SwitchPreference mDisableRoundPreference;
    private SwitchPreference mDisableWorkPreference;
    private SorceryPreference<Boolean> mDisableRound;
    private SorceryPreference<Boolean> mDisableWork;
    private SwitchPreference mNormalizeIconSizePreference;
    private SorceryPreference<Boolean> mNormalizeIconSize;
    private Preference mSkipPreference;

    public static PixelApplyFragment newInstance() {

        Bundle args = new Bundle();

        PixelApplyFragment fragment = new PixelApplyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_apply_pixel, rootKey);

        mApplyPreference = findPreference("preference_apply");
        mRestorePreference = findPreference("preference_restore");
        mDisableRoundPreference =
                (SwitchPreference) findPreference("preference_disable_round");
        mDisableWorkPreference = (SwitchPreference) findPreference("preference_disable_work");
        mNormalizeIconSizePreference =
                (SwitchPreference) findPreference("preference_normalize_icon_size");
        mSkipPreference = findPreference("preference_skip");
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
                            .replaceAll("\\{\\}", "Pixel")
                            .replaceAll("\\|", "\n"))
                    .positiveText(R.string.ok)
                    .negativeText(R.string.cancel)
                    .onPositive((dialog, which) -> PixelApplyProgressActivity
                            .apply(getContext(), true))
                    .onNegative((dialog, which) -> dialog.dismiss())
                    .build().show();
            return true;
        });

        mRestorePreference.setOnPreferenceClickListener(preference -> {
            PixelApplyProgressActivity.apply(getContext(), false);
            return true;
        });

        mDisableRound = mPrefs.isPixelDisableRound();

        mDisableRoundPreference.setChecked(mDisableRound.getValue());
        mDisableRoundPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                    mDisableRound.setValue((Boolean) newValue);
                    showReapplyHint();
                    return true;
                }
        );

        mDisableWork = mPrefs.isPixelDisableWork();

        mDisableWorkPreference.setChecked(mDisableWork.getValue());
        mDisableWorkPreference.setOnPreferenceChangeListener(
                (preference, newValue) -> {
                    mDisableWork.setValue((Boolean) newValue);
                    showReapplyHint();
                    return true;
                });

        mNormalizeIconSize = mPrefs.isPixelNormalizeIconSize();
        mNormalizeIconSizePreference.setChecked(mNormalizeIconSize.getValue());
        mNormalizeIconSizePreference.setOnPreferenceChangeListener(
                (preference, newValue) -> {
                    mNormalizeIconSize.setValue((Boolean) newValue);
                    showReapplyHint();
                    return true;
                });

        mSkipPreference.setOnPreferenceClickListener(preference -> {
            ((BaseFragmentSubActivity) getActivity()).addFragment(new PixelSkipFragment());
            return true;
        });
    }

    private void showReapplyHint() {
        Toast.makeText(getContext(), R.string.apply_option_reapply_hint, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(R.string.apply_pixel_title);
    }

}

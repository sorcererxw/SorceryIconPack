package com.sorcerer.sorcery.iconpack.apply.database.pixel;

import android.os.Bundle;
import android.os.Handler;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.data.models.AppInfo;
import com.sorcerer.sorcery.iconpack.ui.fragments.BasePreferenceFragmentCompat;
import com.sorcerer.sorcery.iconpack.ui.fragments.IOnBackFragment;
import com.sorcerer.sorcery.iconpack.ui.fragments.ProgressFragment;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/3/3
 */

public class PixelSkipFragment extends ProgressFragment implements IOnBackFragment {

    private PixelSkipSettingsFragment mChildFragment = new PixelSkipSettingsFragment();

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(R.string.apply_skip_title);

        Observable.just(getContext())
                .subscribeOn(Schedulers.newThread())
                .map(context -> Stream.of(PackageUtil.getComponentInfo(context, true))
                        .filter(AppInfo::isHasCustomIcon)
                        .collect(Collectors.toList()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(appInfos -> {
                    mChildFragment.setData(appInfos);
                    new Handler().post(() -> setChildFragment(mChildFragment));
                });
    }

    @Override
    public void onBackPress() {
        if (mChildFragment.mChanged) {
            Toast.makeText(getContext(), R.string.apply_option_reapply_hint, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public static class PixelSkipSettingsFragment extends BasePreferenceFragmentCompat {
        private PreferenceScreen mPreferenceScreen;

        private boolean mChanged = false;
        private List<AppInfo> mList;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            mPreferenceScreen = new PreferenceManager(getContext())
                    .createPreferenceScreen(getContext());
            setPreferenceScreen(mPreferenceScreen);
        }

        void setData(List<AppInfo> list) {
            mList = list;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            for (int i = 0; i < mList.size(); i++) {
                SwitchPreference switchPreference = new SwitchPreference(getContext());

                mPrefs.pixelIconSkip(mList.get(i).getPackage())
                        .asObservable().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(switchPreference::setChecked);

                switchPreference.setIcon(mList.get(i).getIcon());
                switchPreference.setTitle(mList.get(i).getName());
                if (i == mList.size() - 1) {
                    switchPreference.setLayoutResource(R.layout.layout_preference_card_last);
                } else {
                    switchPreference.setLayoutResource(R.layout.layout_preference_card);
                }
                int index = i;
                switchPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                    mPrefs.pixelIconSkip(mList.get(index).getPackage())
                            .set((Boolean) newValue);
                    mChanged = true;
                    return true;
                });
                mPreferenceScreen.addPreference(switchPreference);
            }
        }
    }
}
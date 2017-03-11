package com.sorcerer.sorcery.iconpack.settings;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.settings.about.AboutDialog;
import com.sorcerer.sorcery.iconpack.settings.general.tab.CustomizeTabsFragment;
import com.sorcerer.sorcery.iconpack.settings.licenses.ui.OpenSourceLibFragment;
import com.sorcerer.sorcery.iconpack.ui.fragments.BasePreferenceFragmentCompat;
import com.sorcerer.sorcery.iconpack.ui.others.OnMultiTouchListener;
import com.sorcerer.sorcery.iconpack.utils.FileUtil;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/11
 */

@SuppressWarnings("ALL")
public class SettingsFragment extends BasePreferenceFragmentCompat {
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    private PreferenceScreen mPreferenceScreen;

    private Preference mGeneralCustomizeTabs;

    private Preference mGeneralClearCachePreference;

    private SwitchPreference mUiNavBarPreference;

    private PreferenceGroup mDevPreferenceGroup;

    private Preference mAboutAppPreference;

    private Preference mAboutLibPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_settings, rootKey);
        mPreferenceScreen = (PreferenceScreen) findPreference("preference_screen");
        new Handler().post(() -> {
            initGeneral();
            initUi();
            initDevOps();
            initAbout();
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private void initGeneral() {
        mGeneralClearCachePreference = findPreference("preference_general_clear_cache");

        mGeneralCustomizeTabs = findPreference("preference_general_customize_tabs");
        mGeneralCustomizeTabs.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        ((SettingsActivity) getActivity())
                                .addFragment(new CustomizeTabsFragment());
                        return true;
                    }
                });

        mGeneralClearCachePreference.setSummary("...");
        updateCache(Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> emitter) throws Exception {
                emitter.onNext(FileUtil.calculateDirectorySize(getContext().getCacheDir()));
            }
        }).subscribeOn(Schedulers.newThread()));

        mGeneralClearCachePreference.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        updateCache(Observable.create(new ObservableOnSubscribe<Long>() {
                            @Override
                            public void subscribe(ObservableEmitter<Long> emitter)
                                    throws Exception {
                                FileUtil.deleteDirectory(getContext().getCacheDir());
                                emitter.onNext(FileUtil
                                        .calculateDirectorySize(getContext().getCacheDir()));
                            }
                        }).subscribeOn(Schedulers.newThread()));
                        return false;
                    }
                });
    }

    private SwitchPreference mUiLessAnimPreference;

    private void initUi() {
        mUiNavBarPreference =
                (SwitchPreference) findPreference("preference_switch_ui_transparent_nav_bar");

        mUiNavBarPreference.setChecked(mPrefs.enableTransparentNavBar().get());
        mUiNavBarPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            mPrefs.enableTransparentNavBar().set((Boolean) newValue);
            return true;
        });

        mUiLessAnimPreference = (SwitchPreference) findPreference("preference_ui_less_anim");

        mUiLessAnimPreference.setChecked(mPrefs.lessAnim().get());
        mUiLessAnimPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            mPrefs.lessAnim().set((Boolean) newValue);
            return true;
        });
    }

    private void initDevOps() {
        mDevPreferenceGroup = (PreferenceGroup) findPreference("preference_group_dev");
        if (!mPrefs.devOptionsOpened().getValue()) {
            mPreferenceScreen.removePreference(mDevPreferenceGroup);
        }
    }

    private void initAbout() {
        mAboutAppPreference = findPreference("preference_about_app");
        mAboutAppPreference.setOnPreferenceClickListener(preference -> {
            if (!mPrefs.devOptionsOpened().getValue()) {
                AboutDialog.show(getActivity(), new OnMultiTouchListener(5, () -> {
                    Toast.makeText(getContext(), "Dev-options is opened", Toast.LENGTH_SHORT)
                            .show();
                    mPreferenceScreen.addPreference(mDevPreferenceGroup);
                    mPrefs.devOptionsOpened().setValue(true);
                }));
            } else {
                AboutDialog.show(getActivity());
            }
            return true;
        });

        mAboutLibPreference = findPreference("preference_about_lib");
        mAboutLibPreference.setOnPreferenceClickListener(preference -> {
            ((SettingsActivity) getActivity())
                    .addFragment(new OpenSourceLibFragment());
            return true;
        });
    }

    private void updateCache(Observable<Long> observable) {
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long longSize) {
                        double size = Double.valueOf(longSize);
                        String unit = "KB";
                        size /= 1024;
                        if (size > 1024) {
                            unit = "MB";
                            size /= 1024;
                        }
                        mGeneralClearCachePreference
                                .setSummary(String.format("%.2f %s", size, unit));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Timber.e(throwable);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(ResourceUtil.getString(getContext(), R.string.nav_item_settings));
    }
}
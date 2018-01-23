package com.sorcerer.sorcery.iconpack.apply;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import com.afollestad.materialdialogs.MaterialDialog;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.apply.api.LauncherApplier;
import com.sorcerer.sorcery.iconpack.apply.database.googleNow.GoogleNowApplyFragment;
import com.sorcerer.sorcery.iconpack.apply.database.pixel.PixelApplyFragment;
import com.sorcerer.sorcery.iconpack.apply.database.smartisan.SmartisanApplyFragment;
import com.sorcerer.sorcery.iconpack.apply.xposed.XposedApplyFragment;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseFragmentSubActivity;
import com.sorcerer.sorcery.iconpack.ui.utils.Dialogs;
import com.sorcerer.sorcery.iconpack.utils.ImageUtil;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/27
 */

public class OverviewFragment extends PreferenceFragmentCompat {
    private PreferenceScreen mPreferenceScreen;
    private PreferenceCategory mRecommendCategory;
    private PreferenceCategory mLauncherCategory;
    private PreferenceCategory mSystemCategory;
    private List<LauncherInfo> mSystemList;
    private List<LauncherInfo> mLauncherList;
    private List<LauncherInfo> mRecommendList;

    private static List<LauncherInfo> generateRecommend(Context context,
                                                        List<LauncherInfo> systemInfos,
                                                        List<LauncherInfo> launcherInfos) {
        List<LauncherInfo> list = new ArrayList<>();

        for (int i = 0; i < systemInfos.size(); i++) {
            if (systemInfos.get(i).isInstalled()) {
                list.add(systemInfos.get(i));
                if (systemInfos.get(i).getLabel().toLowerCase().equals("flyme")
                        || systemInfos.get(i).getLabel().toLowerCase().equals("miui")) {
                    return list;
                }
            }
        }
        list.addAll(Stream.of(launcherInfos)
                .filter(value -> value.isInstalled()
                        && value.getPackageName().equals(PackageUtil.getCurrentLauncher(context)))
                .collect(Collectors.toList()));
        return list;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_apply, rootKey);

        mPreferenceScreen = (PreferenceScreen) findPreference("preference_screen");
        mSystemCategory = (PreferenceCategory) findPreference("preference_category_system");
        mLauncherCategory = (PreferenceCategory) findPreference("preference_category_launcher");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSystemList = PackageUtil.generateLauncherInfo(getContext(), R.array.apply_systems);
        mLauncherList = PackageUtil.generateLauncherInfo(getContext(), R.array.apply_launchers);
        mRecommendList = generateRecommend(getContext(), mSystemList, mLauncherList);

        initRecommend();
        initSystem();
        initLauncher();
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(R.string.nav_item_apply);
    }

    private void initRecommend() {
        if (mRecommendList.size() == 0) {
            return;
        }
        mRecommendCategory = new PreferenceCategory(getContext());
        mRecommendCategory.setLayoutResource(R.layout.layout_preference_category_card);
        mRecommendCategory.setTitle(R.string.apply_recommend);
        mRecommendCategory.setOrder(0);
        mPreferenceScreen.addPreference(mRecommendCategory);
        for (int i = 0; i < mRecommendList.size(); i++) {
            LauncherInfo info = mRecommendList.get(i);
            Preference preference = new Preference(getContext());
            if (i == mRecommendList.size() - 1) {
                preference.setLayoutResource(R.layout.layout_preference_card_last);
            } else {
                preference.setLayoutResource(R.layout.layout_preference_card);
            }
            preference.setTitle(info.getLabel());

            if (info.isInstalled()) {
                preference.setIcon(info.getIcon());
                preference.setEnabled(true);
                preference.setSummary(R.string.installed);
            } else {
                preference.setIcon(ImageUtil.grayScale(ResourceUtil.getDrawable(
                        getContext(), info.getIcon())));
                preference.setEnabled(false);
                preference.setSummary(R.string.not_installed);
            }
            preference.setOnPreferenceClickListener(preference1 -> {
                if (info.isInstalled()) {
                    onItemClickInstalled(info);
                } else {
                    onItemClickNotInstalled(info);
                }
                return true;
            });
            mRecommendCategory.addPreference(preference);
        }
    }

    private void initSystem() {
        Collections.sort(mSystemList, LauncherInfo::compareTo);
        for (int i = 0; i < mSystemList.size(); i++) {
            LauncherInfo info = mSystemList.get(i);
            Preference preference = new Preference(getContext());
            if (i == mSystemList.size() - 1) {
                preference.setLayoutResource(R.layout.layout_preference_card_last);
            } else {
                preference.setLayoutResource(R.layout.layout_preference_card);
            }
            preference.setTitle(info.getLabel());
            if (info.isInstalled()) {
                preference.setIcon(info.getIcon());
                preference.setEnabled(true);
                preference.setSummary(R.string.installed);
            } else {
                preference.setIcon(ImageUtil.grayScale(ResourceUtil.getDrawable(
                        getContext(), info.getIcon())));
                preference.setEnabled(false);
                preference.setSummary(R.string.not_installed);
            }
            preference.setOnPreferenceClickListener(preference1 -> {
                if (info.isInstalled()) {
                    onItemClickInstalled(info);
                } else {
                    onItemClickNotInstalled(info);
                }
                return true;
            });
            mSystemCategory.addPreference(preference);
        }
    }

    private void initLauncher() {
        Collections.sort(mLauncherList, LauncherInfo::compareTo);
        for (int i = 0; i < mLauncherList.size(); i++) {
            LauncherInfo info = mLauncherList.get(i);
            Preference preference = new Preference(getContext());
            if (i == mLauncherList.size() - 1) {
                preference.setLayoutResource(R.layout.layout_preference_card_last);
            } else {
                preference.setLayoutResource(R.layout.layout_preference_card);
            }
            preference.setTitle(info.getLabel() + " launcher");
            if (info.isInstalled()) {
                preference.setIcon(info.getIcon());
                preference.setEnabled(true);
                preference.setSummary(R.string.installed);
            } else {
                preference.setIcon(ImageUtil.grayScale(ResourceUtil.getDrawable(
                        getContext(), info.getIcon())));
                preference.setEnabled(false);
                preference.setSummary(R.string.not_installed);
            }
            preference.setOnPreferenceClickListener(preference1 -> {
                if (info.isInstalled()) {
                    onItemClickInstalled(info);
                } else {
                    onItemClickNotInstalled(info);
                }
                return true;
            });
            mLauncherCategory.addPreference(preference);
        }
    }

    private void onItemClickInstalled(LauncherInfo item) {
        if (item.getLabel().toLowerCase().equals("flyme")) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.meizu.customizecenter",
                    "com.meizu.customizecenter.OnlineThemeActivity"));
            intent.putExtra("ONLINE_THEME_WAY", "ONLINE_WAY_URL");
            intent.putExtra("URL", "/themes/public/detail/3000115");
            intent.putExtra("search_content_type", "themes");
            intent.putExtra("position", 0);
            intent.putExtra("search_action", "click_history_label");
            intent.putExtra("event_path", "OneSearchActivity");
            intent.putExtra("search_content", "Sorcery");
            intent.putExtra("search_id", "cbe31a19-ac0f-4d86-b8a3-2077fc132088");
            startActivity(intent);
        } else if (item.getLabel().toLowerCase().equals("miui")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                    "http://zhuti.xiaomi.com/detail/db2b63b2-b14e-4f5f-96bf-c12a730d3fd6"));
            startActivity(intent);
        } else if (item.getLabel().toLowerCase().equals("xposed")) {
            ((BaseFragmentSubActivity) getActivity())
                    .addFragment(XposedApplyFragment.newInstance());
        } else if (item.getLabel().toLowerCase().equals("pixel")) {
            ((BaseFragmentSubActivity) getActivity())
                    .addFragment(PixelApplyFragment.newInstance());
        } else if (item.getLabel().toLowerCase().equals("smartisan")) {
            ((BaseFragmentSubActivity) getActivity())
                    .addFragment(SmartisanApplyFragment.newInstance());
        } else if (item.getLabel().toLowerCase().equals("google now")) {
            ((BaseFragmentSubActivity) getActivity())
                    .addFragment(GoogleNowApplyFragment.newInstance());
        } else {
            new LauncherApplier(getContext(), item.getLabel());
        }
    }

    private void onItemClickNotInstalled(LauncherInfo item) {
        MaterialDialog.Builder builder = Dialogs.builder(getContext());
        if (item.getLabel().toLowerCase().equals("flyme")) {
            builder.content(R.string.apply_tip_only_flyme)
                    .positiveText(R.string.ok);
        } else if (item.getLabel().toLowerCase().equals("miui")) {
            builder.content(R.string.apply_tip_only_miui).positiveText(R.string.ok);
        } else if (item.getLabel().toLowerCase().equals("xposed")) {
            builder.content(R.string.apply_by_xposed_no_xposed).positiveText(R.string.ok);
        } else {
            builder.content(R.string.apply_tip_no_launcher)
                    .positiveText(R.string.ok).negativeText(R.string.cancel)
                    .onPositive((dialog, which) -> {
                        String appPackageName = item.getPackageName();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id="
                                            + appPackageName)));
                        } catch (android.content.ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                                    "https://play.google.com/store/apps/details?id="
                                            + appPackageName)));
                        }
                    });
        }
        builder.build().show();
    }

}

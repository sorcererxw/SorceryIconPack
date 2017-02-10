package com.sorcerer.sorcery.iconpack.settings;

import android.app.Activity;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.annimon.stream.Stream;
import com.google.gson.Gson;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.settings.about.AboutDialog;
import com.sorcerer.sorcery.iconpack.settings.licenses.ui.OpenSourceLibDialog;
import com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter.CustomizeTabsAdapter;
import com.sorcerer.sorcery.iconpack.ui.others.OnMultiTouchListener;
import com.sorcerer.sorcery.iconpack.utils.FileUtil;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;
import com.sorcerer.sorcery.iconpack.utils.Prefs.SorceryPrefs;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;
import com.sorcerer.sorcery.iconpack.utils.StringUtil;
import com.sorcerer.sorcery.iconpack.xposed.XposedUtils;
import com.sorcerer.sorcery.iconpack.xposed.theme.IconReplacementItem;
import com.sorcerer.sorcery.iconpack.xposed.theme.Util;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static android.content.Context.MODE_WORLD_READABLE;
import static android.support.v7.widget.LinearLayoutManager.VERTICAL;
import static com.sorcerer.sorcery.iconpack.ui.activities.LabActivity.SHARED_PREFERENCE_NAME;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/11
 */

@SuppressWarnings("ALL")
public class SettingsFragment extends PreferenceFragment {
    private Activity mActivity;

    private SharedPreferences mSharedPreferences;

    private SorceryPrefs mPrefs;

    private boolean mGlobalLoadActive;
    private MaterialDialog mGlobalLoadDialog;

    private static final String KEY_PREFERENCE_SCREEN = "preference_screen";
    private PreferenceScreen mPreferenceScreen;

    private static final String KEY_GENERAL_CUSTOMIZE_TABS = "preference_general_customize_tabs";
    private Preference mGeneralCustomizeTabs;

    private static final String KEY_GENERAL_CLEAR_CACHE = "preference_general_clear_cache";
    private Preference mGeneralClearCachePreference;

    private static final String KEY_LAB_GROUP = "preference_group_laboratory";
    private PreferenceGroup mLabGroup;

    private static final String KEY_LABORATORY_GLOBAL_LOAD =
            "preference_switch_laboratory_global_load";
    private SwitchPreference mLaboratoryGlobalLoadSwitchPreference;

    private static final String KEY_DEV_GROUP = "preference_group_dev";
    private PreferenceGroup mDevPreferenceGroup;

    private static final String KEY_ABOUT_APP = "preference_about_app";
    private Preference mAboutAppPreference;

    private static final String KEY_ABOUT_LIB = "preference_about_lib";
    private Preference mAboutLibPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        mPreferenceScreen = (PreferenceScreen) findPreference(KEY_PREFERENCE_SCREEN);
        initGeneral();
        initLab();
        initDevOps();
        initAbout();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        if (PackageUtil.isXposedInstalled(activity) && Build.VERSION.SDK_INT <= 23) {
            mSharedPreferences =
                    mActivity.getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_WORLD_READABLE);
            mGlobalLoadActive = mSharedPreferences.getBoolean("pref_global_load", false);
        }
        mPrefs = SorceryPrefs.getInstance(activity);
    }

    private void initGeneral() {
        mGeneralClearCachePreference = findPreference(KEY_GENERAL_CLEAR_CACHE);

        mGeneralCustomizeTabs = findPreference(KEY_GENERAL_CUSTOMIZE_TABS);
        mGeneralCustomizeTabs.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        RecyclerView recyclerView = new RecyclerView(mActivity);
                        CustomizeTabsAdapter ctAdapter = new CustomizeTabsAdapter(mActivity);
                        recyclerView.setAdapter(ctAdapter);
                        recyclerView.setLayoutParams(new RecyclerView.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
                        recyclerView.setLayoutManager(
                                new LinearLayoutManager(mActivity, VERTICAL, false));
                        new MaterialDialog.Builder(mActivity)
                                .customView(recyclerView, false)
                                .title(getString(R.string.preference_general_customize_tabs))
                                .positiveText(getString(R.string.action_apply))
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog,
                                                        @NonNull DialogAction which) {
                                        Toast.makeText(mActivity,
                                                getString(R.string.restart_to_take_effect),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }).build().show();
                        return true;
                    }
                });

        mGeneralClearCachePreference.setSummary("...");
        updateCache(Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> emitter) throws Exception {
                emitter.onNext(FileUtil.calculateDirectorySize(mActivity.getCacheDir()));
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
                                FileUtil.deleteDirectory(mActivity.getCacheDir());
                                emitter.onNext(FileUtil
                                        .calculateDirectorySize(mActivity.getCacheDir()));
                            }
                        }).subscribeOn(Schedulers.newThread()));
                        return false;
                    }
                });
    }

    private void initLab() {
        mGlobalLoadDialog = new MaterialDialog.Builder(mActivity)
                .content("Waiting")
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .build();

        if (!PackageUtil.isXposedInstalled(mActivity) || Build.VERSION.SDK_INT >= 24) {
            mLabGroup = (PreferenceGroup) findPreference(KEY_LAB_GROUP);
            mPreferenceScreen.removePreference(mLabGroup);
        } else {
            mLaboratoryGlobalLoadSwitchPreference =
                    (SwitchPreference) findPreference(KEY_LABORATORY_GLOBAL_LOAD);
            mLaboratoryGlobalLoadSwitchPreference.setSummary(StringUtil.handleLongXmlString(
                    ResourceUtil.getString(mActivity, R.string.global_detail)));
            mLaboratoryGlobalLoadSwitchPreference.setChecked(mGlobalLoadActive);
            mLaboratoryGlobalLoadSwitchPreference.setOnPreferenceClickListener(
                    new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            if (!mGlobalLoadActive) {
                                if (!PackageUtil.isXposedInstalled(mActivity)) {
                                    Toast.makeText(mActivity, "need Xposed", Toast.LENGTH_SHORT)
                                            .show();
                                    return true;
                                }
                                new MaterialDialog.Builder(mActivity)
                                        .title(R.string.attention)
                                        .content(StringUtil.handleLongXmlString(ResourceUtil
                                                .getString(mActivity,
                                                        R.string.global_attention)))
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog,
                                                                @NonNull DialogAction which) {
                                                tryAndApplyIcon(mActivity.getApplicationInfo());
                                            }
                                        })
                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog,
                                                                @NonNull DialogAction which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .positiveText(R.string.action_know_what_doing)
                                        .negativeText(R.string.action_later)
                                        .build().show();
                            } else {
                                mGlobalLoadActive = false;
                                mSharedPreferences.edit()
                                        .putBoolean("pref_global_load", false)
                                        .apply();
                                mLaboratoryGlobalLoadSwitchPreference.setChecked(false);
                            }
                            return true;
                        }
                    });
        }
    }

    private void initDevOps() {
        mDevPreferenceGroup = (PreferenceGroup) findPreference(KEY_DEV_GROUP);
        if (!mPrefs.devOptionsOpened().getValue()) {
            mPreferenceScreen.removePreference(mDevPreferenceGroup);
        }
    }

    private void initAbout() {
        mAboutAppPreference = findPreference(KEY_ABOUT_APP);
        mAboutAppPreference.setOnPreferenceClickListener(preference -> {
            if (!mPrefs.devOptionsOpened().getValue()) {
                AboutDialog.show(mActivity, new OnMultiTouchListener(5, () -> {
                    Toast.makeText(mActivity, "Dev-options is opened", Toast.LENGTH_SHORT).show();
                    mPreferenceScreen.addPreference(mDevPreferenceGroup);
                    mPrefs.devOptionsOpened().setValue(true);
                }));
            } else {
                AboutDialog.show(mActivity);
            }
            return true;
        });

        mAboutLibPreference = findPreference(KEY_ABOUT_LIB);
        mAboutLibPreference.setOnPreferenceClickListener(preference -> {
            OpenSourceLibDialog.show(mActivity);
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

    /* <----------------------------------------------> */
    private boolean tryAndApplyIcon(final ApplicationInfo themePackage) {
//        if (!RootTools.isAccessGiven()) {
//            try {
//                Toast.makeText(mActivity, "acquiring root...", Toast.LENGTH_SHORT).show();
//                RootTools.getShell(true).add(new CommandCapture(0, "echo Hello"));
//            } catch (Exception e) {
//                Timber.e(e);
//                return false;
//            }
//        }
//        if (!RootTools.isAccessGiven()) {
//            Toast.makeText(mActivity, "need root access!", Toast.LENGTH_SHORT).show();
//            return false;
//        } else {
//            mGlobalLoadDialog.show();
//            if (!new File(mActivity.getCacheDir().getAbsolutePath() + "/icons").exists()) {
//                try {
//                    RootTools.getShell(true).add(new CommandCapture(0,
//                            "mkdir " + mActivity.getCacheDir().getAbsolutePath() + "/icons",
//                            "chmod 777 " + mActivity.getCacheDir().getAbsolutePath() + "/icons"));
//                } catch (Exception e) {
//                    Timber.e(e);
//                }
//            }


//            if (!appIsInstalledInMountASEC()) {
//                try {
//                    Runtime.getRuntime().exec(
//                            "rm " + mActivity.getExternalCacheDir().getAbsolutePath() + "/tmp.apk");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }

//            apply(themePackage);
//            return true;
//        }
        return false;
    }

    private void apply(final ApplicationInfo themePackage) {
        Observable.just(themePackage).subscribe(new Consumer<ApplicationInfo>() {
            @Override
            public void accept(ApplicationInfo applicationInfo) {
                XmlPullParser xrp, xrp2;
                List<IconReplacementItem> replacementList;
                Resources originPackageRes;
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                Gson gson = new Gson();
                Map<String, List<IconReplacementItem>> replacementListMap = new HashMap<>();
            }
        });
        new Thread(new Runnable() {
            public void run() {
                try {
                    XmlPullParser xrp;
                    ArrayList<IconReplacementItem> items;
                    Resources origPkgRes;
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    Gson gson = new Gson();
                    List<String> mIconPackages = new ArrayList<>();
                    Map<String, ArrayList<IconReplacementItem>> mIconReplacementsHashMap =
                            new HashMap<>();
                    String themePackagePath = themePackage.sourceDir;
                    if (themePackage.sourceDir.contains("/data/app/")) {
//                        RootTools.getShell(true).add(new CommandCapture(0,
//                                "rm /data/data/" + mActivity.getPackageName()
//                                        + "/cache/icons/*",
//                                "rm " + mActivity.getExternalCacheDir()
//                                        .getAbsolutePath()
//                                        + "/current_theme.apk"));
                    } else {
//                        Command commandCapture = new CommandCapture(0,
//                                "rm /data/data/" + mActivity.getPackageName()
//                                        + "/cache/icons/*",
//                                "rm " + mActivity.getExternalCacheDir().getAbsolutePath()
//                                        + "/current_theme.apk",
//                                "cat \"" + themePackage.sourceDir + "\" > "
//                                        + mActivity.getExternalCacheDir()
//                                        .getAbsolutePath() + "/current_theme.apk",
//                                "chmod 644 " + mActivity.getExternalCacheDir().getAbsolutePath()
//                                        + "/current_theme.apk");
//                        RootTools.getShell(true).add(commandCapture);
//
//                        themePackagePath =
//                                mActivity.getExternalCacheDir() + "/current_theme.apk";
                    }
                    PackageManager pm = mActivity.getPackageManager();
                    Resources r = mActivity.getPackageManager()
                            .getResourcesForApplication(themePackage.packageName);
                    if (r.getIdentifier("appfilter", "xml", themePackage.packageName) == 0) {
                        InputStream istr = r.getAssets().open("values/appfilter.xml");
                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        factory.setNamespaceAware(true);
                        xrp = factory.newPullParser();
                        xrp.setInput(istr, "UTF-8");
                    } else {
                        xrp = r.getXml(r
                                .getIdentifier("appfilter", "xml", themePackage.packageName));
                    }
                    Stream.of(mSharedPreferences.getAll().entrySet())
                            .filter(entry -> entry.getKey().contains("theme_icon_for_"))
                            .forEach(entry -> editor.remove(entry.getKey()));
                    editor.commit();
                    DisplayMetrics metrics = new DisplayMetrics();
                    mActivity.getWindowManager().getDefaultDisplay()
                            .getMetrics(metrics);
                    if (metrics.densityDpi == 213) {
                        metrics.densityDpi = 320;
                    }
                    editor.putInt("display_dpi", metrics.densityDpi);
                    Iterator i$ = Util.ParseIconReplacements(themePackage.packageName, r, xrp)
                            .iterator();
                    while (i$.hasNext()) {
                        IconReplacementItem item = (IconReplacementItem) i$.next();
                        try {
                            ActivityInfo activityInfo = pm.getActivityInfo(new ComponentName(
                                            item.getPackageName(),
                                            item.getActivityName()),
                                    PackageManager.GET_META_DATA);
                            if (activityInfo != null) {
                                if (mIconReplacementsHashMap.get(item.getPackageName())
                                        == null) {
                                    mIconReplacementsHashMap
                                            .put(item.getPackageName(), new ArrayList());
                                }
                                items = (ArrayList) mIconReplacementsHashMap
                                        .get(item.getPackageName());
                                origPkgRes =
                                        pm.getResourcesForApplication(item.getPackageName());
                                if (activityInfo.getIconResource() != 0) {
                                    try {
                                        item.setPackageName(origPkgRes
                                                .getResourcePackageName(activityInfo
                                                        .getIconResource()));
                                    } catch (Exception e) {
                                        Timber.e(e);
                                    }
                                }
                                item.setOrigRes(activityInfo.getIconResource());

                                if (!items.contains(item)) {
                                    items.add(item);

                                    XposedUtils.cacheDrawable(item.getPackageName(),
                                            item.getOrigRes(),
                                            new BitmapDrawable(origPkgRes,
                                                    XposedUtils.getBitmapForDensity(r,
                                                            metrics.densityDpi,
                                                            item.getReplacementRes())));
                                }
                            }
                        } catch (Exception e) {
                            Timber.e(e);
                        }
                    }
                    editor.putString("theme_package_name", themePackage.packageName);
                    editor.putString("theme_package_path", themePackagePath);
                    for (Map.Entry<String, ArrayList<IconReplacementItem>> entry2 : mIconReplacementsHashMap
                            .entrySet()) {
                        mIconPackages.add(entry2.getKey());
                        editor.putString("theme_icon_for_" + entry2.getKey(),
                                gson.toJson(((ArrayList) entry2.getValue()).toArray()));
                    }
                    editor.putString("theme_icon_packages",
                            gson.toJson(mIconPackages.toArray()));
                    editor.commit();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            runAfterSuccess();
                        }
                    });
                } catch (Exception e6) {
                    e6.printStackTrace();
                    mActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(mActivity, "Failed to apply icons!",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                } finally {
                    mGlobalLoadDialog.dismiss();
                }
            }
        }).start();

    }

    private void runAfterSuccess() {
        mSharedPreferences.edit().putBoolean("pref_global_load", true).apply();
        mLaboratoryGlobalLoadSwitchPreference.setChecked(true);
        mGlobalLoadActive = true;
    }

    private boolean appIsInstalledInMountASEC() {
        return mActivity.getApplicationInfo().sourceDir.contains("asec/");
    }
}
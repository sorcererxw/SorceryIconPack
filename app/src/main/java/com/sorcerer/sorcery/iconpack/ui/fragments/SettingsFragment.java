package com.sorcerer.sorcery.iconpack.ui.fragments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.OpenSourceLibBean;
import com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter.OpenSourceLibAdapter;
import com.sorcerer.sorcery.iconpack.ui.others.OnMultiTouchListener;
import com.sorcerer.sorcery.iconpack.ui.preferences.SorcerySwitchPreference;
import com.sorcerer.sorcery.iconpack.utils.AppInfoUtil;
import com.sorcerer.sorcery.iconpack.utils.FileUtil;
import com.sorcerer.sorcery.iconpack.utils.OpenSourceLibInformations;
import com.sorcerer.sorcery.iconpack.utils.Prefs.Prefs;
import com.sorcerer.sorcery.iconpack.utils.Prefs.SorceryPrefs;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;
import com.sorcerer.sorcery.iconpack.utils.SimpleCallback;
import com.sorcerer.sorcery.iconpack.utils.StringUtil;
import com.sorcerer.sorcery.iconpack.xposed.XposedUtils;
import com.sorcerer.sorcery.iconpack.xposed.theme.IconReplacementItem;
import com.sorcerer.sorcery.iconpack.xposed.theme.Util;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.Command;
import com.stericson.RootTools.execution.CommandCapture;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.content.Context.MODE_WORLD_READABLE;
import static com.sorcerer.sorcery.iconpack.ui.activities.LabActivity.SHARED_PREFERENCE_NAME;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/11
 */

public class SettingsFragment extends PreferenceFragment {
    private Activity mActivity;

    private SharedPreferences mSharedPreferences;

    private SorceryPrefs mPrefs;

    private boolean mGlobalLoadActive;
    private MaterialDialog mGlobalLoadDialog;

    private static final String KEY_PREFERENCE_SCREEN = "preference_screen";
    private PreferenceScreen mPreferenceScreen;

    private static final String KEY_GENERAL_CLEAR_CACHE = "preference_general_clear_cache";
    private Preference mGeneralClearCachePreference;

    private static final String KEY_LABORATORY_GLOBAL_LOAD =
            "preference_switch_laboratory_global_load";
    private SorcerySwitchPreference mLaboratoryGlobalLoadSwitchPreference;

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

        mGeneralClearCachePreference = findPreference(KEY_GENERAL_CLEAR_CACHE);
        mGeneralClearCachePreference.setSummary("...");
        updateCache(Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                subscriber.onNext(FileUtil.calculateDirectorySize(mActivity.getCacheDir()));
            }
        }).subscribeOn(Schedulers.newThread()));
        mGeneralClearCachePreference.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        updateCache(Observable.create(new Observable.OnSubscribe<Long>() {
                            @Override
                            public void call(Subscriber<? super Long> subscriber) {
                                FileUtil.deleteDirectory(mActivity.getCacheDir());
                                subscriber.onNext(FileUtil
                                        .calculateDirectorySize(mActivity.getCacheDir()));
                            }
                        }).subscribeOn(Schedulers.newThread()));
                        return false;
                    }
                });

        mGlobalLoadDialog = new MaterialDialog.Builder(mActivity)
                .content("Waiting")
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .build();

        mLaboratoryGlobalLoadSwitchPreference =
                (SorcerySwitchPreference) findPreference(KEY_LABORATORY_GLOBAL_LOAD);
        mLaboratoryGlobalLoadSwitchPreference.setSummary(StringUtil.handleLongXmlString(
                ResourceUtil.getString(mActivity, R.string.global_detail)));
        mLaboratoryGlobalLoadSwitchPreference.setChecked(mGlobalLoadActive);
        mLaboratoryGlobalLoadSwitchPreference.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        if (!mGlobalLoadActive) {
                            if (!AppInfoUtil.isXposedInstalled(mActivity)) {
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

        mDevPreferenceGroup = (PreferenceGroup) findPreference(KEY_DEV_GROUP);
        if (!mPrefs.devOptionsOpened().getValue()) {
            mPreferenceScreen.removePreference(mDevPreferenceGroup);
        }

        mAboutAppPreference = findPreference(KEY_ABOUT_APP);
        mAboutAppPreference.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        showAboutDialog();
                        return false;
                    }
                });

        mAboutLibPreference = findPreference(KEY_ABOUT_LIB);
        mAboutLibPreference.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        showLibDialog();
                        return false;
                    }
                });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        if (AppInfoUtil.isXposedInstalled(activity)) {
            mSharedPreferences =
                    mActivity.getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_WORLD_READABLE);
            mGlobalLoadActive = mSharedPreferences.getBoolean("pref_global_load", false);
        }
        mPrefs = SorceryPrefs.getInstance(activity);
    }

    private void updateCache(Observable<Long> observable) {
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long longSize) {
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
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (BuildConfig.DEBUG) {
                            throwable.printStackTrace();
                        }
                    }
                });
    }

    private void showAboutDialog() {
        View view = View.inflate(mActivity, R.layout.layout_about_dialog, null);
        ImageView logoImageView = (ImageView) view.findViewById(R.id.imageView_about_dialog_logo);
        if (!mPrefs.devOptionsOpened().getValue()) {
            logoImageView.setOnTouchListener(new OnMultiTouchListener(5, new SimpleCallback() {
                @Override
                public void call() {
                    Toast.makeText(mActivity, "Dev-options is opened", Toast.LENGTH_SHORT).show();
                    mPreferenceScreen.addPreference(mDevPreferenceGroup);
                    mPrefs.devOptionsOpened().setValue(true);
                }
            }));
        }

        TextView versionTextView =
                (TextView) view.findViewById(R.id.textView_about_dialog_version);
        TextView titleTextView = (TextView) view.findViewById(R.id.textView_about_dialog_title);
        TextView creditsTextView =
                (TextView) view.findViewById(R.id.textView_about_dialog_credits);
        versionTextView
                .setText(String.format("%s: %s",
                        ResourceUtil.getString(mActivity, R.string.version),
                        BuildConfig.VERSION_NAME));
        titleTextView.setTypeface(
                Typeface.createFromAsset(mActivity.getAssets(), "RockwellStd.otf"));
        SpannableString openSource = new SpannableString(getString(R.string.open_source_lib));
        openSource.setSpan(new UnderlineSpan(), 0, openSource.length(), 0);
        String htmlBuilder = "";
        htmlBuilder +=
                ("<a>" + ResourceUtil.getString(mActivity, R.string.contributor) + "</a><br>");
        htmlBuilder += ("<a href=\"https://github.com/sorcererxw\">SorcererXW</a><br>");
        htmlBuilder += ("<a href=\"http://weibo.com/mozartjac\">翟宅宅Jack</a><br>");
        htmlBuilder += ("<a>nako liu</a>");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            creditsTextView.setText(Html.fromHtml(htmlBuilder, Html.FROM_HTML_MODE_LEGACY));
        } else {
            creditsTextView.setText(Html.fromHtml(htmlBuilder));
        }
        creditsTextView.setMovementMethod(LinkMovementMethod.getInstance());
        new MaterialDialog.Builder(mActivity)
                .customView(view, true)
                .build().show();
    }

    private void showLibDialog() {
        List<OpenSourceLibBean> list = Arrays.asList(
                new OpenSourceLibInformations.RxJavaInfoBean(),
                new OpenSourceLibInformations.RxAndroidInfoBean(),
                new OpenSourceLibInformations.RxActivityResultInfoBean(),
                new OpenSourceLibInformations.RxPermissionInfoBean(),
                new OpenSourceLibInformations.XposedBridgeInfoBean(),
                new OpenSourceLibInformations.GlideInfoBean(),
                new OpenSourceLibInformations.TinkerInfoBean(),
                new OpenSourceLibInformations.MaterialDrawerInfoBean(),
                new OpenSourceLibInformations.MaterialDialogsInfoBean(),
                new OpenSourceLibInformations.SliceInfoBean(),
                new OpenSourceLibInformations.AVLoadingIndicatorViewInfoBean(),
                new OpenSourceLibInformations.PhotoViewInfoBean(),
                new OpenSourceLibInformations.IconicsInfoBean(),
                new OpenSourceLibInformations.ButterknifeInfoBean()
        );

        OpenSourceLibAdapter adapter = new OpenSourceLibAdapter(mActivity, list);

        MaterialDialog.Builder builder = new MaterialDialog.Builder(mActivity);

        RecyclerView recyclerView = new RecyclerView(mActivity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(
                new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        builder.customView(recyclerView, false);

        builder.build().show();
    }

    /* <----------------------------------------------> */
    private boolean tryAndApplyIcon(final ApplicationInfo themePackage) {
        if (!RootTools.isAccessGiven()) {
            try {
                Toast.makeText(mActivity, "acquiring root...", Toast.LENGTH_SHORT).show();
                RootTools.getShell(true).add(new CommandCapture(0, "echo Hello"));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        if (!RootTools.isAccessGiven()) {
            Toast.makeText(mActivity, "need root access!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            mGlobalLoadDialog.show();
            if (!new File(mActivity.getCacheDir().getAbsolutePath() + "/icons").exists()) {
                try {
                    RootTools.getShell(true).add(new CommandCapture(0,
                            "mkdir " + mActivity.getCacheDir().getAbsolutePath() + "/icons",
                            "chmod 777 " + mActivity.getCacheDir().getAbsolutePath() + "/icons"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            if (!appIsInstalledInMountASEC()) {
//                try {
//                    Runtime.getRuntime().exec(
//                            "rm " + mActivity.getExternalCacheDir().getAbsolutePath() + "/tmp.apk");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }

            apply(themePackage);
            return true;
        }
    }

    private void apply(final ApplicationInfo themePackage) {
        Observable.just(themePackage).subscribe(new Action1<ApplicationInfo>() {
            @Override
            public void call(ApplicationInfo applicationInfo) {
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
                    XmlPullParser xrp2;
                    ArrayList<IconReplacementItem> items;
                    Resources origPkgRes;
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    Gson gson = new Gson();
                    List<String> mIconPackages = new ArrayList<>();
                    Map<String, ArrayList<IconReplacementItem>> mIconReplacementsHashMap =
                            new HashMap<>();
                    String themePackagePath = themePackage.sourceDir;
                    if (themePackage.sourceDir.contains("/data/app/")) {
                        RootTools.getShell(true).add(new CommandCapture(0,
                                "rm /data/data/" + mActivity.getPackageName()
                                        + "/cache/icons/*",
                                "rm " + mActivity.getExternalCacheDir()
                                        .getAbsolutePath()
                                        + "/current_theme.apk"));

                    } else {
                        Command commandCapture = new CommandCapture(0,
                                "rm /data/data/" + mActivity.getPackageName()
                                        + "/cache/icons/*",
                                "rm " + mActivity.getExternalCacheDir().getAbsolutePath()
                                        + "/current_theme.apk",
                                "cat \"" + themePackage.sourceDir + "\" > "
                                        + mActivity.getExternalCacheDir()
                                        .getAbsolutePath() + "/current_theme.apk",
                                "chmod 644 " + mActivity.getExternalCacheDir().getAbsolutePath()
                                        + "/current_theme.apk");
                        RootTools.getShell(true).add(commandCapture);

                        themePackagePath =
                                mActivity.getExternalCacheDir() + "/current_theme.apk";
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
                        InputStream istr2 = r.getAssets().open("values/appfilter.xml");
                        XmlPullParserFactory factory2 = XmlPullParserFactory.newInstance();
                        factory2.setNamespaceAware(true);
                        xrp2 = factory2.newPullParser();
                        xrp2.setInput(istr2, "UTF-8");
                    } else {
                        xrp = r.getXml(r
                                .getIdentifier("appfilter", "xml", themePackage.packageName));
                        xrp2 = r.getXml(r
                                .getIdentifier("appfilter", "xml", themePackage.packageName));
                    }
                    for (Map.Entry<String, ?> entry : mSharedPreferences.getAll().entrySet()) {
                        if (entry.getKey().contains("theme_icon_for_")) {
                            editor.remove(entry.getKey());
                        }
                    }
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
                                    item.getActivityName()), PackageManager.GET_META_DATA);
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
                                        if (BuildConfig.DEBUG) {
                                            e.printStackTrace();
                                        }
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
                            if (BuildConfig.DEBUG) {
                                e.printStackTrace();
                            }
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
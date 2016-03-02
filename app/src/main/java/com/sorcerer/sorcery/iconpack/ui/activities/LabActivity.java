package com.sorcerer.sorcery.iconpack.ui.activities;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.adapters.SettingsAdapter;
import com.sorcerer.sorcery.iconpack.models.CheckSettingsItem;
import com.sorcerer.sorcery.iconpack.models.SettingsItem;
import com.sorcerer.sorcery.iconpack.util.PermissionsHelper;
import com.sorcerer.sorcery.iconpack.util.Utility;
import com.sorcerer.sorcery.iconpack.xposed.XposedUtils;
import com.sorcerer.sorcery.iconpack.xposed.theme.IconReplacementItem;
import com.sorcerer.sorcery.iconpack.xposed.theme.Util;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.Command;
import com.stericson.RootTools.execution.CommandCapture;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class LabActivity extends SlideInAndOutAppCompatActivity implements View.OnClickListener {

    public static final String SHARED_PREFERENCE_NAME = "SIP_XPOSED";
    private SharedPreferences mPrefs;
    private static final String TAG = "SIP/Lab";
    private Context mContext;
    private Boolean mActive;
    private Toolbar mToolbar;
    private TextView mXposedTitleTextView;
    private TextView mXposedStateTextView;
    private TextView mXposedContentTextView;
    private TextView mXposedAttentionTextView;
    private Button mXposedApplyButton;
    private Button mXposedCloseButton;
    private Button mXposedRefreshButton;
    private Button mXposedRebootButton;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab);
        mXposedTitleTextView = (TextView) findViewById(R.id.textView_lab_xposed_title);
        mXposedStateTextView = (TextView) findViewById(R.id.textView_lab_xposed_state);
        mXposedContentTextView = (TextView) findViewById(R.id.textView_lab_xposed_content);
        mXposedAttentionTextView = (TextView) findViewById(R.id.textView_lab_xposed_attention);
        mXposedApplyButton = (Button) findViewById(R.id.button_lab_xposed_apply);
        mXposedCloseButton = (Button) findViewById(R.id.button_lab_xposed_close);
        mXposedRefreshButton = (Button) findViewById(R.id.button_lab_xposed_refresh);
        mXposedRebootButton = (Button) findViewById(R.id.button_lab_xposed_reboot);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_universal);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mContext = this;
        mPrefs = getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_WORLD_READABLE);
        mActive = mPrefs.getBoolean("pref_global_load", false);

        mXposedContentTextView
                .setText(Utility.handleLongXmlString(getString(R.string.global_detail)));
        mXposedAttentionTextView
                .setText(Utility.handleLongXmlString(getString(R.string.global_attention)));

        mXposedApplyButton.setOnClickListener(this);
        mXposedCloseButton.setOnClickListener(this);
        mXposedRefreshButton.setOnClickListener(this);
        mXposedRebootButton.setOnClickListener(this);

        if (mActive) {
            mXposedStateTextView.setText(getString(R.string.global_state_active));
            mXposedStateTextView.setTextColor(getResources().getColor(R.color.green_500));
            mXposedApplyButton.setEnabled(false);
            mXposedRefreshButton.setEnabled(true);
            mXposedCloseButton.setEnabled(true);
            mXposedRebootButton.setEnabled(true);
        } else {
            if (RootTools.isRootAvailable()) {
                mXposedStateTextView.setText(getString(R.string.global_state_not_active));
                mXposedStateTextView.setTextColor(getResources().getColor(R.color.red_500));
                mXposedApplyButton.setEnabled(true);
                mXposedRefreshButton.setEnabled(false);
                mXposedCloseButton.setEnabled(false);
                mXposedRebootButton.setEnabled(false);
            } else {
                mXposedStateTextView.setText(getString(R.string.global_state_not_root));
                mXposedStateTextView.setTextColor(getResources().getColor(R.color.red_500));
                mXposedApplyButton.setEnabled(false);
                mXposedRefreshButton.setEnabled(false);
                mXposedCloseButton.setEnabled(false);
                mXposedRebootButton.setEnabled(false);
            }
        }
        if (Build.VERSION.SDK_INT >= 23) {
            PermissionsHelper.requestWriteExternalStorage(this);
        }
    }
/*
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshThemeList();
    }

    private void refreshThemeList() {
        if (!this.mIsRefreshingThemeList) {
            this.mIsRefreshingThemeList = true;
            this.mThemePackagesList.clear();
            this.mThemeList.clear();
            this.mThemePackages.clear();
            this.mThemeList.add("\u7cfb\u7edf\u9ed8\u8ba4");
            this.mThemePackages.add(null);
            new Thread(new Runnable() {
                public void run() {
                    final ArrayList<ApplicationInfo> themePackages = new ArrayList();
                    loadThemeListFor(themePackages,
                            "android.intent.action.MAIN",
                            "com.anddoes.launcher.THEME");
                    loadThemeListFor(themePackages,
                            "org.adw.launcher.THEMES",
                            "android.intent.category.DEFAULT");
                    loadThemeListFor(themePackages,
                            "android.intent.action.MAIN",
                            "com.fede.launcher.THEME_ICONPACK");
                    loadThemeListFor(themePackages,
                            "ginlemon.smartlauncher.THEMES",
                            "android.intent.category.DEFAULT");
                    loadThemeListFor(themePackages,
                            "com.gau.go.launcherex.theme",
                            "android.intent.category.DEFAULT");
                    runOnUiThread(new Runnable() {
                        public void run() {
                            mThemePackages.addAll(themePackages);
                            if (mThemeAdapter != null) {
                                mThemeAdapter.notifyDataSetChanged();
                            }
                            if (this.mReApplyTheme) {
                                this.mApplyBtn.callOnClick();
                                this.mReApplyTheme = false;
                            }
                            mIsRefreshingThemeList = false;
                            setSelectedTheme(mCurrentSetThemeIndex);
                        }
                    });
                }
            }).start();
        }
    }

    private void loadThemeListFor(ArrayList<ApplicationInfo> themePackages, String intentName,
                                  String intentCat) {
        PackageManager pm = getPackageManager();
        try {
            Intent intent = new Intent(intentName);
            intent.addCategory(intentCat);
            List<ResolveInfo> themes = pm.queryIntentActivities(intent, 0);
            for (int i = 0; i < themes.size(); i++) {
                try {
                    String packageName = ((ResolveInfo) themes.get(i)).activityInfo.packageName;
                    String themeName = ((ResolveInfo) themes.get(i)).loadLabel(pm).toString();
                    ApplicationInfo appInfo = pm.getApplicationInfo(packageName, PackageManager
                            .GET_META_DATA);
                    if (!this.mThemePackagesList.contains(packageName)) {
                        Resources themeRes =
                                getPackageManager().getResourcesForApplication(packageName);
                        if (themeRes.getIdentifier("appfilter", "xml", packageName) == 0) {
                            InputStream is = themeRes.getAssets().open("appfilter.xml");
                            if (is != null) {
                                is.close();
                            }
                        }
                        if (packageName.equals(this.mCurrentSetTheme)) {
                            themeName = themeName + " *";
                            this.mCurrentSetThemeIndex = this.mThemeList.size();
                        }
                        this.mThemePackagesList.add(packageName);
                        this.mThemeList.add(themeName);
                        themePackages.add(appInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
*/

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_lab_xposed_apply) {
            try {
                Process root = Runtime.getRuntime().exec("su");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!RootTools.isAccessGiven()) {
                Toast.makeText(mContext,
                        getString(R.string.global_state_not_root),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("opening...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
            tryAndApplyIcon(getApplicationInfo());
        } else if (id == R.id.button_lab_xposed_close) {
            mActive = false;
            mPrefs.edit().putBoolean("pref_global_load", false).commit();
            mXposedStateTextView.setText(getString(R.string.global_state_not_active));
            mXposedStateTextView.setTextColor(getResources().getColor(R.color.red_500));
            mXposedApplyButton.setEnabled(true);
            mXposedCloseButton.setEnabled(false);
            mXposedRefreshButton.setEnabled(false);
            mXposedRebootButton.setEnabled(false);
        } else if (id == R.id.button_lab_xposed_refresh) {
//            XposedUtils.killLauncher();
//            XposedUtils.clearNovaCache2(getPackageManager());
            XposedUtils.killAll(
                    (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        } else if (id == R.id.button_lab_xposed_reboot) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
            builder.title(getString(R.string.action_reboot) + "?");
            builder.positiveText(getString(R.string.yes));
            builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    XposedUtils.reboot();
                }
            });
            builder.negativeText(getString(R.string.no));
            builder.show();
        }
    }

    private void runAfterSuccess() {
        mActive = false;
        mPrefs.edit().putBoolean("pref_global_load", true).commit();
        mXposedStateTextView.setText(getString(R.string.global_state_active));
        mXposedStateTextView.setTextColor(getResources().getColor(R.color.green_500));
        mXposedApplyButton.setEnabled(false);
        mXposedCloseButton.setEnabled(true);
        mXposedRefreshButton.setEnabled(true);
        mXposedRebootButton.setEnabled(true);
        mProgressDialog.dismiss();
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
    }

    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23) {
            PermissionsHelper.requestWriteExternalStorage(this);
        }
    }

    private boolean appIsInstalledInMountASEC() {
        return getApplicationInfo().sourceDir.contains("asec/");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return false;
    }

    private boolean tryAndApplyIcon(final ApplicationInfo themePackage) {
        if (!RootTools.isAccessGiven()) {
            try {
                Toast.makeText(this, "acquiring root...", Toast.LENGTH_SHORT).show();
                RootTools.getShell(true).add(new CommandCapture(0, "echo Hello"));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        if (!RootTools.isAccessGiven()) {
            Toast.makeText(this, "need root access!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (RootTools.isAccessGiven()) {
            if (!new File(getCacheDir().getAbsolutePath() + "/icons").exists()) {
                try {
                    RootTools.getShell(true).add(new CommandCapture(0,
                            "mkdir " + getCacheDir().getAbsolutePath() + "/icons",
                            "chmod 777 " + getCacheDir().getAbsolutePath() + "/icons"));
                } catch
                        (Exception e22) {
                    e22.printStackTrace();
                }
            }
            if (!appIsInstalledInMountASEC()) {
                try {
                    Runtime.getRuntime()
                            .exec("rm " + getExternalCacheDir().getAbsolutePath() + "/tmp.apk");
                } catch (Exception e222) {
                    e222.printStackTrace();
                }
            }

            apply(themePackage);
            return true;
        }
        return false;
    }

    private void apply(final ApplicationInfo themePackage) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    XmlPullParser xrp;
                    XmlPullParser xrp2;
                    ArrayList<IconReplacementItem> items;
                    Resources origPkgRes;
                    SharedPreferences.Editor editor = mPrefs.edit();
                    Gson gson = new Gson();
                    ArrayList<String> mIconPackages = new ArrayList();
                    HashMap<String, ArrayList<IconReplacementItem>> mIconReplacementsHashMap =
                            new HashMap();
                    String themePackagePath = themePackage.sourceDir;
                    if (themePackage.sourceDir.contains("/data/app/")) {
                        Command tmp = RootTools.getShell(true).add(new CommandCapture(0,
                                "rm /data/data/" + getPackageName() +
                                        "/cache/icons/*",
                                "rm " + getExternalCacheDir()
                                        .getAbsolutePath() + "/current_theme.apk"));

                    } else {
                        Log.d(TAG,
                                "Original Theme APK is at " + themePackage.sourceDir);
                        Command commandCapture = new CommandCapture(0,
                                "rm /data/data/" + getPackageName() + "/cache/icons/*",
                                "rm " + getExternalCacheDir().getAbsolutePath() +
                                        "/current_theme.apk",
                                "cat \"" + themePackage.sourceDir + "\" > " +
                                        getExternalCacheDir().getAbsolutePath() +
                                        "/current_theme.apk",
                                "chmod 644 " + getExternalCacheDir()
                                        .getAbsolutePath() + "/current_theme.apk");
                        Command tmp = RootTools.getShell(true).add(commandCapture);

                        themePackagePath = getExternalCacheDir() +
                                "/current_theme.apk";
                        Log.d(TAG,
                                "Copied Theme APK is at " + themePackagePath);
                    }
                    PackageManager pm = getPackageManager();
                    Resources r = getPackageManager()
                            .getResourcesForApplication(themePackage.packageName);
                    if (r.getIdentifier("appfilter", "xml", themePackage.packageName) == 0) {
                        InputStream istr = r.getAssets().open("appfilter.xml");
                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        factory.setNamespaceAware(true);
                        xrp = factory.newPullParser();
                        xrp.setInput(istr, "UTF-8");
                        InputStream istr2 = r.getAssets().open("appfilter.xml");
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
                    for (Map.Entry<String, ?> entry : mPrefs.getAll()
                            .entrySet()) {
                        if (((String) entry.getKey()).contains("theme_icon_for_")) {
                            editor.remove((String) entry.getKey());
                        }
                    }
                    editor.commit();
                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay()
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
                                    item.getActivityName()), 128);
                            Log.d(TAG, "activity: " + item.getActivityName());
                            Log.d(TAG, "orig res name: " + item.getOrigResName());
                            Log.d(TAG, "component: " + item.getComponent());
                            Log.d(TAG, "orig res: " + item.getOrigRes());
                            Log.d(TAG, "replacement res: " + item.getReplacementRes());
                            Log.d(TAG, "replacement res name: " + item.getReplacementResName());
                            Log.d(TAG, "package: " + item.getPackageName());
                            if (activityInfo != null) {
                                if (mIconReplacementsHashMap.get(item.getPackageName()) ==
                                        null) {
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
                                    }
                                }
                                item.setOrigRes(activityInfo.getIconResource());

                                if (!items.contains(item)) {
                                    items.add(item);

                                    XposedUtils.cacheDrawable(item.getPackageName(),
                                            item.getOrigRes(),
                                            (BitmapDrawable) new BitmapDrawable(origPkgRes,
                                                    XposedUtils.getBitmapForDensity(r,
                                                            metrics.densityDpi,
                                                            item.getReplacementRes())));
                                }
                            }
                        } catch (Exception e2) {
                        }
                    }
                    editor.putString("theme_package_name", themePackage.packageName);
                    editor.putString("theme_package_path", themePackagePath);
                    for (Map.Entry<String, ArrayList<IconReplacementItem>> entry2 : mIconReplacementsHashMap
                            .entrySet()) {
                        mIconPackages.add(entry2.getKey());
                        editor.putString("theme_icon_for_" + ((String) entry2.getKey()),
                                gson.toJson(((ArrayList) entry2.getValue()).toArray()));
                    }
                    editor.putString("theme_icon_packages",
                            gson.toJson(mIconPackages.toArray()));
                    editor.commit();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            runAfterSuccess();
                        }
                    });
                } catch (Exception e6) {
                    e6.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(mContext, "Failed to apply icons!", Toast.LENGTH_SHORT)
                                    .show();
//                            tryAndApplyIcon(null);
                        }
                    });
                }
            }
        }).start();
    }
}

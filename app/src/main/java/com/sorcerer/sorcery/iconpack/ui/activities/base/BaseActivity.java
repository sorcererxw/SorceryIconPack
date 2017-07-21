package com.sorcerer.sorcery.iconpack.ui.activities.base;

import android.app.Activity;
import android.app.ActivityManager.TaskDescription;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.sorcerer.sorcery.iconpack.App;
import com.sorcerer.sorcery.iconpack.SorceryPrefs;
import com.sorcerer.sorcery.iconpack.data.db.Db;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import javax.inject.Inject;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/5/28 0028
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Inject
    protected SorceryPrefs mPrefs;
    @Inject
    protected Db mDb;

    protected Context mContext = this;
    protected Activity mActivity = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        App.getInstance().getAppComponent().inject(this);
        if (mPrefs.nightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        hookBeforeSuperOnCreate();
        super.onCreate(savedInstanceState);
        hookBeforeSetContentView();
        if (provideLayoutId() != 0) {
            setContentView(provideLayoutId());
            ButterKnife.bind(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            PackageManager pm = getPackageManager();
            ActivityInfo activityInfo = pm.getActivityInfo(
                    getComponentName(), PackageManager.GET_META_DATA);
            TaskDescription taskDescription = new TaskDescription(
                    activityInfo.loadLabel(pm).toString(),
                    BitmapFactory.decodeResource(getResources(), activityInfo.icon),
                    ResourceUtil.getAttrColor(this, android.R.attr.colorPrimary)
            );
            setTaskDescription(taskDescription);
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected abstract int provideLayoutId();

    protected void hookBeforeSetContentView() {

    }

    protected void hookBeforeSuperOnCreate() {

    }
}
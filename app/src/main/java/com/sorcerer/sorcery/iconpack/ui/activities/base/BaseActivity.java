package com.sorcerer.sorcery.iconpack.ui.activities.base;

import android.app.Activity;
import android.app.ActivityManager.TaskDescription;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.avos.avoscloud.AVAnalytics;
import com.sorcerer.sorcery.iconpack.App;
import com.sorcerer.sorcery.iconpack.SorceryPrefs;
import com.sorcerer.sorcery.iconpack.ui.theme.ThemeManager;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/5/28 0028
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Inject
    protected SorceryPrefs mPrefs;
    @Inject
    protected ThemeManager mThemeManager;
    protected Context mContext = this;
    protected Activity mActivity = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        App.getInstance().getAppComponent().inject(this);
        mThemeManager.setTheme(this);
        super.onCreate(savedInstanceState);
        hookBeforeSetContentView();
        if (provideLayoutId() != 0) {
            setContentView(provideLayoutId());
            ButterKnife.bind(this);
        }
        if (rootView() != null) {
            rootView().setBackground(new ColorDrawable(
                    ResourceUtil.getAttrColor(this, android.R.attr.colorBackground)));
        }
        init(savedInstanceState);
    }

    public void resetContentView() {
        mThemeManager.setTheme(this);
        hookBeforeSetContentView();
        if (provideLayoutId() != 0) {
            setContentView(provideLayoutId());
            ButterKnife.bind(this);
        }
        if (rootView() != null) {
            rootView().setBackground(new ColorDrawable(
                    ResourceUtil.getAttrColor(this, android.R.attr.colorBackground)));
        }
        init(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);

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
            e.printStackTrace();
        }

    }

    protected abstract ViewGroup rootView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected abstract int provideLayoutId();

    protected abstract void init(Bundle savedInstanceState);

    protected void hookBeforeSetContentView() {

    }
}
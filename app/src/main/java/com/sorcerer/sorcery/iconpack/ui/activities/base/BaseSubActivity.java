package com.sorcerer.sorcery.iconpack.ui.activities.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;

import com.jaeger.library.StatusBarUtil;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.sorcerer.sorcery.iconpack.App;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.SorceryPrefs;
import com.sorcerer.sorcery.iconpack.data.db.Db;
import com.sorcerer.sorcery.iconpack.ui.others.ToolbarOnGestureListener;
import com.sorcerer.sorcery.iconpack.ui.theme.ThemeManager;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/7
 */

public abstract class BaseSubActivity extends BaseSwipeBackActivity {

    @BindView(R.id.toolbar_universal)
    protected Toolbar mToolbar;
    @Inject
    protected SorceryPrefs mPrefs;
    @Inject
    protected ThemeManager mThemeManager;
    @Inject
    protected Db mDb;
    protected Context mContext = this;
    protected Activity mActivity = this;

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        App.getInstance().getAppComponent().inject(this);
        mThemeManager.setTheme(this);
        super.onCreate(savedInstanceState);
        if (provideLayoutId() != 0) {
            setContentView(provideLayoutId());
            ButterKnife.bind(this);
        }
//        if (rootView() != null) {
//            rootView().setBackground(new ColorDrawable(
//                    ResourceUtil.getAttrColor(this, android.R.attr.colorBackground)));
//        }
        setSupportActionBar(this.getToolbar());

        getToolbar().setBackground(
                new ColorDrawable(ResourceUtil.getAttrColor(this, android.R.attr.colorPrimary)));

        StatusBarUtil.setColorForSwipeBack(this,
                ResourceUtil.getAttrColor(this, android.R.attr.colorPrimaryDark), 0);

//        init(savedInstanceState);
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
            ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(
                    activityInfo.loadLabel(pm).toString(),
                    BitmapFactory.decodeResource(getResources(), activityInfo.icon),
                    ResourceUtil.getAttrColor(this, android.R.attr.colorPrimary)
            );
            setTaskDescription(taskDescription);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

//    protected abstract ViewGroup rootView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected abstract int provideLayoutId();

//    protected void init(Bundle savedInstanceState) {
//
//    }

    protected void setToolbarCloseIndicator() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(
                    new IconicsDrawable(this, GoogleMaterial.Icon.gmd_close)
                            .sizeDp(24)
                            .paddingDp(4)
                            .color(Color.WHITE));
        }
    }

    protected void setToolbarBackIndicator() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void setToolbarDoubleTapListener(
            ToolbarOnGestureListener.DoubleTapListener listener) {
        final GestureDetector detector = new GestureDetector(
                this,
                new ToolbarOnGestureListener(listener)
        );
        this.getToolbar().setOnTouchListener((v, event) -> {
            detector.onTouchEvent(event);
            return true;
        });
    }

    @Override
    public void finish() {

        super.finish();

        Boolean lessAnim = mPrefs.lessAnim().get();
        if (lessAnim != null && lessAnim) {
            overridePendingTransition(0, 0);
        } else {
            overridePendingTransition(0, R.anim.slide_right_out);
        }
    }

}

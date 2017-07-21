package com.sorcerer.sorcery.iconpack.ui.activities.base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;

import com.jaeger.library.StatusBarUtil;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.callbacks.ToolbarOnGestureListener;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import butterknife.BindView;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/7
 */

public abstract class BaseSubActivity extends BaseSwipeBackActivity {

    @BindView(R.id.toolbar_universal)
    protected Toolbar mToolbar;

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(this.getToolbar());

        getToolbar().setBackground(
                new ColorDrawable(ResourceUtil.getAttrColor(this, android.R.attr.colorPrimary)));

        StatusBarUtil.setColorForSwipeBack(this,
                ResourceUtil.getAttrColor(this, android.R.attr.colorPrimaryDark), 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected abstract int provideLayoutId();

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

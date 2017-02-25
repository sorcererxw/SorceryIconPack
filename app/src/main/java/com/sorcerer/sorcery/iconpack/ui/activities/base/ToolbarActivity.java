package com.sorcerer.sorcery.iconpack.ui.activities.base;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.sorcerer.sorcery.iconpack.ui.others.ToolbarOnGestureListener;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/5/30 0030
 */
public abstract class ToolbarActivity extends BaseActivity {
    protected abstract Toolbar getToolbar();

    @Override
    protected void init() {
        assert this.getToolbar() != null;
        setSupportActionBar(this.getToolbar());
    }

    protected void setToolbarText(String s) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(s);
        }
    }

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
}

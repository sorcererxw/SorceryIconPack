package com.sorcerer.sorcery.iconpack.ui.activities.base;

import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.others.ToolbarOnGestureListener;

/**
 * Created by Sorcerer on 2016/5/30 0030.
 */
public abstract class ToolbarActivity extends BaseActivity {
    protected abstract Toolbar provideToolbar();

    @Override
    protected void init() {
        assert provideToolbar() != null;
        setSupportActionBar(provideToolbar());
    }

    protected void setToolbarText(String s) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(s);
        }
    }

    protected Toolbar getToolbar() {
        return provideToolbar();
    }

    protected void setToolbarCloseIndicator() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
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
        provideToolbar().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });
    }
}

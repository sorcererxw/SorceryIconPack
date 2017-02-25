package com.sorcerer.sorcery.iconpack.ui.activities.base;

import android.support.v7.widget.Toolbar;

import com.sorcerer.sorcery.iconpack.R;

import butterknife.BindView;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/6/2 0002
 */
public abstract class UniversalToolbarActivity extends ToolbarActivity {
    @BindView(R.id.toolbar_universal)
    protected Toolbar mToolbar;

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }
}

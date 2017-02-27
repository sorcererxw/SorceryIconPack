package com.sorcerer.sorcery.iconpack.settings;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseFragmentSubActivity;

import butterknife.BindView;

public class SettingsActivity extends BaseFragmentSubActivity {

    @BindView(R.id.frameLayout_settings_fragment_container)
    FrameLayout mFragmentContainer;

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    protected int provideFragmentContainer() {
        return R.id.frameLayout_settings_fragment_container;
    }

    @Override
    protected Fragment provideInitFragment() {
        return SettingsFragment.newInstance();
    }

    @Override
    protected void init() {
        super.init();
        setToolbarBackIndicator();
    }
}

package com.sorcerer.sorcery.iconpack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.sorcerer.sorcery.iconpack.settings.SettingsFragment;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseFragmentSubActivity;

public class SettingsActivity extends BaseFragmentSubActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarBackIndicator();
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
    protected int provideLayoutId() {
        return R.layout.activity_settings;
    }
}

package com.sorcerer.sorcery.iconpack.ui.activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.activities.base.SlideInAndOutAppCompatActivity;

public class SettingsActivity extends SlideInAndOutAppCompatActivity {

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void init() {
        super.init();
        setToolbarBackIndicator();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return false;
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference);
        }
    }
}

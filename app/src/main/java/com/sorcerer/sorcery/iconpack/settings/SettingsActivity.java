package com.sorcerer.sorcery.iconpack.settings;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseSubActivity;

import butterknife.BindView;

public class SettingsActivity extends BaseSubActivity {

    @BindView(R.id.frameLayout_settings_fragment_container)
    FrameLayout mFragmentContiner;

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void init() {
        super.init();
        setToolbarBackIndicator();

        addFragment(SettingsFragment.newInstance());
    }

    public void addFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_settings_fragment_container, fragment)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }
}

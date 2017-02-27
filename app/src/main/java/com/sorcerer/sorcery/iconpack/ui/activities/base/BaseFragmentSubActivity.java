package com.sorcerer.sorcery.iconpack.ui.activities.base;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/27
 */

public abstract class BaseFragmentSubActivity extends BaseSubActivity {
    protected abstract int provideFragmentContainer();

    protected abstract Fragment provideInitFragment();

    public void addFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(provideFragmentContainer(), fragment)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();
    }

    @Override
    protected void init() {
        super.init();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            addFragment(provideInitFragment());
        }
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

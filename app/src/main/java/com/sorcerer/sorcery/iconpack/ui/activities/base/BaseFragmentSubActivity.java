package com.sorcerer.sorcery.iconpack.ui.activities.base;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.fragments.IOnBackFragment;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/27
 */

public abstract class BaseFragmentSubActivity extends BaseSubActivity {
    protected abstract int provideFragmentContainer();

    protected abstract Fragment provideInitFragment();

    protected abstract CoordinatorLayout provideRootView();

    public CoordinatorLayout getRootView() {
        return provideRootView();
    }

    public void addFragment(@NonNull Fragment fragment) {
        addFragment(fragment, true);
    }

    public void addFragment(@NonNull Fragment fragment, boolean anim) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (anim) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                    R.anim.slide_in_left, R.anim.slide_out_right);
        }
        transaction
                .replace(provideFragmentContainer(), fragment)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();
    }

    @Override
    protected void init() {
        super.init();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            addFragment(provideInitFragment(), false);
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
            Fragment currentFragment =
                    getSupportFragmentManager().findFragmentById(provideFragmentContainer());
            if (currentFragment != null && currentFragment instanceof IOnBackFragment) {
                ((IOnBackFragment) currentFragment).onBackPress();
            }
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }
}

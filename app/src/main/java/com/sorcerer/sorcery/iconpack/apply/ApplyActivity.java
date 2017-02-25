package com.sorcerer.sorcery.iconpack.apply;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.apply.ui.OverviewFragment;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseSubActivity;

import butterknife.BindView;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/2
 */

public class ApplyActivity extends BaseSubActivity {

    @BindView(R.id.frameLayout_apply_fragment_container)
    FrameLayout mFragmentContainer;

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_apply;
    }

    @Override
    protected void init() {
        super.init();

        setToolbarBackIndicator();

        addFragment(OverviewFragment.newInstance());
    }

    public void setTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public void addFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_apply_fragment_container, fragment)
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
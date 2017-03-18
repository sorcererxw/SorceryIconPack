package com.sorcerer.sorcery.iconpack.customWorkshop;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseSubActivity;

import butterknife.BindView;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/18
 */

public class CustomWorkshopActivity extends BaseSubActivity {
    @BindView(R.id.coordinatorLayout_custom)
    CoordinatorLayout mCoordinatorLayout;

    @Override
    protected ViewGroup rootView() {
        return mCoordinatorLayout;
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_custom_workshop;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

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
}

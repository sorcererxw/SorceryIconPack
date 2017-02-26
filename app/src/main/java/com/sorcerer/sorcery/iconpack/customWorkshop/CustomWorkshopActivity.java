package com.sorcerer.sorcery.iconpack.customWorkshop;

import android.view.MenuItem;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseSubActivity;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/18
 */

public class CustomWorkshopActivity extends BaseSubActivity {
    @Override
    protected int provideLayoutId() {
        return R.layout.activity_custom_workshop;
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
}

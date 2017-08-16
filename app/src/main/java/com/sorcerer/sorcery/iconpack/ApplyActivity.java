package com.sorcerer.sorcery.iconpack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.sorcerer.sorcery.iconpack.apply.OverviewFragment;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseFragmentSubActivity;

/**
 * @description: *
 * @author: Sorcerer
 * *
 * @date: 2016/12/2
 */

public class ApplyActivity extends BaseFragmentSubActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarBackIndicator();
    }

    @Override
    protected int provideFragmentContainer() {
        return R.id.frameLayout_apply_fragment_container;
    }

    @Override
    protected Fragment provideInitFragment() {
        return new OverviewFragment();
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_apply;
    }
}
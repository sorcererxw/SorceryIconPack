package com.sorcerer.sorcery.iconpack.ui.activities.base;

import com.mikepenz.materialize.MaterializeBuilder;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/7
 */

public abstract class BaseSubActivity extends SlideInAndOutAppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        new MaterializeBuilder(this)
                .withTransparentStatusBar(true)
                .withTintedStatusBar(false)
                .withTranslucentStatusBarProgrammatically(true)
//                .withTintedStatusBar(true)
//                .withStatusBarColor(ResourceUtil.getColor(this, R.color.primary_dark))
                .build();
    }
}

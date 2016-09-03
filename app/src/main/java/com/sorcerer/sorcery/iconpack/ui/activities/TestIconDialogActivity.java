package com.sorcerer.sorcery.iconpack.ui.activities;

import android.support.v4.view.ViewPager;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseActivity;
import com.sorcerer.sorcery.iconpack.ui.adapters.IconDialogPagerAdapter;

import butterknife.BindView;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/7/29
 */
public class TestIconDialogActivity extends BaseActivity {
    @BindView(R.id.viewPager_test_icon)
    ViewPager mViewPager;

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_test_icon_dialog;
    }

    public static final String EXTRA_INDEX = "EXTRA_INDEX";
    public static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";

    @Override
    protected void init() {
        IconDialogPagerAdapter adapter = new IconDialogPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(getIntent().getIntExtra(EXTRA_INDEX, 0));

    }
}

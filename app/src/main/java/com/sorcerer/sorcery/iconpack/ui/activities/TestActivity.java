package com.sorcerer.sorcery.iconpack.ui.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseActivity;
import com.sorcerer.sorcery.iconpack.ui.fragments.IconFragment;
import com.sorcerer.sorcery.iconpack.util.ResourceUtil;

import butterknife.BindView;

public class TestActivity extends BaseActivity {

    @BindView(R.id.materialViewPager)
    MaterialViewPager mMaterialViewPager;

    private Toolbar mToolbar;

    private IconFragment.Flag[] mFlags = IconFragment.Flag.values();

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void init() {
        mToolbar = mMaterialViewPager.getToolbar();
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        final String[] name = getResources().getStringArray(R.array.tab_name);

        mMaterialViewPager.getViewPager()
                .setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                    @Override
                    public Fragment getItem(int position) {
                        return IconFragment.newInstance(mFlags[position], false);
                    }

                    @Override
                    public int getCount() {
                        return mFlags.length;
                    }

                    @Override
                    public CharSequence getPageTitle(int position) {
                        return name[position];
                    }
                });
        mMaterialViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                if (page % 2 == 0) {
                    return HeaderDesign.fromColorAndDrawable(
                            ResourceUtil.getColor(mContext, R.color.primary),
                            ResourceUtil.getDrawable(mContext, R.drawable.sandy_shore)
                    );
                } else {
                    return HeaderDesign.fromColorAndDrawable(
                            ResourceUtil.getColor(mContext, R.color.primary),
                            ResourceUtil.getDrawable(mContext, R.drawable.gnarly_90s)
                    );
                }
            }
        });
    }
}

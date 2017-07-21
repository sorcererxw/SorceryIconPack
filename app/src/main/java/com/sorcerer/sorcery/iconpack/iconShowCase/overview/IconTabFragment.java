package com.sorcerer.sorcery.iconpack.iconShowCase.overview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.materialize.util.UIUtils;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.fragments.BaseFragment;
import com.sorcerer.sorcery.iconpack.ui.views.DoubleTapTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/3/15
 */

public class IconTabFragment extends BaseFragment {
    private static final String KEY_CUSTOM_PICKER = "CUSTOM_PICKER";
    @BindView(R.id.viewPager_icon)
    ViewPager mViewPager;
    @BindView(R.id.tabLayout_icon)
    DoubleTapTabLayout mTabLayout;
    private boolean mCustomPicker;
    private IconViewPageAdapter mPageAdapter;
    private ViewPager.OnPageChangeListener mPageChangeListener =
            new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    mPageAdapter.getItem(mViewPager.getCurrentItem()).onSelected();
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    mPageAdapter.getItem(mViewPager.getCurrentItem()).onStateChange(state);
                }
            };

    public static IconTabFragment newInstance(boolean customPicker) {
        IconTabFragment iconTabFragment = new IconTabFragment();
        Bundle args = new Bundle();
        args.putBoolean(KEY_CUSTOM_PICKER, customPicker);
        iconTabFragment.setArguments(args);
        return iconTabFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCustomPicker = getArguments().getBoolean(KEY_CUSTOM_PICKER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_icon_tab, container, false);
        ButterKnife.bind(this, view);
        initTabAndPager();
        mTabLayout.setTabTextColors(UIUtils.adjustAlpha(Color.WHITE, 204), Color.WHITE);
        return view;
    }

    private void initTabAndPager() {
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mViewPager.setPageMargin((int) UIUtils.convertDpToPixel(16, getContext()));

        mPageAdapter = new IconViewPageAdapter(
                getContext(), getChildFragmentManager(), mCustomPicker);

        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setPageMargin((int) UIUtils.convertDpToPixel(-4, getContext()));

        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.setOnTabDoubleTapListener(() -> {
            int index = mViewPager.getCurrentItem();
            IconViewPageAdapter adapter = (IconViewPageAdapter) mViewPager.getAdapter();
            IconFragment fragment = adapter.getItem(index);
            if (fragment.getRecyclerView() != null) {
                fragment.getRecyclerView().smoothScrollToPosition(0);
            }
        });
    }

    public TabLayout getTabLayout() {
        return mTabLayout;
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

}

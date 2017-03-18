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
import com.sorcerer.sorcery.iconpack.utils.DisplayUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/3/15
 */

public class IconTabFragment extends BaseFragment {
    private static final String KEY_CUSTOM_PICKER = "CUSTOM_PICKER";

    public static IconTabFragment newInstance(boolean customPicker) {
        IconTabFragment iconTabFragment = new IconTabFragment();
        Bundle args = new Bundle();
        args.putBoolean(KEY_CUSTOM_PICKER, customPicker);
        iconTabFragment.setArguments(args);
        return iconTabFragment;
    }

    private boolean mCustomPicker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCustomPicker = getArguments().getBoolean(KEY_CUSTOM_PICKER);
    }

    @BindView(R.id.viewPager_icon)
    ViewPager mViewPager;

    @BindView(R.id.tabLayout_icon)
    DoubleTapTabLayout mTabLayout;

    private IconViewPageAdapter mPageAdapter;

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

    private void initTabAndPager() {
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mViewPager.setPageMargin(DisplayUtil.dip2px(getContext(), 16));

        mPageAdapter = new IconViewPageAdapter(getContext(), getFragmentManager(), mCustomPicker);

        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setPageMargin(DisplayUtil.dip2px(getContext(), -4));

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
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

package com.sorcerer.sorcery.iconpack.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.ArrayMap;

import com.sorcerer.sorcery.iconpack.ui.fragments.LazyIconFragment;
import com.sorcerer.sorcery.iconpack.utils.Prefs.SorceryPrefs;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/1/19 0019
 */
public class ViewPageAdapter extends FragmentPagerAdapter {

    private List<LazyIconFragment.Flag> mFlagList;

    private Map<String, Fragment> mFragmentMap = new ArrayMap<>();

    private Context mContext;

    private SorceryPrefs mPrefs;

    private boolean mCustomPicker;

    public ViewPageAdapter(Context context, FragmentManager fm, boolean customPicker) {
        super(fm);
        mContext = context;
        mCustomPicker = customPicker;
        mPrefs = SorceryPrefs.getInstance(context);
        updateFlags();
    }

    private void updateFlags() {
        List<LazyIconFragment.Flag> flagList = new ArrayList<>();
        LazyIconFragment.Flag[] flags = LazyIconFragment.Flag.values();
        for (LazyIconFragment.Flag flag : flags) {
            if (mPrefs.isTabShow(flag.name().toLowerCase()).getValue()) {
                flagList.add(flag);
            }
        }
        mFlagList = flagList;
    }

    @Override
    public void notifyDataSetChanged() {
        updateFlags();
        super.notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        LazyIconFragment.Flag flag = mFlagList.get(position);
        if (mFragmentMap.containsKey(flag.name())) {
            return mFragmentMap.get(flag.name());
        }
        Fragment fragment = LazyIconFragment.newInstance(flag, mCustomPicker);
        mFragmentMap.put(flag.name(), fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return mFlagList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ResourceUtil.getStringFromResString(mContext,
                "tab_" + mFlagList.get(position).name().toLowerCase());
    }
}

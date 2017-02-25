package com.sorcerer.sorcery.iconpack.iconShowCase.overview;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.ArrayMap;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.sorcerer.sorcery.iconpack.settings.prefs.SorceryPrefs;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/1/19 0019
 */
public class IconViewPageAdapter extends FragmentPagerAdapter {

    private List<IconFlag> mFlagList;

    private Map<String, IconFragment> mFragmentMap = new ArrayMap<>();

    private Context mContext;

    private SorceryPrefs mPrefs;

    private boolean mCustomPicker;

    public IconViewPageAdapter(Context context, FragmentManager fm, boolean customPicker) {
        super(fm);
        mContext = context;
        mCustomPicker = customPicker;
        mPrefs = SorceryPrefs.getInstance(context);
        updateFlags();
    }

    private void updateFlags() {
        mFlagList = Stream.of(IconFlag.values())
                .filter(flag -> mPrefs.isTabShow(flag.name().toLowerCase()).getValue())
                .collect(Collectors.toList());
    }

    @Override
    public void notifyDataSetChanged() {
        updateFlags();
        super.notifyDataSetChanged();
    }

    @Override
    public IconFragment getItem(int position) {
        IconFlag flag = mFlagList.get(position);
        if (mFragmentMap.containsKey(flag.name())) {
            return mFragmentMap.get(flag.name());
        }
        IconFragment fragment = IconFragment.newInstance(flag, mCustomPicker);
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

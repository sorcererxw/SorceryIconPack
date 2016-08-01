package com.sorcerer.sorcery.iconpack.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sorcerer on 2016/7/29.
 */
public class IconDialogPagerAdapter extends FragmentPagerAdapter {

    private List<String> mIconNameList = new ArrayList<>();

    public IconDialogPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}

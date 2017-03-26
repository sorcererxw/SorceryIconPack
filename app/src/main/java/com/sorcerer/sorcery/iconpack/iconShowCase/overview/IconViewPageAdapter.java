package com.sorcerer.sorcery.iconpack.iconShowCase.overview;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.ArrayMap;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.sorcerer.sorcery.iconpack.App;
import com.sorcerer.sorcery.iconpack.SorceryPrefs;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static io.reactivex.schedulers.Schedulers.newThread;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/1/19 0019
 */
public class IconViewPageAdapter extends FragmentStatePagerAdapter {

    private List<IconFlag> mFlagList = new ArrayList<>();

    private Map<String, IconFragment> mFragmentMap = new ArrayMap<>();

    private Context mContext;

    @Inject
    protected SorceryPrefs mPrefs;

    private boolean mCustomPicker;

    IconViewPageAdapter(Context context, FragmentManager fm, boolean customPicker) {
        super(fm);
        App.getInstance().getAppComponent().inject(this);

        mContext = context;
        mCustomPicker = customPicker;

        Stream.of(IconFlag.values())
                .forEach(flag -> mPrefs.isTabShow(flag.name().toLowerCase()).asObservable()
                        .subscribeOn(newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(show -> {
                            if (show) {
                                addPage(flag);
                            } else {
                                removePage(flag);
                            }
                        }, Timber::e));
    }

    private void addPage(IconFlag flag) {
        if (mFlagList.contains(flag)) {
            return;
        }
        mFlagList.add(flag);
        notifyDataSetChanged();
    }

    private void addPages(IconFlag[] flags) {
        mFlagList.addAll(Stream.of(flags)
                .filter(flag -> !mFlagList.contains(flag))
                .collect(Collectors.toList()));
        notifyDataSetChanged();
    }

    private void removePage(IconFlag flag) {
        if (!mFlagList.contains(flag)) {
            return;
        }
        mFlagList.remove(flag);
        notifyDataSetChanged();
    }

    private void removePages(IconFlag[] flags) {
        mFlagList.removeAll(Stream.of(flags)
                .filter(flag -> mFlagList.contains(flag))
                .collect(Collectors.toList()));
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        Collections.sort(mFlagList, new IconFlagComparator());
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

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}

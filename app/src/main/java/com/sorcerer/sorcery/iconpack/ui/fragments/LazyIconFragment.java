package com.sorcerer.sorcery.iconpack.ui.fragments;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.data.models.AppfilterItem;
import com.sorcerer.sorcery.iconpack.data.models.IconBean;
import com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter.IconAdapter;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/10
 */
public class LazyIconFragment extends LazyFragment {

    public enum Flag {
        NEW,
        INSTALLED,
        ALL,
        ALI,
        BAIDU,
        NETEASE,
        GOOGLE,
        //        HTC,
//        LENOVO,
//        LG,
//        MOTO,
        MICROSOFT,
        SAMSUNG,
        SONY,
        TENCENT,
        XIAOMI,
        FLYME,
        ONEPLUS
    }

    private List<IconBean> mIconBeanList;

    @BindView(R.id.recyclerView_icon_gird)
    public RecyclerView mGridView;

    public static LazyIconFragment newInstance(Flag flag, boolean customPicker) {
        LazyIconFragment fragment = new LazyIconFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(mArgFlagKey, flag);
        bundle.putBoolean(mArgCustomPickerKey, customPicker);
        fragment.setArguments(bundle);
        return fragment;
    }

    private static final String mArgFlagKey = "flag";
    private static final String mArgCustomPickerKey = "custom picker";

    private IconAdapter mIconAdapter;
    private GridLayoutManager mGridLayoutManager;

    private boolean mNeedResize = false;

    @Override
    protected void onCreateViewLazy(Bundle savedInstance) {

        View view = View.inflate(getContext(), R.layout.fragment_icon, null);
        ButterKnife.bind(this, view);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        final int h_screen = dm.heightPixels;
        mGridView.setTranslationY(h_screen);
        mGridView.setAlpha(0);

        setContentView(view);
        if (mIconBeanList == null) {
            getIconBeanList((Flag) getArguments().get(mArgFlagKey));
        } else {
            init();
        }
    }

    private void init() {
        calcNumOfRows();

        mGridLayoutManager = new GridLayoutManager(getContext(), mNumOfRows);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mIconAdapter.isItemHead(position) ? mNumOfRows : 1;
            }
        });
        mGridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mGridLayoutManager.scrollToPosition(0);

        final RequestManager rm = Glide.with(this);
        mIconAdapter = new IconAdapter(getActivity(), getContext(), mIconBeanList, mNumOfRows, rm,
                mGridView, mGridLayoutManager);

        boolean customPicker = getArguments().getBoolean(mArgCustomPickerKey, false);

        mGridView.setLayoutManager(mGridLayoutManager);
        mGridView.setHasFixedSize(true);

        if (customPicker) {
            mIconAdapter.setCustomPicker(mHoldingActivity, true);
        }
        mGridView.setAdapter(mIconAdapter);
        if (mNeedResize) {
            resize();
        }
        DisplayMetrics dm = getResources().getDisplayMetrics();
        final int h_screen = dm.heightPixels;
        mGridView.setTranslationY(h_screen);
        mGridView.setAlpha(0);
        mGridView.animate()
                .alpha(1)
                .translationY(0)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(350)
                .start();
    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        resize();
    }

    private void resize() {
        if (mIconBeanList != null) {
            mNeedResize = false;
            calcNumOfRows();
            mGridLayoutManager.setSpanCount(mNumOfRows);
            mIconAdapter.setSpan(mNumOfRows);
        } else {
            mNeedResize = true;
        }
    }

    private int mNumOfRows;

    private void calcNumOfRows() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float iconMargin = getResources().getDimension(R.dimen.icon_grid_item_margin);
        float cardMargin = getResources().getDimension(R.dimen.icon_grid_card_margin);
        float iconSize = getResources().getDimension(R.dimen.icon_grid_item_size) + 2 * iconMargin;
        mNumOfRows = (int) ((size.x - 2 * cardMargin - 2 * iconMargin) / iconSize);
    }

    public RecyclerView getRecyclerView() {
        return mGridView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        resize();
    }

    private void getIconBeanList(Flag flag) {
        Observable.just(flag).map(fg -> Stream.of(getIconNames(getContext(), fg))
                .map(name -> {
                    IconBean iconBean = new IconBean(name);
                    if (name.startsWith("**")) {
                        return iconBean;
                    }
                    int res = getResources()
                            .getIdentifier(name, "drawable",
                                    getContext().getPackageName());
                    if (res != 0) {
                        iconBean.setRes(res);
                        return iconBean;
                    } else {
                        Timber.e("thumb = 0: %s", name);
                        return null;
                    }
                })
                .filter(value -> value != null)
                .collect(Collectors.toList()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    mIconBeanList = list;
                    init();
                }, Timber::e);
    }


    private String[] getIconNames(Context context, Flag flag) {
        if (flag == Flag.INSTALLED) {
            List<String> list = new ArrayList<>();
            List<AppfilterItem> afList = PackageUtil.getAppfilterList(context);
            List<ResolveInfo> installedList = PackageUtil.getInstallApps(context);
            List<String> allIconList =
                    Arrays.asList(ResourceUtil.getStringArray(context, "icon_pack"));
            Collections.sort(allIconList);

            for (ResolveInfo ri : installedList) {
                String comp = ri.activityInfo.packageName + "/" + ri.activityInfo.name;
                for (int i = 0; i < afList.size(); i++) {
                    AppfilterItem ai = afList.get(i);
                    if (comp.equals(ai.getComponent())) {
                        String drawable = ai.getDrawable();
                        list.add(drawable);
                        int index = allIconList.indexOf(drawable);
                        if (index < 0) {
                            break;
                        }
                        for (int j = index + 1; j < allIconList.size(); j++) {
                            if (allIconList.get(j).startsWith(drawable + "_alt") ||
                                    (drawable.endsWith("calendar")
                                            && allIconList.get(j).startsWith(drawable))) {
                                list.add(allIconList.get(j));
                            } else {
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            Collections.sort(list);
            String[] res = new String[list.size()];
            res = list.toArray(res);
            return res;
        }
        if (flag == Flag.ALL) {
            return ResourceUtil.getStringArray(context, "icon_pack");
        }
        String[] iconNames;
        iconNames = ResourceUtil.getStringArray(
                context,
                "icon_pack_" + flag.toString().toLowerCase()
        );
        return iconNames;
    }

}
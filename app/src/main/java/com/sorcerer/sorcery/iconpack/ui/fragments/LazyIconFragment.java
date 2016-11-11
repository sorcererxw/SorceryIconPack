package com.sorcerer.sorcery.iconpack.ui.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.socks.library.KLog;
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.IconBean;
import com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter.IconAdapter;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/10
 */
public class LazyIconFragment extends LazyFragment {

    public enum Flag {
        NEW,
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
//        SAMSUNG,
//        SONY,
        TENCENT,
//        MIUI,
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
        setContentView(view);

        ButterKnife.bind(this, view);

        if (mIconBeanList == null) {
            getIconBeanList((Flag) getArguments().get(mArgFlagKey));
        } else {
            init();
        }
    }

    private void init() {
        calcNumOfRows();
        mIconAdapter = new IconAdapter(getActivity(), getContext(), mIconBeanList, mNumOfRows);

        boolean customPicker = getArguments().getBoolean(mArgCustomPickerKey, false);
        mGridLayoutManager = new GridLayoutManager(getContext(), mNumOfRows);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mIconAdapter.isItemHead(position) ? mNumOfRows : 1;
            }
        });
        mGridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mGridLayoutManager.scrollToPosition(0);

        mGridView.setLayoutManager(mGridLayoutManager);
        mGridView.setHasFixedSize(true);
        mGridView.setItemAnimator(new DefaultItemAnimator());
        mGridView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                       RecyclerView.State state) {
                outRect.set(0, 0, 0, 0);
            }
        });

        if (customPicker) {
            mIconAdapter.setCustomPicker(mHoldingActivity, true);
        }
        mGridView.setAdapter(mIconAdapter);
        if (mNeedResize) {
            resize();
        }
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int h_screen = dm.heightPixels;
        mGridView.setTranslationY(h_screen);
        mGridView.setAlpha(0);
        mGridView.animate()
                .alpha(1)
                .translationY(0)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(500)
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
        float s = getResources().getDimension(R.dimen.icon_grid_item_size);
        mNumOfRows = (int) (size.x / s);
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
        Observable.just(flag).map(new Func1<Flag, List<IconBean>>() {
            @Override
            public List<IconBean> call(Flag flag) {
                List<IconBean> list = new ArrayList<>();
                for (String name : getIconNames(getContext(), flag)) {
                    IconBean iconBean = new IconBean(name);
                    int res = getResources()
                            .getIdentifier(name, "drawable", getContext().getPackageName());
                    if (res != 0) {
                        final int thumbRes =
                                getResources().getIdentifier(name, "drawable",
                                        getContext().getPackageName());
                        if (thumbRes != 0) {
                            iconBean.setRes(thumbRes);
                        } else {
                            KLog.d(TAG, "thumb = 0: " + name);
                        }
                    }
                    list.add(iconBean);
                }
                return list;
            }

            private String[] getIconNames(Context context, Flag flag) {
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
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<IconBean>>() {
                    @Override
                    public void call(List<IconBean> list) {
                        mIconBeanList = list;
                        init();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (BuildConfig.DEBUG) {
                            throwable.printStackTrace();
                        }
                    }
                });
    }
}

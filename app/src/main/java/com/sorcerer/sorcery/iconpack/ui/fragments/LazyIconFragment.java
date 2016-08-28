package com.sorcerer.sorcery.iconpack.ui.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.IconBean;
import com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter.IconAdapter;
import com.sorcerer.sorcery.iconpack.ui.views.IconRecyclerView;
import com.sorcerer.sorcery.iconpack.util.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Sorcerer on 2016/8/10.
 */
public class LazyIconFragment extends LazyFragment {

    public enum Flag {
        NEW,
        ALL,
        ALI,
        BAIDU,
        CYANOGENMOD,
        GOOGLE,
        HTC,
        LENOVO,
        LG,
        MOTO,
        MICROSOFT,
        SAMSUNG,
        SONY,
        TENCENT,
        MIUI,
        FLYME,
    }

    private List<IconBean> mIconBeanList;

    private IconRecyclerView mGridView;

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
        setContentView(R.layout.fragment_icon);

        if (mIconBeanList == null) {
            getIconBeanList((Flag) getArguments().get(mArgFlagKey));
        } else {
            init();
        }
    }

    private void init() {
        calcNumOfRows();
        boolean customPicker = getArguments().getBoolean(mArgCustomPickerKey, false);
        mGridLayoutManager = new GridLayoutManager(getContext(), mNumOfRows);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mIconBeanList.get(position).getName().charAt(0) == '*' ? mNumOfRows : 1;
            }
        });
        mGridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mGridLayoutManager.scrollToPosition(0);

        mGridView = (IconRecyclerView) findViewById(R.id.recyclerView_icon_gird);
        mGridView.setLayoutManager(mGridLayoutManager);
        mGridView.setHasFixedSize(true);
        mGridView.setItemAnimator(new DefaultItemAnimator());

        ((TextView) findViewById(R.id.textView_icon_list_empty_view_icon)).setTypeface(
                Typeface.createFromAsset(getContext().getAssets(), "empty_icon_list.ttf"));
        findViewById(R.id.linearLayout_icon_list_empty_view).setVisibility(View.VISIBLE);
        mGridView.setEmptyView(findViewById(R.id.linearLayout_icon_list_empty_view));

        mIconAdapter = new IconAdapter(getActivity(), getContext(), mIconBeanList, mGridView);

        if (customPicker) {
            mIconAdapter.setCustomPicker(mHoldingActivity, true);
        }
        mGridView.setAdapter(mIconAdapter);
        if (mNeedResize) {
            resize();
        }
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

    public void showWithString(String s) {
        s = s.toLowerCase();
        if (mGridLayoutManager != null) {
            if (s.isEmpty()) {
                mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return mIconBeanList.get(position).getName().charAt(0) == '*'
                                ? mNumOfRows : 1;
                    }
                });
            } else {
                mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return 1;
                    }
                });
            }
        }

        if (mIconAdapter != null) {
            mIconAdapter.showWithString(s);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        resize();
    }

    private void getIconBeanList(Flag flag) {
        Observable.just(flag)
                .map(new Func1<Flag, List<IconBean>>() {
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
                                    Log.d(TAG, "thumb = 0: " + name);
                                }
                            } else {
                                Log.d(TAG, "res = 0: " + name);
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
                });
    }
}

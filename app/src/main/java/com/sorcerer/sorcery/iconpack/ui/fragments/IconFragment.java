package com.sorcerer.sorcery.iconpack.ui.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.IconBean;
import com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter.IconAdapter;
import com.sorcerer.sorcery.iconpack.ui.views.IconRecyclerView;
import com.sorcerer.sorcery.iconpack.util.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class IconFragment extends BaseFragment {

    private int maxCol = 100;

    public enum Flag {
        NEW,
        ALL,
        ALI,
        CYANOGENMOD,
        BAIDU,
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

//    public static final int FLAG_NEW = 1;
//    public static final int FLAG_ALL = 0;
//    public static final int FLAG_ALI = 9;
//    public static final int FLAG_CYANOGENMOD = 14;
//    public static final int FLAG_BAIDU = 8;
//    public static final int FLAG_GOOGLE = 10;
//    public static final int FLAG_HTC = 2;
//    public static final int FLAG_LENOVO = 3;
//    public static final int FLAG_LG = 13;
//    public static final int FLAG_MOTO = 12;
//    public static final int FLAG_MICROSOFT = 11;
//    public static final int FLAG_SAMSUNG = 5;
//    public static final int FLAG_SONY = 6;
//    public static final int FLAG_TENCENT = 7;
//    public static final int FLAG_MIUI = 4;
//    public static final int FLAG_FLYME = 15;

    private List<IconBean> mIconBeanList;

    @BindView(R.id.recyclerView_icon_gird)
    IconRecyclerView mGridView;

    @BindView(R.id.textView_icon_list_empty_view_icon)
    TextView mEmptyIconTextView;

    @BindView(R.id.linearLayout_icon_list_empty_view)
    View mEmptyView;

    public static IconFragment newInstance(Flag flag, boolean customPicker) {
        IconFragment fragment = new IconFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(mArgFlagKey, flag);
        bundle.putBoolean(mArgCustomPickerKey, customPicker);
        fragment.setArguments(bundle);
        return fragment;
    }

    private static String mArgFlagKey = "flag";
    private static String mArgCustomPickerKey = "custom picker";

    private IconAdapter mIconAdapter;
    private GridLayoutManager mGridLayoutManager;

    @Override
    protected void init() {
        Flag flag = (Flag) getArguments().get(mArgFlagKey);
        boolean customPicker = getArguments().getBoolean(mArgCustomPickerKey, false);

        if (mIconBeanList == null) {
            mIconBeanList = getIconBeanList(mContext, flag);
        }
        mGridLayoutManager = new GridLayoutManager(getContext(), calcNumOfRows());
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mIconBeanList.get(position).getName().charAt(0) == '*' ? calcNumOfRows() : 1;
            }
        });
        mGridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mGridLayoutManager.scrollToPosition(0);
        mGridView.setLayoutManager(mGridLayoutManager);
        mGridView.setHasFixedSize(true);
        mGridView.setItemAnimator(new DefaultItemAnimator());

        mEmptyIconTextView.setTypeface(
                Typeface.createFromAsset(getContext().getAssets(), "empty_icon_list.ttf"));
        mGridView.setEmptyView(mEmptyView);

        mIconAdapter = new IconAdapter(getActivity(), getContext(), mIconBeanList, mGridView);

        if (customPicker) {
            mIconAdapter.setCustomPicker(mHoldingActivity, customPicker);
        }
        mGridView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        mGridView.setAdapter(mIconAdapter);
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.fragment_icon;
    }

    @Override
    public void onResume() {
        super.onResume();
        resize();
    }

    private void resize() {
        mGridLayoutManager.setSpanCount(calcNumOfRows());
    }

    private int calcNumOfRows() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float s = getResources().getDimension(R.dimen.icon_grid_item_size);
//                + 2 * getResources().getDimension(R.dimen.icon_grid_item_margin);

        int res = (int) (size.x / s);
        return res > maxCol ? maxCol : res;
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
                                ? calcNumOfRows() : 1;
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

    private List<IconBean> getIconBeanList(Context context, Flag flag) {
        List<IconBean> list = new ArrayList<>();

        for (String name : getIconNames(context, flag)) {
            IconBean iconBean = new IconBean(name);
            int res = context.getResources()
                    .getIdentifier(name, "drawable", context.getPackageName());
            if (res != 0) {
                final int thumbRes = context.getResources().getIdentifier(name, "drawable",
                        context.getPackageName());
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
        String[] iconNames;
        iconNames =
                ResourceUtil.getStringArray(
                        context,
                        "icon_pack_" + flag.toString().toLowerCase()
                );
        return iconNames;
    }
}

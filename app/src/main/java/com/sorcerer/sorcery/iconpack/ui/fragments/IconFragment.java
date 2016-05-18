package com.sorcerer.sorcery.iconpack.ui.fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.adapters.IconAdapter;
import com.sorcerer.sorcery.iconpack.databinding.FragmentIconBinding;
import com.sorcerer.sorcery.iconpack.models.IconBean;
import com.sorcerer.sorcery.iconpack.ui.views.IconRecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class IconFragment extends Fragment {
    private static final String TAG = "IconFragment";

    private int maxCol = 8;

    private Activity mParentActivity;
    private boolean mCustomPicker = false;
    private int mFlag;
    public static final int FLAG_NEW = 1;
    public static final int FLAG_ALL = 0;
    public static final int FLAG_ALI = 9;
    public static final int FLAG_CYANOGENMOD = 14;
    public static final int FLAG_BAIDU = 8;
    public static final int FLAG_GOOGLE = 10;
    public static final int FLAG_HTC = 2;
    public static final int FLAG_LENOVO = 3;
    public static final int FLAG_LG = 13;
    public static final int FLAG_MOTO = 12;
    public static final int FLAG_MICROSOFT = 11;
    public static final int FLAG_SAMSUNG = 5;
    public static final int FLAG_SONY = 6;
    public static final int FLAG_TENCENT = 7;
    public static final int FLAG_MIUI = 4;
    public static final int FLAG_FLYME = 15;

    private List<IconBean> mIconBeanList;

    private IconRecyclerView mGridView;
    private IconAdapter mIconAdapter;
    private SearchListener mSearchListener;
    private GridLayoutManager mGridLayoutManager;

    public interface SearchListener {
        void onSearch();
    }

    public IconFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        int arg = getArguments().getInt("flag", 0);
        if (mIconBeanList == null) {
            mIconBeanList = getIconBeanList(getResources(),
                    getContext().getPackageName(),
                    arg);
        }

        View view = inflater.inflate(R.layout.fragment_icon, container, false);
        FragmentIconBinding binding = DataBindingUtil.bind(view);

        mGridView = binding.recyclerViewIconGird;
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

        TextView emptyView = binding.textViewIconListEmptyViewIcon;
        emptyView.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "empty_icon_list.ttf"));
        mGridView.setEmptyView(binding.linearLayoutIconListEmptyView);

        mIconAdapter =
                new IconAdapter(getActivity(),
                        getContext(),
                        mIconBeanList, mGridView);

        if (mCustomPicker) {
            mIconAdapter.setCustomPicker(mParentActivity, mCustomPicker);
        }
        mGridView.setOnPauseListenerParams(ImageLoader.getInstance(), false, false);
        mGridView.setAdapter(mIconAdapter);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        resize();
    }

    private int calcNumOfRows() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float s = getResources().getDimension(R.dimen.icon_grid_item_size) + getResources()
                .getDimension(R.dimen.icon_grid_item_margin);

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
                        return mIconBeanList.get(position).getName().charAt(0) == '*' ?
                                calcNumOfRows() : 1;
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

    public void setSearchListener(@NonNull final SearchListener searchListener) {
        mSearchListener = searchListener;
    }

    public void setCustomPicker(Activity activity, boolean customPicker) {
        mParentActivity = activity;
        mCustomPicker = customPicker;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        resize();
    }


    private void resize() {
        mGridLayoutManager.setSpanCount(calcNumOfRows());
    }

    private List<IconBean> getIconBeanList(Resources resources, String packageName, int flag) {
        List<IconBean> list = new ArrayList<>();

        for (String name : getIconNames(resources, flag)) {
            IconBean iconBean = new IconBean(name);
            int res = resources.getIdentifier(name, "drawable", packageName);
            if (res != 0) {
                final int thumbRes = resources.getIdentifier(name, "drawable", packageName);
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

    private String[] getIconNames(Resources resources, int flag) {
        String[] mIconNames;

        switch (flag) {
            case IconFragment.FLAG_ALL:
                mIconNames = resources.getStringArray(R.array.icon_pack);
                break;
            case IconFragment.FLAG_NEW:
                mIconNames = resources.getStringArray(R.array.icon_pack_new);
                break;
            case IconFragment.FLAG_ALI:
                mIconNames = resources.getStringArray(R.array.icon_pack_ali);
                break;
            case IconFragment.FLAG_BAIDU:
                mIconNames = resources.getStringArray(R.array.icon_pack_baidu);
                break;
            case IconFragment.FLAG_CYANOGENMOD:
                mIconNames = resources.getStringArray(R.array.icon_pack_cyanogenmod);
                break;
            case IconFragment.FLAG_GOOGLE:
                mIconNames = resources.getStringArray(R.array.icon_pack_google);
                break;
            case IconFragment.FLAG_HTC:
                mIconNames = resources.getStringArray(R.array.icon_pack_htc);
                break;
            case IconFragment.FLAG_LENOVO:
                mIconNames = resources.getStringArray(R.array.icon_pack_lenovo);
                break;
            case IconFragment.FLAG_LG:
                mIconNames = resources.getStringArray(R.array.icon_pack_lg);
                break;
            case IconFragment.FLAG_MICROSOFT:
                mIconNames = resources.getStringArray(R.array.icon_pack_microsoft);
                break;
            case IconFragment.FLAG_MOTO:
                mIconNames = resources.getStringArray(R.array.icon_pack_moto);
                break;
            case IconFragment.FLAG_SAMSUNG:
                mIconNames = resources.getStringArray(R.array.icon_pack_samsung);
                break;
            case IconFragment.FLAG_SONY:
                mIconNames = resources.getStringArray(R.array.icon_pack_sony);
                break;
            case IconFragment.FLAG_TENCENT:
                mIconNames = resources.getStringArray(R.array.icon_pack_tencent);
                break;
            case IconFragment.FLAG_MIUI:
                mIconNames = resources.getStringArray(R.array.icon_pack_miui);
                break;
            case IconFragment.FLAG_FLYME:
                mIconNames = resources.getStringArray(R.array.icon_pack_flyme);
                break;
            default:
                mIconNames = new String[]{""};
        }

        return mIconNames;
    }
}

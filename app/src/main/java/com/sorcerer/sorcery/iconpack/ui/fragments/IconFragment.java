package com.sorcerer.sorcery.iconpack.ui.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.adapters.IconAdapter;

public class IconFragment extends Fragment {

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
    public static final int FLAG_XIAOMI = 4;

    private RecyclerView mGridView;

    public IconFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_icon, container, false);
        mGridView = (RecyclerView) view.findViewById(R.id.recyclerView_icon_gird);
        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(), calcNumOfRows());
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        mGridView.setLayoutManager(layoutManager);
        mGridView.setHasFixedSize(true);
        mGridView.setAdapter(new IconAdapter(view.getContext(), getArguments().getInt("flag", 0)));
        return view;
    }

    private int calcNumOfRows() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float s = getResources().getDimension(R.dimen.icon_grid_item_size) + getResources()
                .getDimension(R.dimen.icon_grid_item_margin);
        return (int) (size.x / s);
    }

    public RecyclerView getRecyclerView(){
        return mGridView;
    }
}

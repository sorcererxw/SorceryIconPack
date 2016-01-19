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

    public IconFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_icon, container, false);
        RecyclerView gridView = (RecyclerView) view.findViewById(R.id.recyclerView_icon_gird);
        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(), calcNumOfRows());
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        gridView.setLayoutManager(layoutManager);
        gridView.setHasFixedSize(true);
        gridView.setAdapter(new IconAdapter(view.getContext()));
        return view;
    }

    private int calcNumOfRows() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return (int) (size.x / getResources().getDimension(R.dimen.icon_grid_item_size));
    }
}

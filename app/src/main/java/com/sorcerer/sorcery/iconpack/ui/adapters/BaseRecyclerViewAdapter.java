package com.sorcerer.sorcery.iconpack.ui.adapters;

import android.support.v7.widget.RecyclerView;

import com.sorcerer.sorcery.iconpack.App;
import com.sorcerer.sorcery.iconpack.SorceryPrefs;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/3/7
 */

public abstract class BaseRecyclerViewAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    protected SorceryPrefs mPrefs;

    public BaseRecyclerViewAdapter() {
        mPrefs = App.getInstance().prefs();
    }
}

package com.sorcerer.sorcery.iconpack.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sorcerer.sorcery.iconpack.App;
import com.sorcerer.sorcery.iconpack.SorceryPrefs;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/3/7
 */

public abstract class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {
    protected SorceryPrefs mPrefs;

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        mPrefs = App.getInstance().prefs();
    }
}

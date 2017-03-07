package com.sorcerer.sorcery.iconpack.ui.adapters.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sorcerer.sorcery.iconpack.App;
import com.sorcerer.sorcery.iconpack.SorceryPrefs;

import javax.inject.Inject;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/3/7
 */

public abstract class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {
    @Inject
    protected SorceryPrefs mPrefs;

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        ((App) itemView.getContext().getApplicationContext()).getAppComponent().inject(this);
    }
}

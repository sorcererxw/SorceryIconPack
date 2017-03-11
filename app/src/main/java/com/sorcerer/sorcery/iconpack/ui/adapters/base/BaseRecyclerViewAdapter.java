package com.sorcerer.sorcery.iconpack.ui.adapters.base;

import android.support.v7.widget.RecyclerView;

import com.sorcerer.sorcery.iconpack.App;
import com.sorcerer.sorcery.iconpack.SorceryPrefs;

import javax.inject.Inject;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/3/7
 */

public abstract class BaseRecyclerViewAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    protected SorceryPrefs mPrefs;

    public BaseRecyclerViewAdapter() {
        mPrefs = new InjectorHelper().getPrefs();
    }

    public static class InjectorHelper {
        @Inject
        protected SorceryPrefs mPrefs;

        private InjectorHelper() {
            App.getInstance().getAppComponent().inject(this);
        }

        private SorceryPrefs getPrefs() {
            return mPrefs;
        }
    }
}

package com.sorcerer.sorcery.iconpack.ui.adapters.base;

import android.support.v7.widget.RecyclerView;

import com.sorcerer.sorcery.iconpack.App;
import com.sorcerer.sorcery.iconpack.SorceryPrefs;
import com.sorcerer.sorcery.iconpack.ui.theme.ThemeManager;

import javax.inject.Inject;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/3/7
 */

public abstract class BaseRecyclerViewAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    protected SorceryPrefs mPrefs;
    protected ThemeManager mThemeManager;

    public BaseRecyclerViewAdapter() {
        InjectorHelper injectorHelper = new InjectorHelper();
        mPrefs = injectorHelper.getPrefs();
        mThemeManager = injectorHelper.getThemeManager();
    }

    public static class InjectorHelper {
        @Inject
        protected SorceryPrefs mPrefs;

        @Inject
        protected ThemeManager mThemeManager;

        private InjectorHelper() {
            App.getInstance().getAppComponent().inject(this);
        }

        private SorceryPrefs getPrefs() {
            return mPrefs;
        }

        private ThemeManager getThemeManager() {
            return mThemeManager;
        }
    }
}

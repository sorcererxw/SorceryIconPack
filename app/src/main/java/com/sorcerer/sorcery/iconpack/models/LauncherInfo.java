package com.sorcerer.sorcery.iconpack.models;

import android.graphics.drawable.Drawable;

/**
 * Created by Sorcerer on 2016/1/24 0024.
 */
public class LauncherInfo {
    private boolean mIsInstalled;
    private String mLabel;
    private Drawable mIcon;

    public LauncherInfo(boolean isInstalled, String label, Drawable icon) {
        mIcon = icon;
        mIsInstalled = isInstalled;
        mLabel = label;
    }

    public boolean isInstalled() {
        return mIsInstalled;
    }

    public void setInstalled(boolean installed) {
        mIsInstalled = installed;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public void setIcon(Drawable icon) {
        mIcon = icon;
    }
}

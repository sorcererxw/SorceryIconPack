package com.sorcerer.sorcery.iconpack.models;

import android.graphics.drawable.Drawable;

/**
 * Created by Sorcerer on 2016/1/21 0021.
 */
public class AppInfo {
    String mCode = null;
    String mName = null;
    Drawable mIcon = null;
    String mPackage;
    boolean mHasCustomIcon = false;

    public AppInfo(String code, String name, Drawable icon) {
        mCode = code;
        mName = name;
        mIcon = icon;
        mPackage = code.split("/")[0];
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
        mPackage = code.split("/")[0];
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public void setIcon(Drawable icon) {
        mIcon = icon;
    }

    public String getPackage() {
        return mPackage;
    }

    public boolean isHasCustomIcon() {
        return mHasCustomIcon;
    }

    public void setHasCustomIcon(boolean hasCustomIcon) {
        mHasCustomIcon = hasCustomIcon;
    }

    @Override
    public String toString() {
        return "AppInfo{"
                + "mCode='" + mCode + '\''
                + ", mName='" + mName + '\''
                + ", mIcon=" + mIcon
                + ", mPackage='" + mPackage + '\''
                + ", mHasCustomIcon=" + mHasCustomIcon
                + '}';
    }
}
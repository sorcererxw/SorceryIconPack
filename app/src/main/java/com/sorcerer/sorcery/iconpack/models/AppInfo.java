package com.sorcerer.sorcery.iconpack.models;

import android.graphics.drawable.Drawable;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/1/21 0021
 */
public class AppInfo {
    public AppInfo(){
    }

    public int getRequestedTimes() {
        return mRequestedTimes;
    }

    public void setRequestedTimes(int requestedTimes) {
        mRequestedTimes = requestedTimes;
    }

    private int mRequestedTimes = -1;
    private String mCode = null;
    private String mName = null;
    private Drawable mIcon = null;
    private String mPackage;
    private boolean mHasCustomIcon = false;

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
package com.sorcerer.sorcery.iconpack.models;

import android.graphics.drawable.Drawable;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/12
 */
public class PermissionBean {
    private String mTitle;
    private String mDescribe;
    private Drawable mDrawable;

    public PermissionBean(String title, String describe, Drawable drawable) {
        mTitle = title;
        mDescribe = describe;
        mDrawable = drawable;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescribe() {
        return mDescribe;
    }

    public void setDescribe(String describe) {
        mDescribe = describe;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
    }
}

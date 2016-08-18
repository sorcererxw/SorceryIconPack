package com.sorcerer.sorcery.iconpack.models;

/**
 * Created by Sorcerer on 2016/8/12.
 */
public class PermissionBean {
    private String mTitle;
    private String mDescribe;
    private int mResId;

    public PermissionBean(String title, String describe, int resId) {
        mTitle = title;
        mDescribe = describe;
        mResId = resId;
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

    public int getResId() {
        return mResId;
    }

    public void setResId(int resId) {
        mResId = resId;
    }
}

package com.sorcerer.sorcery.iconpack.models;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/29
 */

public class AppfilterItem {
    private String mComponent;
    private String mDrawable;

    public AppfilterItem(String component, String drawable) {
        mComponent = component;
        mDrawable = drawable;
    }

    public String getComponent() {
        return mComponent;
    }

    public void setComponent(String component) {
        mComponent = component;
    }

    public String getDrawable() {
        return mDrawable;
    }

    public void setDrawable(String drawable) {
        mDrawable = drawable;
    }

    public String getPackageName() {
        return mComponent.split("/")[0];
    }
}

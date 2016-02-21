package com.sorcerer.sorcery.iconpack.models;

/**
 * Created by Sorcerer on 2016/2/11 0011.
 */
public class ComponentBean {
    private String mComponent;
    private String mDrawable;
    private String mPrefix;
    private boolean isCalendar;
    private String mPackageName;
    private String mActivityName;

    public ComponentBean() {
        isCalendar = false;
    }

    public String getDrawable() {
        return mDrawable;
    }

    public void setDrawable(String drawable) {
        mDrawable = drawable;
    }

    public String getComponent() {
        return mComponent;
    }

    public void setComponent(String component) {
        mComponent = component;
        String[] tmp = component.substring(14, component.length() - 1).split("/");
        mPackageName = tmp[0];
        mActivityName = tmp[1];
    }

    public String getPrefix() {
        return mPrefix;
    }

    public void setPrefix(String prefix) {
        mPrefix = prefix;
    }

    public boolean isCalendar() {
        return isCalendar;
    }

    public void setCalendar(boolean calendar) {
        isCalendar = calendar;
    }

    public String getActivityName() {
        return mActivityName;
    }

    public String getPackageName() {
        return mPackageName;
    }
}

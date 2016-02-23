package com.sorcerer.sorcery.iconpack.models;

import android.graphics.drawable.Drawable;

/**
 * Created by Sorcerer on 2016/1/21 0021.
 */
public class AppInfo {
    String code = null;
    String name = null;
    Drawable icon = null;
    boolean hasCustomIcon = false;

    public AppInfo(String paramString1, String paramString2, Drawable paramDrawable) {
        code = paramString1;
        name = paramString2;
        icon = paramDrawable;
    }

    public void setHasCustomIcon(boolean paramBoolean) {
        hasCustomIcon = paramBoolean;
    }

    public boolean isHasCustomIcon() {
        return hasCustomIcon;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setCode(String paramString) {
        code = paramString;
    }

    public void setName(String paramString) {
        name = paramString;
    }

    public String toString() {
        return name + "\n" + code + "\n";
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
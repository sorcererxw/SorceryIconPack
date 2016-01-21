package com.sorcerer.sorcery.iconpack.models;

import android.graphics.drawable.Drawable;

/**
 * Created by Sorcerer on 2016/1/21 0021.
 */
public class AppInfo {
    String code = null;
    String name = null;

    public AppInfo(String paramString1, String paramString2) {
        code = paramString1;
        name = paramString2;
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
}
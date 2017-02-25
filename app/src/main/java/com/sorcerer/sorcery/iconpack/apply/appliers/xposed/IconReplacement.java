package com.sorcerer.sorcery.iconpack.apply.appliers.xposed;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/9
 */

public class IconReplacement {

    static IconReplacement EMPTY() {
        return new IconReplacement("", "", 0, "");
    }

    boolean isEmpty() {
        return TextUtils.isEmpty(component)
                && TextUtils.isEmpty(packageName);
    }

    @SerializedName("component")
    private String component;
    @SerializedName("originRes")
    private int originRes;
    @SerializedName("packageName")
    private String packageName;
    @SerializedName("replacementRes")
    private int replacementRes;
    @SerializedName("replacementResName")
    private String replacementResName;

    public IconReplacement(String packageName,
                           String component,
                           int replacementRes,
                           String replacementResName) {
        this.component = component;
        this.packageName = packageName;
        this.replacementRes = replacementRes;
        this.replacementResName = replacementResName;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public int getOriginRes() {
        return originRes;
    }

    public void setOriginRes(int originRes) {
        this.originRes = originRes;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getReplacementRes() {
        return replacementRes;
    }

    public void setReplacementRes(int replacementRes) {
        this.replacementRes = replacementRes;
    }

    public String getReplacementResName() {
        return replacementResName;
    }

    public void setReplacementResName(String replacementResName) {
        this.replacementResName = replacementResName;
    }
}

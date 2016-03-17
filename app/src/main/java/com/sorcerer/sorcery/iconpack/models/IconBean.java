package com.sorcerer.sorcery.iconpack.models;

/**
 * Created by Sorcerer on 2016/2/29 0029.
 */
public class IconBean {
    private String mName;
    private String mLabel;
    private int mRes;
    private boolean mShown;

    public IconBean(String name) {
        mName = name;
        mLabel = handleIconName(name);
        mShown = true;
    }

    public IconBean(String name, int res) {
        mName = name;
        mLabel = handleIconName(name);
        mShown = true;
        mRes = res;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public int getRes() {
        return mRes;
    }

    public void setRes(int res) {
        mRes = res;
    }

    public static String handleIconName(String orgin) {
        String res;
        if (Character.isDigit(orgin.charAt(1)) && orgin.charAt(0)=='a') {
            res = orgin.substring(1, orgin.length());
        } else {
            res = orgin.substring(0, orgin.length());
        }
        return res.replaceAll("_", " ");
    }

    public boolean isShown() {
        return mShown;
    }

    public void setShown(boolean shown) {
        mShown = shown;
    }
}

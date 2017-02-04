package com.sorcerer.sorcery.iconpack.data.models;

import android.content.Context;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/2/29 0029
 */
public class IconBean {
    private String mName;
    private String mLabel;
    private int mRes;

    public IconBean(String name) {
        mName = name;
        mLabel = handleIconName(name);
    }

    public IconBean(String name, int res) {
        mName = name;
        mLabel = handleIconName(name);
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

    public static String handleIconName(String origin) {
        String res;
        if (Character.isDigit(origin.charAt(1)) && origin.charAt(0) == 'a') {
            res = origin.substring(1, origin.length());
        } else {
            res = origin.substring(0, origin.length());
        }
        return res.replaceAll("_", " ");
    }

    public static IconBean fromDrawableName(Context context, String name) {
        IconBean iconBean = new IconBean(name);
        int res = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        if (res != 0) {
            int thumbRes = context.getResources()
                    .getIdentifier(name, "drawable", context.getPackageName());
            if (thumbRes != 0) {
                iconBean.setRes(thumbRes);
            }
        }
        return iconBean;
    }
}

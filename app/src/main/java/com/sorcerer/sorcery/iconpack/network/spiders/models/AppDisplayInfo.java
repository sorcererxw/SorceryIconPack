package com.sorcerer.sorcery.iconpack.network.spiders.models;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/8
 */

public class AppDisplayInfo {
    private String mAppName = null;
    private String mIconUrl = null;
    private Drawable mIcon = null;

    public AppDisplayInfo() {
    }

    public boolean needAddition() {
        return TextUtils.isEmpty(mAppName) ||
                (mIcon == null && TextUtils.isEmpty(mIconUrl));
    }

    public void add(AppDisplayInfo info) {
        if (info == null) {
            return;
        }
        if (TextUtils.isEmpty(mAppName)) {
            if (!TextUtils.isEmpty(info.getAppName())) {
                mAppName = info.getAppName();
            }
        }
        if (TextUtils.isEmpty(mIconUrl)) {
            if (!TextUtils.isEmpty(info.getIconUrl())) {
                mIconUrl = info.getIconUrl();
            }
        }
        if (mIcon == null && info.getIcon() != null) {
            mIcon = info.getIcon();
        }
    }

    public String getAppName() {
        return mAppName;
    }

    public void setAppName(String appName) {
        mAppName = appName;
    }

    public String getIconUrl() {
        return mIconUrl;
    }

    public void setIconUrl(String iconUrl) {
        mIconUrl = iconUrl;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public void setIcon(Drawable icon) {
        mIcon = icon;
    }
}

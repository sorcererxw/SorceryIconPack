package com.sorcerer.sorcery.iconpack.models;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.Log;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.utils.AppInfoUtil;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/1/24 0024
 */
public class LauncherInfo implements Comparable {
    private boolean mIsInstalled;
    private String mLabel;
    private int mIcon;
    private String mPackageName;

    private final String TAG = "LauncherInfo";

    public LauncherInfo(Context context, String packageName, String label) {
        mPackageName = packageName;
        mIsInstalled = AppInfoUtil.isLauncherInstalled(context, packageName);
        mLabel = label;
        setIcon(context);
    }

    public boolean isInstalled() {
        return mIsInstalled;
    }

    public void setInstalled(boolean installed) {
        mIsInstalled = installed;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(@DrawableRes int icon) {
        mIcon = icon;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String packageName) {
        mPackageName = packageName;
    }

    private void setIcon(Context context) {
        switch (mLabel.toLowerCase()) {
            case "action":
                mIcon = R.drawable.action_launcher;
                break;
            case "nova":
                mIcon = R.drawable.nova_launcher;
                break;
            case "apex":
                mIcon = R.drawable.apex_launcher;
                break;
            case "miui":
                mIcon = R.drawable.xiaomi_miui;
                break;
            case "flyme":
                mIcon = R.drawable.flyme;
                break;
            case "unicon":
                mIcon = R.drawable.unicon;
                break;
            case "aviate":
                mIcon = R.drawable.aviate_launcher;
                break;
            default:
                Log.d(TAG, mLabel + ": no such launcher");
        }
    }

    @Override
    public int compareTo(Object another) {
        if (another instanceof LauncherInfo) {
            if (this.isInstalled() != ((LauncherInfo) another).isInstalled()) {
                return this.isInstalled() ? -1 : 1;
            } else {
                return getLabel().compareTo(((LauncherInfo) another).getLabel());
            }
        }
        return 0;
    }
}

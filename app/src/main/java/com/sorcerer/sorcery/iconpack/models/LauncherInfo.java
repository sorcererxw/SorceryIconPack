package com.sorcerer.sorcery.iconpack.models;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.Log;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.util.AppInfoUtil;

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
        switch (mLabel) {
            case "Action":
                Log.d(TAG, mLabel + "found");
//                mIcon = R.drawable.action_launcher;
                break;
            case "ADW":
                mIcon = R.drawable.adw_launcher;
                break;
            case "ADW EX":

                mIcon = R.drawable.adwex_launcher;
                break;
            case "Apex":
                mIcon = R.drawable.apex_launcher;
                break;
            case "Atom":
                mIcon = R.drawable.atom_launcher;
                break;
            case "Aviate":
                mIcon = R.drawable.aviate_launcher;
                break;
            case "CM Theme Engine":
                break;
            case "Go":
                mIcon = R.drawable.go_launcher;
                break;
            case "Google Now":
                break;
            case "Holo":
                mIcon = R.drawable.holo_launcher;
                break;
            case "Holo ICS":
                break;
            case "KK":
                mIcon = R.drawable.kk_launcher;
                break;
            case "L":
                break;
            case "LG Home":
                break;
            case "Lucid":
                mIcon = R.drawable.lucid_launcher;
                break;
            case "Mini":
                break;
            case "Next":
                mIcon = R.drawable.next_launcher;
                break;
            case "Nova":
                mIcon = R.drawable.nova_launcher;
                break;
            case "S":
                break;
            case "Smart":
                mIcon = R.drawable.smart_launcher;
                break;
            case "Smart Pro":
                mIcon = R.drawable.smart_launcher;
                break;
            case "Solo":
                mIcon = R.drawable.solo_launcher;
                break;
            case "TSF":
                mIcon = R.drawable.tsf_launcher;
                break;
            case "Unicon":
                mIcon = R.drawable.unicon;
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

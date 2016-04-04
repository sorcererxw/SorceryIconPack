package com.sorcerer.sorcery.iconpack.models;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.Log;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.util.Utility;

/**
 * Created by Sorcerer on 2016/1/24 0024.
 */
public class LauncherInfo implements Comparable {
    private boolean mIsInstalled;
    private String mLabel;
    private int mIcon;
    private String mPackageName;

    public LauncherInfo(Context context, String packageName, String label) {
        mPackageName = packageName;
        mIsInstalled = Utility.isLauncherInstalled(context, packageName);
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
        String label = mLabel.toLowerCase().split(" ")[0];
        switch (label) {
            case "action":
                mIcon = R.drawable.action_launcher;
                break;
            case "apex":
                mIcon = R.drawable.apex_launcher;
                break;
            case "adwex":
                mIcon = R.drawable.adwex_launcher;
                break;
            case "adw":
                switch (mLabel.toLowerCase().split(" ")[1]) {
                    case "ex":
                        mIcon = R.drawable.adwex_launcher;
                        break;
                    default:
                        mIcon = R.drawable.adw_launcher;
                }
                break;
            case "atom":
                mIcon = R.drawable.atom_launcher;
                break;
            case "aviate":
                mIcon = R.drawable.aviate_launcher;
                break;
            case "cm":
                mIcon = R.drawable.cyanogenmod_theme_engine;
                break;
            case "epic":
                break;
            case "go":
                mIcon = R.drawable.go_launcher;
                break;
            case "holohd":
                break;
            case "holo":
                mIcon = R.drawable.holo_launcher;
                break;
            case "inspire":
                mIcon = R.drawable.inspire_launcher;
                break;
            case "kk":
                mIcon = R.drawable.kk_launcher;
                break;
            case "lghome":
                break;
            case "l":
                break;
            case "lucid":
                mIcon = R.drawable.lucid_launcher;
                break;
            case "nine":
                mIcon = R.drawable.nine_launcher;
                break;
            case "mini":
                break;
            case "nemus":
                break;
            case "next":
                mIcon = R.drawable.next_launcher;
                break;
            case "nova":
                mIcon = R.drawable.nova_launcher;
                break;
            case "s":
                break;
            case "smart":
                mIcon = R.drawable.smart_launcher;
                break;
            case "solo":
                mIcon = R.drawable.solo_launcher;
                break;
            case "tsf":
                mIcon = R.drawable.tsf_launcher;
                break;
            default:
                Log.d("<---ERROE--->", "no such launcher");
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

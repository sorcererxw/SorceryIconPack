package com.sorcerer.sorcery.iconpack.models;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.sorcerer.sorcery.iconpack.BuildConfig;

/**
 * Created by Sorcerer on 2016/2/29 0029.
 */
public class IconBmob {

    private String name;
    private Boolean like;
    private String imei;
    private Integer build;

    public IconBmob() {
    }

    public IconBmob(Context context) {
        imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
                .getDeviceId();
        build = BuildConfig.VERSION_CODE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getLike() {
        return like;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }
}

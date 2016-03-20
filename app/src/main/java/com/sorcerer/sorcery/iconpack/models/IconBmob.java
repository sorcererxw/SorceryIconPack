package com.sorcerer.sorcery.iconpack.models;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;

import com.sorcerer.sorcery.iconpack.BuildConfig;

import android.telephony.TelephonyManager;

import cn.bmob.v3.BmobObject;

/**
 * Created by Sorcerer on 2016/2/29 0029.
 */
public class IconBmob extends BmobObject {

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

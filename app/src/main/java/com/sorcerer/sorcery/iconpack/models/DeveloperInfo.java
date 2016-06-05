package com.sorcerer.sorcery.iconpack.models;

import android.graphics.drawable.Drawable;

/**
 * Created by Sorcerer on 2016/2/7 0007.
 */
public class DeveloperInfo {
    private String mName;

    public DeveloperInfo(String name, String job, String email,
            Drawable avatar) {
        mName = name;
        mJob = job;
        mEmail = email;
        mAvatar = avatar;
    }

    private String mJob;
    private String mEmail;
    private Drawable mAvatar;

    public Drawable getAvatar() {
        return mAvatar;
    }

    public void setAvatar(Drawable avatar) {
        mAvatar = avatar;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getJob() {
        return mJob;
    }

    public void setJob(String job) {
        mJob = job;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}

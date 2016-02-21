package com.sorcerer.sorcery.iconpack.models;

/**
 * Created by Sorcerer on 2016/2/15 0015.
 */
public class SettingsItem {
    private String mTitle;
    private String mSummary;

    public SettingsItem(String title) {
        mTitle = title;
    }

    public SettingsItem(String title, String summary) {
        mTitle = title;
        mSummary = summary;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }
}

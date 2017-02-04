package com.sorcerer.sorcery.iconpack.data.models;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/10
 */

public class OpenSourceLibBean {
    private String mAuthor;
    private String mLink;
    private String mName;
    private String mLicense;
    private String mDescription;

    public OpenSourceLibBean(String name, String author, String description, String link,
                             String license) {
        mAuthor = author;
        mLink = link;
        mName = name;
        mLicense = license;
        mDescription = description;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLicense() {
        return mLicense;
    }

    public void setLicense(String license) {
        mLicense = license;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}

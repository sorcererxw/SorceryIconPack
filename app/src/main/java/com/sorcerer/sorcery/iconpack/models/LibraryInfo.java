package com.sorcerer.sorcery.iconpack.models;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/2/7 0007
 */
public class LibraryInfo {
    private String mName;
    private String mDescribe;
    private String mLink;
    private String Developer;

    public LibraryInfo(String name, String developer, String link) {
        mName = name;
        mLink = link;
        Developer = developer;
    }

    public String getDescribe() {
        return mDescribe;
    }

    public void setDescribe(String describe) {
        mDescribe = describe;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public String getDeveloper() {
        return Developer;
    }

    public void setDeveloper(String developer) {
        Developer = developer;
    }
}

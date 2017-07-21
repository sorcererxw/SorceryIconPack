package com.sorcerer.sorcery.iconpack.network.sorcery.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/6/29
 */

public class SorceryRequestStatistic {

    /**
     * id : 57a8c40779bc440054da47cb
     * zhName : 微信
     * enName : WeChat
     * package : com.tencent.mm
     * count : 90
     * components : ["com.tencent.mm/com.tencent.mm.ui.LauncherUI"]
     */

    @SerializedName("id")
    private String mId;
    @SerializedName("zhName")
    private String mZhName;
    @SerializedName("enName")
    private String mEnName;
    @SerializedName("package")
    private String mPackageX;
    @SerializedName("count")
    private int mCount;
    @SerializedName("components")
    private List<String> mComponents;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getZhName() {
        return mZhName;
    }

    public void setZhName(String zhName) {
        mZhName = zhName;
    }

    public String getEnName() {
        return mEnName;
    }

    public void setEnName(String enName) {
        mEnName = enName;
    }

    public String getPackageX() {
        return mPackageX;
    }

    public void setPackageX(String packageX) {
        mPackageX = packageX;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        mCount = count;
    }

    public List<String> getComponents() {
        return mComponents;
    }

    public void setComponents(List<String> components) {
        mComponents = components;
    }
}

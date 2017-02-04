package com.sorcerer.sorcery.iconpack.net.avos.models;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/1/27
 */
@Keep
public class AvosCreateObjectResult {

    /**
     * objectId : 588ab6108fd9c53cecf5d806
     * createdAt : 2017-01-27T02:53:04.908Z
     */
    @Keep
    @SerializedName("objectId")
    private String mObjectId;
    @Keep
    @SerializedName("createdAt")
    private String mCreatedAt;

    public String getObjectId() {
        return mObjectId;
    }

    public void setObjectId(String objectId) {
        mObjectId = objectId;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }
}

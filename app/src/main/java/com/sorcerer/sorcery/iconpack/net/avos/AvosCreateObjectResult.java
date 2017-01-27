package com.sorcerer.sorcery.iconpack.net.avos;

import com.google.gson.annotations.SerializedName;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/1/27
 */

public class AvosCreateObjectResult {

    /**
     * objectId : 588ab6108fd9c53cecf5d806
     * createdAt : 2017-01-27T02:53:04.908Z
     */

    @SerializedName("objectId")
    private String mObjectId;
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

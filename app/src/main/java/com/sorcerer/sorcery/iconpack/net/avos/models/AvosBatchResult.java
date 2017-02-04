package com.sorcerer.sorcery.iconpack.net.avos.models;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/1/27
 */
@Keep
public class AvosBatchResult {

    /**
     * success : {"createdAt":"2015-07-13T10:43:00.282Z","objectId":"55a39634e4b0ed48f0c1845b"}
     */

    @Keep
    @SerializedName("success")
    private SuccessBean mSuccess;

    public SuccessBean getSuccess() {
        return mSuccess;
    }

    public void setSuccess(SuccessBean success) {
        mSuccess = success;
    }

    @Keep
    public static class SuccessBean {
        /**
         * createdAt : 2015-07-13T10:43:00.282Z
         * objectId : 55a39634e4b0ed48f0c1845b
         */
        @Keep
        @SerializedName("createdAt")
        private String mCreatedAt;
        @Keep
        @SerializedName("objectId")
        private String mObjectId;

        public String getCreatedAt() {
            return mCreatedAt;
        }

        public void setCreatedAt(String createdAt) {
            mCreatedAt = createdAt;
        }

        public String getObjectId() {
            return mObjectId;
        }

        public void setObjectId(String objectId) {
            mObjectId = objectId;
        }
    }
}

package com.sorcerer.sorcery.iconpack.net.leancloud;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/7/25
 */

@AVClassName(LikeBean.LIKE_TABLE)
public class LikeBean extends AVObject {
    static final String LIKE_TABLE = "LikeTableNew";
    private static final String COLUMN_LIKE = "like";
    private static final String COLUMN_DEVICE_ID = "deviceId";
    private static final String COLUMN_BUILD = "build";
    private static final String COLUMN_ICON_NAME = "iconName";

    public void setLike(boolean like) {
        put(COLUMN_LIKE, like);
    }

    public void setDeviceId(String deviceId) {
        put(COLUMN_DEVICE_ID, deviceId);
    }

    public void setBuild(String build) {
        put(COLUMN_BUILD, build);
    }

    public void setIconName(String iconName) {
        put(COLUMN_ICON_NAME, iconName);
    }

    public boolean getLike() {
        return getBoolean(COLUMN_LIKE);
    }

    public String getDeviceID() {
        return getString(COLUMN_DEVICE_ID);
    }

    public String getBuild() {
        return getString(COLUMN_BUILD);
    }

    public String getIconName() {
        return getString(COLUMN_ICON_NAME);
    }

    @Override
    public String toString() {
        return "LikeBean{"
                + "ObjectId: " + getObjectId() + "\n"
                + "IconName: " + getIconName() + "\n"
                + "Like: " + getLike() + "\n"
                + "Build: " + getBuild() + "\n"
                + "DeviceId: " + getDeviceID() + "\n"
                + "}";
    }
}

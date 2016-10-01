package com.sorcerer.sorcery.iconpack.net.leancloud;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/7/22
 */

@AVClassName(RequestBean.REQUEST_TABLE)
public class RequestBean extends AVObject {
    static final String REQUEST_TABLE = "RequestTable";
    public static final String COLUMN_APP_PACKAGE = "appPackage";
    public static final String COLUMN_COMPONENT = "component";
    public static final String COLUMN_DEVICE_ID = "deviceId";
    public static final String COLUMN_EN_NAME = "enName";
    public static final String COLUMN_ZH_NAME = "zhName";

    public void setComponent(String component) {
        put(COLUMN_COMPONENT, component);
    }

    public String getComponent() {
        return getString(COLUMN_COMPONENT);
    }

    public void setAppPackage(String appPackage) {
        put(COLUMN_APP_PACKAGE, appPackage);
    }

    public String getAppPackage() {
        return getString(COLUMN_APP_PACKAGE);
    }

    public void setDeviceId(String times) {
        put(COLUMN_DEVICE_ID, times);
    }

    public String getDeviceId() {
        return getString(COLUMN_DEVICE_ID);
    }

    public void setEnName(String name) {
        put(COLUMN_EN_NAME, name);
    }

    public String getEnName() {
        return getString(COLUMN_EN_NAME);
    }

    public void setZhName(String name) {
        put(COLUMN_ZH_NAME, name);
    }

    public String getZhName() {
        return getString(COLUMN_ZH_NAME);
    }
}

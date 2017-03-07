package com.sorcerer.sorcery.iconpack.network.avos.models;

import android.support.annotation.Keep;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/1/27
 */
@Keep
public class AvosIconRequestBean {
    public static final String REQUEST_TABLE = "RequestTable";
    public static final String COLUMN_APP_PACKAGE = "appPackage";
    public static final String COLUMN_COMPONENT = "component";
    public static final String COLUMN_DEVICE_ID = "deviceId";
    public static final String COLUMN_EN_NAME = "enName";
    public static final String COLUMN_ZH_NAME = "zhName";

    final String zhName;
    final String enName;
    final String appPackage;
    final String component;
    final String deviceId;

    public AvosIconRequestBean(String zhName,
                               String enName,
                               String appPackage,
                               String component,
                               String deviceId) {
        this.zhName = zhName;
        this.enName = enName;
        this.appPackage = appPackage;
        this.component = component;
        this.deviceId = deviceId;
    }

}

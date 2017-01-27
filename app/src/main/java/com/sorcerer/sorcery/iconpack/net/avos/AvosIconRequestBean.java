package com.sorcerer.sorcery.iconpack.net.avos;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/1/27
 */

public class AvosIconRequestBean {
    static final String REQUEST_TABLE = "RequestTable";
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

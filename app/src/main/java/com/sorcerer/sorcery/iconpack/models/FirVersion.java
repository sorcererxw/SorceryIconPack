package com.sorcerer.sorcery.iconpack.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sorcerer on 2016/1/26 0026.
 */
public class FirVersion {

    public FirVersion(String json) throws JSONException {
        JSONObject object = new JSONObject(json);
        mName = object.getString("name");
        mVersion = object.getString("version");
        mChangelog = object.getString("changelog");
        mUpdated_at = object.getString("updated_at");
        mVersionShort = object.getString("versionShort");
        mBuild = object.getString("build");
        mInstallUrl = object.getString("installUrl");
        mInstall_Url = object.getString("install_url");
        mDirect_install_url = object.getString("direct_install_url");
        mUpdate_url = object.getString("update_url");
        mBinary = object.getString("binary");
    }

    private String mName;
    private String mVersion;
    private String mChangelog;
    private String mUpdated_at;
    private String mVersionShort;
    private String mBuild;
    private String mInstallUrl;
    private String mInstall_Url;
    private String mDirect_install_url;
    private String mUpdate_url;
    private String mBinary;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getVersion() {
        return mVersion;
    }

    public void setVersion(String version) {
        mVersion = version;
    }

    public String getChangelog() {
        return mChangelog;
    }

    public void setChangelog(String changelog) {
        mChangelog = changelog;
    }

    public String getUpdate_at() {
        return mUpdated_at;
    }

    public void setUpdate_at(String update_at) {
        mUpdated_at = update_at;
    }

    public String getVersionShort() {
        return mVersionShort;
    }

    public void setVersionShort(String versionShort) {
        mVersionShort = versionShort;
    }

    public String getBuild() {
        return mBuild;
    }

    public void setBuild(String build) {
        mBuild = build;
    }

    public String getInstallUrl() {
        return mInstallUrl;
    }

    public void setInstallUrl(String installUrl) {
        mInstallUrl = installUrl;
    }

    public String getInstall_Url() {
        return mInstall_Url;
    }

    public void setInstall_Url(String install_Url) {
        mInstall_Url = install_Url;
    }

    public String getDirect_install_url() {
        return mDirect_install_url;
    }

    public void setDirect_install_url(String direct_install_url) {
        mDirect_install_url = direct_install_url;
    }

    public String getUpdate_url() {
        return mUpdate_url;
    }

    public void setUpdate_url(String update_url) {
        mUpdate_url = update_url;
    }

    public String getBinary() {
        return mBinary;
    }

    public void setBinary(String binary) {
        mBinary = binary;
    }
}

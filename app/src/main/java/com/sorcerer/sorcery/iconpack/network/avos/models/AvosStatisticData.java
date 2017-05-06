package com.sorcerer.sorcery.iconpack.network.avos.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/5/6
 */

public class AvosStatisticData {

    /**
     * client : {"id":"vdkGm4dtRNmhQ5gqUTFBiA","platform":"iOS","app_version":"1.0","app_channel":"App Store"}
     * session : {"id":"Q5tYi4BTQ5i3Xuycgr7l"}
     * events : [{"event":"_page","duration":2000,"tag":"BookDetail"},{"event":"buy-item","attributes":{"item-category":"book"}},{"event":"_session.close","duration":10000}]
     */

    @SerializedName("client")
    private ClientBean mClient;
    @SerializedName("session")
    private SessionBean mSession;
    @SerializedName("events")
    private List<EventsBean> mEvents;

    public ClientBean getClient() {
        return mClient;
    }

    public void setClient(ClientBean client) {
        mClient = client;
    }

    public SessionBean getSession() {
        return mSession;
    }

    public void setSession(SessionBean session) {
        mSession = session;
    }

    public List<EventsBean> getEvents() {
        return mEvents;
    }

    public void setEvents(List<EventsBean> events) {
        mEvents = events;
    }

    public static class ClientBean {
        /**
         * id : vdkGm4dtRNmhQ5gqUTFBiA
         * platform : iOS
         * app_version : 1.0
         * app_channel : App Store
         */

        @SerializedName("id")
        private String mId;
        @SerializedName("platform")
        private String mPlatform;
        @SerializedName("app_version")
        private String mAppVersion;
        @SerializedName("app_channel")
        private String mAppChannel;
        @SerializedName("os_version")
        private String mOsVersion;
        @SerializedName("device_brand")
        private String mDeviceBrand;
        @SerializedName("device_model")
        private String mDeviceModel;
        @SerializedName("device_resolution")
        private String mDeviceResolution;
        @SerializedName("network_access")
        private String mNetworkAccess;
        @SerializedName("network_carrier")
        private String mNetworkCarrier;

        public String getId() {
            return mId;
        }

        public void setId(String id) {
            mId = id;
        }

        public String getPlatform() {
            return mPlatform;
        }

        public void setPlatform(String platform) {
            mPlatform = platform;
        }

        public String getAppVersion() {
            return mAppVersion;
        }

        public void setAppVersion(String appVersion) {
            mAppVersion = appVersion;
        }

        public String getAppChannel() {
            return mAppChannel;
        }

        public void setAppChannel(String appChannel) {
            mAppChannel = appChannel;
        }

        public String getOsVersion() {
            return mOsVersion;
        }

        public void setOsVersion(String osVersion) {
            mOsVersion = osVersion;
        }

        public String getDeviceBrand() {
            return mDeviceBrand;
        }

        public void setDeviceBrand(String deviceBrand) {
            mDeviceBrand = deviceBrand;
        }

        public String getDeviceModel() {
            return mDeviceModel;
        }

        public void setDeviceModel(String deviceModel) {
            mDeviceModel = deviceModel;
        }

        public String getDeviceResolution() {
            return mDeviceResolution;
        }

        public void setDeviceResolution(String deviceResolution) {
            mDeviceResolution = deviceResolution;
        }

        public String getNetworkAccess() {
            return mNetworkAccess;
        }

        public void setNetworkAccess(String networkAccess) {
            mNetworkAccess = networkAccess;
        }

        public String getNetworkCarrier() {
            return mNetworkCarrier;
        }

        public void setNetworkCarrier(String networkCarrier) {
            mNetworkCarrier = networkCarrier;
        }
    }

    public static class SessionBean {
        /**
         * id : Q5tYi4BTQ5i3Xuycgr7l
         */

        @SerializedName("id")
        private String mId;

        public String getId() {
            return mId;
        }

        public void setId(String id) {
            mId = id;
        }
    }

    public static class EventsBean {
        /**
         * event : _page
         * duration : 2000
         * tag : BookDetail
         * attributes : {"item-category":"book"}
         */

        @SerializedName("event")
        private String mEvent;
        @SerializedName("duration")
        private int mDuration;
        @SerializedName("tag")
        private String mTag;
        @SerializedName("attributes")
        private AttributesBean mAttributes;

        public String getEvent() {
            return mEvent;
        }

        public void setEvent(String event) {
            mEvent = event;
        }

        public int getDuration() {
            return mDuration;
        }

        public void setDuration(int duration) {
            mDuration = duration;
        }

        public String getTag() {
            return mTag;
        }

        public void setTag(String tag) {
            mTag = tag;
        }

        public AttributesBean getAttributes() {
            return mAttributes;
        }

        public void setAttributes(AttributesBean attributes) {
            mAttributes = attributes;
        }

        public static class AttributesBean {
            /**
             * item-category : book
             */

            @SerializedName("item-category")
            private String mItemcategory;

            public String getItemcategory() {
                return mItemcategory;
            }

            public void setItemcategory(String itemcategory) {
                mItemcategory = itemcategory;
            }
        }
    }
}

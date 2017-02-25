package com.sorcerer.sorcery.iconpack.net.weather.he.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/17
 */

public class HeForecastBean {

    @SerializedName("HeWeather5")
    private List<HeWeather5Bean> mHeWeather5;

    public List<HeWeather5Bean> getHeWeather5() {
        return mHeWeather5;
    }

    public void setHeWeather5(List<HeWeather5Bean> heWeather5) {
        mHeWeather5 = heWeather5;
    }

    public static class HeWeather5Bean {
        /**
         * basic : {"city":"北京","cnty":"中国","id":"CN101010100","lat":"39.904000","lon":"116.391000","update":{"loc":"2017-02-17 18:51","utc":"2017-02-17 10:51"}}
         * daily_forecast : [{"astro":{"mr":"null","ms":"10:27","sr":"07:04","ss":"17:52"},"cond":{"code_d":"100","code_n":"100","txt_d":"晴","txt_n":"晴"},"date":"2017-02-17","hum":"22","pcpn":"0.0","pop":"0","pres":"1032","tmp":{"max":"6","min":"-4"},"uv":"3","vis":"10","wind":{"deg":"208","dir":"无持续风向","sc":"微风","spd":"7"}},{"astro":{"mr":"00:08","ms":"11:00","sr":"07:02","ss":"17:53"},"cond":{"code_d":"101","code_n":"104","txt_d":"多云","txt_n":"阴"},"date":"2017-02-18","hum":"29","pcpn":"0.0","pop":"0","pres":"1022","tmp":{"max":"8","min":"-1"},"uv":"3","vis":"10","wind":{"deg":"162","dir":"无持续风向","sc":"微风","spd":"0"}},{"astro":{"mr":"01:04","ms":"11:36","sr":"07:01","ss":"17:54"},"cond":{"code_d":"104","code_n":"100","txt_d":"阴","txt_n":"晴"},"date":"2017-02-19","hum":"32","pcpn":"0.0","pop":"0","pres":"1015","tmp":{"max":"13","min":"-2"},"uv":"2","vis":"10","wind":{"deg":"325","dir":"北风","sc":"3-4","spd":"10"}}]
         * status : ok
         */

        @SerializedName("basic")
        private BasicBean mBasic;
        @SerializedName("status")
        private String mStatus;
        @SerializedName("daily_forecast")
        private List<DailyForecastBean> mDailyForecast;

        public BasicBean getBasic() {
            return mBasic;
        }

        public void setBasic(BasicBean basic) {
            mBasic = basic;
        }

        public String getStatus() {
            return mStatus;
        }

        public void setStatus(String status) {
            mStatus = status;
        }

        public List<DailyForecastBean> getDailyForecast() {
            return mDailyForecast;
        }

        public void setDailyForecast(List<DailyForecastBean> dailyForecast) {
            mDailyForecast = dailyForecast;
        }

        public static class BasicBean {
            /**
             * city : 北京
             * cnty : 中国
             * id : CN101010100
             * lat : 39.904000
             * lon : 116.391000
             * update : {"loc":"2017-02-17 18:51","utc":"2017-02-17 10:51"}
             */

            @SerializedName("city")
            private String mCity;
            @SerializedName("cnty")
            private String mCnty;
            @SerializedName("id")
            private String mId;
            @SerializedName("lat")
            private String mLat;
            @SerializedName("lon")
            private String mLon;
            @SerializedName("update")
            private UpdateBean mUpdate;

            public String getCity() {
                return mCity;
            }

            public void setCity(String city) {
                mCity = city;
            }

            public String getCnty() {
                return mCnty;
            }

            public void setCnty(String cnty) {
                mCnty = cnty;
            }

            public String getId() {
                return mId;
            }

            public void setId(String id) {
                mId = id;
            }

            public String getLat() {
                return mLat;
            }

            public void setLat(String lat) {
                mLat = lat;
            }

            public String getLon() {
                return mLon;
            }

            public void setLon(String lon) {
                mLon = lon;
            }

            public UpdateBean getUpdate() {
                return mUpdate;
            }

            public void setUpdate(UpdateBean update) {
                mUpdate = update;
            }

            public static class UpdateBean {
                /**
                 * loc : 2017-02-17 18:51
                 * utc : 2017-02-17 10:51
                 */

                @SerializedName("loc")
                private String mLoc;
                @SerializedName("utc")
                private String mUtc;

                public String getLoc() {
                    return mLoc;
                }

                public void setLoc(String loc) {
                    mLoc = loc;
                }

                public String getUtc() {
                    return mUtc;
                }

                public void setUtc(String utc) {
                    mUtc = utc;
                }
            }
        }

        public static class DailyForecastBean {
            /**
             * astro : {"mr":"null","ms":"10:27","sr":"07:04","ss":"17:52"}
             * cond : {"code_d":"100","code_n":"100","txt_d":"晴","txt_n":"晴"}
             * date : 2017-02-17
             * hum : 22
             * pcpn : 0.0
             * pop : 0
             * pres : 1032
             * tmp : {"max":"6","min":"-4"}
             * uv : 3
             * vis : 10
             * wind : {"deg":"208","dir":"无持续风向","sc":"微风","spd":"7"}
             */

            @SerializedName("astro")
            private AstroBean mAstro;
            @SerializedName("cond")
            private CondBean mCond;
            @SerializedName("date")
            private String mDate;
            @SerializedName("hum")
            private String mHum;
            @SerializedName("pcpn")
            private String mPcpn;
            @SerializedName("pop")
            private String mPop;
            @SerializedName("pres")
            private String mPres;
            @SerializedName("tmp")
            private TmpBean mTmp;
            @SerializedName("uv")
            private String mUv;
            @SerializedName("vis")
            private String mVis;
            @SerializedName("wind")
            private WindBean mWind;

            public AstroBean getAstro() {
                return mAstro;
            }

            public void setAstro(AstroBean astro) {
                mAstro = astro;
            }

            public CondBean getCond() {
                return mCond;
            }

            public void setCond(CondBean cond) {
                mCond = cond;
            }

            public String getDate() {
                return mDate;
            }

            public void setDate(String date) {
                mDate = date;
            }

            public String getHum() {
                return mHum;
            }

            public void setHum(String hum) {
                mHum = hum;
            }

            public String getPcpn() {
                return mPcpn;
            }

            public void setPcpn(String pcpn) {
                mPcpn = pcpn;
            }

            public String getPop() {
                return mPop;
            }

            public void setPop(String pop) {
                mPop = pop;
            }

            public String getPres() {
                return mPres;
            }

            public void setPres(String pres) {
                mPres = pres;
            }

            public TmpBean getTmp() {
                return mTmp;
            }

            public void setTmp(TmpBean tmp) {
                mTmp = tmp;
            }

            public String getUv() {
                return mUv;
            }

            public void setUv(String uv) {
                mUv = uv;
            }

            public String getVis() {
                return mVis;
            }

            public void setVis(String vis) {
                mVis = vis;
            }

            public WindBean getWind() {
                return mWind;
            }

            public void setWind(WindBean wind) {
                mWind = wind;
            }

            public static class AstroBean {
                /**
                 * mr : null
                 * ms : 10:27
                 * sr : 07:04
                 * ss : 17:52
                 */

                @SerializedName("mr")
                private String mMr;
                @SerializedName("ms")
                private String mMs;
                @SerializedName("sr")
                private String mSr;
                @SerializedName("ss")
                private String mSs;

                public String getMr() {
                    return mMr;
                }

                public void setMr(String mr) {
                    mMr = mr;
                }

                public String getMs() {
                    return mMs;
                }

                public void setMs(String ms) {
                    mMs = ms;
                }

                public String getSr() {
                    return mSr;
                }

                public void setSr(String sr) {
                    mSr = sr;
                }

                public String getSs() {
                    return mSs;
                }

                public void setSs(String ss) {
                    mSs = ss;
                }
            }

            public static class CondBean {
                /**
                 * code_d : 100
                 * code_n : 100
                 * txt_d : 晴
                 * txt_n : 晴
                 */

                @SerializedName("code_d")
                private String mCodeD;
                @SerializedName("code_n")
                private String mCodeN;
                @SerializedName("txt_d")
                private String mTxtD;
                @SerializedName("txt_n")
                private String mTxtN;

                public String getCodeD() {
                    return mCodeD;
                }

                public void setCodeD(String codeD) {
                    mCodeD = codeD;
                }

                public String getCodeN() {
                    return mCodeN;
                }

                public void setCodeN(String codeN) {
                    mCodeN = codeN;
                }

                public String getTxtD() {
                    return mTxtD;
                }

                public void setTxtD(String txtD) {
                    mTxtD = txtD;
                }

                public String getTxtN() {
                    return mTxtN;
                }

                public void setTxtN(String txtN) {
                    mTxtN = txtN;
                }
            }

            public static class TmpBean {
                /**
                 * max : 6
                 * min : -4
                 */

                @SerializedName("max")
                private String mMax;
                @SerializedName("min")
                private String mMin;

                public String getMax() {
                    return mMax;
                }

                public void setMax(String max) {
                    mMax = max;
                }

                public String getMin() {
                    return mMin;
                }

                public void setMin(String min) {
                    mMin = min;
                }
            }

            public static class WindBean {
                /**
                 * deg : 208
                 * dir : 无持续风向
                 * sc : 微风
                 * spd : 7
                 */

                @SerializedName("deg")
                private String mDeg;
                @SerializedName("dir")
                private String mDir;
                @SerializedName("sc")
                private String mSc;
                @SerializedName("spd")
                private String mSpd;

                public String getDeg() {
                    return mDeg;
                }

                public void setDeg(String deg) {
                    mDeg = deg;
                }

                public String getDir() {
                    return mDir;
                }

                public void setDir(String dir) {
                    mDir = dir;
                }

                public String getSc() {
                    return mSc;
                }

                public void setSc(String sc) {
                    mSc = sc;
                }

                public String getSpd() {
                    return mSpd;
                }

                public void setSpd(String spd) {
                    mSpd = spd;
                }
            }
        }
    }
}

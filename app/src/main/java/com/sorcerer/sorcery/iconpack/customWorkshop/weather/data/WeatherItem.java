package com.sorcerer.sorcery.iconpack.customWorkshop.weather.data;

import java.util.Date;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/18
 */

public class WeatherItem {
    private WeatherType mType;
    private Date mDate;

    public WeatherItem(WeatherType type, Date date) {
        mType = type;
        mDate = date;
    }

    public WeatherType getType() {
        return mType;
    }

    public void setType(WeatherType type) {
        mType = type;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}

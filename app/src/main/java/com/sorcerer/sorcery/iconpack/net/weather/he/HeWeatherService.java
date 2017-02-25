package com.sorcerer.sorcery.iconpack.net.weather.he;

import com.sorcerer.sorcery.iconpack.net.weather.he.models.HeForecastBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/17
 */

interface HeWeatherService {
    @GET("forecast")
    Observable<HeForecastBean> forecast(@Query("city") String city,
                                        @Query("key") String key,
                                        @Query("lang") String lang);
}

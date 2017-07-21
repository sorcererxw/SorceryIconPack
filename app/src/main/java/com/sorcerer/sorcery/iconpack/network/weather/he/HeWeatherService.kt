package com.sorcerer.sorcery.iconpack.network.weather.he

import com.sorcerer.sorcery.iconpack.network.weather.he.models.HeForecastBean

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @description:
 * *
 * @author: Sorcerer
 * *
 * @date: 2017/2/17
 */

internal interface HeWeatherService {
    @GET("forecast")
    fun forecast(@Query("city") city: String,
                 @Query("key") key: String,
                 @Query("lang") lang: String): Observable<HeForecastBean>
}

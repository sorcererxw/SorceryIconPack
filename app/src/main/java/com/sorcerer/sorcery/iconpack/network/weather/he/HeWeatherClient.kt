package com.sorcerer.sorcery.iconpack.network.weather.he

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @description:
 * *
 * @author: Sorcerer
 * *
 * @date: 2017/2/17
 */

class HeWeatherClient private constructor() {

    private val mService: HeWeatherService

    init {
        mService = Retrofit.Builder()
                .baseUrl("https://free-api.heweather.com/v5/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(
                        GsonBuilder().create()))
                .build().create(HeWeatherService::class.java)
    }

    companion object {
        private var sHeWeatherClient: HeWeatherClient? = null

        val instance: HeWeatherClient
            get() {
                if (sHeWeatherClient == null) {
                    sHeWeatherClient = HeWeatherClient()
                }
                return sHeWeatherClient!!
            }
    }
}

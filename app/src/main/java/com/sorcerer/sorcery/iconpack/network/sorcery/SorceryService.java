package com.sorcerer.sorcery.iconpack.network.sorcery;

import com.sorcerer.sorcery.iconpack.network.sorcery.models.SorceryRequestStatistic;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/6/29
 */
interface SorceryService {
    @GET("request/statistic")
    Observable<SorceryRequestStatistic> requestStatisitc(@Query("packageName") String packageName);
}
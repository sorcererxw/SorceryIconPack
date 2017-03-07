package com.sorcerer.sorcery.iconpack.network.avos;

import com.sorcerer.sorcery.iconpack.network.avos.models.AvosBatchRequest;
import com.sorcerer.sorcery.iconpack.network.avos.models.AvosBatchResult;
import com.sorcerer.sorcery.iconpack.network.avos.models.AvosCreateObjectResult;
import com.sorcerer.sorcery.iconpack.network.avos.models.AvosIconRequestStatisticQueryResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/1/27
 */

interface AvosService {
    @POST("1.1/classes/{class}")
    Observable<AvosCreateObjectResult> createObject(@Path("class") String className,
                                                    @Body Object o);

    @POST("1.1/batch")
    Observable<List<AvosBatchResult>> batch(@Body AvosBatchRequest request);

    @GET("1.1/classes/RequestStatistic")
    Observable<AvosIconRequestStatisticQueryResult> queryRequestStatistic(
            @Query("where") String where);

    @GET("1.1/classes/RequestStatistic")
    Observable<AvosIconRequestStatisticQueryResult> queryRequestStatistic(
            @Query("where") String where, @Query("keys") String keys);

}

package com.sorcerer.sorcery.iconpack.net.avos;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
}

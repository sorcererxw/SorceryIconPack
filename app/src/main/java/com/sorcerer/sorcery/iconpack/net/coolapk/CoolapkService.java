package com.sorcerer.sorcery.iconpack.net.coolapk;

import com.sorcerer.sorcery.iconpack.net.coolapk.models.CoolapkAppDetail;
import com.sorcerer.sorcery.iconpack.net.coolapk.models.CoolapkSearchResult;

import io.reactivex.Observable;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/1/26
 */

interface CoolapkService {
    //    https://api.coolapk.com/v6/apk/search?q=qq&apktype=1&rankType=default&page=1
    @GET("apk/search")
    Observable<CoolapkSearchResult> search(@Query("q") String q,
                                           @Query("page") int page);

    @GET("apk/search")
    Observable<CoolapkSearchResult> search(@Query("q") String q);

    @GET("device/ip")
    Observable<Result<String>> getDeviceIp();

    @GET("apk/detail")
    Observable<CoolapkAppDetail> getAppDetail(@Query("id") String packageName);
}

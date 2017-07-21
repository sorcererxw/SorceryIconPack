package com.sorcerer.sorcery.iconpack.network.avos;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.gson.Gson;
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.network.avos.models.AvosBatchRequest;
import com.sorcerer.sorcery.iconpack.network.avos.models.AvosBatchResult;
import com.sorcerer.sorcery.iconpack.network.avos.models.AvosIconRequestBean;
import com.sorcerer.sorcery.iconpack.network.avos.models.AvosRequest;
import com.sorcerer.sorcery.iconpack.network.avos.models.AvosStatisticData;
import com.sorcerer.sorcery.iconpack.network.avos.models.EmptyBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/1/27
 */

public class AvosClient {
    private static AvosClient sAvosClient;
    private AvosService mAvosService;

    private AvosClient() {
        mAvosService = new Retrofit.Builder()
                .baseUrl("https://api.leancloud.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new OkHttpClient.Builder().addInterceptor(chain -> {
                    Request request = chain.request();
                    request = request.newBuilder()
                            .addHeader("X-LC-Id", BuildConfig.LEANCLOUD_ID)
                            .addHeader("X-LC-Key", BuildConfig.LEANCLOUD_KEY)
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(request);
                }).build())
                .build()
                .create(AvosService.class);
    }

    public static AvosClient getInstance() {
        if (sAvosClient == null) {
            sAvosClient = new AvosClient();
        }
        return sAvosClient;
    }

    public Observable<Boolean> postIconRequest(AvosIconRequestBean iconRequestBean) {
        return mAvosService
                .createObject(AvosIconRequestBean.REQUEST_TABLE, iconRequestBean)
                .map(avosCreateObjectResult -> avosCreateObjectResult.getObjectId() != null
                        && !avosCreateObjectResult.getObjectId().isEmpty());
    }

    public Observable<List<Boolean>> postIconRequests(List<AvosIconRequestBean> list) {
        return mAvosService.batch(
                new AvosBatchRequest(Stream.of(list)
                        .peek(iconRequestBean -> Timber.d(new Gson().toJson(iconRequestBean)))
                        .map(iconRequestBean -> new AvosRequest(
                                AvosRequest.METHOD_POST,
                                "/1.1/classes/RequestTable",
                                iconRequestBean))
                        .collect(Collectors.toList())))
                .flatMap(Observable::fromIterable)
                .doOnEach(avosBatchResultNotification -> {
                    Timber.d(new Gson().toJson(avosBatchResultNotification));
                })
                .map(avosBatchResult -> avosBatchResult.getSuccess() != null
                        && avosBatchResult.getSuccess().getObjectId() != null)
                .toList()
                .toObservable();
    }

    public Observable<List<AvosBatchResult>> postBatch(AvosBatchRequest request) {
        return mAvosService.batch(request);
    }

    public Observable<EmptyBean> pustStatistic(AvosStatisticData data) {
        return mAvosService.postStatistic(data);
    }
}

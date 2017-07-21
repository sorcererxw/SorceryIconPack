package com.sorcerer.sorcery.iconpack.network.sorcery;

import com.google.gson.GsonBuilder;
import com.sorcerer.sorcery.iconpack.network.sorcery.models.SorceryRequestStatistic;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/6/29
 */

public class SorceryClient {

    private SorceryService mService;

    private SorceryClient() {
        mService = new Retrofit.Builder()
                .baseUrl("http://sorcerycons.leanapp.cn/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
//                .client(new OkHttpClient.Builder().addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Request request = chain.request();
//                        Response response = chain.proceed(request);
//                        Timber.d(response.body().string());
//                        return response;
//                    }
//                }).build())
                .build()
                .create(SorceryService.class);
    }

    public static SorceryClient getInstance() {
        return Holder.INSTANCE;
    }

    public Observable<SorceryRequestStatistic> getRequestStatistic(String packageName) {
        return mService.requestStatisitc(packageName);
    }

    private static class Holder {
        private static SorceryClient INSTANCE = new SorceryClient();
    }
}

package com.sorcerer.sorcery.iconpack.net.coolapk;

import android.os.Build;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/1/26
 */

public class CoolapkClient {

    private CoolapkService mService;

    private static CoolapkClient sCoolapkClient;

    public static CoolapkClient getInstance() {
        if (sCoolapkClient == null) {
            sCoolapkClient = new CoolapkClient();
        }
        return sCoolapkClient;
    }

    private CoolapkClient() {
        mService = new Retrofit.Builder()
                .baseUrl("https://api.coolapk.com/v6/")
                .client(new OkHttpClient.Builder().addInterceptor(chain -> {
                    Request request = chain.request();
                    // TODO: 2017/1/26 do not use static token
                    request = request.newBuilder()
//                            .addHeader("User-Agent", Utils.getUserAgent())
                            .addHeader("User-Agent",
                                    "User-Agent: Dalvik/2.1.0 (Linux; U; Android 5.1.1; Nexus 4 Build/LMY48T) (#Build; google; Nexus 4; LMY48T; 5.1.1) +CoolMarket/7.3")
                            .addHeader("X-Requested-With", "XMLHttpRequest")
                            .addHeader("X-Sdk-Int", String.valueOf(Build.VERSION.SDK_INT))
//                            .addHeader("X-Sdk-Locale", Utils.getLocaleString())
                            .addHeader("X-Sdk-Locale", "zh-CN")
                            .addHeader("X-App-Id", "coolmarket")
//                            .addHeader("X-App-Token", AuthUtils.getAS(Utils.getUUID()))
                            .addHeader("X-App-Token",
                                    "2a6e2adc2897c8d8133db17c2cd3b1045834ce58-d7d5-38eb-95d5-563167a1983d0x588f16cd")
                            .addHeader("X-App-Version", "7.3")
                            .addHeader("X-App-Code", "1701135")
                            .addHeader("X-Api-Version", "7").build();
                    return chain.proceed(request);
                }).build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().create()))
                .build().create(CoolapkService.class);
    }

    public Observable<CoolapkSearchResult> search(String q, int page) {
        return mService.search(q, page);
    }

    public Observable<CoolapkSearchResult> search(String q) {
        return mService.search(q);
    }

    public Observable<String> getAppName(String packageName) {
        return mService.getAppDetail(packageName)
                .map(coolapkAppDetail -> {
                    if (coolapkAppDetail.getStatus() != 0 || coolapkAppDetail.getData() == null) {
                        return "";
                    }
                    return coolapkAppDetail.getData().getTitle();
                });
    }
}

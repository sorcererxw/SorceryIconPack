package com.sorcerer.sorcery.iconpack.network.coolapk;

import android.os.Build;

import com.coolapk.market.util.AuthUtils;
import com.google.gson.GsonBuilder;
import com.sorcerer.sorcery.iconpack.network.coolapk.models.CoolapkSearchResult;
import com.sorcerer.sorcery.iconpack.network.spiders.models.AppDisplayInfo;

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
                    request = request.newBuilder()
                            .addHeader("User-Agent", Utils.getUserAgent())
                            .addHeader("X-Requested-With", "XMLHttpRequest")
                            .addHeader("X-Sdk-Int", String.valueOf(Build.VERSION.SDK_INT))
                            .addHeader("X-Sdk-Locale", Utils.getLocaleString())
                            .addHeader("X-App-Id", "coolmarket")
                            .addHeader("X-App-Token", AuthUtils.getAS(Utils.getUUID()))
                            .addHeader("X-App-Version", "7.3")
                            .addHeader("X-App-Code", "1701135")
                            .addHeader("X-Api-Version", "7").build();

                    Timber.d(request.header("User-Agent"));
                    Timber.d(request.header("X-Sdk-Locale"));
                    Timber.d(request.header("X-App-Token"));

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

    public Observable<AppDisplayInfo> getAppDisplayInfo(String packageName) {
        return mService.getAppDetail(packageName)
                .map(coolapkAppDetail -> {
                    AppDisplayInfo info = new AppDisplayInfo();
                    if (coolapkAppDetail.getStatus() != 0 || coolapkAppDetail.getData() == null) {
                        return info;
                    }
                    String title = coolapkAppDetail.getData().getTitle();
                    String[] titles = title.split(":");
                    if (titles.length == 2) {
                        info.setAppName(titles[0]);
                    } else {
                        info.setAppName(title);
                    }
                    String icon = coolapkAppDetail.getData().getLogo();
                    info.setIconUrl(icon);
                    return info;
                });
    }

    public Observable<String> getAppName(String packageName) {
        return mService.getAppDetail(packageName)
                .map(coolapkAppDetail -> {
                    if (coolapkAppDetail.getStatus() != 0 || coolapkAppDetail.getData() == null) {
                        return "";
                    }
                    String title = coolapkAppDetail.getData().getTitle();
                    String[] titles = title.split(":");
                    if (titles.length == 2) {
                        return titles[0];
                    }
                    return title;
                });
    }
}

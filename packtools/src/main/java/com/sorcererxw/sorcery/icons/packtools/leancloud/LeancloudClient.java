package com.sorcererxw.sorcery.icons.packtools.leancloud;

import com.sorcererxw.sorcery.icons.packtools.Config;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/5/1
 */

public class LeancloudClient {
    private static LeancloudClient sAvosClient;

    public static LeancloudClient getInstance() {
        if (sAvosClient == null) {
            sAvosClient = new LeancloudClient();
        }
        return sAvosClient;
    }

    private LeancloudSearvice mService;

    private LeancloudClient() {
        mService = new Retrofit.Builder()
                .baseUrl("https://api.leancloud.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new OkHttpClient.Builder().addInterceptor(chain -> {
                    Request request = chain.request();
                    request = request.newBuilder()
                            .addHeader("X-LC-Id", Config.LEANCLOUD_ID)
                            .addHeader("X-LC-Key", Config.LEANCLOUD_KEY)
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(request);
                }).build())
                .build()
                .create(LeancloudSearvice.class);
    }

    private String mCursor = "";

    public List<IconTableItem.IconBean> scanIconTable() {
        List<IconTableItem.IconBean> list = new ArrayList<>();
        mCursor = null;
        do {
            mService.scanIconTable(mCursor).subscribe(iconTableItem -> {
                mCursor = iconTableItem.getCursor();
                list.addAll(iconTableItem.getResults());
            });
        } while (mCursor != null);
        return list;
    }

    public static void main(String... args) {
        getInstance().scanIconTable().forEach(
                iconBean -> System.out.println(iconBean.getDrawable()));
    }
}

package com.sorcerer.sorcery.iconpack.network.avos;

import android.content.Context;
import android.os.Build;

import com.sorcerer.sorcery.iconpack.App;
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.SorceryPrefs;
import com.sorcerer.sorcery.iconpack.network.avos.models.AvosStatisticData;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/5/6
 */

public class AvosStatisticManager {
    @Inject
    protected SorceryPrefs mPrefs;

    private AvosClient mAvosClient;

    private Context mContext;

    public AvosStatisticManager(Context context) {
        App.getInstance().getAppComponent().inject(this);

        mAvosClient = AvosClient.getInstance();

        mContext = context;
    }

    public void post() {
        AvosStatisticData data = new AvosStatisticData();
        AvosStatisticData.ClientBean clientBean = new AvosStatisticData.ClientBean();
        clientBean.setId(mPrefs.getUuid().getValue());
        clientBean.setPlatform("Android");
        clientBean.setOsVersion(Build.VERSION.SDK_INT + "");
        clientBean.setAppVersion(BuildConfig.VERSION_NAME);
        clientBean.setAppChannel(PackageUtil.isInstallFromPlay(mContext) ? "Google play" :
                (BuildConfig.DEBUG ? "Debug" : "other"));
        clientBean.setDeviceBrand(Build.BRAND);
        clientBean.setDeviceModel(Build.MODEL);

        data.setClient(clientBean);

        mAvosClient.pustStatistic(data).subscribeOn(Schedulers.io()).subscribe(emptyBean -> {

        }, Timber::e);
    }
}

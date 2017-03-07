package com.sorcerer.sorcery.iconpack.network.weather.he;

import com.annimon.stream.Stream;
import com.google.gson.GsonBuilder;
import com.sorcerer.sorcery.iconpack.customWorkshop.weather.data.WeatherItem;
import com.sorcerer.sorcery.iconpack.customWorkshop.weather.data.WeatherType;
import com.sorcerer.sorcery.iconpack.network.weather.he.models.HeWeatherType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

import static com.sorcerer.sorcery.iconpack.BuildConfig.HE_WEATHER_KEY;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/17
 */

public class HeWeatherClient {
    private static HeWeatherClient sHeWeatherClient;

    public static HeWeatherClient getInstance() {
        if (sHeWeatherClient == null) {
            sHeWeatherClient = new HeWeatherClient();
        }
        return sHeWeatherClient;
    }

    private HeWeatherService mService;

    private HeWeatherClient() {
        mService = new Retrofit.Builder()
                .baseUrl("https://free-api.heweather.com/v5/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().create()))
                .build().create(HeWeatherService.class);
    }

    public Single<List<WeatherItem>> forecast(String city) {
        return mService.forecast(city, HE_WEATHER_KEY, "en")
                .flatMap(heForecastBean -> {
                    if (heForecastBean.getHeWeather5().size() > 0) {
                        return Observable.fromIterable(
                                heForecastBean.getHeWeather5().get(0)
                                        .getDailyForecast());
                    } else {
                        return Observable.fromIterable(new ArrayList<>());
                    }
                })
                .map(dailyForecastBean -> {
                    Date date = null;
                    SimpleDateFormat format =
                            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    try {
                        date = format.parse(dailyForecastBean.getDate());
                    } catch (ParseException e) {
                        Timber.e(e);
                    }
                    int code = Integer.valueOf(dailyForecastBean.getCond().getCodeD());
                    WeatherType type = Stream.of(HeWeatherType.values())
                            .filter(value -> value.getCode() == code)
                            .findFirst()
                            .map(HeWeatherType::getWeatherType)
                            .get();
                    return new WeatherItem(type, date);
                })
                .toList();
    }
}

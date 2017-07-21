package com.sorcerer.sorcery.iconpack.network.weather.he.models;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/17
 */

public enum HeWeatherType {
    SUNNY(100),
    CLOUDY(101),
    FEW_CLOUDS(102),
    PARTLY_CLOUDY(103),
    OVERCAST(104),
    WINDY(200),
    CALM(201),
    LIGHT_BREEZE(202),
    GENTLE_BREEZE(203),
    FRESH_BREEZE(204),
    STRONG_BREEZE(205),
    HIGH_WIND(206),
    GALE(207),
    STRONG_GALE(208),
    STORM(209),
    VIOLENT_STORM(210),
    HURRICANE(211),
    TORNADO(212),
    TROPICAL_STORM(213),
    SHOWER_RAIN(300),
    HEAVY_SHOWER_RAIN(301),
    THUNDERSHOWER(302),
    HEAVY_THUNDERSTORM(303),
    HAIL(304),
    LIGHT_RAIN(305),
    MODERATE_RAIN(306),
    HEAVY_RAIN(307),
    EXTREME_RAIN(308),
    DRIZZLE_RAIN(309),
    STORM_RAIN(310),
    HEAVY_STORM_RAIN(311),
    SEVERE_STORM_RAIN(312),
    FREEZING_RAIN(313),
    LIGHT_SNOW(400),
    MODERATE_SNOW(401),
    HEAVY_SNOW(402),
    SNOWSTORM(403),
    SLEET(404),
    RAIN_AND_SNOW(405),
    SHOWER_SNOW(406),
    SNOW_FLURRY(407),
    MIST(500),
    FOGGY(501),
    HAZE(502),
    SAND(503),
    DUST(504),
    DUSTSTORM(507),
    SANDSTORM(508),
    HOT(900),
    COLD(901),
    UNKNOWN(999);


    private static final HeWeatherType[] SUNNY_CODES = new HeWeatherType[]{
            SUNNY, CALM, HOT, CLOUDY, UNKNOWN
    };
    private static final HeWeatherType[] WINDY_CODES = new HeWeatherType[]{
            WINDY, LIGHT_BREEZE, GENTLE_BREEZE, FRESH_BREEZE,
            STRONG_BREEZE, HIGH_WIND, GALE, STRONG_GALE,
            STORM, VIOLENT_STORM, HURRICANE, TORNADO,
            TROPICAL_STORM,
    };
    private static final HeWeatherType[] RAIN_CODES = new HeWeatherType[]{
            SHOWER_RAIN, HEAVY_SHOWER_RAIN, THUNDERSHOWER,
            HEAVY_THUNDERSTORM, HAIL,
            LIGHT_RAIN, MODERATE_RAIN,
            HEAVY_RAIN, EXTREME_RAIN,
            DRIZZLE_RAIN, STORM_RAIN,
            HEAVY_STORM_RAIN, SEVERE_STORM_RAIN,
            FREEZING_RAIN,
    };
    private static final HeWeatherType[] SNOW_CODES = new HeWeatherType[]{
            LIGHT_SNOW, MODERATE_SNOW, HEAVY_SNOW,
            SNOWSTORM, SLEET,
            RAIN_AND_SNOW, SHOWER_SNOW,
            SNOW_FLURRY,
    };
    private static final HeWeatherType[] CLOUDY_CODES = new HeWeatherType[]{
            CLOUDY, FEW_CLOUDS, PARTLY_CLOUDY, OVERCAST
    };
    private static final HeWeatherType[] HAZE_CODES = new HeWeatherType[]{
            MIST, FOGGY, HAZE, SAND, DUST, DUSTSTORM, SANDSTORM
    };
    private int mCode;

    HeWeatherType(int code) {
        mCode = code;
    }

//    private static final HeWeatherType[] THUNDER_CODES = new HeWeatherType[]{
//
//    };

    public int getCode() {
        return mCode;
    }

//    public WeatherType getWeatherType() {
//        if (Stream.of(SUNNY_CODES).anyMatch(value -> mCode == value.mCode)) {
//            return WeatherType.SUNNY;
//        }
//        if (Stream.of(WINDY_CODES).anyMatch(value -> mCode == value.mCode)) {
//            return WeatherType.WINDY;
//        }
//        if (Stream.of(RAIN_CODES).anyMatch(value -> mCode == value.mCode)) {
//            return WeatherType.RAIN;
//        }
//        if (Stream.of(HAZE_CODES).anyMatch(value -> mCode == value.mCode)) {
//            return WeatherType.HAZE;
//        }
//        if (Stream.of(CLOUDY_CODES).anyMatch(value -> mCode == value.mCode)) {
//            return WeatherType.CLOUDY;
//        }
//        if (Stream.of(SNOW_CODES).anyMatch(value -> mCode == value.mCode)) {
//            return WeatherType.SNOW;
//        }
//        return WeatherType.SUNNY;
//    }
}

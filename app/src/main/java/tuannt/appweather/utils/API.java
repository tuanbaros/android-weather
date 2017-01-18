package tuannt.appweather.utils;

/**
 * Created by tuannt on 17/01/2017.
 * Project: AppWeather
 * Package: tuannt.appweather.utils
 */
public class API {

    private static final String HOST = "http://api.openweathermap.org/data/2.5/weather";

    private static final String OPEN_WEATHER_KEY = "e9c132971d8648208975f12e910ec7e8";

    public static String getApiCurrentCityById(String cityId) {
        return HOST + "?id=" + cityId + "&appid=" + OPEN_WEATHER_KEY;
    }

    static String getApiCurrentCityByCoordinate(String lat, String lon) {
        return HOST + "?lat=" + lat + "&lon=" + lon + "&appid=" + OPEN_WEATHER_KEY;
    }

    public static String getApiCurrentCityNextDay(String cityId) {
        return "http://api.openweathermap.org/data/2" +
            ".5/forecast?id="+ cityId + "&cnt=6&appid=" + OPEN_WEATHER_KEY;
    }

    public static String getApiIcon(String iconId) {
        return "http://openweathermap.org/img/w/" + iconId + ".png";
    }

}

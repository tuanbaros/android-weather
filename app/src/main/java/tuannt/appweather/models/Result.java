package tuannt.appweather.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tuannt on 17/01/2017.
 * Project: AppWeather
 * Package: tuannt.appweather.models
 */
public class Result {
    @SerializedName("weather")
    private Weather[] weather;
    @SerializedName("wind")
    private Wind wind;
    @SerializedName("main")
    private Main main;
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }
}

package tuannt.appweather.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tuannt on 17/01/2017.
 * Project: AppWeather
 * Package: tuannt.appweather.models
 */
public class Wind {
    @SerializedName("speed")
    private String speed;

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }
}

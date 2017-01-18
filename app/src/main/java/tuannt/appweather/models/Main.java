package tuannt.appweather.models;

/**
 * Created by tuannt on 17/01/2017.
 * Project: AppWeather
 * Package: tuannt.appweather.models
 */
public class Main {
    private String temp;
    private String humidity;
    private String temp_min;
    private String temp_max;

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(String temp_max) {
        this.temp_max = temp_max;
    }

    public String getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(String temp_min) {
        this.temp_min = temp_min;
    }
}

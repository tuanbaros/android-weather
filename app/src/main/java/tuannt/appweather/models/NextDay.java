package tuannt.appweather.models;

/**
 * Created by tuannt on 17/01/2017.
 * Project: AppWeather
 * Package: tuannt.appweather.models
 */
public class NextDay {
    private City city;
    private Result[] list;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Result[] getList() {
        return list;
    }

    public void setList(Result[] list) {
        this.list = list;
    }
}

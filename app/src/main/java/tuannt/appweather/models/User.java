package tuannt.appweather.models;


/**
 * Created by tuan on 18/02/2016.
 */
public class User {

    private static volatile User user;

    private Weather weather;

    private Weather[] weatherArray;

    private String[] sixNextDay;

    private WeatherVideo[] weatherVideos;

    private User(){}

    public static User getInstance(){
        if(user == null){
            synchronized (User.class){
                user = new User();
            }
        }

        return user;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public Weather[] getWeatherArray() {
        return weatherArray;
    }

    public void setWeatherArray(Weather[] weatherArray) {
        this.weatherArray = weatherArray;
    }

    public String[] getSixNextDay() {
        return sixNextDay;
    }

    public void setSixNextDay(String[] sixNextDay) {
        this.sixNextDay = sixNextDay;
    }

    public WeatherVideo[] getWeatherVideos() {
        return weatherVideos;
    }

    public void setWeatherVideos(WeatherVideo[] weatherVideos) {
        this.weatherVideos = weatherVideos;
    }
}

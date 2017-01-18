package tuannt.appweather.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import tuannt.appweather.models.NextDay;
import tuannt.appweather.models.Result;
import tuannt.appweather.models.User;
import tuannt.appweather.activities.SlideActivity;
import tuannt.appweather.utils.DBAdapter;
import tuannt.appweather.utils.MyMethods;
import tuannt.appweather.utils.Variables;
import tuannt.appweather.models.Weather;

/**
 * Created by tuan on 18/02/2016.
 */
public class GetNextDayWeather extends AsyncTask<String, Void, Void> {

    private Activity appCompatActivity;

    public GetNextDayWeather(Activity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    @Override
    protected Void doInBackground(String... params) {

        String link = params[0];
        try {
            URL url=new URL(link);

            InputStreamReader reader = new InputStreamReader(url.openStream(),"UTF-8");

            User user = User.getInstance();
            NextDay nextDay = new Gson().fromJson(reader, NextDay.class);
            ArrayList<Weather> weathers = new ArrayList<>();
            for (Result result : nextDay.getList()) {
                Weather weather = new Weather();
                weather.setCity_id(nextDay.getCity().getId());
                weather.setCity_name(nextDay.getCity().getName());
                weather.setDescription(result.getWeather()[0].getDescription());
                weather.setHumidity(result.getMain().getHumidity());
                weather.setTemp(result.getMain().getTemp());
                weather.setTemp_max(result.getMain().getTemp_max());
                weather.setTemp_min(result.getMain().getTemp_min());
                weather.setWind_speed(result.getWind().getSpeed());
                weather.setIcon(result.getWeather()[0].getIcon());
                weathers.add(weather);
            }

            user.setWeatherArray(weathers.toArray(new Weather[weathers.size()]));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        new GetVideo(appCompatActivity).execute();
    }
}

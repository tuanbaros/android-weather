package tuannt.appweather.tasks;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import tuannt.appweather.models.Result;
import tuannt.appweather.models.User;
import tuannt.appweather.models.Weather;
import tuannt.appweather.utils.API;
import tuannt.appweather.utils.DBAdapter;
import tuannt.appweather.utils.MyMethods;
import tuannt.appweather.utils.MyNotification;
import tuannt.appweather.utils.Variables;

/**
 * Created by tuan on 17/02/2016.
 */
public class GetCityWeather extends AsyncTask<String, Void, Weather> {

    private Activity appCompatActivity;

    public GetCityWeather(Activity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }


    @Override
    protected Weather doInBackground(String... params) {
        Weather[] weather = new Weather[1];
        String link = params[0];
        try {
            URL url=new URL(link);

            Log.i("null-a", link);

            InputStreamReader reader = new InputStreamReader(url.openStream(),"UTF-8");

            Result result = new Gson().fromJson(reader, Result.class);

            weather[0] = new Weather();
            weather[0].setCity_id(result.getId());
            weather[0].setCity_name(result.getName());
            weather[0].setDescription(result.getWeather()[0].getDescription());
            weather[0].setHumidity(result.getMain().getHumidity());
            weather[0].setTemp(result.getMain().getTemp());
            weather[0].setTemp_max(result.getMain().getTemp_max());
            weather[0].setTemp_min(result.getMain().getTemp_min());
            weather[0].setWind_speed(result.getWind().getSpeed());
            weather[0].setIcon(result.getWeather()[0].getIcon());

//            weather = new Gson().fromJson(reader, Weather[].class);

            return weather[0];

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
    protected void onPostExecute(Weather weather) {
        super.onPostExecute(weather);
        if(weather!=null){
            User user = User.getInstance();
            user.setWeather(weather);
            user.getWeather().setCity_name(String.valueOf(MyMethods.capitalize(user.getWeather().getCity_name())));
            new GetNextDayWeather(appCompatActivity).execute(API.getApiCurrentCityNextDay(String.valueOf(weather.getCity_id())));
            //Log.i("Status", "Status: " + Variables.canRefresh);
            if(Variables.reSetLocation==true){
                MyNotification myNotification = new MyNotification(appCompatActivity);
                myNotification.showStatus();
                Variables.reSetLocation = false;
            }
        }else{
            Log.i("null-a", "abc");
//            appCompatActivity.finish();
            MyNotification.showDialogNotification(appCompatActivity);
            if(appCompatActivity.getLocalClassName().equals("tuannt.appweather.activities.SlideActivity")){
                MyMethods.hideUpdateDialog();
            }
        }

    }
}

package tuannt.appweather.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import tuannt.appweather.activities.SlideActivity;
import tuannt.appweather.models.City;
import tuannt.appweather.models.User;
import tuannt.appweather.models.Weather;
import tuannt.appweather.models.WeatherVideo;
import tuannt.appweather.utils.DBAdapter;
import tuannt.appweather.utils.MyMethods;
import tuannt.appweather.utils.Variables;

/**
 * Created by tuan on 22/02/2016.
 */
public class GetVideo extends AsyncTask<Void, Void, WeatherVideo[]> {
    
    private Activity activity;

    public GetVideo(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected WeatherVideo[] doInBackground(Void... params) {

        WeatherVideo[] weatherVideos = new WeatherVideo[1];
//        String link = "http://content.amobi.vn/api/thoitiet/get-video";
//        try {
//            URL url=new URL(link);
//
//            InputStreamReader reader = new InputStreamReader(url.openStream(),"UTF-8");
//
//            weatherVideos = new Gson().fromJson(reader, WeatherVideo[].class);
//
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        weatherVideos[0] = new WeatherVideo();
        weatherVideos[0].setTitle("Ban tin thoi tiet");
        weatherVideos[0].setDate("1/1/2017");
        weatherVideos[0].setDescription("Nguyen Thanh Tuan");
        weatherVideos[0].setVideoId("Fdhbvdpd-p8");
        return weatherVideos;
    }

    @Override
    protected void onPostExecute(WeatherVideo[] weatherVideos) {
        super.onPostExecute(weatherVideos);
        User.getInstance().setWeatherVideos(weatherVideos);
        DBAdapter dbAdapter = MyMethods.openDB(activity, "IDCity");
        if(dbAdapter.getAllRow().getCount() < 1){
            new GetIDCity(activity).execute("http://content.amobi.vn/api/thoitiet/city-id");
        }else{
            if (Variables.canRefresh == false){
                Intent intent = new Intent(activity, SlideActivity.class);
                activity.startActivity(intent);
                dbAdapter.close();
                activity.finish();
            }else{
                MyMethods.hideUpdateDialog();
            }
        }
        dbAdapter.close();
    }
}

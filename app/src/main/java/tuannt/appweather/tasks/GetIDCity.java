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

import tuannt.appweather.activities.FirstActivity;
import tuannt.appweather.activities.SlideActivity;
import tuannt.appweather.models.City;
import tuannt.appweather.utils.DBAdapter;
import tuannt.appweather.utils.MyMethods;
import tuannt.appweather.utils.Variables;

/**
 * Created by tuan on 19/02/2016.
 */
public class GetIDCity extends AsyncTask<String, Void, Void> {

    private Activity appCompatActivity;

    public GetIDCity(Activity appCompatActivity){
        this.appCompatActivity = appCompatActivity;
    }

    @Override
    protected Void doInBackground(String... params) {
        City[] cities = null;
        String link = params[0];
        try {
            URL url=new URL(link);

            InputStreamReader reader = new InputStreamReader(url.openStream(),"UTF-8");

            cities = new Gson().fromJson(reader, City[].class);

            DBAdapter dbAdapter = MyMethods.openDB(appCompatActivity, "IDCity");

            for (City city : cities){
                dbAdapter.insertRow(new String[]{city.get_id(), MyMethods.capitalize(city.getName())});
            }

            dbAdapter.close();

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
        if(Variables.isFirstStartApp == false) {
            Intent intent = new Intent(appCompatActivity, SlideActivity.class);
//            intent.putExtra("info", weather);
            appCompatActivity.startActivity(intent);
            appCompatActivity.finish();
        }else{
            Variables.isFirstStartApp = false;
            Intent intent = new Intent(appCompatActivity, FirstActivity.class);
//            intent.putExtra("info", weather);
            appCompatActivity.startActivity(intent);
            appCompatActivity.finish();
        }
    }
}

package tuannt.appweather.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Calendar;

import tuannt.appweather.R;
import tuannt.appweather.tasks.GetCityWeather;
import tuannt.appweather.utils.API;
import tuannt.appweather.utils.DBAdapter;
import tuannt.appweather.utils.MyLocation;
import tuannt.appweather.utils.MyMethods;
import tuannt.appweather.utils.Variables;

public class SplashActivity extends Activity {

    MyLocation myLocation;

    ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        background = (ImageView)findViewById(R.id.background);

        setMainBackground();

        SharedPreferences sharedPreferences = getSharedPreferences("tuannt", MODE_PRIVATE);

        MyMethods.setMyBackground(sharedPreferences.getInt("background_position", 0), (RelativeLayout) findViewById(R.id.rlBackground));

        Variables.saveIcon();
    }


    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("tuannt", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("canAutoLocation", true)) {
            myLocation = new MyLocation(this);
            if (myLocation.checkGPSAndNetwork()) {
                myLocation.connectApi();
            } else {
                myLocation.showSettingsAlert();
            }
        } else {
            DBAdapter dbAdapter = MyMethods.openDB(this, "Favorite");
            Cursor cursor = dbAdapter.getAllRow();
            cursor.moveToPosition(0);
            new GetCityWeather(this).execute(API.getApiCurrentCityById(cursor.getString(0)));
            Log.i("tuan", "tuan" + "http://content.amobi.vn/api/thoitiet/day?city_id=" + cursor.getString(0));
        }
//        getMyLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences("tuannt", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("canAutoLocation", true)) {
            myLocation.disconnectApi(myLocation);
        }
    }

    private void setMainBackground() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 18) {
            background.setImageResource(R.drawable.bg_fine_night);
        } else {
            background.setImageResource(R.drawable.bg_nice_day);
        }

    }

//    private void getMyLocation(){
//        gps = new GPSTracker(this);
//
//        // check if GPS enabled
//        if(gps.canGetLocation()){
//
//            double latitude = gps.getLatitude();
//            double longitude = gps.getLongitude();
//
////            latitude = 20.999939;
////            longitude = 105.871320;
//
//            new GetCityWeather(this).execute("http://content.amobi.vn/api/thoitiet/get-location?lon=" + longitude + "&lat=" + latitude);
//
//            // \n is for new line
//            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
//        }else{
//            // can't get location
//            // GPS or Network is not enabled
//            // Ask user to enable GPS/network in settings
//            gps.showSettingsAlert();
//        }
//    }
}
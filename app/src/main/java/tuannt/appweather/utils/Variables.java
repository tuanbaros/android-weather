package tuannt.appweather.utils;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import java.util.HashMap;

import tuannt.appweather.R;
import tuannt.appweather.adapters.LocationAdapter;
import tuannt.appweather.adapters.NextDayAdapter;
import tuannt.appweather.models.Setting;
import tuannt.appweather.pagetransformers.DepthPageTransformer;
import tuannt.appweather.pagetransformers.ZoomOutPageTransformer;

/**
 * Created by tuan on 19/02/2016.
 */
public class Variables {

    public static boolean canRefresh = false;

    public static LocationAdapter locationAdapter=null;

    public static PagerAdapter pagerAdapter=null;

    public static NextDayAdapter nextDayAdapter= null;

    public static TextView tvCity;

    public static boolean reSetLocation = false;

    public static boolean isFirstStartApp = false;

    public static boolean updateLocation = false;

    public static final String API_KEY = "AIzaSyBF47VNtWXhkslg0EwIsgo5Kh_Csj2dn_Y";

//    public static String nameColor[] = {"Blue", "Pink", "Green"};

    public static int colorBackground[] = {Color.parseColor("#039be5"), Color.parseColor("#ec407a"), Color.parseColor("#43a047")};

    public static ViewPager.PageTransformer[] pageTransformers = {new DepthPageTransformer(), new DepthPageTransformer(), new ZoomOutPageTransformer()};

//    public static String nameTransformer[] = {"None", "Depth", "Zoom out"};

    public static Setting setting;

    public static boolean fromNotify = false;

    public static boolean isFullScreen = false;

    public static HashMap<String, Integer> WEATHER_ICON = new HashMap<>();

    public static String[] ICON_ID = {
        "01d", "02d", "03d", "04d", "09d", "10d", "11d",
        "13d", "50d", "01n", "02n", "03n", "04n", "09n",
        "10n", "11n", "13n", "50n"
    };

    public static int[] ICON = {
        R.drawable.ic_01d, R.drawable.ic_02d, R.drawable.ic_03d, R.drawable.ic_04d,
        R.drawable.ic_09d, R.drawable.ic_10d, R.drawable.ic_11d, R.drawable.ic_13d,
        R.drawable.ic_50d, R.drawable.ic_01n, R.drawable.ic_02n, R.drawable.ic_03n,
        R.drawable.ic_09n, R.drawable.ic_10n, R.drawable.ic_11n, R.drawable.ic_13n,
        R.drawable.ic_50n, R.drawable.ic_04n
    };

    public static void saveIcon() {
        for (int i = 0; i < ICON_ID.length; i++) {
            WEATHER_ICON.put(ICON_ID[i], ICON[i]);
        }
    }

}

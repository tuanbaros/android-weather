package tuannt.appweather.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;

import tuannt.appweather.R;
import tuannt.appweather.models.User;
import tuannt.appweather.utils.API;
import tuannt.appweather.utils.MyMethods;
import tuannt.appweather.models.Weather;
import tuannt.appweather.utils.Variables;

/**
 * Created by tuan on 17/02/2016.
 */
public class FirstFragment extends Fragment {

    private TextView tvDes, tvTemp, tvTempUp, tvTempDown;
    private ImageView ivSunRays, ivSunDisc;
    private Animation rotation;
    private Weather weather;

    private TextView tvHumidity, tvWind;

    private RelativeLayout rlTempCityWeather, rlMoreCityWeather;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        User user = User.getInstance();

        weather = user.getWeather();

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_slide_page1, container, false);


        int temp = Math.round(Float.parseFloat(weather.getTemp())) - 273;
        int temp_max = Math.round(Float.parseFloat(weather.getTemp_max())) - 273;
        int temp_min = Math.round(Float.parseFloat(weather.getTemp_min())) - 273;


//        rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.sunray_rotate);
        ivSunRays = (ImageView)rootView.findViewById(R.id.ivSunRays);
        Log.i("ICON", "ICON: " + weather.getDescription());

        setMainIconStatus(weather.getDescription(), weather);
//        Calendar calendar = Calendar.getInstance();
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        if(hour > 18){
//            ivSunDisc.setVisibility(View.GONE);
//            ivSunRays.setImageResource(R.drawable.moon);
//        }else{
//            ivSunDisc.setVisibility(View.VISIBLE);
//            ivSunRays.setImageResource(R.drawable.sun_rays);
//        }
//        ivSunRays.setImageResource(R.drawable.sun);
//        ivSunRays.startAnimation(rotation);

        tvTemp = (TextView)rootView.findViewById(R.id.tvTemp);
        tvTemp.setText(temp + "\u2103");

        tvTempUp = (TextView)rootView.findViewById(R.id.tvTempUp);
        tvTempUp.setText(temp_max + "\u2103" + ",");

        tvTempDown = (TextView)rootView.findViewById(R.id.tvTempDown);
        tvTempDown.setText(temp_min + "\u2103");

        tvDes = (TextView)rootView.findViewById(R.id.tvDes);
        tvDes.setText(MyMethods.capitalize(weather.getDescription()));

        rlTempCityWeather = (RelativeLayout)rootView.findViewById(R.id.rlTempCity);

        rlTempCityWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlTempCityWeather.setVisibility(View.GONE);
                rlMoreCityWeather.setVisibility(View.VISIBLE);
            }
        });

        rlMoreCityWeather = (RelativeLayout)rootView.findViewById(R.id.rlMoreCity);

        rlMoreCityWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlMoreCityWeather.setVisibility(View.GONE);
                rlTempCityWeather.setVisibility(View.VISIBLE);
            }
        });

        tvHumidity = (TextView)rootView.findViewById(R.id.tvHumidity);
        tvWind = (TextView)rootView.findViewById(R.id.tvWind);

        tvWind.setText("" + weather.getWind_speed() + " km/h");
        tvHumidity.setText("" + weather.getHumidity() + "%");

        return rootView;
    }

    private void setMainIconStatus(String s, Weather weather){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        Picasso.with(getContext()).load(Variables.WEATHER_ICON.get(weather.getIcon()))
            .resize(500, 500)
//            .placeholder(R.drawable.clear_sky_night_icon)
//            .error(R.drawable.clear_sky_night_icon)
            .into(ivSunRays);

//        if(hour >= 18){
//            Picasso.with(getContext()).load(Variables.WEATHER_ICON.get(weather.getIcon()))
//                .resize(500, 500)
//                .placeholder(R.drawable.clear_sky_night_icon)
//                .error(R.drawable.clear_sky_night_icon)
//                .into(ivSunRays);
////            ivSunRays.setImageResource(R.drawable.clear_sky_night_icon);
//        }else{
//            Picasso.with(getContext()).load(Variables.WEATHER_ICON.get(weather.getIcon()))
//                .resize(500, 500)
//                .placeholder(R.drawable.clear_sky_night_icon)
//                .error(R.drawable.clear_sky_icon)
//                .into(ivSunRays);
////            ivSunRays.setImageResource(R.drawable.clear_sky_icon);
//        }

    }



}

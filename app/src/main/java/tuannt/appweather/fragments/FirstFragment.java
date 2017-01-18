package tuannt.appweather.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tuannt.appweather.R;
import tuannt.appweather.models.User;
import tuannt.appweather.models.Weather;
import tuannt.appweather.utils.MyMethods;
import tuannt.appweather.utils.Variables;

/**
 * Created by tuan on 17/02/2016.
 */
public class FirstFragment extends Fragment {
    private RelativeLayout rlTempCityWeather, rlMoreCityWeather;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        User user = User.getInstance();
        Weather weather = user.getWeather();

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_slide_page1, container, false);


        int temp = Math.round(Float.parseFloat(weather.getTemp())) - 273;
        int temp_max = Math.round(Float.parseFloat(weather.getTemp_max())) - 273;
        int temp_min = Math.round(Float.parseFloat(weather.getTemp_min())) - 273;
        ImageView ivSunRays = (ImageView) rootView.findViewById(R.id.ivSunRays);

        ivSunRays.setImageResource(Variables.WEATHER_ICON.get(weather.getIcon()));
        TextView tvTemp = (TextView) rootView.findViewById(R.id.tvTemp);
        tvTemp.setText(temp + "\u2103");
        TextView tvTempUp = (TextView) rootView.findViewById(R.id.tvTempUp);
        tvTempUp.setText(temp_max + "\u2103" + ",");
        TextView tvTempDown = (TextView) rootView.findViewById(R.id.tvTempDown);
        tvTempDown.setText(temp_min + "\u2103");
        TextView tvDes = (TextView) rootView.findViewById(R.id.tvDes);
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
        TextView tvHumidity = (TextView) rootView.findViewById(R.id.tvHumidity);
        TextView tvWind = (TextView) rootView.findViewById(R.id.tvWind);

        tvWind.setText("" + weather.getWind_speed() + " km/h");
        tvHumidity.setText("" + weather.getHumidity() + "%");

        return rootView;
    }
}

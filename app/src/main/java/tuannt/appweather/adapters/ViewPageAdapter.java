package tuannt.appweather.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.Calendar;

import tuannt.appweather.R;
import tuannt.appweather.models.User;
import tuannt.appweather.models.Weather;
import tuannt.appweather.tasks.GetCityWeather;
import tuannt.appweather.utils.API;
import tuannt.appweather.utils.CustomPagerEnum;
import tuannt.appweather.utils.DBAdapter;
import tuannt.appweather.utils.MyMethods;
import tuannt.appweather.utils.Variables;

/**
 * Created by tuan on 23/02/2016.
 */
public class ViewPageAdapter extends PagerAdapter {

    private Activity mContext;

    private TextView tvDes, tvTemp, tvTempUp, tvTempDown;
    private ImageView ivSunRays, ivSunDisc;
    private Animation rotation;
    private Weather weather;

    private TextView tvHumidity, tvWind;

    private RelativeLayout rlTempCityWeather, rlMoreCityWeather;

    public ViewPageAdapter(Activity context) {
        mContext = context;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(customPagerEnum.getLayoutResId(), collection, false);

        switch (position){
            case 0:
                ListView listView = (ListView)layout.findViewById(R.id.listView);

                if(Variables.locationAdapter == null){
                    Variables.locationAdapter = new LocationAdapter(mContext);
                }

                listView.setAdapter(Variables.locationAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        DBAdapter dbAdapter = MyMethods.openDB(mContext, "Favorite");
                        Cursor c = dbAdapter.getAllRow();
                        c.moveToPosition(position);
                        MyMethods.showUpdateDialog(mContext);
                        Variables.reSetLocation = true;
                        new GetCityWeather(mContext).execute(API.getApiCurrentCityById(c.getString(0)));
                    }
                });
                break;
            case 1:
                User user = User.getInstance();

                weather = user.getWeather();

                int temp = Math.round(Float.parseFloat(weather.getTemp())) - 273;
                int temp_max = Math.round(Float.parseFloat(weather.getTemp_max())) - 273;
                int temp_min = Math.round(Float.parseFloat(weather.getTemp_min())) - 273;


//        rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.sunray_rotate);
                ivSunRays = (ImageView)layout.findViewById(R.id.ivSunRays);
                Log.i("ICON", "ICON: " + weather.getDescription());
//        ivSunDisc = (ImageView)layout.findViewById(R.id.ivSunDisc);

                setMainIconStatus(ivSunRays, weather.getDescription());
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

                tvTemp = (TextView)layout.findViewById(R.id.tvTemp);
                tvTemp.setText(temp + "\u2103");

                tvTempUp = (TextView)layout.findViewById(R.id.tvTempUp);
                tvTempUp.setText(temp_max + "\u2103" + ",");

                tvTempDown = (TextView)layout.findViewById(R.id.tvTempDown);
                tvTempDown.setText(temp_min + "\u2103");

                tvDes = (TextView)layout.findViewById(R.id.tvDes);
                tvDes.setText(MyMethods.capitalize(weather.getDescription()));

                rlTempCityWeather = (RelativeLayout)layout.findViewById(R.id.rlTempCity);

                rlTempCityWeather.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rlTempCityWeather.setVisibility(View.GONE);
                        rlMoreCityWeather.setVisibility(View.VISIBLE);
                    }
                });

                rlMoreCityWeather = (RelativeLayout)layout.findViewById(R.id.rlMoreCity);

                rlMoreCityWeather.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rlMoreCityWeather.setVisibility(View.GONE);
                        rlTempCityWeather.setVisibility(View.VISIBLE);
                    }
                });

                tvHumidity = (TextView)layout.findViewById(R.id.tvHumidity);
                tvWind = (TextView)layout.findViewById(R.id.tvWind);

                tvWind.setText("" + weather.getWind_speed() + " km/h");
                tvHumidity.setText("" + weather.getHumidity() + "%");
                break;
            case 2:
                ListView listView1 = (ListView)layout.findViewById(R.id.listView);
                if(Variables.nextDayAdapter == null){
                    Variables.nextDayAdapter = new NextDayAdapter(mContext);
                }

                listView1.setAdapter(Variables.nextDayAdapter);
                break;
            case 3:
//                YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
//
//                FragmentTransaction transaction = mContext.getChildFragmentManager().beginTransaction();
//                transaction.add(R.id.youtube_layout, youTubePlayerFragment).commit();
//
//                youTubePlayerFragment.initialize(Variables.API_KEY, new YouTubePlayer.OnInitializedListener() {
//
//                    @Override
//                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
//                        if (!wasRestored) {
//                            player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
//                            User user = User.getInstance();
////                    player.loadVideo(user.getWeatherVideos()[0].getVideoId());
////                    player.play();
//                            player.cueVideo(user.getWeatherVideos()[0].getVideoId());
//                        }
//                    }
//
//                    @Override
//                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
//                        // YouTube error
//                        String errorMessage = error.toString();
//                        Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
//                        Log.d("errorMessage:", errorMessage);
//                    }
//
//
//                });
//                break;
        }

        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return CustomPagerEnum.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
        return mContext.getString(customPagerEnum.getTitleResId());
    }

    private void setMainIconStatus(ImageView view, String s){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        switch (s){
            case "clear sky":
                if(hour >= 18){
                    view.setImageResource(R.drawable.clear_sky_night_icon);
                }else{
                    view.setImageResource(R.drawable.clear_sky_icon);
                }
                break;
            case "light rain":
                if(hour >= 18){
                    view.setImageResource(R.drawable.light_rain_night_icon);
                }else{
                    view.setImageResource(R.drawable.light_rain_icon);
                }
                break;
            case "few clouds":
                if(hour >= 18){
                    view.setImageResource(R.drawable.few_clouds_night_icon);
                }else{
                    view.setImageResource(R.drawable.few_clouds_icon);
                }
                break;
        }
    }

}

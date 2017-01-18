package tuannt.appweather.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import tuannt.appweather.R;
import tuannt.appweather.models.User;
import tuannt.appweather.models.Weather;
import tuannt.appweather.utils.API;
import tuannt.appweather.utils.MyMethods;
import tuannt.appweather.utils.Variables;

/**
 * Created by tuan on 18/02/2016.
 */
public class NextDayAdapter extends BaseAdapter {

    private User user;

    LayoutInflater layoutInflater;

    public NextDayAdapter(Context context) {
        user = User.getInstance();
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return user.getWeatherArray().length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        convertView = layoutInflater.inflate(R.layout.item_list_next_day, parent, false);

        ViewHolder viewHolder = new ViewHolder();

        viewHolder.tvDay = (TextView)convertView.findViewById(R.id.tvDay);

        Weather weather = user.getWeatherArray()[position];

        viewHolder.tvDay.setText(user.getSixNextDay()[position]);

        viewHolder.tvDes = (TextView)convertView.findViewById(R.id.tvDes);
        viewHolder.tvDes.setText(MyMethods.capitalize(weather.getDescription()));

        viewHolder.tvMax = (TextView)convertView.findViewById(R.id.tvMax);
        viewHolder.tvMax.setText(MyMethods.getCTemp(weather.getTemp_max()));

        viewHolder.tvMin = (TextView)convertView.findViewById(R.id.tvMin);
        viewHolder.tvMin.setText(MyMethods.getCTemp(weather.getTemp_min()));

        viewHolder.ivIconStatus = (ImageView)convertView.findViewById(R.id.ivIconStatusWeather);
        Picasso.with(parent.getContext()).load(Variables.WEATHER_ICON.get(weather.getIcon())).resize(125, 125)
            .into
            (viewHolder.ivIconStatus);
        Log.i("abc", API.getApiIcon(weather.getIcon()));
        convertView.setTag(viewHolder);
//        MyMethods.setIconStatus(viewHolder.ivIconStatus, weather.getDescription());

        return convertView;
    }

    private static class ViewHolder{
        TextView tvDay;
        TextView tvDes;
        TextView tvMax;
        TextView tvMin;
        ImageView ivIconStatus;
    }
}

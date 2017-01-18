package tuannt.appweather.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import tuannt.appweather.R;
import tuannt.appweather.models.User;
import tuannt.appweather.utils.DBAdapter;
import tuannt.appweather.utils.MyLocation;
import tuannt.appweather.utils.MyMethods;
import tuannt.appweather.utils.Variables;

/**
 * Created by tuan on 19/02/2016.
 */
public class LocationAdapter extends BaseAdapter {

    private Activity context;

    private LayoutInflater layoutInflater;

    public LocationAdapter(Activity context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        DBAdapter dbAdapter = MyMethods.openDB(context, "Favorite");
        int count = dbAdapter.getAllRow().getCount();
        dbAdapter.close();
        return count;
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

        convertView = layoutInflater.inflate(R.layout.item_list_location, parent, false);

        ViewHolder holder = new ViewHolder();
        holder.linearLayout = (LinearLayout)convertView.findViewById(R.id.llBackground);
        holder.tvName = (TextView)convertView.findViewById(R.id.tvNameCity);
        holder.ivRemove = (ImageView)convertView.findViewById(R.id.ivRemove);
        holder.ivSelected = (ImageView)convertView.findViewById(R.id.ivSelected);
        convertView.setTag(holder);

        DBAdapter dbAdapter = MyMethods.openDB(context, "Favorite");
        final Cursor c = dbAdapter.getAllRow();
        c.moveToPosition(position);

        holder.tvName.setText("" + MyMethods.capitalize(c.getString(1)));

        User user = User.getInstance();

        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBAdapter dbAdapter = MyMethods.openDB(context, "Favorite");
                dbAdapter.deletePost(c.getString(0));
                dbAdapter.close();
                notifyDataSetChanged();
            }
        });


        if(position==0){
            holder.ivRemove.setImageResource(R.drawable.my_now_see);
                holder.ivRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyLocation myLocation = new MyLocation(context);
                        if(myLocation.checkGPSAndNetwork()){
                            Variables.reSetLocation = true;
                            Variables.updateLocation = true;
                            MyMethods.showUpdateDialog(context);
                            myLocation.connectApi();
                        }else{
                            myLocation.showSettingsAlert();
                        }
                    }
                });
        }


        if(c.getString(0).equals(user.getWeather().getCity_id())){
            if(position!=0){
                holder.ivRemove.setImageResource(R.drawable.my_location);
                holder.ivRemove.setEnabled(false);
            }
            holder.linearLayout.setBackgroundColor(Color.WHITE);
            holder.ivSelected.setImageResource(R.drawable.location_dot_selected);

        }

        dbAdapter.close();

        return convertView;
    }

    private static class ViewHolder {
        LinearLayout linearLayout;
        TextView tvName;
        ImageView ivSelected;
        ImageView ivRemove;
    }


}

package tuannt.appweather.fragments;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;

import tuannt.appweather.R;
import tuannt.appweather.adapters.LocationAdapter;
import tuannt.appweather.tasks.GetCityWeather;
import tuannt.appweather.utils.API;
import tuannt.appweather.utils.DBAdapter;
import tuannt.appweather.utils.MyMethods;
import tuannt.appweather.utils.Variables;

/**
 * Created by tuan on 19/02/2016.
 */
public class ZeroFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_slide_page0, container, false);

        ListView listView = (ListView)rootView.findViewById(R.id.listView);

        if(Variables.locationAdapter == null){
            Variables.locationAdapter = new LocationAdapter(getActivity());
        }

        listView.setAdapter(Variables.locationAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DBAdapter dbAdapter = MyMethods.openDB(getActivity(), "Favorite");
                Cursor c = dbAdapter.getAllRow();
                c.moveToPosition(position);
                MyMethods.showUpdateDialog(getActivity());
                Variables.reSetLocation = true;
                new GetCityWeather(getActivity()).execute(API.getApiCurrentCityById(c.getString(0)));
                dbAdapter.close();
            }
        });

        return rootView;
    }



    @Override
    public void onResume(){
        super.onResume();
        MyMethods.updateCityName();
    }

}

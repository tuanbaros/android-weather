package tuannt.appweather.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import tuannt.appweather.R;
import tuannt.appweather.adapters.NextDayAdapter;
import tuannt.appweather.utils.Variables;

/**
 * Created by tuan on 18/02/2016.
 */
public class SecondFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_slide_page2, container, false);

        ListView listView = (ListView)rootView.findViewById(R.id.listView);
        if(Variables.nextDayAdapter == null){
            Variables.nextDayAdapter = new NextDayAdapter(getActivity());
        }

        listView.setAdapter(Variables.nextDayAdapter);

        return rootView;
    }

}

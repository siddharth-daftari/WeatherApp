package com.apoorv.android.weatherapp;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apoorv.android.weatherapp.dummy.DefaultList;
import com.apoorv.android.weatherapp.dummy.DummyContent;
import com.apoorv.android.weatherapp.helper.Constants;
import com.apoorv.android.weatherapp.helper.GetTimeZone;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Date;

/**
 * A fragment representing a single City detail screen.
 * This fragment is either contained in a {@link CityListActivity}
 * in two-pane mode (on tablets) or a {@link CityDetailActivity}
 * on handsets.
 */
public class CityDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CityDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            Activity activity = this.getActivity();
            RelativeLayout relativeLayout = (RelativeLayout) activity.findViewById(R.id.city_detail_relative_layout);
            DefaultList.CityItem cityItem = DefaultList.getCityDetails(this.getContext(), String.valueOf(getArguments().get(ARG_ITEM_ID)));

            if(cityItem != null) {
                //Setting city name
                TextView cityNameTextView = (TextView) relativeLayout.findViewById(R.id.city_detail_name_value);
                cityNameTextView.setText(cityItem.name);

                //Setting day date
                TextView cityDetailDate = (TextView) relativeLayout.findViewById(R.id.city_detail_date);
                HashMap<String, String> dateDetails = null;
                try {
                    dateDetails = GetTimeZone.getTimeDetailsWithoutApiCall(cityItem.timeZone);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(dateDetails!=null){
                    String dateFromTimeZone = dateDetails.get(Constants.TIMEZONE_API_CALC_FIELD_DATE);
                    SimpleDateFormat df = new SimpleDateFormat("EEEE MMM dd yyyy");

                    cityDetailDate.setText(df.format(new Date(dateFromTimeZone)));

                }

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.city_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.city_detail)).setText("hello");
        }

        return rootView;
    }
}

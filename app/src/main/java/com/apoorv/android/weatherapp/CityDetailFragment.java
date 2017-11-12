package com.apoorv.android.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.CustomFab;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.apoorv.android.weatherapp.dummy.DefaultList;
import com.apoorv.android.weatherapp.dummy.DummyContent;
import com.apoorv.android.weatherapp.helper.Constants;
import com.apoorv.android.weatherapp.helper.ExceptionMessageHandler;
import com.apoorv.android.weatherapp.helper.GetCurrentWeather;
import com.apoorv.android.weatherapp.helper.GetForcastWeather;
import com.apoorv.android.weatherapp.helper.GetTimeZone;
import com.apoorv.android.weatherapp.helper.LogHelper;
import com.apoorv.android.weatherapp.helper.RequestClass;

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
    public static final String ARG_ITEM_ID = "item_id";
    public static CityDetailFragment obj;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CityDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogHelper.logMessage("Apoorv", "Came to detail fragment with key"+String.valueOf(getArguments().get(ARG_ITEM_ID)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.city_detail, container, false);

        // Show the Up button in the action bar.
        android.support.v7.widget.Toolbar toolbar = rootView.findViewById(R.id.detail_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        System.out.println("Apoorv actionbar"+actionBar);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               LogHelper.logMessage("Apoorv","Back button pressed");
                getActivity().navigateUpTo(new Intent(getActivity(), CityListActivity.class));
            }
        });



        try {
            if (getArguments().containsKey(ARG_ITEM_ID)) {

                HashMap<String, String> responseHashMap = null;

                RelativeLayout relativeLayout = rootView.findViewById(R.id.city_detail_relative_layout);
                int index = getArguments().getInt(ARG_ITEM_ID);
                
                DefaultList.CityItem cityItem = DefaultList.getCityDetails(this.getContext(), index);
                System.out.println(cityItem.getCityName());

                if(cityItem != null) {
                    //Setting city name
                    TextView cityNameTextView = relativeLayout.findViewById(R.id.city_detail_name_value);
                    cityNameTextView.setText(cityItem.name);

                    //Setting "You are here" icon
                    CustomFab floatingActionButton = rootView.findViewById(R.id.fab);
                    if(floatingActionButton != null && cityItem.isCurrent){
                        floatingActionButton.setVisibility(View.VISIBLE);
                    }

                    //Setting day date
                    TextView cityDetailDate = relativeLayout.findViewById(R.id.city_detail_date);


                    responseHashMap = GetTimeZone.getTimeDetailsWithoutApiCall(cityItem.timeZone);

                    if(responseHashMap!=null){
                        String dateFromTimeZone = responseHashMap.get(Constants.TIMEZONE_API_CALC_FIELD_DATE);
                        SimpleDateFormat df = new SimpleDateFormat("EEEE MMM dd yyyy");

                        cityDetailDate.setText(df.format(new Date(dateFromTimeZone)));

                    }

                    //Setting Weather and temperature
                    RequestClass.startRequestQueue();
                    new GetCurrentWeather().processWeatherApiCurrent(getContext(), cityItem.latitude, cityItem.longitude, Constants.ACTION_UPDATE_CITY_DETAIL_UI, rootView, null);

                    //Setting today's 3 hour weather and forecast for 4 days
                    RequestClass.startRequestQueue();
                    HashMap hm = new HashMap<String, Object>();
                    hm.put(Constants.TIMEZONE, cityItem.timeZone);
                    LogHelper.logMessage("Apoorv","Trying to get forcast for "+cityItem.name);
                    new GetForcastWeather().processWeatherApiForecast(getContext(), cityItem.latitude, cityItem.longitude, Constants.ACTION_UPDATE_CITY_DETAIL_UI_FOR_WEATHER, rootView, hm);

                }
            }
        } catch (JSONException e) {
            ExceptionMessageHandler.handleError(getActivity(), e.getMessage(), e, null);
        }

        return rootView;
    }
}

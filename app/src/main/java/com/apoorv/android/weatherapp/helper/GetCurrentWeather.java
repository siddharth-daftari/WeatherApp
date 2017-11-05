package com.apoorv.android.weatherapp.helper;

import android.app.Activity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.apoorv.android.weatherapp.CityListActivity;
import com.apoorv.android.weatherapp.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by siddharthdaftari on 11/2/17.
 */

public class GetCurrentWeather {

    public void processWeatherApiCurrent(String latitude, String longitude, final String action, final Activity activity, final HashMap<String, Object> extraParams) throws JSONException{

        String urlString = "https://api.openweathermap.org/data/2.5/weather?lat="
                + latitude + "&lon=" + longitude + "&APPID=" + Secrets.SECRET_FOR_WEATHER_API;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, urlString,null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {

                            HashMap returnHashMap = new HashMap<String, String>();

                            JSONObject jsonObjectMain = jsonObject.getJSONObject(Constants.CURRENT_WEATHER_API_PROP_MAIN);
                            Double currentTemperature = jsonObjectMain.getDouble(Constants.CURRENT_WEATHER_API_PROP_MAIN_TEMP);
                            Double temperatureMax = jsonObjectMain.getDouble(Constants.CURRENT_WEATHER_API_PROP_MAIN_TEMP_MAX);
                            Double temperatureMin = jsonObjectMain.getDouble(Constants.CURRENT_WEATHER_API_PROP_MAIN_TEMP_MIN);

                            JSONObject jsonObjectWeather = jsonObject.getJSONArray(Constants.CURRENT_WEATHER_API_PROP_WEATHER).getJSONObject(0);
                            String currentWeather = jsonObjectWeather.getString(Constants.CURRENT_WEATHER_API_PROP_WEATHER_MAIN);

                            System.out.println("currentTemperature: " + currentTemperature);
                            System.out.println("temperatureMax: " + temperatureMax);
                            System.out.println("temperatureMin: " + temperatureMin);
                            System.out.println("currentWeather: " + currentWeather);

                            returnHashMap.put(Constants.CURRENT_WEATHER_API_PROP_MAIN_TEMP, currentTemperature);
                            returnHashMap.put(Constants.CURRENT_WEATHER_API_PROP_MAIN_TEMP_MAX, temperatureMax);
                            returnHashMap.put(Constants.CURRENT_WEATHER_API_PROP_MAIN_TEMP_MIN, temperatureMin);
                            returnHashMap.put(Constants.CURRENT_WEATHER_API_PROP_MAIN_TEMP, currentTemperature);
                            returnHashMap.put(Constants.CURRENT_WEATHER_API_PROP_WEATHER, currentWeather);

                            updateUI(action, activity, returnHashMap, extraParams);


                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                });
        RequestClass.getRequestQueue().add(jsObjRequest);
    }

    public void updateUI(String action, Activity activity, HashMap<String, String> returnHashMap, HashMap<String,Object> extraparams){

        switch (action) {
            case Constants.ACTION_UPDATE_CITY_DETAIL_UI:
                RelativeLayout relativeLayout = (RelativeLayout) activity.findViewById(R.id.city_detail_relative_layout);

                TextView cityNameTextView = (TextView) relativeLayout.findViewById(R.id.city_detail_name_value);
                cityNameTextView.setText(returnHashMap.get(Constants.CURRENT_WEATHER_API_PROP_WEATHER));
                break;
            case Constants.ACTION_UPDATE_CITY_LIST_ITEM_FOR_TEMPERATURE:
                TextView temperatureView = (TextView) extraparams.get("temperatureView");
                temperatureView.setText(String.valueOf(returnHashMap.get(Constants.CURRENT_WEATHER_API_PROP_MAIN_TEMP)));
//                CityListActivity.SimpleItemRecyclerViewAdapter givenAdapter = (CityListActivity.SimpleItemRecyclerViewAdapter) extraparams.get("adapter");
//                givenAdapter.notifyDataSetChanged();
        }


    }
}

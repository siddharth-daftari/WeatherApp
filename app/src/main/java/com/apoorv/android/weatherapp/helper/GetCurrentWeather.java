package com.apoorv.android.weatherapp.helper;

import java.io.BufferedReader;
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

    private HashMap<String, String> processWeatherApiCurrent(String latitude, String longitude) throws JSONException{

        HashMap returnHashMap = new HashMap<String, String>();

        JSONObject jsonObject = CallApi.callApi("https://api.openweathermap.org/data/2.5/weather?lat="
				+ latitude + "&lon=" + longitude + "&APPID=" + Secrets.SECRET_FOR_WEATHER_API);

        JSONObject jsonObjectMain = jsonObject.getJSONObject(Constants.CURRENT_WEATHER_API_PROP_MAIN);
        Double currentTemperature = jsonObjectMain.getDouble(Constants.CURRENT_WEATHER_API_PROP_MAIN_TEMP);
        Double temperatureMax = jsonObjectMain.getDouble(Constants.CURRENT_WEATHER_API_PROP_MAIN_TEMP_MAX);
        Double temperatureMin = jsonObjectMain.getDouble(Constants.CURRENT_WEATHER_API_PROP_MAIN_TEMP_MIN);

        JSONObject jsonObjectWeather = jsonObject.getJSONArray(Constants.CURRENT_WEATHER_API_PROP_WEATHER).getJSONObject(0);
        String currentWeather = jsonObjectWeather.getString(Constants.CURRENT_WEATHER_API_PROP_WEATHER_MAIN);

        System.out.println("currentTemperature: " + currentTemperature );
        System.out.println("temperatureMax: " + temperatureMax );
        System.out.println("temperatureMin: " + temperatureMin );
        System.out.println("currentWeather: " + currentWeather );

        returnHashMap.put(Constants.CURRENT_WEATHER_API_PROP_MAIN_TEMP, currentTemperature);
        returnHashMap.put(Constants.CURRENT_WEATHER_API_PROP_MAIN_TEMP_MAX, temperatureMax);
        returnHashMap.put(Constants.CURRENT_WEATHER_API_PROP_MAIN_TEMP_MIN, temperatureMin);
        returnHashMap.put(Constants.CURRENT_WEATHER_API_PROP_MAIN_TEMP, currentTemperature);
        returnHashMap.put(Constants.CURRENT_WEATHER_API_PROP_WEATHER, currentWeather);

        return returnHashMap;

    }
}

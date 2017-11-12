package com.apoorv.android.weatherapp.helper;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.apoorv.android.weatherapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by siddharthdaftari on 11/2/17.
 */

public class GetCurrentWeather {

    public void processWeatherApiCurrent(final Context context, String latitude, String longitude, final String action, final View view, final HashMap<String, Object> extraParams) throws JSONException{

        String urlString = "https://api.openweathermap.org/data/2.5/weather?lat="
                + latitude + "&lon=" + longitude + "&APPID=" + Secrets.getSecretForWeatherApi() + "&units="+SettingsPreference.getSelectedUnitParam();

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
                            String currentWeatherIcon = jsonObjectWeather.getString(Constants.CURRENT_WEATHER_API_PROP_WEATHER_ICON);

                            LogHelper.logMessage("Apoorv","currentTemperature: " + currentTemperature);
                            LogHelper.logMessage("Apoorv","temperatureMax: " + temperatureMax);
                            LogHelper.logMessage("Apoorv","temperatureMin: " + temperatureMin);
                            LogHelper.logMessage("Apoorv","currentWeather: " + currentWeather);
                            LogHelper.logMessage("Apoorv","currentWeatherIcon: " + currentWeatherIcon);

                            returnHashMap.put(Constants.CURRENT_WEATHER_API_PROP_MAIN_TEMP, (int)Math.round(currentTemperature));
                            returnHashMap.put(Constants.CURRENT_WEATHER_API_PROP_MAIN_TEMP_MAX, (int)Math.round(temperatureMax));
                            returnHashMap.put(Constants.CURRENT_WEATHER_API_PROP_MAIN_TEMP_MIN, (int)Math.round(temperatureMin));
                            returnHashMap.put(Constants.CURRENT_WEATHER_API_PROP_MAIN_TEMP, (int)Math.round(currentTemperature));
                            returnHashMap.put(Constants.CURRENT_WEATHER_API_PROP_WEATHER, currentWeather);
                            returnHashMap.put(Constants.CURRENT_WEATHER_API_PROP_WEATHER_ICON, currentWeatherIcon);

                            updateUI(context, action, view, returnHashMap, extraParams);


                        }catch (JSONException e){
                          ExceptionMessageHandler.handleError(context, e.getMessage(), e, null);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                       ExceptionMessageHandler.handleError(context, error.getMessage(), error, null);
                    }
                });
        RequestClass.getRequestQueue().add(jsObjRequest);
    }

    public void updateUI(Context context, String action, View activity, HashMap<String, String> returnHashMap, HashMap<String,Object> extraparams){

        switch (action) {
            case Constants.ACTION_UPDATE_CITY_DETAIL_UI:
                RelativeLayout relativeLayout = (RelativeLayout) activity.findViewById(R.id.city_detail_relative_layout);

                //Setting weather icon
                ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.city_detail_weather_icon);
                imageView.setImageDrawable(context.getDrawable( getDrawableResourceId("icon_" + returnHashMap.get(Constants.CURRENT_WEATHER_API_PROP_WEATHER_ICON)) ));

                //Setting weather status
                TextView cityWeatherTextView = (TextView) relativeLayout.findViewById(R.id.city_detail_weather_status);
                cityWeatherTextView.setText(returnHashMap.get(Constants.CURRENT_WEATHER_API_PROP_WEATHER));

                //Setting current temperature
                TextView cityCurrentTempTextView = (TextView) relativeLayout.findViewById(R.id.city_detail_current_temp_value);
                cityCurrentTempTextView.setText(String.valueOf(returnHashMap.get(Constants.CURRENT_WEATHER_API_PROP_MAIN_TEMP))+" "+SettingsPreference.getSelectedUnitSuffix());

                //Setting Max temperature
                TextView cityMaxTempTextView = (TextView) relativeLayout.findViewById(R.id.city_detail_max_temp_value);
                cityMaxTempTextView.setText(String.valueOf(returnHashMap.get(Constants.CURRENT_WEATHER_API_PROP_MAIN_TEMP_MAX))+" "+SettingsPreference.getSelectedUnitSuffix());

                //Setting current temperature
                TextView cityMinTempTextView = (TextView) relativeLayout.findViewById(R.id.city_detail_min_temp_value);
                cityMinTempTextView.setText(String.valueOf(returnHashMap.get(Constants.CURRENT_WEATHER_API_PROP_MAIN_TEMP_MIN))+" "+SettingsPreference.getSelectedUnitSuffix());

                break;
            case Constants.ACTION_UPDATE_CITY_LIST_ITEM_FOR_TEMPERATURE:
                TextView temperatureView = (TextView) extraparams.get("temperatureView");
                temperatureView.setText(String.valueOf(returnHashMap.get(Constants.CURRENT_WEATHER_API_PROP_MAIN_TEMP))+" "+SettingsPreference.getSelectedUnitSuffix());
        }
    }

    public int getDrawableResourceId(String resourceName){
        int returnValue = 0;

        switch (resourceName){
            case Constants.ICON_01D:
                return R.drawable.icon_01d;
            case Constants.ICON_01N:
                return R.drawable.icon_01n;
            case Constants.ICON_02D:
                return R.drawable.icon_02d;
            case Constants.ICON_02N:
                return R.drawable.icon_02n;
            case Constants.ICON_03D:
                return R.drawable.icon_03d;
            case Constants.ICON_03N:
                return R.drawable.icon_03n;
            case Constants.ICON_04D:
                return R.drawable.icon_04d;
            case Constants.ICON_04N:
                return R.drawable.icon_04n;
            case Constants.ICON_09D:
                return R.drawable.icon_09d;
            case Constants.ICON_09N:
                return R.drawable.icon_09n;
            case Constants.ICON_10D:
                return R.drawable.icon_10d;
            case Constants.ICON_10N:
                return R.drawable.icon_10n;
            case Constants.ICON_11D:
                return R.drawable.icon_11d;
            case Constants.ICON_11N:
                return R.drawable.icon_11n;
            case Constants.ICON_13D:
                return R.drawable.icon_13d;
            case Constants.ICON_13N:
                return R.drawable.icon_13n;
            case Constants.ICON_50D:
                return R.drawable.icon_50d;
            case Constants.ICON_50N:
                return R.drawable.icon_50n;
            default:
                return R.drawable.icon_01d;
        }
    }
}

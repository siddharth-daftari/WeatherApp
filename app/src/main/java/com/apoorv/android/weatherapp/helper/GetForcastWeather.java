package com.apoorv.android.weatherapp.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.apoorv.android.weatherapp.R;
import com.apoorv.android.weatherapp.model.CityDetailModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by siddharthdaftari on 11/2/17.
 */

public class GetForcastWeather {

    public void processWeatherApiForecast(final Context context, String latitude, String longitude, final String action, final View view, final HashMap<String, Object> extraParams) throws JSONException {

        String urlString = "https://api.openweathermap.org/data/2.5/forecast?lat="
                + latitude + "&lon=" + longitude + "&APPID=" + Secrets.SECRET_FOR_WEATHER_API + "&units=" + SettingsPreference.getSelectedUnitParam();


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, urlString, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            HashMap returnHashMap = new HashMap<String, String>();

                            List<CityDetailModel> cityDetailList = new ArrayList<CityDetailModel>();
                            CityDetailModel cityDetailModel = null;
                            JSONObject jsonObjectTemp = null;

                            jsonObjectTemp = jsonObject.getJSONObject(Constants.FORECAST_WEATHER_API_PROP_CITY);
                            String cityName = jsonObjectTemp.getString(Constants.FORECAST_WEATHER_API_PROP_CITY_NAME);

                            jsonObjectTemp = null;
                            JSONArray jsonArrayList = jsonObject.getJSONArray(Constants.FORECAST_WEATHER_API_PROP_LIST);

                            int index = 0;
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                cityDetailModel = new CityDetailModel();
                                jsonObjectTemp = (JSONObject) jsonArrayList.get(i);

                                cityDetailModel.setCityName(cityName);
                                cityDetailModel.setDateValue(jsonObjectTemp.getString(Constants.FORECAST_WEATHER_API_PROP_DATE), extraParams.get(Constants.TIMEZONE).toString());

                                JSONObject jsonObjectMain = jsonObjectTemp.getJSONObject(Constants.FORECAST_WEATHER_API_PROP_LIST_MAIN);
                                cityDetailModel.setTemp(Double.valueOf(jsonObjectMain.getDouble(Constants.FORECAST_WEATHER_API_PROP_LIST_MAIN_TEMP)));
                                cityDetailModel.setTempHigh(jsonObjectMain.getDouble(Constants.FORECAST_WEATHER_API_PROP_LIST_MAIN_TEMP_MAX));
                                cityDetailModel.setTempLow(jsonObjectMain.getDouble(Constants.FORECAST_WEATHER_API_PROP_LIST_MAIN_TEMP_MIN));


                                JSONObject jsonObjectWeather = jsonObjectTemp.getJSONArray(Constants.FORECAST_WEATHER_API_PROP_LIST_WEATHER).getJSONObject(0);
                                cityDetailModel.setWeatherStatus(jsonObjectWeather.getString(Constants.FORECAST_WEATHER_API_PROP_LIST_WEATHER_MAIN));
                                cityDetailModel.setWeatherIcon(jsonObjectWeather.getString(Constants.FORECAST_WEATHER_API_PROP_LIST_WEATHER_ICON));

                                cityDetailModel.setIndex(index);
                                index++;
                                cityDetailList.add(cityDetailModel);
                            }

                            extraParams.put(Constants.CITY_DETAIL_LIST, cityDetailList);
                            updateUI(context, action, view, returnHashMap, extraParams);

                        } catch (JSONException e) {
                           ExceptionMessageHandler.handleError(context, e.getMessage(), e, null);
                        } catch (ParseException e) {
                            ExceptionMessageHandler.handleError(context, e.getMessage(), e, null);
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

    public void updateUI(Context context, String action, View activity, HashMap<String, String> returnHashMap, HashMap<String, Object> extraparams) throws ParseException {

        switch (action) {
            case Constants.ACTION_UPDATE_CITY_DETAIL_UI_FOR_WEATHER:
                ArrayList<CityDetailModel> cityDetailList = (ArrayList<CityDetailModel>) extraparams.get(Constants.CITY_DETAIL_LIST);

                ArrayList<ArrayList<CityDetailModel>> arrayList = new ArrayList<ArrayList<CityDetailModel>>();

                //Segregating the list by day
                Date prevDate = null;
                ArrayList<CityDetailModel> tempCityDetailList = new ArrayList<CityDetailModel>();
                for (int i = 0; i < cityDetailList.size(); i++) {

                    CityDetailModel cityDetailModel = cityDetailList.get(i);
                    Date currDate = cityDetailModel.getDateInProperFormat();

                    if (prevDate != null && prevDate.getDate() != currDate.getDate()) {
                        arrayList.add(tempCityDetailList);
                        tempCityDetailList = new ArrayList<CityDetailModel>();
                    }

                    tempCityDetailList.add(cityDetailModel);
                    prevDate = currDate;
                }

                if (arrayList.size() < 5) {

                    LinearLayout fifthLayout = activity.findViewById(R.id.day5_details);
                    ((ViewGroup) fifthLayout.getParent()).removeView(fifthLayout);
                }


                System.out.println("------------------------Day" + "0" + "------------------------");
                for (int i = 0; i < 8; i++) {
                    CityDetailModel cityDetailModel = cityDetailList.get(i);

                    cityDetailModel.printValues();
                    TextView textView = (TextView) activity.findViewById(getDay1ResourceIds(i + 1).get("time"));
                    textView.setText(new SimpleDateFormat("h:mm a").format(cityDetailModel.getDateInProperFormat()) + "  ");

                    textView = (TextView) activity.findViewById(getDay1ResourceIds(i + 1).get("weather"));
                    textView.setText(cityDetailModel.getWeatherStatus().toString());

                    textView = (TextView) activity.findViewById(getDay1ResourceIds(i + 1).get("temp"));
                    textView.setText(cityDetailModel.getTemp() + " " + SettingsPreference.getSelectedUnitSuffix());
                }

                for (int i = 1; i < arrayList.size(); i++) {
                    ArrayList<CityDetailModel> cityDetailModelList = arrayList.get(i);
                    System.out.println("------------------------Day" + i + "------------------------");

                    Date tempDate = cityDetailModelList.get(0).getDateInProperFormat();
                    tempDate = new Date(tempDate.getYear(), tempDate.getMonth(), tempDate.getDate(), 12, 00);
                    ArrayList<Date> listOfDates = new ArrayList<Date>();
                    for (int k = 0; k < cityDetailModelList.size(); k++) {
                        listOfDates.add(cityDetailModelList.get(k).getDateInProperFormat());
                    }
                    Date nearestToNoonTime = nearestNoonTime(listOfDates, tempDate);
                    listOfDates.clear();

                    Double maxTemp = 0.0;
                    Double minTemp = 9999.0;

                    for (int j = 0; j < cityDetailModelList.size(); j++) {
                        CityDetailModel cityDetailModel = cityDetailModelList.get(j);
                        TextView textView = null;
                        switch (i) {
                            case 1:
                                cityDetailModel.printValues();

                                if (nearestToNoonTime.compareTo(cityDetailModel.getDateInProperFormat()) == 0) {
                                    System.out.println("nearestToNoonTime: " + nearestToNoonTime + "cityDetailModel.getDateInProperFormat(): " + cityDetailModel.getDateInProperFormat());
                                    textView = (TextView) activity.findViewById(R.id.day2_textView);
                                    Date date = cityDetailModel.getDateInProperFormat();
                                    textView.setText(new SimpleDateFormat("EEEE").format(date));

                                    //Setting weather icon
                                    ImageView imageView = (ImageView) activity.findViewById(R.id.day2_details_weather_icon);
                                    imageView.setImageDrawable(context.getDrawable(getDrawableResourceId("icon_" + cityDetailModel.getWeatherIcon())));

                                    textView = (TextView) activity.findViewById(R.id.day2_details_row1_weather_status);
                                    textView.setText(cityDetailModel.getWeatherStatus().toString());

                                    textView = (TextView) activity.findViewById(R.id.day2_details_row1_temp_value);
                                    textView.setText(cityDetailModel.getTemp() + " " + SettingsPreference.getSelectedUnitSuffix());
                                }

                                if( cityDetailModel.getTempHighInDouble() > maxTemp) {
                                    maxTemp = cityDetailModel.getTempHighInDouble();
                                    textView = (TextView) activity.findViewById(R.id.day2_details_row2_max_temp_value);
                                    textView.setText(cityDetailModel.getTempHigh() + " " + SettingsPreference.getSelectedUnitSuffix());
                                }

                                if( cityDetailModel.getTempLowInDouble() < minTemp) {
                                    minTemp = cityDetailModel.getTempLowInDouble();
                                    textView = (TextView) activity.findViewById(R.id.day2_details_row2_min_temp_value);
                                    textView.setText(cityDetailModel.getTempLow() + " " + SettingsPreference.getSelectedUnitSuffix());
                                }

                                break;
                            case 2:
                                cityDetailModel.printValues();

                                if (nearestToNoonTime.compareTo(cityDetailModel.getDateInProperFormat()) == 0) {

                                    System.out.println("nearestToNoonTime: " + nearestToNoonTime + "cityDetailModel.getDateInProperFormat(): " + cityDetailModel.getDateInProperFormat());
                                    textView = (TextView) activity.findViewById(R.id.day3_textView);
                                    Date date = cityDetailModel.getDateInProperFormat();
                                    textView.setText(new SimpleDateFormat("EEEE").format(date));

                                    //Setting weather icon
                                    ImageView imageView = (ImageView) activity.findViewById(R.id.day3_details_weather_icon);
                                    imageView.setImageDrawable(context.getDrawable(getDrawableResourceId("icon_" + cityDetailModel.getWeatherIcon())));

                                    textView = (TextView) activity.findViewById(R.id.day3_details_row1_weather_status);
                                    textView.setText(cityDetailModel.getWeatherStatus().toString());

                                    textView = (TextView) activity.findViewById(R.id.day3_details_row1_temp_value);
                                    textView.setText(cityDetailModel.getTemp() + " " + SettingsPreference.getSelectedUnitSuffix());

                                }

                                if( cityDetailModel.getTempHighInDouble() > maxTemp) {
                                    maxTemp = cityDetailModel.getTempHighInDouble();
                                    textView = (TextView) activity.findViewById(R.id.day3_details_row2_max_temp_value);
                                    textView.setText(cityDetailModel.getTempHigh() + " " + SettingsPreference.getSelectedUnitSuffix());
                                }

                                if( cityDetailModel.getTempLowInDouble() < minTemp) {
                                    minTemp = cityDetailModel.getTempLowInDouble();
                                    textView = (TextView) activity.findViewById(R.id.day3_details_row2_min_temp_value);
                                    textView.setText(cityDetailModel.getTempLow() + " " + SettingsPreference.getSelectedUnitSuffix());
                                }
                                break;
                            case 3:
                                cityDetailModel.printValues();
                                if (nearestToNoonTime.compareTo(cityDetailModel.getDateInProperFormat()) == 0) {

                                    System.out.println("nearestToNoonTime: " + nearestToNoonTime + "cityDetailModel.getDateInProperFormat(): " + cityDetailModel.getDateInProperFormat());
                                    textView = (TextView) activity.findViewById(R.id.day4_textView);
                                    Date date = cityDetailModel.getDateInProperFormat();
                                    textView.setText(new SimpleDateFormat("EEEE").format(date));

                                    //Setting weather icon
                                    ImageView imageView = (ImageView) activity.findViewById(R.id.day4_details_weather_icon);
                                    imageView.setImageDrawable(context.getDrawable(getDrawableResourceId("icon_" + cityDetailModel.getWeatherIcon())));

                                    textView = (TextView) activity.findViewById(R.id.day4_details_row1_weather_status);
                                    textView.setText(cityDetailModel.getWeatherStatus().toString());

                                    textView = (TextView) activity.findViewById(R.id.day4_details_row1_temp_value);
                                    textView.setText(cityDetailModel.getTemp() + " " + SettingsPreference.getSelectedUnitSuffix());
                                }
                                if( cityDetailModel.getTempHighInDouble() > maxTemp) {
                                    maxTemp = cityDetailModel.getTempHighInDouble();
                                    textView = (TextView) activity.findViewById(R.id.day4_details_row2_max_temp_value);
                                    textView.setText(cityDetailModel.getTempHigh() + " " + SettingsPreference.getSelectedUnitSuffix());
                                }

                                if( cityDetailModel.getTempLowInDouble() < minTemp) {
                                    minTemp = cityDetailModel.getTempLowInDouble();
                                    textView = (TextView) activity.findViewById(R.id.day4_details_row2_min_temp_value);
                                    textView.setText(cityDetailModel.getTempLow() + " " + SettingsPreference.getSelectedUnitSuffix());
                                }
                                break;
                            case 4:
                                cityDetailModel.printValues();
                                if (nearestToNoonTime.compareTo(cityDetailModel.getDateInProperFormat()) == 0) {

                                    System.out.println("nearestToNoonTime: " + nearestToNoonTime + "cityDetailModel.getDateInProperFormat(): " + cityDetailModel.getDateInProperFormat());
                                    textView = (TextView) activity.findViewById(R.id.day5_textView);
                                    Date date = cityDetailModel.getDateInProperFormat();
                                    textView.setText(new SimpleDateFormat("EEEE").format(date));

                                    //Setting weather icon
                                    ImageView imageView = (ImageView) activity.findViewById(R.id.day5_details_weather_icon);
                                    imageView.setImageDrawable(context.getDrawable(getDrawableResourceId("icon_" + cityDetailModel.getWeatherIcon())));

                                    textView = (TextView) activity.findViewById(R.id.day5_details_row1_weather_status);
                                    textView.setText(cityDetailModel.getWeatherStatus().toString());

                                    textView = (TextView) activity.findViewById(R.id.day5_details_row1_temp_value);
                                    textView.setText(cityDetailModel.getTemp() + " " + SettingsPreference.getSelectedUnitSuffix());
                                }
                                if( cityDetailModel.getTempHighInDouble() > maxTemp) {
                                    maxTemp = cityDetailModel.getTempHighInDouble();
                                    textView = (TextView) activity.findViewById(R.id.day5_details_row2_max_temp_value);
                                    textView.setText(cityDetailModel.getTempHigh() + " " + SettingsPreference.getSelectedUnitSuffix());
                                }

                                if( cityDetailModel.getTempLowInDouble() < minTemp) {
                                    minTemp = cityDetailModel.getTempLowInDouble();
                                    textView = (TextView) activity.findViewById(R.id.day5_details_row2_min_temp_value);
                                    textView.setText(cityDetailModel.getTempLow() + " " + SettingsPreference.getSelectedUnitSuffix());
                                }
                                break;
                        }
                    }
                }
                break;
        }
    }

    public Date nearestNoonTime(List<Date> dates, Date date) {
        final long now = date.getTime();

        Date closest = Collections.min(dates, new Comparator<Date>() {
            public int compare(Date d1, Date d2) {
                long diff1 = Math.abs(d1.getTime() - now);
                long diff2 = Math.abs(d2.getTime() - now);
                return Long.compare(diff1, diff2);
            }
        });
        return closest;
    }

    public HashMap<String, Integer> getDay1ResourceIds(int dayNumber) {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();

        switch (dayNumber) {
            case 1:
                hashMap.put("time", R.id.day1_time1);
                hashMap.put("weather", R.id.day1_weather1);
                hashMap.put("temp", R.id.day1_temp1);
                return hashMap;
            case 2:
                hashMap.put("time", R.id.day1_time2);
                hashMap.put("weather", R.id.day1_weather2);
                hashMap.put("temp", R.id.day1_temp2);
                return hashMap;
            case 3:
                hashMap.put("time", R.id.day1_time3);
                hashMap.put("weather", R.id.day1_weather3);
                hashMap.put("temp", R.id.day1_temp3);
                return hashMap;
            case 4:
                hashMap.put("time", R.id.day1_time4);
                hashMap.put("weather", R.id.day1_weather4);
                hashMap.put("temp", R.id.day1_temp4);
                return hashMap;
            case 5:
                hashMap.put("time", R.id.day1_time5);
                hashMap.put("weather", R.id.day1_weather5);
                hashMap.put("temp", R.id.day1_temp5);
                return hashMap;
            case 6:
                hashMap.put("time", R.id.day1_time6);
                hashMap.put("weather", R.id.day1_weather6);
                hashMap.put("temp", R.id.day1_temp6);
                return hashMap;
            case 7:
                hashMap.put("time", R.id.day1_time7);
                hashMap.put("weather", R.id.day1_weather7);
                hashMap.put("temp", R.id.day1_temp7);
                return hashMap;
            case 8:
                hashMap.put("time", R.id.day1_time8);
                hashMap.put("weather", R.id.day1_weather8);
                hashMap.put("temp", R.id.day1_temp8);
                return hashMap;
            default:
                hashMap.put("time", R.id.day1_time1);
                hashMap.put("weather", R.id.day1_weather1);
                hashMap.put("temp", R.id.day1_temp1);
                return hashMap;
        }
    }

    public int getDrawableResourceId(String resourceName) {
        int returnValue = 0;

        switch (resourceName) {
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

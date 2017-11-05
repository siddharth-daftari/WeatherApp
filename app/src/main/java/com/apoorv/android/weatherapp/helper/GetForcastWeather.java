package com.apoorv.android.weatherapp.helper;

import com.apoorv.android.weatherapp.model.CityDetailModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by siddharthdaftari on 11/2/17.
 */

public class GetForcastWeather {

    private HashMap<String, String> processWeatherApiCurrent(String latitude, String longitude) throws JSONException, ParseException {
        HashMap returnHashMap = new HashMap<String, String>();

        JSONObject jsonObject = CallApi.callApi("https://api.openweathermap.org/data/2.5/forecast?lat="
                + latitude + "&lon=" + longitude + "&APPID=" + Secrets.SECRET_FOR_WEATHER_API + "&units=" + SettingsPreference.getSelectedUnitParam());

        List<CityDetailModel> cityDetailList = new ArrayList<CityDetailModel>();
        CityDetailModel cityDetailModel = null;
        JSONObject jsonObjectTemp = null;

        jsonObjectTemp = jsonObject.getJSONObject(Constants.FORECAST_WEATHER_API_PROP_CITY);
        String cityName = jsonObjectTemp.getString(Constants.FORECAST_WEATHER_API_PROP_CITY_NAME);

        jsonObjectTemp = null;
        JSONArray jsonArrayList = jsonObject.getJSONArray(Constants.FORECAST_WEATHER_API_PROP_LIST);

        int index = 0;
        for(int i=0; i<jsonArrayList.length(); i++){
            cityDetailModel = new CityDetailModel();
            jsonObjectTemp = (JSONObject)jsonArrayList.get(i);

            cityDetailModel.setCityName(cityName);
            cityDetailModel.setDateValue(jsonObjectTemp.getString(Constants.FORECAST_WEATHER_API_PROP_DATE));

            JSONObject jsonObjectMain = jsonObjectTemp.getJSONObject(Constants.FORECAST_WEATHER_API_PROP_LIST_MAIN);
            cityDetailModel.setTemp(Double.valueOf((int)jsonObjectMain.getDouble(Constants.FORECAST_WEATHER_API_PROP_LIST_MAIN_TEMP)));
            cityDetailModel.setTempHigh(jsonObjectMain.getDouble(Constants.FORECAST_WEATHER_API_PROP_LIST_MAIN_TEMP_MAX));
            cityDetailModel.setTempLow(jsonObjectMain.getDouble(Constants.FORECAST_WEATHER_API_PROP_LIST_MAIN_TEMP_MIN));


            JSONObject jsonObjectWeather = jsonObjectTemp.getJSONArray(Constants.FORECAST_WEATHER_API_PROP_LIST_WEATHER).getJSONObject(0);
            cityDetailModel.setWeatherStatus(jsonObjectWeather.getString(Constants.FORECAST_WEATHER_API_PROP_LIST_WEATHER_MAIN));

            cityDetailModel.setIndex(index);
            index++;
            cityDetailList.add(cityDetailModel);
        }

        for(CityDetailModel cityDetailModelTemp : cityDetailList){
            cityDetailModelTemp.printValues();
        }

        return returnHashMap;
    }
}

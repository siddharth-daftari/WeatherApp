package com.apoorv.android.weatherapp.helper;

import android.app.Activity;
import android.util.Log;

import com.apoorv.android.weatherapp.CityListActivity;
import com.apoorv.android.weatherapp.helper.Constants;

import static com.apoorv.android.weatherapp.helper.Constants.TEMPERATURE_UNIT_CELSIUS;
import static com.apoorv.android.weatherapp.helper.Constants.TEMPERATURE_UNIT_FARENHEIT;

/**
 * Created by apoorv.mehta on 11/2/17.
 */

public class SettingsPreference {
    private static String  temperaturePreference = null;

    //set default preference as Celsius
    static {
        setPreferenceCelsius();
    }

    public  static void setPreferenceCelsius() {

        LogHelper.logMessage("Apoorv", "Set preference Celsius");
        temperaturePreference = TEMPERATURE_UNIT_CELSIUS;
    }

    public static void setPreferenceFarenheit() {
        LogHelper.logMessage("Apoorv", "Set preference Farenheit");
        temperaturePreference = TEMPERATURE_UNIT_FARENHEIT;
    }

    public static boolean toggleUnits(Activity activity, CityListActivity.SimpleItemRecyclerViewAdapter adapter) {
        if(getTemperaturePreference().equals(TEMPERATURE_UNIT_CELSIUS))
            setPreferenceFarenheit();

        else setPreferenceCelsius();

        adapter.notifyDataSetChanged();
        return true;
    }
    public static String getTemperaturePreference () {
        return temperaturePreference;
    }

    public static String getSelectedUnitParam() {
        switch (getTemperaturePreference())
        {
            case TEMPERATURE_UNIT_CELSIUS:
                return "metric";
            case TEMPERATURE_UNIT_FARENHEIT:
                return  "imperial";
            default:
                return "metric";
        }
    }

    public static String getSelectedUnitSuffix() {
        switch (getTemperaturePreference())
        {
            case TEMPERATURE_UNIT_CELSIUS:
                return "°C";
            case TEMPERATURE_UNIT_FARENHEIT:
                return "°F";
            default:
                return "°C";
        }
    }

    public static String getToggleLabel () {
        switch (temperaturePreference) {
            case TEMPERATURE_UNIT_CELSIUS:
                return TEMPERATURE_UNIT_FARENHEIT;
            case TEMPERATURE_UNIT_FARENHEIT:
                return TEMPERATURE_UNIT_CELSIUS;
            default:
                return "Error";
        }
    }
}

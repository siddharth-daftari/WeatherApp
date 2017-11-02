package com.apoorv.android.weatherapp.helper;

import android.util.Log;
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

        Log.i("Apoorv", "Set preference Celsius");
        temperaturePreference = TEMPERATURE_UNIT_CELSIUS;
    }

    public static void setPreferenceFarenheit() {
        Log.i("Apoorv", "Set preference Farenheit");
        temperaturePreference = TEMPERATURE_UNIT_FARENHEIT;
    }

    public static boolean toggleUnits() {
        if(getTemperaturePreference().equals(TEMPERATURE_UNIT_CELSIUS))
            setPreferenceFarenheit();

        else setPreferenceCelsius();

        return true;
    }
    public static String getTemperaturePreference () {

        Log.i("Apoorv", "Get preference :"+temperaturePreference);
        return temperaturePreference;
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

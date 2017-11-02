package com.apoorv.android.weatherapp.helper;

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
        temperaturePreference = "c";
    }

    public static void setPreferenceFarenheit() {
        temperaturePreference = "f";
    }

    public static String getTemperaturePreference () {
        return temperaturePreference;
    }
}

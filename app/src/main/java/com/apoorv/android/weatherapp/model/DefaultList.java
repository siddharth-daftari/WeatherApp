package com.apoorv.android.weatherapp.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.apoorv.android.weatherapp.CityListActivity;
import com.apoorv.android.weatherapp.helper.Constants;
import com.apoorv.android.weatherapp.helper.GetTimeZone;
import com.apoorv.android.weatherapp.helper.LogHelper;
import com.apoorv.android.weatherapp.helper.RequestClass;
import com.google.android.gms.location.places.Place;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

/**
 * Created by apoorv.mehta on 10/28/17.
 */

public class DefaultList {

    public static void writetoSharedInitial(Context c) {
        SharedPreferences sharedPref = c.getSharedPreferences("weathercities",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        LogHelper.logMessage("Apoorv","Writing to database size"+citiesset.size());
       editor.putStringSet("citieslist",citiesset);
        editor.apply();
    }

    public static CityItem getCityDetails (Context c, int index) {
        CityItem cityItem = null;

//        SharedPreferences sharedPref = c.getSharedPreferences("weathercities",Context.MODE_PRIVATE);
//        Set<String> returnedset = sharedPref.getStringSet("citieslist", Collections.EMPTY_SET);
//
//        for (String s: returnedset) {
//            //for each city string in return set, construct a cityItem PoJo and add to the static list.
//            String [] citydetails = s.split("@");
//            CityItem city1 = new CityItem(citydetails[0],citydetails[1],citydetails[2],citydetails[3],citydetails[4],citydetails[5],citydetails[6],citydetails[7]);
//            if(id.equalsIgnoreCase(city1.id)){
//                cityItem = city1;
//            }
//        }
//
//        return cityItem;
        return DefaultList.LISTPLACES.get(index);
    }

    public static void readFromSharedInitial (Context c) {
        SharedPreferences sharedPref = c.getSharedPreferences("weathercities",Context.MODE_PRIVATE);
        Set<String> returnedset = sharedPref.getStringSet("citieslist", Collections.EMPTY_SET);
        LISTPLACES.clear();
        for (String s: returnedset) {
            //for each city string in return set, construct a cityItem PoJo and add to the static list.
            String [] citydetails = s.split("@");
            LogHelper.logMessage("Apoorv", "Got a city from database, adding it to the list");
            LogHelper.logMessage("Siddharth",citydetails[7]);
            CityItem city1 = new CityItem(citydetails[0],citydetails[1],citydetails[2],citydetails[3],citydetails[4],citydetails[5],citydetails[6],citydetails[7]);
            addItem(city1);
            citiesset.add(city1.getDelimitedString());
        }


    }

    public static void addCity (Place selectedPlace, Context c, String action, Activity activity, CityListActivity.SimpleItemRecyclerViewAdapter cityListAdapter) throws JSONException {
        if(selectedPlace!=null) {

            for(CityItem city : LISTPLACES)
            {
                LogHelper.logMessage("Apoorv list",city.getDelimitedString());
            }

            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put(Constants.SELECTED_PLACE, selectedPlace);
            hashMap.put(Constants.CITY_LIST_ADAPATER, cityListAdapter);
            RequestClass.startRequestQueue();
            new GetTimeZone().getTimeDetails(String.valueOf(selectedPlace.getLatLng().latitude),String.valueOf(selectedPlace.getLatLng().longitude), action, activity, hashMap);

        }
    }

    public static String[] defaultCities = {"1@San Jose@CA, United States@37.338208@-121.886329@true@America/Los_Angeles@sj123","2@Mumbai@Maharashtra, India@19.075984@72.877656@false@Asia/Calcutta"};
    public static Set<String> citiesset = new HashSet<String>();

    public static final List<CityItem> LISTPLACES = new ArrayList<>();




    public static void addItem(DefaultList.CityItem item) {
        LISTPLACES.add(item);
    }

    public static void deleteCity(int position, Context context) {

        CityItem cityToBeRemoved = LISTPLACES.get(position);
        LogHelper.logMessage("Apoorv", "Will delete from Database"+cityToBeRemoved.getDelimitedString());
        LogHelper.logMessage("Apoorv","Set size before delete"+citiesset.size());
        citiesset.remove(cityToBeRemoved.getDelimitedString());
        LISTPLACES.remove(position);
        LogHelper.logMessage("Apoorv","Set size after delete"+citiesset.size());
        writetoSharedInitial(context);
    }


    public static void updateCurrentCity(int position, CityItem city, Context c) {


        CityItem existingCurrentCity = null;
        for (CityItem existingCity: LISTPLACES
             ) {
            if(existingCity.isCurrent == true)
                existingCurrentCity = existingCity;
        }

        if(existingCurrentCity!=null) {
            citiesset.remove(existingCurrentCity.getDelimitedString());
            LogHelper.logMessage("Apoorv",existingCurrentCity.name+" is no longer default");
            existingCurrentCity.isCurrent = false;
            citiesset.add(existingCurrentCity.getDelimitedString());
        }
        CityItem cityToBeUpdated = LISTPLACES.get(position);
        LogHelper.logMessage("Apoorv","Marking city for default"+cityToBeUpdated.getDelimitedString());
        citiesset.remove(cityToBeUpdated.getDelimitedString());
        cityToBeUpdated.isCurrent = true;
        citiesset.add(cityToBeUpdated.getDelimitedString());
        writetoSharedInitial(c);
    }

//    static {
//        citiesset.add(defaultCities[0]);
//        citiesset.add(defaultCities[1]);
//        String[] citydetails = defaultCities[0].split("@");
//        CityItem city1 = new CityItem(citydetails[0],citydetails[1],citydetails[2],citydetails[3],citydetails[4],citydetails[5]);
//        String[] citydetails2 = defaultCities[1].split("@");
//        CityItem city2 = new CityItem(citydetails2[0],citydetails2[1],citydetails2[2],citydetails2[3],citydetails2[4],citydetails2[5]);
//        addItem(city1);
//        addItem(city2);
//    }


    /**
     * A City item Pojo representing a piece of content.
     */
    public static class CityItem implements  Comparable<CityItem> {
        public final String id;
        public final String name;
        public final String description;
        public final String latitude;
        public final String longitude;
        public boolean isCurrent;
        public String timeZone;
        public String cityId;

        public CityItem(String id, String name, String description, String latitude, String longitude, String isCurrent, String timeZone, String cityId) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.latitude = latitude;
            this.longitude = longitude;
            this.isCurrent =  Boolean.parseBoolean(isCurrent);
            this.timeZone = timeZone;
            this.cityId = cityId;
        }


        public String descriptiontoString() {


            return description+", "+getListViewString(name,timeZone);
        }

        public String getCityName() {
            return name;
        }
        public String getCityDescription() {
            return description;
        }

        public String getTimeString() {
           return getListViewString(name,timeZone);
        }



        public String getListViewString(String name, String timeZoneid) {
            SimpleDateFormat df = new SimpleDateFormat();
            df.setTimeZone(TimeZone.getTimeZone(timeZoneid));
            Date date = new Date();
            LogHelper.logMessage("Apoorv","Time at " +name+" is: " + new Date(df.format(date)));
            df.applyPattern("h:mm a");
            return df.format(date);
        }

        public String getUniqueDelimitedString() {
            return name+"@"+description+"@"+latitude+"@"+longitude+"@"+timeZone+"@"+cityId;
        }

        public String getDelimitedString() {
            return id+"@"+name+"@"+description+"@"+latitude+"@"+longitude+"@"+isCurrent+"@"+timeZone+"@"+cityId;
        }


        @Override
        public int compareTo(CityItem newcity) {
            return Integer.parseInt(this.id) - Integer.parseInt(newcity.id);
        }


    }

}
